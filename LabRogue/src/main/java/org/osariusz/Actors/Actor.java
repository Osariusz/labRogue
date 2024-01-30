package org.osariusz.Actors;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.java.Log;
import org.osariusz.GameElements.GameElement;
import org.osariusz.GameElements.Spawnable;
import org.osariusz.Items.Equipment;
import org.osariusz.Items.Item;
import org.osariusz.Items.Weapon;
import org.osariusz.Utils.FightReport;
import org.osariusz.Utils.Logging;
import org.osariusz.Utils.Point;

import java.util.*;
import java.util.logging.Level;

@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public abstract class Actor extends GameElement {

    @Getter
    protected int hp;

    @Getter
    protected int agility;

    @Getter
    protected int movementSpeed;

    protected int sightRange; //should always be odd //TODO: rewrite to be Radius instead

    public int getSightRange() {
        if(sightRange%2==0) {
            return sightRange+1;
        }
        return sightRange;
    }

    @Getter
    protected boolean canPickItems;

    @Setter
    @Getter
    protected Weapon weapon;

    @Getter
    protected Map<EquipmentSlots, List<Equipment>> equipment;

    @Getter
    protected Map<EquipmentSlots, Integer> equipmentCapacity;

    @Getter
    protected List<Item> backpack;

    @Getter
    protected int backpackCapacity;

    @Getter
    @Setter
    protected Point position;


    @Override
    protected void initializeDefaults() {
        super.initializeDefaults();
        super.name = "Generic Actor";
        super.id = "generic_actor";
        super.symbol = 'A';
        this.hp = 10;
        this.agility = 20;
        this.movementSpeed = 1;
        this.canPickItems = false;
        this.backpack = new ArrayList<>();
        this.backpackCapacity = 6;
        this.equipment = initialEquipment();
        this.equipmentCapacity = initialEquipmentCapacity();
        this.weapon = Weapon.builder().damage(1).shootChance(50).build(); //TODO: rewrite as an item in equipment
        this.sightRange = 10;
    }

    public boolean canAddToBackpack(int itemAmount) {
        return backpack.size()+itemAmount <= backpackCapacity;
    }

    public void addToBackpack(List<Item> items) {
        if(!canAddToBackpack(items.size())) {
            Logging.logger.log(Level.INFO, "Can't equip that item");
            return;
        }
        backpack.addAll(items);
    }

    public void removeFromBackpack(Item item) {
        if(!getBackpack().contains(item)) {
            Logging.logger.log(Level.WARNING, item+" not present in backpack of "+this);
            return;
        }
        getBackpack().remove(item);
    }

    public boolean immuneToUpgrader() {
        for(Item item : getBackpack()) {
            if(item.isPreventUpgrader()) {
                return true;
            }
        }
        for(Equipment activeEquipment : getAllEquipment()) {
            if(activeEquipment.isPreventUpgrader()) {
                return true;
            }
        }
        return false;
    }

    public Item getRandomItemInBackpack() {
        if(getBackpack().isEmpty()) {
            Logging.logger.log(Level.WARNING, "Can't get random item as the backpack is empty");
            return null;
        }
        Random random = new Random();
        int itemSlot = random.nextInt(0, getBackpack().size());
        return getItemInBackpack(itemSlot);
    }

    public Item getItemInBackpack(int backpackSlot) {
        if(backpackSlot >= backpack.size()) {
            Logging.logger.log(Level.WARNING, "Requested "+backpackSlot+" item in backpack of size "+backpack.size());
            return null;
        }
        return getBackpack().get(backpackSlot);
    }

    public Equipment getEquipmentIfPresent(EquipmentSlots equipmentSlot, int slotNumber) {
        if(!getEquipment().containsKey(equipmentSlot)) {
            return null;
        }
        if(getEquipment().get(equipmentSlot).size() <= slotNumber) {
            return null;
        }
        return getEquipment().get(equipmentSlot).get(slotNumber);
    }

    public boolean canEquip(EquipmentSlots equipmentSlot, int slotNumber, Equipment equipment) {
        if (!getEquipment().containsKey(equipmentSlot)) {
            return false;
        }
        if(!equipment.getAllowedSlots().contains(equipmentSlot)) {
            return false;
        }
        if(slotNumber >= getEquipmentCapacity().get(equipmentSlot)) {
            return false;
        }
        return true;
    }

    public void equip(int backpackSlot, EquipmentSlots equipmentSlot, int slotNumber) {
        Item backpackItem = getItemInBackpack(backpackSlot);
        if(!(backpackItem instanceof Equipment backpackEquipment)) {
            Logging.logger.log(Level.INFO, "Can't equip non-equipment");
            return;
        }
        Equipment alreadyThere = getEquipmentIfPresent(equipmentSlot, slotNumber);
        if(canEquip(equipmentSlot, slotNumber, backpackEquipment) && canDeequip(equipmentSlot, slotNumber, alreadyThere)) {
            if(alreadyThere != null) {
                addToBackpack(new ArrayList<>(List.of(alreadyThere)));
                getEquipment().get(equipmentSlot).remove(alreadyThere);
            }
            getEquipment().get(equipmentSlot).add(backpackEquipment);
            getBackpack().remove(backpackItem);
        }
        else {
            Logging.logger.log(Level.INFO, "Can't equip "+backpackEquipment);
        }
    }

    public boolean canDeequip(EquipmentSlots equipmentSlot, int slotNumber, Equipment equipment) {
        if(!canAddToBackpack(1)) {
            return false;
        }
        return true;
    }

    public void deequip(EquipmentSlots slot, int equipmentSlotNumber) {
        Equipment equipmentThere = getEquipmentIfPresent(slot, equipmentSlotNumber);
        if(equipmentThere != null && canDeequip(slot, equipmentSlotNumber, equipmentThere) && canAddToBackpack(1)) {
            addToBackpack(new ArrayList<>(List.of(equipmentThere)));
            getEquipment().get(slot).remove(equipmentSlotNumber);
        }
        else {
            Logging.logger.log(Level.INFO,"Can't deequip "+ equipmentThere);
        }
    }

    public void throwItem(org.osariusz.Map.Map map, Item item) {
        if(getBackpack().contains(item)) {
            removeFromBackpack(item);
            map.placeItem(item, getPosition());
        }
    }

    public enum EquipmentSlots {
        HELMET,
        OUTFIT,
        PASSIVE,
        WEAPON
    }

    public EquipmentSlots getEquipmentSlot(String name) {
        return EquipmentSlots.valueOf(name);
    }

    private static Map<EquipmentSlots, List<Equipment>> initialEquipment() {
        Map<EquipmentSlots, List<Equipment>> newEquipment = new HashMap<>();
        for (EquipmentSlots slot : EquipmentSlots.values()) {
            newEquipment.put(slot, new ArrayList<>());
        }
        return newEquipment;
    }

    private static Map<EquipmentSlots, Integer> initialEquipmentCapacity() {
        Map<EquipmentSlots, Integer> capacity = new HashMap<>();
        for(EquipmentSlots slot : EquipmentSlots.values()) {
            capacity.put(slot, 1);
        }
        capacity.put(EquipmentSlots.PASSIVE, 5);
        return capacity;
    }

    public void dealDamage(int damage) {
        hp -= damage;
    }

    public void healHp(int heal) {
        hp += heal;
    }

    public List<Equipment> getAllEquipment() {
        List<Equipment> result = new ArrayList<>();
        for (List<Equipment> layer : equipment.values()) {
            result.addAll(layer);
        }
        return result;
    }

    public int getRealHP() {
        int realHp = hp;
        List<Equipment> equippedItems = getAllEquipment();
        for (Equipment item : equippedItems) {
            realHp += item.getHpBonus();
        }
        return realHp;
    }

    public int getRealAgility() {
        int realAgility = agility;
        List<Equipment> equippedItems = getAllEquipment();
        for (Equipment item : equippedItems) {
            realAgility += item.getAgilityBonus();
        }
        return realAgility;
    }

    public boolean isAlive() {
        return getRealHP() > 0;
    }

    public int getShootThreshold() {
        if (weapon == null) {
            return 50;
        }
        return weapon.getShootChance();
    }

    public int getWeaponDamage() {
        if (weapon == null) {
            return 1;
        }
        return weapon.getDamage();
    }

    public int getRealShootChance(Actor attacked) {
        int distance = getPosition().distanceTo(attacked.getPosition());
        int result = getShootThreshold() - attacked.agility - 2*distance;
        if (result > 100) {
            return 100;
        }
        if (result < 1) {
            return 1;
        }
        return result;
    }

    public void attackActor(Actor attacked) {
        Random random = new Random();
        FightReport.FightReportBuilder fightReportBuilder = new FightReport.FightReportBuilder().setAttacker(this).setDefender(attacked);
        int randomNumber = random.nextInt(1, 101);//TODO: subtract distance
        fightReportBuilder.rolledShot(randomNumber);
        if (randomNumber <= getRealShootChance(attacked)) { //will be replaced
            fightReportBuilder.setDamage(getWeaponDamage());
            attacked.dealDamage(getWeaponDamage());
        }
        //fightReportBuilder.build().showReport();
    }

    @Override
    public String toString() {
        return this.name;
    }

}

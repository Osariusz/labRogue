package org.osariusz.Actors;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.osariusz.GameElements.GameElement;
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

    protected int sightRange; //should always be odd

    public int getSightRange() {
        if(sightRange%2==0) {
            return sightRange+1;
        }
        return sightRange;
    }

    @Getter
    protected boolean canPickItems;

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
        this.agility = 5;
        this.movementSpeed = 1;
        this.canPickItems = false;
        this.backpack = new ArrayList<>();
        this.backpackCapacity = 6;
        this.equipment = initialEquipment();
        this.equipmentCapacity = initialEquipmentCapacity();
        this.sightRange = 7;
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
        for(Item item : getAllItems()) {
            if(item.isPreventUpgrader()) {
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

    public boolean equipmentSlotReady(EquipmentSlots equipmentSlots) {
        if(!getEquipment().containsKey(equipmentSlots)) {
            return false;
        }
        if(getEquipment().get(equipmentSlots).isEmpty()) {
            return true;
        }
        return false;
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

    public void swapItems(Equipment alreadyThere, EquipmentSlots equipmentSlot, Equipment backpackEquipment) {
        if(alreadyThere != null) {
            addToBackpack(new ArrayList<>(List.of(alreadyThere)));
            getEquipment().get(equipmentSlot).remove(alreadyThere);
        }
        getEquipment().get(equipmentSlot).add(backpackEquipment);
        getBackpack().remove(backpackEquipment);
    }

    public void equip(int backpackSlot, EquipmentSlots equipmentSlot, int slotNumber) {
        //TODO: fix equiping last item from backpack
        Item backpackItem = getItemInBackpack(backpackSlot);
        if(!(backpackItem instanceof Equipment backpackEquipment)) {
            Logging.logger.log(Level.INFO, "Can't equip non-equipment");
            return;
        }
        Equipment alreadyThere = getEquipmentIfPresent(equipmentSlot, slotNumber);
        if(canEquip(equipmentSlot, slotNumber, backpackEquipment) && canDeequip(equipmentSlot, slotNumber, alreadyThere)) {
            swapItems(alreadyThere, equipmentSlot, backpackEquipment);
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

    public void increaseAgility(int amount) {
        agility += amount;
    }

    public List<Equipment> getAllEquipment() {
        List<Equipment> result = new ArrayList<>();
        for (List<Equipment> layer : equipment.values()) {
            result.addAll(layer);
        }
        return result;
    }

    public List<Item> getAllItems() {
        List<Item> result = new ArrayList<>(getAllEquipment().stream().map(e -> (Item) e).toList());
        result.addAll(backpack);
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

    public int getRealMovementSpeed() {
        int realMovementSpeed = getMovementSpeed();
        List<Equipment> equippedItems = getAllEquipment();
        for (Equipment item : equippedItems) {
            realMovementSpeed += item.getMovementSpeedBonus();
        }
        return realMovementSpeed;
    }

    public boolean isAlive() {
        return getRealHP() > 0;
    }

    public int getShootThreshold(Weapon weapon) {
        if (weapon == null) {
            return 50;
        }
        return weapon.getShootChance();
    }

    public int getWeaponDamage(Weapon weapon) {
        if (weapon == null) {
            return 1;
        }
        return weapon.getDamage();
    }

    public int getRealShootChance(Actor attacked, Weapon weapon) {
        int distance = getPosition().distanceTo(attacked.getPosition());
        int result = getShootThreshold(weapon) - attacked.agility - 2*distance;
        if (result > 100) {
            return 100;
        }
        if (result < 1) {
            return 1;
        }
        return result;
    }

    public boolean canShoot(org.osariusz.Map.Map map, Actor actor, Weapon weapon) {
        return getPosition().bfsTo(actor.getPosition(),weapon.getRange(), map::canShootThrough) != null;
    }

    public List<Weapon> getActorsWeapons() {
        List<Equipment> actorWeapons = new ArrayList<>(getEquipment().get(EquipmentSlots.WEAPON));
        if(actorWeapons.isEmpty()) {
            actorWeapons.add(new Weapon().toBuilder().range(1).shootChance(50).damage(1).id("hand").name("Hand").build());
        }
        List<Weapon> result = new ArrayList<>();
        for(Equipment equipmentWeapon : actorWeapons) {
            result.add((Weapon) equipmentWeapon);
        }
        return result;
    }

    public boolean canShootAnyWeapon(org.osariusz.Map.Map map, Actor actor) {
        List<Weapon> weapons = getActorsWeapons();
        for(Weapon weapon : weapons) {
            if(canShoot(map, actor, weapon)) {
                return true;
            }
        }
        return false;
    }

    public void useItem(Item item) {
        if(getAllItems().contains(item)) {
            item.useItem(this);
            removeFromBackpack(item); //can only use items from backpack
        }
    }

    public List<FightReport> attackActor(org.osariusz.Map.Map map, Actor attacked) {
        List<FightReport> result = new ArrayList<>();

        Random random = new Random();
        FightReport.FightReportBuilder<?, ?> fightReportBuilder = FightReport.builder().attacker(this).defender(attacked);
        int randomNumber = random.nextInt(1, 101);
        fightReportBuilder.rolledShot(randomNumber);

        List<Weapon> actorWeapons = getActorsWeapons();
        for(Weapon weapon : actorWeapons) {
            if(!canShoot(map, attacked, weapon)) {
                continue;
            }
            fightReportBuilder.weapon(weapon);
            if (randomNumber <= getRealShootChance(attacked, weapon)) { //will be replaced
                fightReportBuilder.damage(getWeaponDamage(weapon));
                attacked.dealDamage(getWeaponDamage(weapon));
            }
            result.add(fightReportBuilder.build());
        }
        map.actorAttacked(attacked);
        return result;
    }

    @Override
    public String toString() {
        String stats = "(Hp: "+getRealHP()+", Agility: "+getRealAgility()+", Movement speed: "+getMovementSpeed()+")";
        return getName()+ " " + stats;
    }

}

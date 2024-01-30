package org.osariusz.Actors;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.osariusz.GameElements.GameElement;
import org.osariusz.GameElements.Spawnable;
import org.osariusz.Items.Equipment;
import org.osariusz.Items.Item;
import org.osariusz.Items.Weapon;
import org.osariusz.Utils.FightReport;
import org.osariusz.Utils.Point;

import java.util.*;

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
    protected Map<String, List<Equipment>> equipment;

    @Getter
    protected List<Item> backpack;

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
        this.equipment = initialEquipment();
        this.weapon = Weapon.builder().damage(1).shootChance(50).build(); //TODO: rewrite as an item in equipment
        this.sightRange = 10;
    }

    public void addToBackpack(List<Item> items) {
        backpack.addAll(items);
    }



    public enum EquipmentSlots {
        HELMET,
        OUTFIT,
        PASSIVE,
        WEAPON
    }

    private static Map<String, List<Equipment>> initialEquipment() {
        Map<String, List<Equipment>> newEquipment = new HashMap<>();
        for (EquipmentSlots slot : EquipmentSlots.values()) {
            newEquipment.put(slot.toString(), new ArrayList<>());
        }
        return newEquipment;
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
            hp += item.getHpBonus();
        }
        return realHp;
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
        int result = getShootThreshold() - attacked.agility;
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

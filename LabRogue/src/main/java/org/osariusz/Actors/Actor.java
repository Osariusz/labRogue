package org.osariusz.Actors;

import org.osariusz.GameElements.GameElement;
import org.osariusz.Items.Weapon;
import org.osariusz.Utils.FightReport;

import java.util.Random;

public class Actor extends GameElement {

    private String name;

    private int hp;

    private Weapon weapon;

    public Weapon getWeapon() {
        return weapon;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    public int getHp() {
        return hp;
    }

    public void dealDamage(int damage) {
        hp -= damage;
    }

    public void healHp(int heal) {
        hp += heal;
    }

    public boolean isAlive() {
        return hp > 0;
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

    public void attackActor(Actor attacked) {
        Random random = new Random();
        FightReport.FightReportBuilder fightReportBuilder = new FightReport.FightReportBuilder().setAttacker(this).setDefender(attacked);
        int randomNumber = random.nextInt(1, 101);
        fightReportBuilder.rolledShot(randomNumber);
        if (randomNumber <= getShootThreshold()) { //will be replaced
            fightReportBuilder.setDamage(getWeaponDamage());
            attacked.dealDamage(getWeaponDamage());
        }
        fightReportBuilder.build().showReport();
    }


    public Actor(int hp) {
        this.name = "Actor";
        this.symbol = 'A';
        this.hp = hp;
    }

    public Actor(String name, int hp) {
        this(hp);
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}

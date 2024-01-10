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

    public Actor(ActorBuilder builder) {
        this.name = builder.name;
        this.symbol = builder.symbol;
        this.hp = builder.hp;
        this.weapon = builder.weapon;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public static class ActorBuilder {

        int hp = 10;
        String name = "Actor";
        char symbol = 'A';
        Weapon weapon = new Weapon(1,50);

        public void setHp(int hp) {
            this.hp = hp;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setSymbol(char symbol) {
            this.symbol = symbol;
        }

        public void setWeapon(Weapon weapon) {
            this.weapon = weapon;
        }

        public Actor build() {
            return new Actor(this);
        }
    }
}

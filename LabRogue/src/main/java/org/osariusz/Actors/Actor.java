package org.osariusz.Actors;

import org.osariusz.GameElements.GameElement;
import org.osariusz.Items.Weapon;

import java.util.Random;

public class Actor extends GameElement {

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
        return hp <= 0;
    }

    public int getShootThreshold() {
        return weapon.getShootChance();
    }

    public int getWeaponDamage() {
        return weapon.getDamage();
    }

    public void attackActor(Actor attacked) {
        Random random = new Random();
        int randomNumber = random.nextInt(1,100);
        if(randomNumber < getShootThreshold()) { //will be replaced
            attacked.dealDamage(getWeaponDamage());
        }
    }

    public Actor(int hp) {
        this.name = "Actor";
        this.symbol = 'A';
        this.hp = hp;
    }
}

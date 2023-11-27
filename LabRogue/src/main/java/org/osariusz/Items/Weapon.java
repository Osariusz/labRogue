package org.osariusz.Items;

import org.osariusz.Actors.Actor;

public class Weapon extends Item {

    int damage;
    int shootChance;

    public int getDamage() {
        return damage;
    }

    public int getShootChance() {
        return shootChance;
    }

    public Weapon(int damage, int shootChance) {
        this.name = "Weapon";
        this.symbol = 'w';
        this.damage = damage;
        this.shootChance = shootChance;
    }
}

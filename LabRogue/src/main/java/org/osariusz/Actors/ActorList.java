package org.osariusz.Actors;

import org.osariusz.Items.Weapon;

import java.util.ArrayList;
import java.util.List;

public class ActorList {
    public static final List<Monster.MonsterBuilder<?, ?>> monsters = new ArrayList<>() {
        {
            add(Monster.builder().spawnChance(80).id("rat").name("Szczur").hp(2).agility(60).weapon(Weapon.builder().damage(1).shootChance(10).build()));
            add(Monster.builder().spawnChance(40).id("plague_doctor").name("Zarażony doktor").hp(10).weapon(Weapon.builder().damage(1).shootChance(40).range(2).build()));
            add(Monster.builder().spawnChance(40).id("escaped").name("Uciekinier"));
            add(Monster.builder().spawnChance(10).id("statue").name("Statua").hp(15).agility(0).weapon(Weapon.builder().damage(2).shootChance(50).range(2).build()));
            add(Monster.builder().spawnChance(1).id("dinosaur").name("Dinozaur").hp(100).agility(-20).weapon(Weapon.builder().damage(10).shootChance(70).range(5).build()));
        }
    };
}

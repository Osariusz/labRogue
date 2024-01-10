package org.osariusz.Actors;

import org.osariusz.Items.Weapon;

import java.util.ArrayList;
import java.util.List;

public class ActorList {
    public static final List<Actor> monsters = new ArrayList<>(List.of(
            Monster.builder().spawnChance(80).id("rat").name("Szczur").hp(2).weapon(Weapon.builder().damage(1).shootChance(10).build()).build(),
            Monster.builder().spawnChance(40).id("plague_doctor").name("Zarażony doktor").hp(10).weapon(Weapon.builder().damage(1).shootChance(40).range(2).build()).build(),
            Monster.builder().spawnChance(40).id("escaped").name("Uciekinier").hp(10).build(),
            Monster.builder().spawnChance(10).id("statue").name("Statua").hp(20).weapon(Weapon.builder().damage(2).shootChance(60).range(2).build()).build(),
            Monster.builder().spawnChance(1).id("dinosaur").name("Dinozaur").hp(100).weapon(Weapon.builder().damage(10).shootChance(70).range(5).build()).build()

            ));
}

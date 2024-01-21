package org.osariusz.Actors;

import org.osariusz.GameElements.GameElement;
import org.osariusz.Items.Weapon;

import java.util.*;

public class ActorList {
    public static final List<Monster.MonsterBuilder<?, ?>> monsters = new ArrayList<>() {
        {
            add(new Monster().toBuilder().spawnChance(80).hp(2).agility(60).weapon(Weapon.builder().damage(1).shootChance(10).build()).id("rat").name("Szczur"));
            add(new Monster().toBuilder().spawnChance(40).hp(10).weapon(Weapon.builder().damage(1).shootChance(40).range(2).build()).id("plague_doctor").name("Zarażony doktor"));
            add(new Monster().toBuilder().spawnChance(40).id("escaped").name("Uciekinier"));
            add(new Monster().toBuilder().spawnChance(10).hp(15).agility(0).weapon(Weapon.builder().damage(2).shootChance(50).range(2).build()).id("statue").name("Statua"));
            add(new Monster().toBuilder().spawnChance(1).hp(100).agility(-20).weapon(Weapon.builder().damage(10).shootChance(70).range(5).build()).id("dinosaur").name("Dinozaur"));
        }
        
    };

    public static List<Map.Entry<Integer, Monster.MonsterBuilder<?, ?>>> getMonsterSpawnList() {
        List<Map.Entry<Integer, Monster.MonsterBuilder<?, ?>>> result = new ArrayList<>();
        for(Monster.MonsterBuilder<?, ?> builder : monsters) {
            Monster exampleMonster = builder.build();
            result.add(new AbstractMap.SimpleEntry<>(exampleMonster.getSpawnChance(),builder));
        }
        return result;
    }
}

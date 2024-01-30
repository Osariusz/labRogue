package org.osariusz.Actors;

import org.osariusz.GameElements.GameElement;
import org.osariusz.GameElements.Spawnable;
import org.osariusz.Items.Equipment;
import org.osariusz.Items.Weapon;
import org.osariusz.Utils.Logging;
import org.osariusz.Utils.SpawnHelper;

import java.util.*;
import java.util.logging.Level;

public class ActorList {
    public static final List<Monster.MonsterBuilder<?, ?>> monsters = new ArrayList<>() {
        {
            add(new Monster().toBuilder().equipment(new HashMap<>(Map.ofEntries(
                    new AbstractMap.SimpleEntry<>(Actor.EquipmentSlots.WEAPON, new ArrayList<>(List.of(new Weapon().toBuilder().damage(1).shootChance(10).build())))
            ))).hp(2).agility(20).name("Szczur").id("rat").spawnChance(80));

            add(new Monster().toBuilder().equipment(new HashMap<>(Map.ofEntries(
                    new AbstractMap.SimpleEntry<>(Actor.EquipmentSlots.WEAPON, new ArrayList<>(List.of(new Weapon().toBuilder().damage(1).shootChance(40).range(2).build())))
            ))).hp(10).name("Zarażony doktor").id("plague_doctor").spawnChance(40));

            add(new Monster().toBuilder().spawnChance(40).id("escaped").name("Uciekinier"));

            add(new Monster().toBuilder().spawnChance(10).hp(15).agility(0).equipment(new HashMap<>(Map.ofEntries(
                    new AbstractMap.SimpleEntry<>(Actor.EquipmentSlots.WEAPON, new ArrayList<>(List.of(new Weapon().toBuilder().damage(2).shootChance(50).range(2).build())))
            ))).name("Statua").id("statue"));

            add(new Monster().toBuilder().spawnChance(1).hp(100).agility(-10).equipment(new HashMap<>(Map.ofEntries(
                    new AbstractMap.SimpleEntry<>(Actor.EquipmentSlots.WEAPON, new ArrayList<>(List.of(new Weapon().toBuilder().damage(10).shootChance(70).range(5).build())))
            ))).name("Dinozaur").id("dinosaur"));
        }

        private void add(Spawnable.SpawnableBuilder spawnableBuilder) {
            add((Monster.MonsterBuilder<?, ?>) spawnableBuilder);
        }

    };

    public static List<Map.Entry<Integer, Monster.MonsterBuilder<?, ?>>> getMonsterSpawnList() {
        return SpawnHelper.getSpawnList(monsters);
    }

    public static Monster.MonsterBuilder<?, ?> getMonster(String id) {
        return SpawnHelper.getGameElement(monsters, id);
    }
}

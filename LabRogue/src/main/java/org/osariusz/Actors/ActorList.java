package org.osariusz.Actors;

import org.osariusz.GameElements.GameElement;
import org.osariusz.GameElements.Spawnable;
import org.osariusz.Items.Equipment;
import org.osariusz.Items.ItemList;
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
            ))).startingItems(List.of("rat_tail")).hp(2).agility(20).movementSpeed(2).name("Szczur").id("rat").spawnChance(80).symbol(','));

            add(new Monster().toBuilder().equipment(new HashMap<>(Map.ofEntries(
                    new AbstractMap.SimpleEntry<>(Actor.EquipmentSlots.WEAPON, new ArrayList<>(List.of(new Weapon().toBuilder().damage(1).shootChance(40).range(2).build())))
            ))).hp(10).name("Zarażony doktor").id("plague_doctor").spawnChance(40).symbol('Z'));

            add(new Monster().toBuilder().spawnChance(40).id("escaped").name("Uciekinier").symbol('H'));

            add(new Monster().toBuilder().spawnChance(1).hp(15).agility(0).movementSpeed(3).equipment(new HashMap<>(Map.ofEntries(
                    new AbstractMap.SimpleEntry<>(Actor.EquipmentSlots.WEAPON, new ArrayList<>(List.of(new Weapon().toBuilder().damage(2).shootChance(50).range(2).build())))
            ))).name("Statua").id("statue").symbol('S'));

            add(new Monster().toBuilder().spawnChance(0).hp(100).agility(-10).equipment(new HashMap<>(Map.ofEntries(
                    new AbstractMap.SimpleEntry<>(Actor.EquipmentSlots.WEAPON, new ArrayList<>(List.of(new Weapon().toBuilder().damage(10).shootChance(70).range(5).build())))
            ))).name("Dinozaur").id("dinosaur").symbol('D'));
        }

        private void add(Spawnable.SpawnableBuilder spawnableBuilder) {
            add((Monster.MonsterBuilder<?, ?>) spawnableBuilder);
        }

    };

    public static List<Map.Entry<Integer, Monster.MonsterBuilder<?, ?>>> getMonsterSpawnList() {
        return SpawnHelper.getSpawnList(monsters);
    }

    public static List<Map.Entry<Integer, Monster.MonsterBuilder<?, ?>>> getCorrectedMonsterSpawnList(List<Map.Entry<String, Integer>> correction) {
        return SpawnHelper.getCorrectedSpawnList(monsters, correction);
    }

    public static Monster.MonsterBuilder<?, ?> getMonster(String id) {
        return SpawnHelper.getGameElement(monsters, id);
    }
}

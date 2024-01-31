package org.osariusz.Items;

import org.osariusz.Actors.Actor;
import org.osariusz.Actors.Monster;
import org.osariusz.GameElements.GameElement;
import org.osariusz.Utils.SpawnHelper;

import java.util.*;

public class ItemList {
    public static final List<Item.ItemBuilder<?, ?>> items = new ArrayList<>() {
        {
            add(new Item().toBuilder().spawnChance(0).id("ash").name("Ash"));

            add(new Item().toBuilder().transmutationChances(new ArrayList<>(List.of(
                    new AbstractMap.SimpleEntry<>(20, "rat_tail"),
                    new AbstractMap.SimpleEntry<>(1, "spacesuit"),
                    new AbstractMap.SimpleEntry<>(1, "a_500")
            ))).id("rat_tail").name("Rat tail"));

            add(new PassiveEquipment().toBuilder().spawnChance(5).allowedSlots(new ArrayList<>(List.of(
                    Actor.EquipmentSlots.PASSIVE)
            )).transmutationChances(new ArrayList<>(List.of(
                    new AbstractMap.SimpleEntry<>(2, "ash"),
                    new AbstractMap.SimpleEntry<>(10, "a_1000")
            ))).hpBonus(2).id("a_500").name("A-500"));

            add(new PassiveEquipment().toBuilder().spawnChance(1).allowedSlots(new ArrayList<>(List.of(
                    Actor.EquipmentSlots.PASSIVE))
            ).transmutationChances(new ArrayList<>(List.of(
                    new AbstractMap.SimpleEntry<>(1, "ash")
            ))).hpBonus(4).id("a_1000").name("A-1000"));

            add(new Equipment().toBuilder().hpBonus(1).agilityBonus(-1).hpBonus(2).allowedSlots(new ArrayList<>(List.of(
                    Actor.EquipmentSlots.OUTFIT
            ))).transmutationChances(new ArrayList<>(List.of(
                    new AbstractMap.SimpleEntry<>(20, "spacesuit"),
                    new AbstractMap.SimpleEntry<>(30, "rat_tail"),
                    new AbstractMap.SimpleEntry<>(5, "ash")
            ))).id("spacesuit").name("Space suit"));

            add(new Equipment().toBuilder().hpBonus(1).agilityBonus(1).preventUpgrader(true).allowedSlots(new ArrayList<>(List.of(
                    Actor.EquipmentSlots.HELMET
            ))).name("Upgrader's eye").id("upgraders_eye"));

            add(new Equipment().toBuilder().hpBonus(1).movementSpeedBonus(1).spawnChance(3).allowedSlots(new ArrayList<>(List.of(
                    Actor.EquipmentSlots.OUTFIT
            ))).transmutationChances(new ArrayList<>(List.of(
                    new AbstractMap.SimpleEntry<>(5, "spacesuit"),
                    new AbstractMap.SimpleEntry<>(50, "ash")
            ))).name("Hermes exosuit").id("hermes_exosuit"));

            add(new Weapon().toBuilder().shootChance(30).damage(5).spawnChance(4).transmutationChances(new ArrayList<>(List.of(
                    new AbstractMap.SimpleEntry<>(5, "hermes_exosuit"),
                    new AbstractMap.SimpleEntry<>(20, "rat_tail"),
                    new AbstractMap.SimpleEntry<>(1, "eyes_shotgun")
            ))).name("Shotgun").id("shotgun"));

            add(new Weapon().toBuilder().shootChance(50).damage(5).spawnChance(0).range(3).transmutationChances(new ArrayList<>(List.of(
                    new AbstractMap.SimpleEntry<>(5, "shotgun")
            ))).name("Shotgun with eyes").id("eyes_shotgun"));
        }

    };

    public static List<Map.Entry<Integer, Item.ItemBuilder<?, ?>>> getItemSpawnList() {
        return SpawnHelper.getSpawnList(items);
    }

    public static Item.ItemBuilder<?, ?> getItem(String id) {
        return SpawnHelper.getGameElement(items, id);
    }
}

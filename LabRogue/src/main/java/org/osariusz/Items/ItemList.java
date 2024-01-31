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
                    new AbstractMap.SimpleEntry<>(2000, "rat_tail"),
                    new AbstractMap.SimpleEntry<>(100, "spacesuit"),
                    new AbstractMap.SimpleEntry<>(100, "a_500"),
                    new AbstractMap.SimpleEntry<>(1, "sniper_rifle")
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
                    new AbstractMap.SimpleEntry<>(4, "a_1000"),
                    new AbstractMap.SimpleEntry<>(1, "hp_syringe"),
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

            add(new Weapon().toBuilder().shootChance(70).transmutationChances(new ArrayList<>(List.of(
                    new AbstractMap.SimpleEntry<>(10, "stick"),
                    new AbstractMap.SimpleEntry<>(5, "lightsaber")
            ))).name("Stick").id("stick"));

            add(new Weapon().toBuilder().spawnChance(1).shootChance(75).damage(3).transmutationChances(new ArrayList<>(List.of(
                    new AbstractMap.SimpleEntry<>(10, "stick"),
                    new AbstractMap.SimpleEntry<>(6, "ash"),
                    new AbstractMap.SimpleEntry<>(2, "lightsaber"),
                    new AbstractMap.SimpleEntry<>(1, "upgraders_eye")
            ))).name("Lightsaber").id("lightsaber"));

            add(new Item().toBuilder().spawnChance(4).useFunction(a -> a.healHp(3)).transmutationChances(new ArrayList<>(List.of(
                new AbstractMap.SimpleEntry<>(5, "hp_syringe"),
                new AbstractMap.SimpleEntry<>(3, "a_500")
            ))).name("Syringe with HP").id("hp_syringe"));

            add(new Item().toBuilder().spawnChance(4).useFunction(a -> a.increaseAgility(1)).transmutationChances(new ArrayList<>(List.of(
                    new AbstractMap.SimpleEntry<>(5, "agility_syringe"),
                    new AbstractMap.SimpleEntry<>(2, "hp_syringe")
            ))).name("Syringe with Adrenaline").id("agility_syringe"));

            add(new Weapon().toBuilder().spawnChance(1).shootChance(75).damage(2).range(3).transmutationChances(new ArrayList<>(List.of(
                    new AbstractMap.SimpleEntry<>(10, "stick"),
                    new AbstractMap.SimpleEntry<>(1, "ash"),
                    new AbstractMap.SimpleEntry<>(3, "lightsaber"),
                    new AbstractMap.SimpleEntry<>(1, "hp_syringe")
            ))).name("Rifle").id("rifle"));
            add(new Weapon().toBuilder().spawnChance(0).shootChance(90).damage(10).range(10).transmutationChances(new ArrayList<>(List.of(
                    new AbstractMap.SimpleEntry<>(10, "sniper_rifle"),
                    new AbstractMap.SimpleEntry<>(3, "rat_tail"),
                    new AbstractMap.SimpleEntry<>(5, "rifle")
            ))).name("Sniper rifle").id("sniper_rifle"));
        }

    };

    public static Item.ItemBuilder<?, ?> itemBuilderById(String id) {
        for(Item.ItemBuilder<?, ?> builder : items) {
            if(builder.build().getId().equals(id)) {
                return builder;
            }
        }
        return null;
    }

    public static List<Map.Entry<Integer, Item.ItemBuilder<?, ?>>> getItemSpawnList() {
        return SpawnHelper.getSpawnList(items);
    }

    public static List<Map.Entry<Integer, Item.ItemBuilder<?, ?>>> getCorrectedItemSpawnList(List<Map.Entry<String, Integer>> correction) {
        return SpawnHelper.getCorrectedSpawnList(items, correction);
    }

    public static Item.ItemBuilder<?, ?> getItem(String id) {
        return SpawnHelper.getGameElement(items, id);
    }
}

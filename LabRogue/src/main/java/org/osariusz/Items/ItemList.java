package org.osariusz.Items;

import org.osariusz.Actors.Actor;
import org.osariusz.Actors.Monster;
import org.osariusz.GameElements.GameElement;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ItemList {
    public static final List<Item.ItemBuilder<?, ?>> items = new ArrayList<>() {
        {
            add(new Item().toBuilder().id("rat_tail").name("Rat tail"));
            add(new PassiveEquipment().toBuilder().allowedSlots(new ArrayList<>(List.of(Actor.EquipmentSlots.PASSIVE))).hpBonus(2).id("a_500").name("A-500"));
        }

    };

    public static List<Map.Entry<Integer, Item.ItemBuilder<?, ?>>> getItemSpawnList() {
        List<Map.Entry<Integer, Item.ItemBuilder<?, ?>>> result = new ArrayList<>();
        for(Item.ItemBuilder<?, ?> builder : items) {
            Item exampleItem = builder.build();
            result.add(new AbstractMap.SimpleEntry<>(exampleItem.spawnChance,builder));
        }
        return result;
    }
}

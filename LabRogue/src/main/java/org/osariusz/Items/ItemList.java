package org.osariusz.Items;

import org.osariusz.Actors.Actor;
import org.osariusz.Actors.Monster;
import org.osariusz.GameElements.GameElement;
import org.osariusz.Utils.SpawnHelper;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ItemList {
    public static final List<Item.ItemBuilder<?, ?>> items = new ArrayList<>() {
        {
            add(new Item().toBuilder().id("rat_tail").name("Rat tail"));
            add(new PassiveEquipment().toBuilder().allowedSlots(new ArrayList<>(List.of(Actor.EquipmentSlots.PASSIVE))).hpBonus(2).id("a_500").name("A-500"));
            add(new Equipment().toBuilder().hpBonus(1).agilityBonus(-1).hpBonus(2).allowedSlots(new ArrayList<>(List.of(Actor.EquipmentSlots.OUTFIT))).id("spacesuit").name("Space suit"));
        }

    };

    public static List<Map.Entry<Integer, Item.ItemBuilder<?, ?>>> getItemSpawnList() {
        return SpawnHelper.getSpawnList(items);
    }

    public static Item.ItemBuilder<?, ?> getItem(String id) {
        return SpawnHelper.getGameElement(items, id);
    }
}

package org.osariusz.Items;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.osariusz.Actors.Actor;
import org.osariusz.GameElements.GameElement;
import org.osariusz.GameElements.Spawnable;
import org.osariusz.Map.Rooms.Room;
import org.osariusz.Utils.RandomChoice;

import java.util.*;

@SuperBuilder(toBuilder = true)
@Getter
@NoArgsConstructor
public class Item extends GameElement {

    @Override
    protected void initializeDefaults() {
        super.initializeDefaults();
        super.name = "Generic Item";
        super.id = "generic_item";
        super.symbol = 'i';
        super.spawnChance = 10;
        this.transmutationChances = null;
        this.preventUpgrader = false;
    }

    protected List<Map.Entry<Integer, String>> transmutationChances;

    protected boolean preventUpgrader;

    public void initializeTransmutation() {
        if(this.transmutationChances == null) {
            this.transmutationChances = new ArrayList<>(List.of(new AbstractMap.SimpleEntry<>(1,this.id)));
        }
    }

    public Item transmuteItem() {
        String itemId = RandomChoice.choose(new Random(), this.transmutationChances);
        ItemBuilder<?, ?> itemBuilder = ItemList.getItem(itemId);
        return itemBuilder.build();
    }

    private static final class ItemBuilderImpl extends Item.ItemBuilder<Item, Item.ItemBuilderImpl> {
        public Item build() {
            Item result = new Item(this);
            result.initializeTransmutation();
            return result;
        }
    }

    @Override
    public String toString() {
        String base = name;
        if(preventUpgrader) {
            base += " (prevents upgrader) ";
        }
        return base;
    }

}

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
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;

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
        super.spawnChance = 20;
        this.transmutationChances = null;
        this.preventUpgrader = false;
        this.dropsFromDead = true;
        this.useFunction = null;
    }

    protected List<Map.Entry<Integer, String>> transmutationChances;

    protected boolean preventUpgrader;

    protected boolean dropsFromDead;

    protected Consumer<Actor> useFunction;

    public boolean canUseItem() {
        return useFunction != null;
    }

    public void useItem(Actor actor) {
        if(canUseItem()) {
            useFunction.accept(actor);
        }
    }

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

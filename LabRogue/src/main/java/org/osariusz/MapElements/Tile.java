package org.osariusz.MapElements;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.osariusz.Actors.Actor;
import org.osariusz.GameElements.GameElement;
import org.osariusz.Items.Item;
import org.osariusz.Utils.Logging;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public class Tile extends MapElement {

    @Override
    protected void initializeDefaults() {
        super.initializeDefaults();
        super.name = "Tile";
        super.symbol = ' ';
        this.items = new ArrayList<>();
    }


    @Getter
    @Setter
    private Actor actor;

    @Getter
    private List<Item> items;

    public void removeActor() {
        actor = null;
    }

    public boolean hasActor() {
        return getActor() != null;
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public void removeItem(Item item) {
        items.remove(item);
    }

    public void removeAllItems() {
        items = new ArrayList<>();
    }

    public boolean hasItems() {
        return !items.isEmpty();
    }

    public void transferItemsToActor(Actor actor) {
        if(actor.canAddToBackpack(items.size())) {
            actor.addToBackpack(items);
            items.clear();
        }
        else {
            Logging.logger.log(Level.INFO, "You can't carry more items so you didn't pick anything");
        }
    }

    public char getSymbol() {
        if (actor != null) {
            return actor.getSymbol();
        } else if (items.size() == 1) {
            return items.get(0).getSymbol();
        } else if (items.size() > 1) {
            return 'I';
        } else {
            return symbol;
        }
    }
}

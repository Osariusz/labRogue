package org.osariusz.MapElements;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.osariusz.Actors.Actor;
import org.osariusz.GameElements.GameElement;
import org.osariusz.Items.Item;

import java.util.ArrayList;
import java.util.List;

@SuperBuilder
@Getter
public class Tile extends MapElement {
    {
        super.name = "Tile";
        super.symbol = 'G';
    }

    @Setter
    private Actor actor;

    @Builder.Default
    private List<Item> items = new ArrayList<>();

    public Actor getActor() {
        return actor;
    }

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

    public List<Item> getItems() {
        return items;
    }

    public void removeAllItems() {
        items = new ArrayList<>();
    }

    public char getSymbol() {
        if(actor != null) {
            return actor.getSymbol();
        }
        else if(items.size() == 1) {
            return items.get(0).getSymbol();
        }
        else if(items.size() > 1) {
            return 'I';
        }
        else {
            return symbol;
        }
    }
}

package org.osariusz.Map;

import lombok.Builder;
import lombok.Getter;
import org.osariusz.Actors.Actor;
import org.osariusz.MapElements.MapElement;
import org.osariusz.MapElements.Tile;
import org.osariusz.MapElements.Wall;
import org.osariusz.Items.Item;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class Map {

    List<List<MapElement>> map;

    @Builder.Default
    private int width = 100;
    @Builder.Default
    private int height = 100;

    public static MapBuilder builder() {
        return new GeneratorMapBuilder();
    }

    public MapElement getFeature(int x, int y) {
        return map.get(y).get(x);
    }

    public MapElement generateFeature(int x, int y) {
        if(x == 0 || x == width-1 || y == 0 || y == height-1) {
            return Wall.builder().build();
        }
        return Tile.builder().build();
    }

    public Map generateMap() {
        map = new ArrayList<>();
        for(int y = 0;y<height;++y) {
            map.add(new ArrayList<>());
            for(int x = 0;x<width;++x) {
                map.get(y).add(generateFeature(x,y));
            }
        }
        return this;
    }

    public List<Actor> getAllActors() {
        List<Actor> actors = new ArrayList<>();
        for (int x = 0; x < map.size(); x++) {
            for (int y = 0; y < map.get(x).size(); y++) {
                MapElement mapElement = map.get(x).get(y);
                if (mapElement instanceof Tile && ((Tile) mapElement).hasActor()) {
                    actors.add(((Tile) mapElement).getActor());
                }
            }
        }
        return actors;
    }

    public void placeActor(Actor actor, int x, int y) {
        if (canPlaceActor(actor, x, y)) {
            Tile tile = (Tile) map.get(x).get(y);
            tile.setActor(actor);
        }
    }

    public void removeActor(int x, int y) {
        if (map.get(x).get(y) instanceof Tile tile) {
            tile.removeActor();
        }
    }

    public boolean canPlaceActor(Actor actor, int x, int y) {
        if (map.get(x).get(y) instanceof Tile tile) {
            return tile.getActor() == null;
        }
        return false;
    }

    public List<Item> getAllItems() {
        List<Item> items = new ArrayList<>();
        for (int x = 0; x < map.size(); x++) {
            for (int y = 0; y < map.get(x).size(); y++) {
                if (map.get(x).get(y) instanceof Tile tile) {
                    items.addAll(tile.getItems());
                }
            }
        }
        return items;
    }

    public void placeItem(Item item, int x, int y) {
        if (canPlaceItem(item, x, y)) {
            Tile tile = (Tile) map.get(x).get(y);
            tile.addItem(item);
        }
    }

    public void removeAllItems(int x, int y) {
        if (map.get(x).get(y) instanceof Tile tile) {
            tile.removeAllItems();
        }
    }

    public void removeItem(Item item, int x, int y) {
        if (map.get(x).get(y) instanceof Tile tile) {
            tile.removeItem(item);
        }
    }

    public boolean canPlaceItem(Item item, int x, int y) {
        if (map.get(x).get(y) instanceof Tile tile) {
            return tile.getItems().isEmpty();
        }
        return false;
    }

    public static class GeneratorMapBuilder extends MapBuilder {
        GeneratorMapBuilder() {
            super();
        }

        @Override public Map build() {
            Map map = super.build();
            map.generateMap();
            return map;
        }
    }
}

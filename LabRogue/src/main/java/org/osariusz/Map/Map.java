package org.osariusz.Map;

import org.osariusz.Actors.Actor;
import org.osariusz.MapElements.MapElement;
import org.osariusz.MapElements.Tile;
import org.osariusz.GameElements.GameElement;
import org.osariusz.MapElements.Wall;
import org.osariusz.Items.Item;
import org.osariusz.Utils.TypeUtils;

import java.util.ArrayList;
import java.util.List;

public class Map {

    List<List<MapElement>> map;

    public Map(MapBuilder mapBuilder) {
        this.map = mapBuilder.map;
    }

    public List<List<MapElement>> getMap() {
        return map;
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
        if(map.get(x).get(y) instanceof Tile tile) {
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

    public static class MapBuilder {
        int width;
        int height;
        int itemCount;

        int enemyCount;

        List<List<MapElement>> map;

        public MapBuilder setWidth(int width) {
            this.width = width;
            return this;
        }

        public MapBuilder setHeight(int height) {
            this.height = height;
            return this;
        }

        public MapBuilder setItemCount(int itemCount) {
            this.itemCount = itemCount;
            return this;
        }

        public MapBuilder setEnemyCount(int enemyCount) {
            this.enemyCount = enemyCount;
            return this;
        }

        public MapBuilder buildMap() {
            map = new ArrayList<>();
            for (int x = 0; x < width; x++) {
                List<MapElement> column = new ArrayList<>();


                for (int y = 0; y < height; y++) {
                    MapElement mapElement;
                    if (x == 0 || x == width - 1 || y == 0 || y == height - 1) {
                        mapElement = new Wall();
                    } else if (x == width / 2 && y == width / 2) {
                        Tile tile = new Tile();
                        tile.setActor(new Actor(20));
                        mapElement = tile;
                    } else if (x % 25 == 2 && y % 13 == 1) {
                        Tile tile = new Tile();
                        tile.addItem(new Item());
                        mapElement = tile;
                    } else {
                        mapElement = new Tile();
                    }
                    column.add(mapElement);
                }
                map.add(column);
            }
            return this;
        }


        public Map build() {
            return new Map(this);
        }
    }

}

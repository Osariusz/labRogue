package org.osariusz.Map;

import org.osariusz.Actors.Actor;
import org.osariusz.GameElements.Empty;
import org.osariusz.GameElements.GameElement;
import org.osariusz.GameElements.Wall;
import org.osariusz.Items.Item;
import org.osariusz.Utils.TypeUtils;

import java.util.ArrayList;
import java.util.List;

public class Map {

    List<List<GameElement>> map;

    public Map(MapBuilder mapBuilder) {
        this.map = mapBuilder.map;
    }

    public List<List<GameElement>> getMap() {
        return map;
    }

    public List<Actor> getAllActors() {
        List<Actor> items = new ArrayList<>();
        for (int x = 0; x < map.size(); x++) {
            for (int y = 0; y < map.get(x).size(); y++) {
                if (map.get(x).get(y) instanceof Actor) {
                    items.add((Actor) map.get(x).get(y));
                }
            }
        }
        return items;
    }

    public void placeActor(Actor actor, int x, int y) {
        if (canPlaceActor(actor, x, y)) {
            map.get(x).set(y, actor);
        }
    }

    public void removeActor(int x, int y) {
        map.get(x).set(y, new Empty());
    }

    public boolean canPlaceActor(Actor actor, int x, int y) {
        List<Class<?>> exclusiveTypes = new ArrayList<>(List.of(Wall.class, Actor.class));
        Object object = map.get(x).get(y);
        if (TypeUtils.objectOfTypes(object, exclusiveTypes)) {
            return false;
        }
        return true;
    }

    public List<Item> getAllItems() {
        List<Item> items = new ArrayList<>();
        for (int x = 0; x < map.size(); x++) {
            for (int y = 0; y < map.get(x).size(); y++) {
                if (map.get(x).get(y) instanceof Item) {
                    items.add((Item) map.get(x).get(y));
                }
            }
        }
        return items;
    }

    public void placeItem(Item item, int x, int y) {
        if (canPlaceItem(item, x, y)) {
            map.get(x).set(y, item);
        }
    }

    public void removeItem(int x, int y) {
        map.get(x).set(y, new Empty());
    }

    public boolean canPlaceItem(Item item, int x, int y) {
        List<Class<?>> exclusiveTypes = new ArrayList<>(List.of(Wall.class, Item.class));
        Object object = map.get(x).get(y);
        if (TypeUtils.objectOfTypes(object, exclusiveTypes)) {
            return false;
        }
        return true;
    }

    public static class MapBuilder {
        int width;
        int height;
        int itemCount;

        int enemyCount;

        List<List<GameElement>> map;

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
                List<GameElement> column = new ArrayList<>();



                for (int y = 0; y < height; y++) {
                    GameElement gameElement;
                    if(x == 0 || x == width-1 || y == 0 || y == height-1) {
                        gameElement = new Wall();
                    }
                    else if(x == width/2 && y == width/2) {
                        gameElement = new Actor();
                    }
                    else if(x%25 == 2 && y%13 == 1) {
                        gameElement = new Item();
                    }
                    else {
                        gameElement = new Empty();
                    }
                    column.add(gameElement);
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

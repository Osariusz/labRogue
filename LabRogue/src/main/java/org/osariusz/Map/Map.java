package org.osariusz.Map;

import lombok.Builder;
import lombok.Getter;
import org.osariusz.Actors.Actor;
import org.osariusz.Actors.ActorList;
import org.osariusz.Actors.Monster;
import org.osariusz.Actors.Player;
import org.osariusz.Items.ItemList;
import org.osariusz.Map.Rooms.Room;
import org.osariusz.MapElements.MapElement;
import org.osariusz.MapElements.Tile;
import org.osariusz.MapElements.Wall;
import org.osariusz.Items.Item;
import org.osariusz.Utils.Logging;
import org.osariusz.Utils.Point;
import org.osariusz.Utils.RandomChoice;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

@Getter
@Builder
public class Map {

    List<List<MapElement>> map;

    @Builder.Default
    private int width = 100;
    @Builder.Default
    private int height = 100;

    @Builder.Default
    private int itemGenerationChance = 3; //3 is 3%

    @Getter
    @Builder.Default
    private Player player = new Player().toBuilder().build();

    public static MapBuilder builder() {
        return new GeneratorMapBuilder();
    }

    public MapElement getFeature(int x, int y) {
        return map.get(y).get(x);
    }

    public MapElement generateFeature(int x, int y) {
        return new Wall().toBuilder().build();
    }

    public void placeRoom(Room room, int startX, int startY) {
        if(room.getWidth()+startX >= getWidth()) {
            Logging.logger.log(Level.WARNING,"Can't generate room at "+startX+", "+startY+" because of width");
            return;
        }
        if(room.getHeight()+startY >= getHeight()) {
            Logging.logger.log(Level.WARNING,"Can't generate room at "+startX+", "+startY+" because of height");
            return;
        }

        for(int roomX = 0;roomX< room.getWidth();++roomX) {
            for(int roomY = 0;roomY< room.getHeight();++roomY) {
                int mapX = roomX+startX;
                int mapY = roomY+startY;

                placeMapElement(room.getFeature(roomX,roomY), mapX, mapY);
            }
        }
    }

    public boolean isSpaceTaken(List<Room> rooms, int x, int y) {
        for(Room room : rooms) {
            if(room.pointInside(x,y)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkPath(List<Room> rooms, int x, int y) {
        for(Room room : rooms) {
            if(room.getClosestRoom(rooms).isRoomPathTo(room,x,y)) {
                return true;
            }
        }
        return false;
    }

    public void generateRooms() {
        List<Room> rooms = new ArrayList<>();
        for (int y = 0; y < getHeight(); ++y) {
            for (int x = 0; x < getWidth(); ++x) {
                Room room = new Room().toBuilder().startX(x).startY(y).build();
                if(room.canPlace(rooms, this,x,y)) {
                    placeRoom(room, x, y);
                    rooms.add(room);
                }
            }
        }
    }

    public void placeGenerateItem(int x, int y) {
        Random random = new Random();
        if (random.nextInt(1, 101) <= itemGenerationChance) {
            Item item = RandomChoice.choose(random, ItemList.getItemSpawnList()).build();
            if (getFeature(x, y) instanceof Tile tile) {
                placeItem(item, x, y);
            }
        }
    }

    public void placeMapElement(MapElement mapElement, int x, int y) {
        getMap().get(y).set(x, mapElement);
    }

    public Point getAnyFieldForActor(Actor actor) {
        for(int y = 0;y<map.size();++y) {
            for(int x = 0;x<map.size();++x) {
                if(map.get(y).get(x) instanceof Tile tile) {
                    if(canPlaceActor(actor,x,y)) {
                        return new Point(x, y);
                    }
                }
            }
        }
        return new Point(0,0);
    }

    public Map generateMap() {
        map = new ArrayList<>();
        for (int y = 0; y < getHeight(); ++y) {
            map.add(new ArrayList<>());
            for (int x = 0; x < getWidth(); ++x) {
                map.get(y).add(generateFeature(x, y));
            }
        }
        generateRooms();
        for (int y = 0; y < getHeight(); ++y) {
            for (int x = 0; x < getWidth(); ++x) {
                placeGenerateItem(x, y);
            }
        }
        Point emptySlot = getAnyFieldForActor(player);
        placeActor(getPlayer(), emptySlot.getX(), emptySlot.getY());
        Monster rat = ActorList.getMonster("rat").build();
        Point emptySlot2 = getAnyFieldForActor(rat);
        placeActor(rat,emptySlot2.getX(),emptySlot2.getY());
        return this;
    }

    public List<Actor> getAllActors() {
        //TODO: list of actors
        List<Actor> actors = new ArrayList<>();
        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                MapElement mapElement = map.get(y).get(x);
                if (mapElement instanceof Tile && ((Tile) mapElement).hasActor()) {
                    actors.add(((Tile) mapElement).getActor());
                }
            }
        }
        return actors;
    }

    public void actorsTurn() {
        List<Actor> actors = getAllActors();
        for(Actor actor : actors) {
            if(actor instanceof Monster monster) {
                monster.moveMonster(this);
            }
        }
    }

    public void moveActor(Actor actor, int xMovement, int yMovement) {
        int x = actor.getX() + xMovement;
        int y = actor.getY() + yMovement;
        if (canPlaceActor(actor, x, y)) {
            removeActor(actor.getX(), actor.getY());
            actor.setX(x);
            actor.setY(y);
            placeActor(actor, x, y);
        } else {
            Logging.logger.log(Level.WARNING, "Can't move " + actor.toString() + " to " + x + ", " + y);
        }
    }

    public void placeActor(Actor actor, int x, int y) {
        if (canPlaceActor(actor, x, y)) {
            Tile tile = (Tile) map.get(y).get(x);
            tile.setActor(actor);
            actor.setX(x);
            actor.setY(y);

            if (actor.isCanPickItems() && tile.hasItems()) {
                tile.transferItemsToActor(actor);
            }
        }
    }

    public void removeActor(int x, int y) {
        if (map.get(y).get(x) instanceof Tile tile) {
            tile.removeActor();
        }
    }

    public boolean canPlaceActor(Actor actor, int x, int y) {
        if (map.get(y).get(x) instanceof Tile tile) {
            return tile.getActor() == null;
        }
        return false;
    }

    public List<Item> getAllItems() {
        List<Item> items = new ArrayList<>();
        for (int x = 0; x < map.size(); x++) {
            for (int y = 0; y < map.get(x).size(); y++) {
                if (map.get(y).get(x) instanceof Tile tile) {
                    items.addAll(tile.getItems());
                }
            }
        }
        return items;
    }

    public void placeItem(Item item, int x, int y) {
        if (canPlaceItem(item, x, y)) {
            Tile tile = (Tile) map.get(y).get(x);
            tile.addItem(item);
        }
    }

    public void removeAllItems(int x, int y) {
        if (map.get(y).get(x) instanceof Tile tile) {
            tile.removeAllItems();
        }
    }

    public void removeItem(Item item, int x, int y) {
        if (map.get(y).get(x) instanceof Tile tile) {
            tile.removeItem(item);
        }
    }

    public boolean canPlaceItem(Item item, int x, int y) {
        if (map.get(y).get(x) instanceof Tile tile) {
            return tile.getItems().isEmpty();
        }
        return false;
    }

    public static class GeneratorMapBuilder extends MapBuilder {
        GeneratorMapBuilder() {
            super();
        }

        @Override
        public Map build() {
            Map map = super.build();
            map.generateMap();
            return map;
        }
    }
}

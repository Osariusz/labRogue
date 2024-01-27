package org.osariusz.Map;

import lombok.Builder;
import lombok.Getter;
import org.osariusz.Actors.Actor;
import org.osariusz.Actors.ActorList;
import org.osariusz.Actors.Monster;
import org.osariusz.Actors.Player;
import org.osariusz.Items.ItemList;
import org.osariusz.Map.Rooms.Room;
import org.osariusz.Map.Rooms.RoomsList;
import org.osariusz.MapElements.Door;
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

    @Builder.Default
    private List<Point> corridorPoints = new ArrayList<>();

    public static MapBuilder builder() {
        return new GeneratorMapBuilder();
    }

    public boolean outOfBounds(Point point) {
        if(point.getX() < 0 || point.getY() < 0 || point.getX() >= getWidth() || point.getY() >= getHeight()) {
            return true;
        }
        return false;
    }

    public MapElement getFeature(Point point) {
        if(outOfBounds(point)) {
            return new Wall().toBuilder().build();
        }
        return map.get(point.getY()).get(point.getX());
    }

    public MapElement generateFeature(int x, int y) {
        return new Wall().toBuilder().build();
    }

    public void placeRoom(Room room) {
        if(room.getWidth()+room.getStartX() > getWidth()) {
            Logging.logger.log(Level.WARNING,"Can't generate room at "+room.getStartX()+", "+room.getStartY()+" because of width");
            return;
        }
        if(room.getHeight()+room.getStartY() > getHeight()) {
            Logging.logger.log(Level.WARNING,"Can't generate room at "+room.getStartX()+", "+room.getStartY()+" because of height");
            return;
        }

        for(int roomX = 0;roomX< room.getWidth();++roomX) {
            for(int roomY = 0;roomY< room.getHeight();++roomY) {
                int mapX = roomX+room.getStartX();
                int mapY = roomY+room.getStartY();

                placeMapElement(room.getFeature(roomX,roomY), new Point(mapX, mapY));
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

    public boolean checkPath(List<Room> rooms, Point point) {
        return point.pointInList(getCorridorPoints());
    }

    public void generateRooms() {
        List<Room> rooms = new ArrayList<>();
        for (int y = 0; y < getHeight(); ++y) {
            for (int x = 0; x < getWidth(); ++x) {

                Room.RoomBuilder<?, ?> chosenRoom = RandomChoice.choose(new Random(), RoomsList.getRoomSpawnList());
                Room room = chosenRoom.startPoint(new Point(x,y)).build();
                if(room.canPlace(rooms, this)) {
                    placeRoom(room);
                    rooms.add(room);
                    generatePaths(rooms);
                }
            }
        }
    }

    public List<Point> getAllCorridorPoints(List<CorridorDigger> corridorDiggers) {
        List<Point> result = new ArrayList<>(corridorPoints);
        for(CorridorDigger digger : corridorDiggers) {
            result.addAll(digger.getCorridorPoints());
        }
        return result;
    }

    public void updateCorridorPoints(List<CorridorDigger> corridorDiggers) {
        for(CorridorDigger digger : corridorDiggers) {
            corridorPoints.addAll(digger.getCorridorPoints());
        }
    }

    public static boolean checkIfDoorInThisDirection(Point startingPoint, Point direction, Point door) {
        int xDiff = door.getX()-startingPoint.getX();
        int yDiff = door.getY()-startingPoint.getY();
        if((direction.getX() == 0 || Math.signum(xDiff) == Math.signum(direction.getX())) &&
                (direction.getY() == 0 || Math.signum(yDiff) == Math.signum(direction.getY()))) {
            return true;
        }
        return false;
    }

    public boolean anyDoorInThisDirection(Point startingPoint, Point direction, List<Room> rooms) {
        for(Room room : rooms) {
            for(Point door : room.getDoors()) {
                if(checkIfDoorInThisDirection(startingPoint, door, direction)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void generatePaths(List<Room> rooms) {
        List<CorridorDigger> diggers = new ArrayList<>();
        for(Room room : rooms) {
            double minimalDoorDistance = Double.MAX_VALUE;
            java.util.Map.Entry<Point, Point> minimalDoors = null;
            for(Room anotherRoom : rooms) {
                if(room.equals(anotherRoom)) {
                    continue;
                }
                java.util.Map.Entry<Point, Point> doors = room.closestUnusedDoorsInCorrectDirectionForRooms(anotherRoom);
                if(doors == null) {
                    continue;
                }
                double doorDistance = doors.getKey().distanceTo(doors.getValue());
                if(doorDistance < minimalDoorDistance) {
                    minimalDoorDistance = doorDistance;
                    minimalDoors = doors;
                }
            }
            if(minimalDoors != null) {
                Logging.logger.log(Level.INFO, "door1: "+ minimalDoors.getKey()+" door2: "+minimalDoors.getValue());
                diggers.add(new CorridorDigger(minimalDoors.getKey(), minimalDoors.getValue(), this, rooms));
                room.useDoor(minimalDoors.getKey());
            }
        }
        int allowedIterations = 50;
        for(int i = 0;i<allowedIterations; ++i) {
            boolean allFinished = true;
            for(CorridorDigger digger : diggers) {
                if(!digger.diggerFinished()) {
                    allFinished = false;

                    List<CorridorDigger> otherDiggers = new ArrayList<>(diggers);
                    otherDiggers.remove(digger);
                    digger.updateDestination(this, getAllCorridorPoints(otherDiggers));
                    digger.stepForward();
                }
            }
            if(allFinished) {
                break;
            }
        }
        List<Point> digPoints = getAllCorridorPoints(diggers);
        for(Point dig : digPoints) {
            if(getFeature(dig) instanceof Wall) {
                placeMapElement(new Tile().toBuilder().symbol('/').build(), dig);
            }
        }
        updateCorridorPoints(diggers);
    }

    public void placeGenerateItem(Point point) {
        Random random = new Random();
        if (random.nextInt(1, 101) <= itemGenerationChance) {
            Item item = RandomChoice.choose(random, ItemList.getItemSpawnList()).build();
            if (getFeature(point) instanceof Tile tile) {
                placeItem(item, point);
            }
        }
    }

    public void placeMapElement(MapElement mapElement, Point point) {
        if(outOfBounds(point)) {
            Logging.logger.log(Level.WARNING, "Can't place "+mapElement+" on "+point+" because it is out of bounds!");
            return;
        }
        getMap().get(point.getY()).set(point.getX(), mapElement);
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
                placeGenerateItem(new Point(x,y));
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
        int x = actor.getPosition().getX() + xMovement;
        int y = actor.getPosition().getY() + yMovement;

        if(getFeature(new Point(x,y)) instanceof Door) {
            xMovement *= 2;
            yMovement *= 2;
        }

        x = actor.getPosition().getX() + xMovement;
        y = actor.getPosition().getY() + yMovement;

        if (canPlaceActor(actor, x, y)) {
            removeActor(actor.getPosition().getX(), actor.getPosition().getY());
            actor.setPosition(new Point(x,y));
            placeActor(actor, x, y);
        }
        else {
            Logging.logger.log(Level.WARNING, "Can't move " + actor.toString() + " to " + x + ", " + y);
        }
    }

    public void placeActor(Actor actor, int x, int y) {
        if (canPlaceActor(actor, x, y)) {
            Tile tile = (Tile) map.get(y).get(x);
            tile.setActor(actor);
            actor.setPosition(new Point(x,y));

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

    public void placeItem(Item item, Point point) {
        if (canPlaceItem(item, point)) {
            Tile tile = (Tile) map.get(point.getY()).get(point.getX());
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

    public boolean canPlaceItem(Item item, Point point) {
        if (map.get(point.getY()).get(point.getX()) instanceof Tile tile) {
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

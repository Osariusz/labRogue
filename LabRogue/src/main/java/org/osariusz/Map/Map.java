package org.osariusz.Map;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;
import org.osariusz.Actors.Actor;
import org.osariusz.Actors.ActorList;
import org.osariusz.Actors.Monster;
import org.osariusz.Actors.Player;
import org.osariusz.Graphics.IO;
import org.osariusz.Items.ItemList;
import org.osariusz.Map.Rooms.Room;
import org.osariusz.Map.Rooms.RoomsList;
import org.osariusz.MapElements.*;
import org.osariusz.Items.Item;
import org.osariusz.Utils.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.stream.Collectors;

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
    @Builder.Default
    int monsterSpawnPlayerDistance = 10;
    @Builder.Default
    int monsterSpawnChance = 3; //10 is 10%

    @Builder.Default
    List<java.util.Map.Entry<String, Integer>> randomOverride = new ArrayList<>();

    @Getter
    @Setter
    @Builder.Default
    int moveBetweenMaps = 0;

    @Getter
    @Builder.Default
    private Player player = new Player().toBuilder().build();

    @Builder.Default
    private List<Point> corridorPoints = new ArrayList<>();

    @Builder.Default
    private List<Room> rooms = new ArrayList<>();

    @Builder.Default
    Random random = new Random();

    @Builder.Default
    List<CorridorDigger> failedDiggers = new ArrayList<>();

    @Builder.Default
    List<FightReport> fightReports = new ArrayList<>();

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
//        if(room.getWidth()+room.getStartX() > getWidth()) {
//            Logging.logger.log(Level.WARNING,"Can't generate room at "+room.getStartX()+", "+room.getStartY()+" because of width");
//            return;
//        }
//        if(room.getHeight()+room.getStartY() > getHeight()) {
//            Logging.logger.log(Level.WARNING,"Can't generate room at "+room.getStartX()+", "+room.getStartY()+" because of height");
//            return;
//        }

        for(int roomX = 0;roomX< room.getWidth();++roomX) {
            for(int roomY = 0;roomY< room.getHeight();++roomY) {
                int mapX = roomX+room.getStartX();
                int mapY = roomY+room.getStartY();

                placeMapElement(room.getFeature(new Point(roomX,roomY), random), new Point(mapX, mapY));
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
        rooms.clear();
        for (int y = 0; y < getHeight(); ++y) {
            for (int x = 0; x < getWidth(); ++x) {

                Room.RoomBuilder<?, ?> chosenRoom = RandomChoice.choose(random, RoomsList.getCorrectedRoomSpawnList(randomOverride));
                Room room = chosenRoom.startPoint(new Point(x,y)).build();
                if(room.canPlace(rooms, this)) {
                    placeRoom(room);
                    rooms.add(room);
                    generatePaths(rooms);
                }
            }
        }
        retryGeneratePaths(rooms);
        generateStartAndFinish(rooms);
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

    public int getAllowedDiggersOperations() {
        return Math.min(getWidth(), getHeight());
    }

    public void retryGeneratePaths(List<Room> rooms) {
        failedDiggers.forEach(d -> Logging.logger.log(Level.INFO, "failed digger "+d));
        List<CorridorDigger> newDiggers = new ArrayList<>();
        for(CorridorDigger failedDigger : failedDiggers) {
            newDiggers.add(new CorridorDigger(failedDigger.startPoint,failedDigger.destination,this,rooms,failedDigger.random));
        }
        runAllDiggers(newDiggers, getAllowedDiggersOperations()*2);
        newDiggers = newDiggers.stream().filter(CorridorDigger::diggerFinished).toList();
        digCorridors(newDiggers);
    }

    public void runAllDiggers(List<CorridorDigger> diggers, int allowedIterations) {
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
        for(CorridorDigger digger : diggers) {
            for(Room room : rooms) {
                if(digger.destination.pointInList(room.getUnconnectedDoors())) {
                    room.connectDoor(digger.destination);
                }
            }
        }
    }

    public void digCorridors(List<CorridorDigger> diggers) {
        List<Point> digPoints = getAllCorridorPoints(diggers);
        for(Point dig : digPoints) {
            if(getFeature(dig) instanceof Wall) {
                placeMapElement(new Tile().toBuilder().build(), dig);
            }
        }
    }

    public void generatePaths(List<Room> rooms) {
        List<CorridorDigger> diggers = new ArrayList<>();
        for(Room room : rooms) {
            double minimalDoorDistance = Double.MAX_VALUE;
            java.util.Map.Entry<Point, Point> minimalDoors = null;
            Room chosenAnotherRoom = null;
            for(Room anotherRoom : rooms) {
                if(room.equals(anotherRoom)) {
                    continue;
                }
                java.util.Map.Entry<Point, Point> doors = room.closestUnusedDoorsInCorrectDirectionForRooms(anotherRoom);
                if(doors == null) {
                    continue;
                }
                int doorDistance = doors.getKey().distanceTo(doors.getValue());
                if(doorDistance < minimalDoorDistance) {
                    minimalDoorDistance = doorDistance;
                    minimalDoors = doors;
                    chosenAnotherRoom = anotherRoom;
                }
            }
            if(minimalDoors != null) {
                Logging.logger.log(Level.INFO, "door1: "+ minimalDoors.getKey()+" door2: "+minimalDoors.getValue());
                diggers.add(new CorridorDigger(minimalDoors.getKey(), minimalDoors.getValue(), this, rooms, random));
                room.useDoor(minimalDoors.getKey());
            }
        }
        runAllDiggers(diggers, getAllowedDiggersOperations());
        failedDiggers.addAll(diggers.stream().filter(d -> !d.diggerFinished()).toList());
        diggers = diggers.stream().filter(CorridorDigger::diggerFinished).toList();
        digCorridors(diggers);
        updateCorridorPoints(diggers);
    }

    public void placeGenerateItem(Point point) {
        if (random.nextInt(1, 101) <= itemGenerationChance) {
            Item item = RandomChoice.choose(random, ItemList.getCorrectedItemSpawnList(randomOverride)).build();
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
        for(int y = 0;y<height;++y) {
            for(int x = 0;x<width;++x) {
                Point point = new Point(x,y);
                if(getFeature(point) instanceof Tile tile) {
                    if(canPlaceActor(actor,point)) {
                        return point;
                    }
                }
            }
        }
        return new Point(0,0);
    }

    public void placePlayer() {
        Point alphaPoint = null;
        for(int y = 0; y<height;++y) {
            for(int x = 0;x<width;++x) {
                Point newPoint = new Point(x,y);
                if(getFeature(newPoint) instanceof MapLeave mapLeave) {
                    if(mapLeave.getDirection() == -1) { //find position of alpha
                        alphaPoint = newPoint;
                    }
                }
            }
        }
        if(alphaPoint == null) {
            Logging.logger.log(Level.SEVERE, "Map without alpha!");
            return;
        }
        for(int y = 0; y<height;++y) {
            for(int x = 0;x<width;++x) {
                Point newPoint = new Point(x,y);
                if(canPlaceActor(getPlayer(), newPoint) && newPoint.distanceTo(alphaPoint) < 3) {
                    System.out.println(newPoint+" is player point");
                    placeActor(getPlayer(), newPoint);
                    return;
                }
            }
        }
    }

    public void generateMonsters() {
        for(int y = 0;y<getHeight();++y) {
            for(int x = 0;x<getWidth();++x) {
                int r = random.nextInt(1, 101);

                if(r<=monsterSpawnChance) {
                    Point place = new Point(x,y);
                    Monster monster = RandomChoice.choose(random,ActorList.getCorrectedMonsterSpawnList(randomOverride)).build();
                    if(canPlaceActor(monster, place) && place.distanceTo(player.getPosition()) > monsterSpawnPlayerDistance) {
                        placeActor(monster, place);
                    }
                }
            }
        }
    }

    public void generateStartAndFinish(List<Room> roomsOriginal) {
        List<Room> rooms = roomsOriginal.stream().filter(r -> !r.getUnconnectedDoors().isEmpty()).toList();
        Room randomStartRoom = RandomChoice.chooseEvenly(random, rooms);
        List<Room> otherRooms = new ArrayList<>(rooms);
        otherRooms.remove(randomStartRoom);
        Room randomEndRoom = RandomChoice.chooseEvenly(random, otherRooms);

        Point startDoor = RandomChoice.chooseEvenly(random, randomStartRoom.getUnconnectedDoors());
        Point endDoor = RandomChoice.chooseEvenly(random, randomEndRoom.getUnconnectedDoors());

        placeMapElement(new MapLeave().toBuilder().direction(-1).symbol('ɑ').build(), startDoor);
        placeMapElement(new MapLeave().toBuilder().build(), endDoor);
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
        placePlayer();
        generateMonsters();
        return this;
    }

    public Actor getActorAtPosition(Point position) {
        if(getFeature(position) instanceof Tile tile) {
            return tile.getActor();
        }
        return null;
    }

    public List<Actor> getAllActors() {
        //TODO: list of actors
        List<Actor> actors = new ArrayList<>();
        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                MapElement mapElement = getFeature(new Point(x,y));
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

    public void activateNearbyUpgraders(Point position, Actor actor) {
        if(actor.getBackpack().isEmpty()) {
            return;
        }
        for(int x = -1;x<=1;++x) {
            for(int y = -1;y<=1;++y) {
                if(getFeature(position.offset(new Point(x,y))) instanceof Upgrader upgrader) {
                    upgrader.upgradeBackpackItem(actor, actor.getRandomItemInBackpack());
                }
            }
        }
    }

    public boolean actorsPresent(Point point) {
        if(getFeature(point) instanceof Tile tile) {
            return tile.getActor() != null;
        }
        return false;
    }

    public void moveActor(Actor actor, Point destination) {
        int xMovement = destination.getX()-actor.getPosition().getX();
        int yMovement = destination.getY()-actor.getPosition().getY();

        moveActor(actor, xMovement, yMovement);
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

        Point point = new Point(x,y);

        if (canPlaceActor(actor, point)) {
            removeActor(actor.getPosition());
            actor.setPosition(point);
            placeActor(actor, point);
            if(!actor.immuneToUpgrader()) {
                activateNearbyUpgraders(point, actor);
            }
        }
        else if(actor instanceof Player && getFeature(point) instanceof MapLeave mapLeave) {
            setMoveBetweenMaps(mapLeave.getDirection());
        }
        else {
            Logging.logger.log(Level.WARNING, "Can't move " + actor.toString() + " to " + x + ", " + y);
        }
    }

    public void placeActor(Actor actor, Point point) {
        if (canPlaceActor(actor, point)) {
            Tile tile = (Tile) map.get(point.getY()).get(point.getX());
            tile.setActor(actor);
            actor.setPosition(point);

            if (actor.isCanPickItems() && tile.hasItems()) {
                tile.transferItemsToActor(actor);
            }
        }
    }

    public void removeActor(Point point) {
        if (getFeature(point) instanceof Tile tile) {
            tile.removeActor();
        }
    }

    public boolean canPlaceActor(Actor actor, Point point) {
        if (getFeature(point) instanceof Tile tile) {
            return tile.getActor() == null;
        }
        return false;
    }

    public boolean canMoveThrough(Actor actor, Point point) {
        return canPlaceActor(actor, point) || getFeature(point) instanceof Door || actor instanceof Player && getFeature(point) instanceof MapLeave;
    }

    public boolean canShootThrough(Point point) {
        return canPlaceActor(player, point) || actorsPresent(point);
    }

    public void actorAttacked(Actor actor) {
        if(!actor.isAlive()) {
            placeItems(actor.getAllItems().stream().filter(Item::isDropsFromDead).collect(Collectors.toList()), actor.getPosition());
            removeActor(actor.getPosition());
        }
    }

    public List<Item> getAllItems() {
        List<Item> items = new ArrayList<>();
        for (int x = 0; x < map.size(); x++) {
            for (int y = 0; y < map.get(x).size(); y++) {
                if (getFeature(new Point(x,y)) instanceof Tile tile) {
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

    public void placeItems(List<Item> items, Point point) {
        for(Item item : items) {
            placeItem(item, point);
        }

    }

    public void removeAllItems(Point point) {
        if (getFeature(point) instanceof Tile tile) {
            tile.removeAllItems();
        }
    }

    public void removeItem(Item item, Point point) {
        if (getFeature(point) instanceof Tile tile) {
            tile.removeItem(item);
        }
    }

    public boolean canPlaceItem(Item item, Point point) {
        return true;
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

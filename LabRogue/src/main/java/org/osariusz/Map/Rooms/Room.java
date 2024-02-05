package org.osariusz.Map.Rooms;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.osariusz.GameElements.Spawnable;
import org.osariusz.Map.Map;
import org.osariusz.MapElements.*;
import org.osariusz.Utils.Logging;
import org.osariusz.Utils.Point;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.stream.Collectors;

@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public class Room extends Spawnable {
    //TODO: Path generation optimization
    protected int width;

    protected int height;

    protected Point startPoint;

    public int getStartX() {
        return startPoint.getX();
    }

    public int getStartY() {
        return startPoint.getY();
    }

    protected int roomBordersSize;

    protected List<Point> doors;
    protected List<Point> usedDoors;
    protected List<Point> connectedDoors;

    protected int upgraderChance; //in %


    public MapElement getRoomSpecificFeature(Point point, Random random) {
        boolean notNearSpecial = true;
        for (int xOffset = -1; xOffset <= 1; ++xOffset) {
            for (int yOffset = -1; yOffset <= 1; ++yOffset) {
                Point newPoint = point.offset(new Point(xOffset, yOffset));
                if (doorPosition(newPoint) || wallPosition(newPoint)) {
                    notNearSpecial = false;
                    break;
                }
            }
            if (!notNearSpecial) {
                break;
            }
        }
        if (notNearSpecial) {
            int randomNumber = random.nextInt(1, 101);
            if (randomNumber <= upgraderChance) {
                return new Upgrader().toBuilder().build();
            }
        }
        return new Tile().toBuilder().build();
    }

    public boolean pointInside(int x, int y) {
        return x >= getStartX() && x < getStartX() + getWidth() && y >= getStartY() && y < getStartY() + getHeight();
    }

    public boolean pointIsDoor(Point point) {
        return point.offsetReversed(startPoint).pointInList(doors);
    }

    public boolean integralWallPart(Point point) {
        point = point.offsetReversed(startPoint);
        if (point.pointInList(getDoors())) {
            return false;
        }
        if (point.getY() >= roomBordersSize - 1 && point.getY() <= getHeight() - roomBordersSize && point.getX() >= roomBordersSize - 1 && point.getX() <= getWidth() - roomBordersSize) {
            return true;
        }
        return false;
    }

    public boolean canPlace(List<Room> rooms, Map map) {
        for (int x = getStartX(); x < getWidth() + getStartX(); ++x) {
            for (int y = getStartY(); y < getHeight() + getStartY(); ++y) {
                if (x >= map.getWidth()) {
                    return false;
                }
                if (y >= map.getHeight()) {
                    return false;
                }
                if (map.isSpaceTaken(rooms, x, y)) {
                    return false;
                }
                if (map.checkPath(rooms, new Point(x, y))) {
                    return false;
                }
            }
        }
        return true;
    }

    public Point nearestCentrePoint() {
        return startPoint.offset(new Point(width / 2, height / 2));
    }

    public boolean doorPosition(Point point) {
        return point.pointInList(doors);
    }

    public boolean wallPosition(Point point) {
        return point.getX() <= (roomBordersSize - 1) || point.getX() >= width - roomBordersSize || point.getY() <= (roomBordersSize - 1) || point.getY() >= height - roomBordersSize;
    }

    public MapElement getFeature(Point point, Random random) {
        if (doorPosition(point)) {
            return new Door().toBuilder().build();
        }
        if (wallPosition(point)) {
            return new Wall().toBuilder().build();
        }
        return getRoomSpecificFeature(point, random);
    }

    public void useDoor(Point point) {
        Point doorPoint = point.offsetReversed(startPoint);
        if (!doorPoint.pointInList(usedDoors)) {
            usedDoors.add(doorPoint);
        }
        connectDoor(point);
    }

    public void connectDoor(Point point) {
        Point doorPoint = point.offsetReversed(startPoint);
        if (!doorPoint.pointInList(connectedDoors)) {
            connectedDoors.add(doorPoint);
        }
    }

    public List<Point> getAvailableDoors() {
        List<Point> result = new ArrayList<>(doors);
        result.replaceAll(p -> p.offset(startPoint));
        return result;
    }

    public List<Point> getUnconnectedDoors() {
        List<Point> result = doors.stream().filter(p -> p.pointInList(doors) && !p.pointInList(connectedDoors)).collect(Collectors.toList());
        result.replaceAll(p -> p.offset(startPoint));
        return result;
    }


    public java.util.Map.Entry<Point, Point> closestUnusedDoorsInCorrectDirectionForRooms(Room room) {
        List<Point> thisDoors = getAvailableDoors();
        List<Point> hisDoors = room.getAvailableDoors();


        if (thisDoors.isEmpty()) {
            Logging.logger.log(Level.WARNING, "No unused door found for room1 in closestUnusedDoorForRoom " + this);
            return null;
        }
        if (hisDoors.isEmpty()) {
            Logging.logger.log(Level.WARNING, "No unused door found for room2 in closestUnusedDoorForRoom " + room);
            return null;
        }

        double minimalDistance = Double.MAX_VALUE;
        Point minimalThisPoint = null;
        Point minimalHisPoint = null;

        for (Point thisDoor : thisDoors) {
            for (Point hisDoor : hisDoors) {
                Point thisDoorRoomDirection = new Point(thisDoor.getX() - nearestCentrePoint().getX(), thisDoor.getY() - nearestCentrePoint().getY());
                if (!Map.checkIfDoorInThisDirection(thisDoor, thisDoorRoomDirection, hisDoor)) {
                    continue;
                }
                if (thisDoor.distanceTo(hisDoor) < minimalDistance) {
                    minimalDistance = thisDoor.distanceTo(hisDoor);
                    minimalThisPoint = thisDoor;
                    minimalHisPoint = hisDoor;
                }
            }
        }

        return new AbstractMap.SimpleEntry<>(minimalThisPoint, minimalHisPoint);

    }

    public void initializeDoors() {
        this.doors = new ArrayList<>(List.of(
                new Point(width / 2, roomBordersSize - 1),
                new Point(width - roomBordersSize, height / 2),
                new Point(width / 2, height - roomBordersSize),
                new Point(roomBordersSize - 1, height / 2)
        ));
        this.usedDoors = new ArrayList<>();
        this.connectedDoors = new ArrayList<>();
    }

    @Override
    public void initializeDefaults() {
        super.initializeDefaults();
        super.id = "generic_room";
        super.spawnChance = 10;
        this.width = 7;
        this.height = 7;
        this.roomBordersSize = 2; //minimum is 2
        this.doors = null;
        this.usedDoors = null;
        this.upgraderChance = 5;
    }

    private static final class RoomBuilderImpl extends RoomBuilder<Room, RoomBuilderImpl> {
        public Room build() {
            Room result = new Room(this);
            result.initializeDoors();
            return result;
        }
    }

    @Override
    public String toString() {
        return id + " start: " + startPoint;
    }
}

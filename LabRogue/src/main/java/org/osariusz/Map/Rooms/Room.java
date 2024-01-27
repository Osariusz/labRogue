package org.osariusz.Map.Rooms;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.osariusz.GameElements.Spawnable;
import org.osariusz.Map.Map;
import org.osariusz.MapElements.Door;
import org.osariusz.MapElements.MapElement;
import org.osariusz.MapElements.Tile;
import org.osariusz.MapElements.Wall;
import org.osariusz.Utils.Logging;
import org.osariusz.Utils.Point;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

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


    public MapElement getRoomSpecificFeature(int x, int y) {
        return new Tile().toBuilder().build();
    }

    public boolean pointInside(int x, int y) {
        return x>=getStartX() && x<getStartX()+getWidth() && y>=getStartY() && y<getStartY()+getHeight();
    }

    public double centerDistanceTo(Point point) {
        return point.distanceTo(nearestCentrePoint());
    }

    public boolean canPlace(List<Room> rooms, Map map) {
        for(int x = getStartX();x<getWidth()+getStartX();++x) {
            for(int y = getStartY();y<getHeight()+getStartY();++y) {
                if(x > map.getWidth()) {
                    return false;
                }
                if(y > map.getHeight()) {
                    return false;
                }
                if(map.isSpaceTaken(rooms, x,y)) {
                    return false;
                }
                //if(map.checkPath(rooms, x, y)) {
                //    return false;
                //}
            }
        }
        return true;
    }

    public boolean pointBetweenCenters(Room room, int x, int y) {
        Point first = nearestCentrePoint();
        Point second = room.nearestCentrePoint();

        int firstX = Math.min(first.getX(),second.getX());
        int firstY = Math.min(first.getY(),second.getY());

        int secondX = Math.max(first.getX(),second.getX());
        int secondY = Math.max(first.getY(),second.getY());

        return x >= firstX && x <= secondX && y >= firstY && y <= secondY;
    }

    public Point nearestCentrePoint() {
        return startPoint.offset(new Point(width/2,height/2));
    }

    public Room getClosestRoom(List<Room> rooms) {
        if(rooms.isEmpty() || (rooms.size() == 1 && rooms.get(0).equals(this))) {
            Logging.logger.log(Level.WARNING, "Room " +getStartX()+", "+getStartY()+" was passed an array with no other rooms to check distance");
            return this;
        }
        double minimumDistance = -1;
        Room result = null;
        for(Room room : rooms) {
            if(room.equals(this)) {
                continue;
            }
            double distance = centerDistanceTo(room.nearestCentrePoint());
            if(minimumDistance < 0 || minimumDistance > distance) {
                minimumDistance = distance;
                result = room;
            }

        }
        return result;
    }

    public boolean isRoomPathTo(Room room, int x, int y) {
        if(room.pointInside(x, y)) {
            return false;
        }
        if(!pointBetweenCenters(room, x , y)) {
            return false;
        }

        int workingCoordinate1, passiveCoordinate1;
        int workingCoordinate2, passiveCoordinate2;

        int workingCoordinate, passiveCoordinate;

        if((room.nearestCentrePoint().getX()- nearestCentrePoint().getX()==0) ||
                (room.nearestCentrePoint().getY()-nearestCentrePoint().getY())/(room.nearestCentrePoint().getX()- nearestCentrePoint().getX()) > 1) {
            workingCoordinate1 = nearestCentrePoint().getY();
            passiveCoordinate1 = nearestCentrePoint().getX();

            workingCoordinate2 = room.nearestCentrePoint().getY();
            passiveCoordinate2 = room.nearestCentrePoint().getX();

            workingCoordinate = y;
            passiveCoordinate = x;
        }
        else {
            workingCoordinate1 = nearestCentrePoint().getX();
            passiveCoordinate1 = nearestCentrePoint().getY();

            workingCoordinate2 = room.nearestCentrePoint().getX();
            passiveCoordinate2 = room.nearestCentrePoint().getY();

            workingCoordinate = x;
            passiveCoordinate = y;
        }

        return
                (passiveCoordinate == passiveCoordinate1 && workingCoordinate == (workingCoordinate1+workingCoordinate2)/2) ||
                (workingCoordinate == (workingCoordinate1+workingCoordinate2)/2) ||
                (passiveCoordinate == passiveCoordinate2 && workingCoordinate > (workingCoordinate1+workingCoordinate2)/2);

    }

    public MapElement getFeature(int x, int y) {
        if(new Point(x,y).pointInList(doors)) {
            return new Door().toBuilder().build();
        }
        if(x <= (roomBordersSize-1) || x >= width-roomBordersSize || y <= (roomBordersSize-1) || y >= height-roomBordersSize) {
            return new Wall().toBuilder().build();
        }
        return getRoomSpecificFeature(x,y);
    }

    public void useDoor(Point point) {
        usedDoors.add(point);
    }

    public List<Point> getUnusedDoors() {
        List<Point> result = new ArrayList<>(doors);
        result.removeAll(usedDoors);
        result.replaceAll(p -> p.offset(startPoint));
        return result;
    }

    public java.util.Map.Entry<Point, Point> closestUnusedDoorsForRooms(Room room) {
        List<Point> thisDoors = getUnusedDoors();
        List<Point> hisDoors = room.getUnusedDoors();

        //TODO: check why many doors often take the first room's first door

        if(thisDoors.isEmpty()) {
            Logging.logger.log(Level.WARNING,"No unused door found for room1 in closestUnusedDoorForRoom "+this);
            return null;
        }
        if(hisDoors.isEmpty()) {
            Logging.logger.log(Level.WARNING,"No unused door found for room2 in closestUnusedDoorForRoom "+room);
            return null;
        }

        double minimalDistance = Double.MAX_VALUE;
        Point minimalThisPoint = null;
        Point minimalHisPoint = null;

        for(Point thisDoor : thisDoors) {
            for(Point hisDoor : hisDoors) {
                if(thisDoor.distanceTo(hisDoor) < minimalDistance) {
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
                new Point(width/2, roomBordersSize-1),
                new Point(width-roomBordersSize, height/2),
                new Point(width/2,height-roomBordersSize),
                new Point(roomBordersSize-1, height/2)
        ));
        this.usedDoors = new ArrayList<>();
    }

    @Override
    public void initializeDefaults() {
        super.initializeDefaults();
        super.id = "generic_room";
        super.spawnChance = 10;
        this.width = 7;
        this.height = 7;
        this.roomBordersSize = 2;
        this.doors = null;
        this.usedDoors = null;
    }

    private static final class RoomBuilderImpl extends RoomBuilder<Room, RoomBuilderImpl> {
        public Room build() {
            Room result = new Room(this);
            result.initializeDoors();
            return result;
        }
    }

}

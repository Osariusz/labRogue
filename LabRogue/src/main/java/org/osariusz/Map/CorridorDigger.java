package org.osariusz.Map;

import lombok.Getter;
import lombok.extern.java.Log;
import org.osariusz.Map.Rooms.Room;
import org.osariusz.Utils.Logging;
import org.osariusz.Utils.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

@Getter
public class CorridorDigger {

    private final List<Point> corridorPoints = new ArrayList<>();

    private final List<Point> forbiddenPoints = new ArrayList<>();

    Map map;

    List<Room> rooms;

    Point destination;

    Point startPoint;

    Random random = new Random();

    boolean reverted = false;

    public CorridorDigger(Point startPoint, Point destination, Map map, List<Room> rooms) {
        this.startPoint = startPoint;
        corridorPoints.add(startPoint);
        this.destination = destination;
        this.map = map;
        this.rooms = rooms;
    }

    public CorridorDigger(Point startPoint, Point destination, Map map, List<Room> rooms, Random random) {
        this(startPoint, destination, map, rooms);
        this.random = random;
    }

    public Point getLastPoint() {
        if (corridorPoints.isEmpty()) {
            Logging.logger.log(Level.WARNING, this+" points size is 0");
            return new Point(0, 0);
        }
        return corridorPoints.get(corridorPoints.size() - 1);
    }


    public Point getNewPointByAxis(Point movingDirection, boolean moveY) {
        Point newPoint = null;
        if(moveY || movingDirection.getX() == 0) {
            newPoint = new Point(getLastPoint().getX(), getLastPoint().getY() + movingDirection.getY());
        }
        else {
            newPoint = new Point(getLastPoint().getX() + movingDirection.getX(), getLastPoint().getY());
        }
        return newPoint;
    }

    public Point validNewPoint(List<Point> newPoints) {
        for(Point newPoint : newPoints) {
            boolean validPoint = true;
            if(newPoint.pointInList(forbiddenPoints)) {
                continue;
            }
            if (map.outOfBounds(newPoint)) {
                continue;
            }
            for(Room room : rooms) {
                if(room.integralWallPart(newPoint)) {
                    validPoint = false;
                    break;
                }
            }
            if(!validPoint) {
                continue;
            }
            if(!newPoint.pointInList(corridorPoints)) {
                return newPoint;
            }
        }
        return null;
    }

    public void revert() {
        reverted = true;
        Logging.logger.log(Level.WARNING, "Corridor Digger can't find route "+this);
        forbiddenPoints.add(getLastPoint());
        corridorPoints.remove(getLastPoint());
    }

    public void stepTowardChosenDirection(Point movingDirection) {
        reverted = false;
        //TODO: add to avoid going through rooms
        boolean moveY = random.nextBoolean();
        Point newPoint = validNewPoint(new ArrayList<>(List.of(
                getNewPointByAxis(movingDirection, moveY),
                getNewPointByAxis(movingDirection, !moveY),
                getNewPointByAxis(movingDirection.multiplyPoints(new Point(-1,-1)),moveY),
                getNewPointByAxis(movingDirection.multiplyPoints(new Point(-1,-1)),!moveY),
                getNewPointByAxis(new Point(1,1),moveY),
                getNewPointByAxis(new Point(1,1),!moveY),
                getNewPointByAxis(new Point(-1,-1),moveY),
                getNewPointByAxis(new Point(-1,-1),!moveY)
        )));

        if(newPoint == null) {
            revert();
            return;
        }
        Logging.logger.log(Level.INFO, this+" with moving direction of "+ movingDirection+" moved to "+newPoint);
        corridorPoints.add(newPoint);
    }

    public boolean diggerFinished() {
        if(getLastPoint().equals(destination) || corridorPoints.isEmpty()) {
            return true;
        }
        return false;
    }

    public boolean validDestination(Map map, Point realPoint, List<Point> othersCorridorPoints) {
        if(realPoint.pointInList(othersCorridorPoints)) {
            return true;
        }
        for(Room room : map.getRooms()) {
            if(room.pointIsDoor(realPoint) && !room.pointInside(getCorridorPoints().get(0).getX(), getCorridorPoints().get(0).getY())) {
                return true;
            }
        }
        return false;
    }

    public void updateDestination(Map map, List<Point> othersCorridorPoints) {
        if(corridorPoints.get(0).getX() == 106 && corridorPoints.get(0).getY() == 7) {
            int h= 0;
        }
        int diggerSightRadius = 5;
        for(int y = -diggerSightRadius;y<diggerSightRadius;++y) {
            for(int x = -diggerSightRadius;x<diggerSightRadius;++x) {
                Point realPoint = getLastPoint().offset(new Point(x,y));
                if(validDestination(map, realPoint, othersCorridorPoints) && (getLastPoint().distanceTo(destination) > getLastPoint().distanceTo(realPoint))) {
                    Logging.logger.log(Level.INFO, this+" found new destination "+realPoint);
                    destination = realPoint;
                }
            }
        }
    }

    public void stepForward() {
        if(diggerFinished()) {
            return;
        }
        Point movingDirection = getLastPoint().movingDirection(destination);
        stepTowardChosenDirection(movingDirection);
    }

    @Override
    public String toString() {
        Point start = null;
        if(!corridorPoints.isEmpty()) {
            start = corridorPoints.get(0);
        }
        return "CorridorDigger starting at"+ start +" with destination "+destination;
    }
}

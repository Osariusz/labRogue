package org.osariusz.Map;

import lombok.Getter;
import org.osariusz.Utils.Logging;
import org.osariusz.Utils.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

@Getter
public class CorridorDigger {

    private final List<Point> corridorPoints = new ArrayList<>();

    Point destination;

    Random random = new Random();

    public CorridorDigger(Point startPoint, Point destination) {
        corridorPoints.add(startPoint);
        this.destination = destination;
    }

    public CorridorDigger(Point startPoint, Point destination, Random random) {
        this(startPoint, destination);
        this.random = random;
    }

    public Point getLastPoint() {
        if (corridorPoints.isEmpty()) {
            Logging.logger.log(Level.WARNING, "CorridorDigger points size is 0");
            return new Point(0, 0);
        }
        return corridorPoints.get(corridorPoints.size() - 1);
    }


    public Point getNewPointByAxis(Point movingDirection, boolean moveY) {
        Point newPoint = null;
        if (!moveY) {
            newPoint = new Point(getLastPoint().getX() + movingDirection.getX(), getLastPoint().getY());
        } else {
            newPoint = new Point(getLastPoint().getX(), getLastPoint().getY() + movingDirection.getY());
        }
        return newPoint;
    }

    public Point validNewPoint(List<Point> newPoints) {
        for(Point newPoint : newPoints) {
            if(corridorPoints.stream().filter(
                    p -> p.getX() == newPoint.getX() && p.getY() == newPoint.getY()
            ).toList().isEmpty()) {
                return newPoint;
            }
        }
        return null;
    }


    public void stepTowardChosenDirection(Point movingDirection) {
        //TODO: add to avoid going through rooms
        boolean moveY = random.nextBoolean();
        Point newPoint = validNewPoint(new ArrayList<>(List.of(
                getNewPointByAxis(movingDirection, moveY),
                getNewPointByAxis(movingDirection.multiplyPoints(new Point(0,1)),moveY),
                getNewPointByAxis(movingDirection.multiplyPoints(new Point(1,0)),moveY),
                getNewPointByAxis(movingDirection.multiplyPoints(new Point(-1,-1)),moveY),
                getNewPointByAxis(movingDirection.multiplyPoints(new Point(-1,-1)),!moveY)
        )));

        if(newPoint == null) {
            Logging.logger.log(Level.WARNING, "Corridor Digger can't find route "+this);
        }
    }

    public boolean diggerFinished() {
        if(getLastPoint().equals(destination)) {
            return true;
        }
        return false;
    }

    public void updateDestination(Map map, List<Point> othersCorridorPoints) {
        int diggerSightRadius = 5;
        for(int y = -diggerSightRadius;y<diggerSightRadius;++y) {
            for(int x = -diggerSightRadius;x<diggerSightRadius;++x) {
                Point realPoint = getLastPoint().offset(new Point(x,y));
                if(realPoint.pointInList(othersCorridorPoints) && getLastPoint().distanceTo(destination) > getLastPoint().distanceTo(realPoint)) {
                    destination = realPoint;
                }
            }
        }
    }

    public void stepForward() {
        Point movingDirection = getLastPoint().movingDirection(destination);

        if (movingDirection.getY() != 0 && movingDirection.getX() != 0) {
            stepTowardChosenDirection(movingDirection);
        } else {
            corridorPoints.add(getLastPoint().offset(movingDirection));
        }
    }

}

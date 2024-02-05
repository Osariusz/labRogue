package org.osariusz.Utils;

import lombok.Getter;
import org.osariusz.MapElements.Upgrader;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.function.Supplier;

@Getter
public class Point {

    int x, y;

    public boolean pointInList(List<Point> points) {
        return !points.stream().filter(this::equals).toList().isEmpty();
    }

    public int distanceTo(Point point) {
        return Math.abs(x-point.x)+Math.abs(y-point.y);
    }

    public List<Point> getAdjacentTiles() {
        List<Point> result = new ArrayList<>();
        for(int x = -1;x<=1;++x) {
            for(int y = -1;y<=1;++y) {
                result.add(this.offset(new Point(x,y)));
            }
        }
        return result;
    }

    public List<Point> getNeighbors() {
        return new ArrayList<>(List.of(
                this.offset(new Point(0,-1)),
                this.offset(new Point(0,1)),
                this.offset(new Point(1,0)),
                this.offset(new Point(-1,0))
        ));
    }

    public List<Point> bfsTo(Point destination, int pathLength, Function<Point, Boolean> validPointCheck) {
        int limit = pathLength+1;

        Queue<List<Point>> nextPoints = new LinkedList<>();
        List<Point> visited = new ArrayList<>();
        nextPoints.add(List.of(this));
        while(!nextPoints.isEmpty()) {
            List<Point> nextPointList = nextPoints.remove();
            Point nextPoint = nextPointList.get(nextPointList.size()-1);
            if(nextPoint.pointInList(visited) || nextPointList.size() > limit) {
                continue;
            }
            visited.add(nextPoint);
            if(nextPoint.equals(destination)) {
                return nextPointList;
            }
            for(Point neighbor : nextPoint.getNeighbors()) {
                if(!neighbor.pointInList(visited) && validPointCheck.apply(neighbor)) {
                    List<Point> newList = new ArrayList<>(nextPointList);
                    newList.add(neighbor);

                    nextPoints.add(newList);
                }
            }
        }
        return null;
    }

    public Point offset(Point pointOffset) {
        return new Point(x+pointOffset.getX(), y+pointOffset.getY());
    }

    public Point offsetReversed(Point pointOffset) {
        return offset(pointOffset.reverse());
    }

    public Point reverse() {
        return multiplyPoints(new Point(-1, -1));
    }

    public Point multiplyPoints(Point multiply) {
        return new Point(x*multiply.getX(), y*multiply.getY());
    }

    public Point movingDirection(Point destination) {
        int xDest = 0;
        int yDest = 0;
        if(destination.getX() > getX()) {
            xDest = 1;
        }
        else if(destination.getX() < getX()) {
            xDest = -1;
        }

        if(destination.getY() > getY()) {
            yDest = 1;
        }
        else if(destination.getY() < getY()) {
            yDest = -1;
        }
        return new Point(xDest, yDest);
    }

    public boolean equals(Point point) {
        return this.getX() == point.getX() && this.getY() == point.getY();
    }

    @Override
    public String toString() {
        return "Point: "+getX()+", "+getY();
    }

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

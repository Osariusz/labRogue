package org.osariusz.Utils;

import lombok.Getter;

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

    public List<Point> bfsTo(Point destination, int limit, Function<Point, Boolean> checkPoint) {
        Queue<List<Point>> nextPoints = new LinkedList<>();
        List<Point> visited = new ArrayList<>();
        nextPoints.add(List.of(this));
        while(!nextPoints.isEmpty()) {
            List<Point> nextPointList = nextPoints.remove();
            Point nextPoint = nextPointList.get(nextPointList.size()-1);
            if(nextPoint.pointInList(visited) || nextPointList.size() >= limit) {
                continue;
            }
            visited.add(nextPoint);
            if(nextPoint.equals(destination)) {
                return nextPointList;
            }
            List<Point> neighbors = new ArrayList<>(List.of(
                    nextPoint.offset(new Point(0,-1)),
                    nextPoint.offset(new Point(0,1)),
                    nextPoint.offset(new Point(1,0)),
                    nextPoint.offset(new Point(-1,0))
            ));
            for(Point neighbor : neighbors) {
                if(!neighbor.pointInList(visited) && checkPoint.apply(neighbor)) {
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

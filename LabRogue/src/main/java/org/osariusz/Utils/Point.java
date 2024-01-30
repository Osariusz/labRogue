package org.osariusz.Utils;

import lombok.Getter;

import java.util.List;

@Getter
public class Point {

    int x, y;

    public boolean pointInList(List<Point> points) {
        return !points.stream().filter(this::equals).toList().isEmpty();
    }

    public int distanceTo(Point point) {
        return Math.abs(x-point.x)+Math.abs(y-point.y);
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

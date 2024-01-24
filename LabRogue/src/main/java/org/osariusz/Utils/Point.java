package org.osariusz.Utils;

import lombok.Getter;

@Getter
public class Point {

    int x, y;

    public double distanceTo(Point point) {
        return Math.sqrt(Math.pow(getX()-point.getX(),2)+Math.pow(getY()-point.getY(),2));
    }

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

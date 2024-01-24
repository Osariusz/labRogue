package org.osariusz.Map.Rooms;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.osariusz.Map.Map;
import org.osariusz.MapElements.MapElement;
import org.osariusz.MapElements.Tile;
import org.osariusz.MapElements.Wall;
import org.osariusz.Utils.Logging;
import org.osariusz.Utils.Point;

import java.util.List;
import java.util.logging.Level;

@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public class Room {
    {
        initializeDefaults();
    }

    protected int width;

    protected int height;

    protected int startX;
    protected int startY;

    protected boolean roomBorders;

    public MapElement getRoomSpecificFeature(int x, int y) {
        return new Tile().toBuilder().build();
    }

    public boolean pointInside(int x, int y) {
        return x>=getStartX() && x<=getStartX()+getWidth() && y>=getStartY() && y<=getStartY()+getHeight();
    }

    public double centerDistanceTo(Point point) {
        return point.distanceTo(nearestCentrePoint());
    }

    public boolean canPlace(List<Room> rooms, Map map, int positionX, int positionY) {
        for(int x = positionX;x<getWidth()+positionX;++x) {
            for(int y = positionY;y<getHeight()+positionY;++y) {
                if(map.isSpaceTaken(rooms, x,y)) {
                    return false;
                }
                if(map.checkPath(rooms, x, y)) {
                    return false;
                }
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
        return new Point(startX+(width/2),startY+(height/2));
    }

    public Room getClosestRoom(List<Room> rooms) {
        if(rooms.isEmpty()) {
            Logging.logger.log(Level.WARNING, "Room was passed an empty array of rooms to check distance");
            return this;
        }
        double minimumDistance = centerDistanceTo(rooms.get(0).nearestCentrePoint());
        Room result = rooms.get(0);
        for(Room room : rooms) {
            minimumDistance = Math.min(minimumDistance, centerDistanceTo(room.nearestCentrePoint()));
            result = room;
        }
        return result;
    }

    public boolean isRoomPathTo(Room room, int x, int y) {

        if(!pointBetweenCenters(room, x , y)) {
            return false;
        }

        int workingCoordinate1, passiveCoordinate1;
        int workingCoordinate2, passiveCoordinate2;

        int workingCoordinate, passiveCoordinate;

        if((room.nearestCentrePoint().getY()-nearestCentrePoint().getY())/(room.nearestCentrePoint().getX()- nearestCentrePoint().getX()) > 1) {
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
        if(roomBorders && (x == 0 || x == width-1 || y == 0 || y == height-1)) {
            return new Wall().toBuilder().build();
        }
        return getRoomSpecificFeature(x,y);
    }

    public void initializeDefaults() {
        this.width = 7;
        this.height = 7;
        this.roomBorders = true;
    }


}

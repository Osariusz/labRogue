package org.osariusz.Map.Rooms;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.osariusz.GameElements.Spawnable;
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
public class Room extends Spawnable {
    protected int width;

    protected int height;

    protected int startX;
    protected int startY;

    protected boolean roomBorders;

    public MapElement getRoomSpecificFeature(int x, int y) {
        return new Tile().toBuilder().build();
    }

    public boolean pointInside(int x, int y) {
        return x>=getStartX() && x<getStartX()+getWidth() && y>=getStartY() && y<getStartY()+getHeight();
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
        if(roomBorders && (x == 0 || x == width-1 || y == 0 || y == height-1)) {
            return new Wall().toBuilder().build();
        }
        return getRoomSpecificFeature(x,y);
    }

    @Override
    public void initializeDefaults() {
        super.initializeDefaults();
        super.id = "generic_room";
        super.spawnChance = 10;
        this.width = 7;
        this.height = 7;
        this.roomBorders = true;
    }

}

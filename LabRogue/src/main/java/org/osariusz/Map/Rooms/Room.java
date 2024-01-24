package org.osariusz.Map.Rooms;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.osariusz.MapElements.MapElement;
import org.osariusz.MapElements.Tile;
import org.osariusz.MapElements.Wall;

import java.util.List;

@Getter
@SuperBuilder
public class Room {
    {
        initializeDefaults();
    }

    protected int width;

    protected int height;

    protected boolean roomBorders;

    public MapElement getRoomSpecificFeature(int x, int y) {
        return new Tile().toBuilder().build();
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

package org.osariusz.Map.Rooms;

import org.osariusz.Actors.Monster;
import org.osariusz.Items.Item;
import org.osariusz.Items.Weapon;
import org.osariusz.Utils.SpawnHelper;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RoomsList {
    public static final List<Room.RoomBuilder<?, ?>> rooms = new ArrayList<>() {
        {
            add(new Room().toBuilder().width(7).height(7));
            add(new Room().toBuilder().width(10).height(4));
        }
    };
    public static List<Map.Entry<Integer, Room.RoomBuilder<?, ?>>> getRoomSpawnList() {
        return SpawnHelper.getSpawnList(rooms);
    }

    public static Room.RoomBuilder<?, ?> getRoom(String id) {
        return SpawnHelper.getGameElement(rooms, id);
    }
}

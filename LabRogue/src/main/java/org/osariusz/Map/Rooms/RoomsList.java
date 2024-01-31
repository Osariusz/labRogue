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
            add(new Room().toBuilder().id("10x10").width(10).height(10).upgraderChance(10));
            add(new Room().toBuilder().id("15x7").width(15).height(7));
            add(new Room().toBuilder().id("10x15").width(10).height(15));
            add(new Room().toBuilder().id("20x10").width(20).height(10));
            add(new Room().toBuilder().id("10x7").width(10).height(7));
            add(new Room().toBuilder().spawnChance(0).id("15x15_bunker_room").width(15).height(15).roomBordersSize(4));
        }
    };
    public static List<Map.Entry<Integer, Room.RoomBuilder<?, ?>>> getRoomSpawnList() {
        return SpawnHelper.getSpawnList(rooms);
    }

    public static List<Map.Entry<Integer, Room.RoomBuilder<?, ?>>> getCorrectedRoomSpawnList(List<Map.Entry<String, Integer>> correction) {
        return SpawnHelper.getCorrectedSpawnList(rooms, correction);
    }

    public static Room.RoomBuilder<?, ?> getRoom(String id) {
        return SpawnHelper.getGameElement(rooms, id);
    }
}

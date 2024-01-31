package org.osariusz.Utils;

import org.osariusz.GameElements.Spawnable;
import org.osariusz.Items.Item;
import org.osariusz.Map.Rooms.Room;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class SpawnHelper {
    public static <B extends Spawnable.SpawnableBuilder<?, ?>, G extends Spawnable> List<Map.Entry<Integer, B>> getSpawnList(List<B> entries) {
        List<Map.Entry<Integer, B>> result = new ArrayList<>();
        for(B builder : entries) {
            G exampleElement = (G) builder.build();
            result.add(new AbstractMap.SimpleEntry<>(exampleElement.getSpawnChance(),builder));
        }
        return result;
    }

    public static <B extends Spawnable.SpawnableBuilder<?,?>, G extends Spawnable> List<Map.Entry<Integer, B>> getCorrectedSpawnList(List<B> entries, List<Map.Entry<String, Integer>> correction) {
        List<Map.Entry<Integer, B>> base = getSpawnList(entries);
        List<Map.Entry<Integer, B>> result = new ArrayList<>();
        for(Map.Entry<Integer, B> baseEntry : base) {
            boolean found = false;
            for(Map.Entry<String, Integer> entry : correction) {
                if(baseEntry.getValue().build().getId().equals(entry.getKey())) {
                    result.add(new AbstractMap.SimpleEntry<>(baseEntry.getKey()+entry.getValue(),baseEntry.getValue()));
                    found = true;
                    break;
                }
            }
            if(found) {
                continue;
            }
            result.add(baseEntry);
        }
        return result;
    }

    public static <B extends Spawnable.SpawnableBuilder<?, ?>, G extends Spawnable> B getGameElement(List<B> entries, String id) {
        for(B builder : entries) {
            String e = builder.build().getId();
            if(builder.build().getId().equals(id)) {
                return builder;
            }
        }
        Logging.logger.log(Level.WARNING,"No gameElement with id: "+id+" found!");
        return null;
    }
}

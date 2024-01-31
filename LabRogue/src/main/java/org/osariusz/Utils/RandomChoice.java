package org.osariusz.Utils;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class RandomChoice {

    public static <T> T chooseEvenly(Random random, List<T> items) {
        List<Map.Entry<Integer, T>> list = items.stream().map(i -> new AbstractMap.SimpleEntry<>(1, i)).collect(Collectors.toList());
        return choose(random, list);
    }

    public static <T> T choose(Random random, List<Map.Entry<Integer, T>> list) {
        if(list == null) {
            Logging.logger.log(Level.WARNING, "Passed null instead of list to random choice");
            return null;
        }
        if(list.isEmpty()) {
            Logging.logger.log(Level.WARNING, "Empty list passed to RandomChoice");
            return null;
        }

        int max = 0;
        for (Map.Entry<Integer, T> integerObjectSimpleEntry : list) {
            max += integerObjectSimpleEntry.getKey();
        }

        int randomNumber = random.nextInt(1, max+1);

        int passed = 0;
        for(int i = 0;i<list.size();++i) {
            passed += list.get(i).getKey();
            if(randomNumber <= passed) {
                return list.get(i).getValue();
            }
        }
        return list.get(list.size()-1).getValue();
    }

    public static <T> T chooseExcludingItself(Random random, List<T> list, T itself) {
        List<T> newList = new ArrayList<T>(list);
        newList.remove(itself);

        List<Map.Entry<Integer, T>> toChoose = new ArrayList<>();
        for(T element: newList) {
            toChoose.add(new AbstractMap.SimpleEntry<>(1, element));
        }

        return choose(random, toChoose);
    }
}

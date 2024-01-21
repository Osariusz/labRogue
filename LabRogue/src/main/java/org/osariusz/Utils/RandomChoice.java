package org.osariusz.Utils;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RandomChoice {

    public static <T> T choose(Random random, List<Map.Entry<Integer, T>> list) {
        if(list.isEmpty()) {
            Logging.logger.log(Level.WARNING, "Empty list passed to ");
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
}

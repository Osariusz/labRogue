package org.osariusz;

import org.osariusz.Graphics.IO;
import org.osariusz.Map.Map;

import static org.osariusz.Utils.FightScenarios.*;

public class Main {
    public static void main(String[] args) {
        //testNewEnemies();
        Map map = Map.builder().width(100).height(10).build();
        IO display = new IO();
        display.IOLoop(map);
    }
}

package org.osariusz;

import org.osariusz.Graphics.IO;
import org.osariusz.Map.Map;

import static org.osariusz.Utils.FightScenarios.*;

public class Main {
    public static void main(String[] args) {
        //testNewEnemies();
        Map map = new Map.MapBuilder().setWidth(100).setHeight(100).buildMap().build();
        IO display = new IO();
        display.IOLoop(map);
    }
}

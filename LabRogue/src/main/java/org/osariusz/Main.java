package org.osariusz;

import org.osariusz.Graphics.IO;
import org.osariusz.Map.Map;

import static org.osariusz.Utils.FightScenarios.humanFightingAnomaly;

public class Main {
    public static void main(String[] args) {
        humanFightingAnomaly();
        //Map map = new Map.MapBuilder().setWidth(100).setHeight(100).buildMap().build();
        //IO display = new IO();
        //display.IOLoop(map);
    }
}
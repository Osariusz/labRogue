package org.osariusz;

import org.osariusz.Graphics.IO;
import org.osariusz.Map.Map;

public class Main {
    public static void main(String[] args) {
        Map map = new Map.MapBuilder().setWidth(100).setHeight(100).buildMap().build();
        IO display = new IO();
        display.IOLoop(map);
    }
}
package org.osariusz;

import org.osariusz.Actors.Player;
import org.osariusz.Graphics.IO;
import org.osariusz.Map.Map;

import static org.osariusz.Utils.FightScenarios.*;

public class Main {
    public static void main(String[] args) {
        //testNewEnemies();
        Player player = new Player().toBuilder().build();
        Map map = Map.builder().width(100).height(30).player(player).build();
        IO display = new IO();
        display.IOLoop(map);

    }
}

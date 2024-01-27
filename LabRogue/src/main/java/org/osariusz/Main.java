package org.osariusz;

import org.osariusz.Actors.Player;
import org.osariusz.Graphics.IO;
import org.osariusz.Map.Map;

import java.util.Random;

import static org.osariusz.Utils.FightScenarios.*;

public class Main {
    public static void main(String[] args) {
        Random random = new Random();
        random.setSeed(2138);
        //testNewEnemies();
        Player player = new Player().toBuilder().build();
        Map map = Map.builder().random(random).width(100).height(30).player(player).build();
        IO display = new IO();
        display.IOLoop(map);

    }
}

package org.osariusz.Game;

import org.osariusz.Actors.Player;
import org.osariusz.Graphics.IO;
import org.osariusz.Map.Map;

import java.util.*;

public class Game {

    IO display = new IO();

    String seed = "";

    String lastState = "menu";

    public void gameLoop() {
        while (true) {
            if(lastState.equals("menu")) {
                gameMenu();
            }
            else if(lastState.equals("play")) {
                play();
            }
            else if(lastState.equals("seed")) {
                seedSet();
            }
            else if(lastState.equals("exit")) {
                break;
            }
        }
    }

    public void gameMenu() {
        String command = display.displayMenuAndGetCommand();
        if(command.equals("play") || command.equals("1")) {
            lastState = "play";
        }
        if(command.equals("seed") || command.equals("2")) {
            lastState = "seed";
        }
        if(command.equals("exit") || command.equals("3")) {
            lastState = "exit";
        }
    }

    public void seedSet() {
        String newSeed = display.showSeedAndGetSeed(seed);
        if(newSeed.equals("reset")) {
            seed = "";
        }
        else if(!newSeed.isEmpty()) {
            seed = newSeed;
        }
        lastState = "menu";
    }

    public void play() {
        Random random = new Random();
        if(!seed.isEmpty()) {
            random.setSeed(seed.hashCode());
        }
        Player player = new Player().toBuilder().build();
        Map map = Map.builder().random(random).width(200).height(30).randomOverride(new ArrayList<>(List.of(
                new AbstractMap.SimpleEntry<>("15x15_bunker_room", 100000)
        ))).player(player).build();
        display.IOLoop(map);
        lastState = "menu";
    }
}

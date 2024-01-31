package org.osariusz.Game;

import org.osariusz.Actors.Player;
import org.osariusz.Graphics.IO;
import org.osariusz.Map.Map;
import org.osariusz.Utils.Logging;

import java.util.*;
import java.util.logging.Level;

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

    public void playLoop(Map map) {
        //display.displayMap(map);
        display.displayMapActor(map, map.getPlayer());
        display.displayNewFightReports(map);
        display.displayPlayerStats(map.getPlayer());
        display.displayPlayerEquipment(map.getPlayer());
        display.userInput(map);
        map.getPlayer().turn();
        map.actorsTurn();
        display.clearDisplay();
    }

    public void play() {
        int numberOfMaps = 6;
        int mapWidth = 200;
        int mapHeight = 30;
        Random random = new Random();
        if(!seed.isEmpty()) {
            random.setSeed(seed.hashCode());
        }
        Player player = new Player().toBuilder().build();
        List<Map> maps = new ArrayList<>();
        maps.add(Map.builder().random(random).width(mapWidth).height(mapHeight).player(player).build());
        int currentMap = 0;
        while(true) {
            if(maps.get(currentMap).getMoveBetweenMaps() != 0) {
                int move = maps.get(currentMap).getMoveBetweenMaps();
                maps.get(currentMap).setMoveBetweenMaps(0);
                currentMap += move;
            }
            if(currentMap < 0) {
                Logging.logger.log(Level.WARNING, "The map can't be lower than 0!");
                currentMap = 0;
            }
            if(currentMap >= numberOfMaps) {
                System.out.println("You won! :D");
                break;
            }
            if(currentMap >= maps.size()) {
                System.out.println("generating new map");
                maps.add(Map.builder().random(random).width(mapWidth).height(mapHeight).player(player).build());
            }
            if(!maps.get(currentMap).getPlayer().isAlive()) {
                System.out.println("You died! :(");
                break;
            }
            System.out.println(maps);
            playLoop(maps.get(currentMap));
        }
        lastState = "menu";
    }
}

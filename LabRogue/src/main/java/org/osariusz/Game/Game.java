package org.osariusz.Game;

import org.osariusz.Actors.Player;
import org.osariusz.Graphics.IO;
import org.osariusz.Items.ItemList;
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
        Random random = new Random();
        if(!seed.isEmpty()) {
            random.setSeed(seed.hashCode());
        }
        Player player = new Player().toBuilder().build();
        player.getBackpack().add(ItemList.getItem("rifle").build());

        List<Map.MapBuilder> builders = new ArrayList<>(List.of(
                Map.builder().random(random).width(50).height(20).player(player),
                Map.builder().random(random).width(200).height(30).player(player),
                Map.builder().random(random).width(150).height(40).player(player).randomOverride(new ArrayList<>(List.of(
                        new AbstractMap.SimpleEntry<>("15x15_bunker_room", 2),
                        new AbstractMap.SimpleEntry<>("statue", 1),
                        new AbstractMap.SimpleEntry<>("lighsaber",3),
                        new AbstractMap.SimpleEntry<>("rifle",2)
                ))),
                Map.builder().random(random).width(100).height(30).player(player).randomOverride(new ArrayList<>(List.of(
                        new AbstractMap.SimpleEntry<>("15x15_bunker_room", 1),
                        new AbstractMap.SimpleEntry<>("statue", 2),
                        new AbstractMap.SimpleEntry<>("dinosaur", 1),
                        new AbstractMap.SimpleEntry<>("rat", -5),
                        new AbstractMap.SimpleEntry<>("lighsaber",1),
                        new AbstractMap.SimpleEntry<>("hp_syringe",5),
                        new AbstractMap.SimpleEntry<>("sniper_rifle",2)
                ))),
                Map.builder().random(random).width(150).height(40).player(player).randomOverride(new ArrayList<>(List.of(
                        new AbstractMap.SimpleEntry<>("15x15_bunker_room", 2),
                        new AbstractMap.SimpleEntry<>("statue", 5),
                        new AbstractMap.SimpleEntry<>("rat", -10),
                        new AbstractMap.SimpleEntry<>("dinosaur", 3),
                        new AbstractMap.SimpleEntry<>("lighsaber",3),
                        new AbstractMap.SimpleEntry<>("rifle",2)
                ))),
                Map.builder().random(random).width(150).height(30).player(player).randomOverride(new ArrayList<>(List.of(
                        new AbstractMap.SimpleEntry<>("15x15_bunker_room", 2000),
                        new AbstractMap.SimpleEntry<>("statue", 10),
                        new AbstractMap.SimpleEntry<>("dinosaur", 10),
                        new AbstractMap.SimpleEntry<>("rat", -20),
                        new AbstractMap.SimpleEntry<>("rifle",10),
                        new AbstractMap.SimpleEntry<>("sniper_rifle",10)
                )))
        ));

        List<Map> maps = new ArrayList<>();
        maps.add(builders.get(0).build());
        int currentMap = 0;
        int move = -1;
        while(true) {
            if(maps.get(currentMap).getMoveBetweenMaps() != 0) {
                move = maps.get(currentMap).getMoveBetweenMaps();
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
                int builderIndex = currentMap;
                if(builderIndex >= builders.size()) {
                    builderIndex = builders.size()-1;
                }
                maps.add(builders.get(builderIndex).build());
            }
            if(!maps.get(currentMap).getPlayer().isAlive()) {
                System.out.println("You died! :(");
                break;
            }

            playLoop(maps.get(currentMap));
        }
        lastState = "menu";
    }
}

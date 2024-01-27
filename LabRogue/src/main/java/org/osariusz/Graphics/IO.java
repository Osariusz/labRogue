package org.osariusz.Graphics;

import org.osariusz.Actors.Actor;
import org.osariusz.Actors.Player;
import org.osariusz.Items.Equipment;
import org.osariusz.Items.Item;
import org.osariusz.Map.Map;
import org.osariusz.Utils.Logging;
import org.osariusz.Utils.Point;

import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;

public class IO {

    String helpText = """
            w s a d - move
            e [slotNumber] [slotName] - equip item on slotNumber from backpack to slotName
            de [slotNumber] [slotName] - deequip item on position slotNumber from slotName
            f [wsad] - fire/attack on selected position 
            """;

    public void displayMap(Map map) {
        for (int y = 0; y < map.getHeight(); y++) {
            for (int x = 0; x < map.getWidth(); x++) {
                System.out.print(map.getFeature(new Point(x,y)).getSymbol());
            }
            System.out.print("\n");
        }
    }

    public void displayMapActor(Map map, Actor actor) {
        int offset = actor.getSightRange()/2;
        Point start = actor.getPosition().offset(new Point(-offset, -offset));
        Point end = actor.getPosition().offset(new Point(offset, offset));
        for (int y = start.getY(); y < end.getY(); y++) {
            for (int x = start.getX(); x < end.getX(); x++) {
                System.out.print(map.getFeature(new Point(x,y)).getSymbol());
            }
            System.out.print("\n");
        }
    }

    public void displayPlayerEquipment(Player player) {
        System.out.println("Your equipment:");
        java.util.Map<String, List<Equipment>> equipment = player.getEquipment();
        for(String slot : equipment.keySet()) {
            System.out.println(slot+":");
            for(int i = 0;i<equipment.get(slot).size();++i) {
                System.out.println(i+". "+equipment.get(slot).get(i));
            }
        }
        System.out.println("Your backpack: ");
        List<Item> backpack = player.getBackpack();
        for(int i = 0;i<backpack.size();++i) {
            System.out.println((i+1)+". "+backpack.get(i).toString());
        }
        for (Item item : player.getBackpack()) {

        }
    }

    public void displayMapItems(Map map) {
        for (Item item : map.getAllItems()) {
            System.out.println(item.toString());
        }
    }

    public void displayMapActors(Map map) {
        for (Actor actor : map.getAllActors()) {
            System.out.println(actor.toString());
        }
    }

    public void clearDisplay() {
        try {
            String operatingSystem = System.getProperty("os.name"); // Check the current operating system

            if (operatingSystem.contains("Windows")) {
                // Runtime.getRuntime().exec("cls");
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                // Assuming Unix or Linux
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (final Exception e) {
            System.out.println(e.toString());
        }
    }

    public void userInput(Map map) {
        while (true) {
            Scanner s = new Scanner(System.in);
            String c = s.nextLine();
            switch (c) {
                case "w":
                    map.moveActor(map.getPlayer(), 0, -1);
                    break;
                case "s":
                    map.moveActor(map.getPlayer(), 0, 1);
                    break;
                case "a":
                    map.moveActor(map.getPlayer(), -1, 0);
                    break;
                case "d":
                    map.moveActor(map.getPlayer(), 1, 0);
                    break;
                case "help":
                    System.out.println(helpText);
                    continue;
                case "pos":
                    System.out.println("Player position: "+map.getPlayer().getPosition());
                    continue;
                default:
                    System.out.println("Wrong input!");
                    continue;
            }
            break;
        }
    }

    public void IOLoop(Map map) {
        while (true) {
            displayMap(map);
            //displayMapActor(map, map.getPlayer());
            displayPlayerEquipment(map.getPlayer());
            userInput(map);
            map.actorsTurn();
            clearDisplay();
        }

    }
}

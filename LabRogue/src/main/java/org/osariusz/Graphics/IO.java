package org.osariusz.Graphics;

import org.osariusz.Actors.Actor;
import org.osariusz.Actors.Player;
import org.osariusz.Items.Equipment;
import org.osariusz.Items.Item;
import org.osariusz.Map.Map;
import org.osariusz.Utils.Logging;

import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;

public class IO {
    public void displayMap(Map map) {
        for (int y = 0; y < map.getHeight(); y++) {
            for (int x = 0; x < map.getWidth(); x++) {
                System.out.print(map.getFeature(x, y).getSymbol());
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
        for (Item item : player.getBackpack()) {
            System.out.println(item.toString());
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
            int xMovement = 0;
            int yMovement = 0;
            switch (c) {
                case "w":
                    yMovement = -1;
                    break;
                case "s":
                    yMovement = 1;
                    break;
                case "a":
                    xMovement = -1;
                    break;
                case "d":
                    xMovement = 1;
                    break;
                default:
                    System.out.println("Wrong input!");
                    continue;
            }
            map.moveActor(map.getPlayer(), xMovement, yMovement);
            break;
        }
    }

    public void IOLoop(Map map) {
        while (true) {
            displayMap(map);
            displayPlayerEquipment(map.getPlayer());
            userInput(map);
            clearDisplay();
        }

    }
}

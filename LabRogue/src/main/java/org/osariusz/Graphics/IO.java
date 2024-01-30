package org.osariusz.Graphics;

import org.osariusz.Actors.Actor;
import org.osariusz.Actors.Player;
import org.osariusz.Items.Equipment;
import org.osariusz.Items.Item;
import org.osariusz.Map.Map;
import org.osariusz.Utils.Logging;
import org.osariusz.Utils.Point;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;

public class IO {

    String helpText = """
            help - shows this message
            rules - read the rules
            w s a d - move
            e [backpackSlotNumber] [slotName] [equipmentSlotNumber] - equip item on backpackSlotNumber from backpack to slotName on position equipmentSlotNumber
            de [slotName] [equipmentSlotNumber] - deequip item on position equipmentSlotNumber from slotName
            f [wsad] - fire/attack on selected position 
            u [backpackSlotNumber] - upgrade an item on backpackSlotNumber position in your backpack
            
            """;

    String rulesText = """
            You are a fugitive in a secret laboratory. You used to be a test subject, but luckily for you a containment breach happened and you are free.
            The only problem is that all the dangerous anomalies you were to be tested on are free as well!!!
            Try to escape by going through all the levels in the facility and try not to get killed by all the monsters.
            Beware of the Upgraders as they will upgrade a random item from your backpack if you are not protected!
            All of the game elements are symbolised by letters. Below is a glossary of all the elements.
            
            You can always get this message again by typing rules or get help with other command by typing help
            
            e - equipment
            p - passive equipment
            # - door
            i - item
            @ - You!
            U - upgrader
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
        java.util.Map<Actor.EquipmentSlots, List<Equipment>> equipment = player.getEquipment();
        for(Actor.EquipmentSlots slot : equipment.keySet()) {
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

    public String statisticString(int real, int original, String name) {
        int diff = real-original;
        String string = name+": "+real;
        if(diff != 0) {
            string += " (";
            string += diff;
            string += ")";
        }
        return string;
    }

    public void displayPlayerStats(Player player) {
        System.out.println("Player stats: ");

        String hpString = statisticString(player.getRealHP(), player.getHp(), "Hp");
        System.out.println(hpString);

        String agilityString = statisticString(player.getRealAgility(), player.getAgility(), "Agility");
        System.out.println(agilityString);
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
            Scanner input = new Scanner(System.in);
            String command = input.nextLine();
            String[] arguments = command.split(" ");
            try {
                command = arguments[0];
                switch (command) { //break means you lose a turn, continue means you don't
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
                    case "rules":
                        System.out.println(rulesText);
                        continue;
                    case "pos":
                        System.out.println("Player position: " + map.getPlayer().getPosition());
                        continue;
                    case "e":
                        map.getPlayer().equip(Integer.parseInt(arguments[1]), map.getPlayer().getEquipmentSlot(arguments[2]), Integer.parseInt(arguments[3]));
                        break;
                    case "de":
                        map.getPlayer().deequip(map.getPlayer().getEquipmentSlot(arguments[1]), Integer.parseInt(arguments[2]));
                        break;
                    default:
                        System.out.println("Wrong input!");
                        continue;
                }
            }
            catch (Exception e) {
                Logging.logger.log(Level.WARNING, "Can't read command exception "+ Arrays.toString(e.getStackTrace()));
                continue;
            }
            break;
        }
    }

    public void IOLoop(Map map) {
        while (true) {
            displayMap(map);
            //displayMapActor(map, map.getPlayer());
            displayPlayerStats(map.getPlayer());
            displayPlayerEquipment(map.getPlayer());
            userInput(map);
            map.actorsTurn();
            clearDisplay();
        }

    }
}

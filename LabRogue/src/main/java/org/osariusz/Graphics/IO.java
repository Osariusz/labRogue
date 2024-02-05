package org.osariusz.Graphics;

import org.osariusz.Actors.Actor;
import org.osariusz.Actors.Player;
import org.osariusz.Items.Equipment;
import org.osariusz.Items.Item;
import org.osariusz.Map.Map;
import org.osariusz.Utils.FightReport;
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
            t [backpackSlotNumber] - throw item at backpackSlotNumber on the ground
            targets - prints a list of available targets
            skip - skips one turn
            """;

    String rulesText = """
            You are a fugitive in a secret laboratory. You used to be a test subject, but luckily for you a containment breach happened and you are free.
            The only problem is that all the dangerous anomalies you were to be tested on are free as well!!!
            Try to escape by going through all the levels in the facility and try not to get killed by all the monsters.
            You die when your hp is lower than 0 for 3 consecutive turns!
            Beware of the Upgraders as they will upgrade a random item from your backpack if you are not protected!
            All of the game elements are symbolised by letters. Below is a glossary of all the elements.
            
            You can always get this message again by typing rules or get help with other command by typing help
            
            e - equipment
            p - passive equipment
            # - door
            i - item
            w - weapon
            @ - You!
            U - upgrader
            , - a rat (weak enemy)
            H - Another human
            Z - Infected Doctor
            S - Statue (powerful enemy)
            ɑ - door to the previous map
            ჲ - door to the next map
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

    public void displayNewFightReports(Map map) {
        for(FightReport fightReport : map.getFightReports()) {
            fightReport.showReport();
        }
        map.getFightReports().clear();
    }

    public void displayPlayerEquipment(Player player) {
        System.out.println("Your equipment:");
        java.util.Map<Actor.EquipmentSlots, List<Equipment>> equipment = player.getEquipment();
        for(Actor.EquipmentSlots slot : equipment.keySet()) {
            System.out.println(slot+":");
            for(int i = 0;i<equipment.get(slot).size();++i) {
                System.out.println((i+1)+". "+equipment.get(slot).get(i));
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

    public boolean successfulMovement(Map map, Point movePoint) {
        if(map.canMoveThrough(map.getPlayer(),map.getPlayer().getPosition().offset(movePoint))) {
            map.moveActor(map.getPlayer(), movePoint.getX(), movePoint.getY());
            return true;
        }
        System.out.println("Can't move here");
        return false;
    }

    public void displayActors(List<Actor> actors, Player player) {
        int i = 1;
        for(Actor actor : actors) {
            System.out.println(i+". "+actor.toString()+"(shoot chance: "+player.getRealShootChance(actor, player.getActorsWeapons().get(0))+")");
            i++;
        }
    }

    public void userInput(Map map) {
        while (true) {
            int turns = map.getPlayer().getRealMovementSpeed();
            while(turns > 0) {
                Scanner input = new Scanner(System.in);
                String command = input.nextLine();
                String[] arguments = command.split(" ");
                try {
                    command = arguments[0];
                    switch (command) { //break means you lose a turn, continue means you don't
                        case "w":
                            if (successfulMovement(map, new Point(0, -1))) {
                                break;
                            }
                            continue;
                        case "s":
                            if (successfulMovement(map, new Point(0, 1))) {
                                break;
                            }
                            continue;
                        case "a":
                            if (successfulMovement(map, new Point(-1, 0))) {
                                break;
                            }
                            continue;
                        case "d":
                            if (successfulMovement(map, new Point(1, 0))) {
                                break;
                            }
                            continue;
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
                            map.getPlayer().equip(Integer.parseInt(arguments[1]) - 1, map.getPlayer().getEquipmentSlot(arguments[2]), Integer.parseInt(arguments[3]) - 1);
                            break;
                        case "de":
                            map.getPlayer().deequip(map.getPlayer().getEquipmentSlot(arguments[1]), Integer.parseInt(arguments[2]) - 1);
                            break;
                        case "t":
                            map.getPlayer().throwItem(map, map.getPlayer().getItemInBackpack(Integer.parseInt(arguments[1]) - 1));
                            continue;
                        case "f":
                            boolean result = map.getPlayer().shootMonster(map, arguments[1]);
                            if (result) {
                                break;
                            }
                            System.out.println("Can't hit the target");
                            continue;
                        case "targets":
                            displayActors(map.getPlayer().actorsInSightRange(map), map.getPlayer());
                            continue;
                        case "skip":
                            break;
                        case "u":
                            Item item = map.getPlayer().getItemInBackpack(Integer.parseInt(arguments[1])-1);
                            if(item.canUseItem()) {
                                map.getPlayer().useItem(item);
                                break;
                            }
                            else {
                                System.out.println("Can't use that item");
                                continue;
                            }
                        default:
                            System.out.println("Wrong input!");
                            continue;
                    }
                    turns--;
                } catch (Exception e) {
                    Logging.logger.log(Level.WARNING, "Can't read command exception " + Arrays.toString(e.getStackTrace()));
                    continue;
                }
            }
            break;
        }
    }

    public String displayMenuAndGetCommand() {
        System.out.println("Welcome to LabRogue. Type in menu name or number to enter");
        System.out.println("1. Play");
        System.out.println("2. Set seed");
        System.out.println("3. Exit");
        Scanner input = new Scanner(System.in);
        String command = input.nextLine();
        String[] arguments = command.split(" ");
        return arguments[0];
    }

    public String showSeedAndGetSeed(String seed) {
        if(seed.isEmpty()) {
            System.out.println("There is currently no seed! (so it will generate randomly)");
        }
        System.out.println("Seed: "+seed);

        System.out.println("Type in the new seed or nothing if you want to leave current seed. You can also type in reset to reset the seed");

        Scanner input = new Scanner(System.in);
        String newSeed = input.nextLine();
        newSeed = newSeed.trim();
        return newSeed;
    }
}

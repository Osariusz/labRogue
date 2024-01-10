package org.osariusz.Graphics;

import org.osariusz.Actors.Actor;
import org.osariusz.Items.Item;
import org.osariusz.Map.Map;

import java.util.Scanner;

public class IO {
    public void displayMap(Map map) {
        for(int x = 0;x<map.getMap().size();x++) {
            for(int y = 0;y<map.getMap().get(x).size();y++) {
                System.out.print(map.getMap().get(x).get(y).getSymbol());
            }
            System.out.print("\n");
        }
    }

    public void displayMapItems(Map map) {
        for(Item item : map.getAllItems()) {
            System.out.println(item.toString());
        }
    }

    public void displayMapActors(Map map) {
        for(Actor actor : map.getAllActors()) {
            System.out.println(actor.toString());
        }
    }

    public void clearDisplay() {
        System. out. print("\033[H\033[2J");
        System. out. flush();
    }

    public void userInput(Map map) {
        Scanner s = new Scanner(System.in);
        String c = s.nextLine();
        if(c.equals("w")) {
            //map.placeActor( new Actor(20),10,10);
        }
    }

    public void IOLoop(Map map) {
        while(true) {
            displayMap(map);
            displayMapActors(map);
            displayMapItems(map);
            userInput(map);
            clearDisplay();
        }

    }
}

package org.osariusz.Utils;

import org.osariusz.Actors.Actor;
import org.osariusz.Items.Weapon;

public class FightScenarios {
    public static void humanFightingAnomaly() {
        Actor anomaly = new Actor("Anomaly",100);
        Actor human = new Actor("Human", 10);
        human.setWeapon(new Weapon(50,5));
        while (human.isAlive() && anomaly.isAlive()) {
            anomaly.attackActor(human);
            if(human.isAlive() && anomaly.isAlive()) {
                human.attackActor(anomaly);
            }
        }
        if(human.isAlive()) {
            System.out.println("Human won");
        }
        else {
            System.out.println("Anomaly won");
        }
    }
    public static int bothHumans() {
        Weapon deadchatxd = new Weapon(10,100);
        Actor human1 = new Actor("Human1", 10);
        human1.setWeapon(deadchatxd);
        Actor human2 = new Actor("Human2", 10);
        human2.setWeapon(deadchatxd);
        while(human1.isAlive() && human2.isAlive()) {
            human1.attackActor(human2);
            if(human1.isAlive() && human2.isAlive()) {
                human2.attackActor(human1);
            }
        }
        if(human1.isAlive()) {
            System.out.println("Human1 won");
            return 1;
        }
        else {
            System.out.println("Human2 won");
            return 2;
        }
    }

    public static void countHumanWins() {
        int firstWins = 0;
        int secondWins = 0;
        for(int i = 0;i<5000;++i) {
            int result = bothHumans();
            if(result == 1) {
                firstWins++;
            }
            else if(result == 2) {
                secondWins++;
            }
        }
        System.out.println("Human1 won "+firstWins+" times and Human2 won "+secondWins);
    }
}

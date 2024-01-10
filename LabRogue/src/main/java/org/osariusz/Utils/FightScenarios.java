package org.osariusz.Utils;

import org.osariusz.Actors.Actor;
import org.osariusz.Actors.Monster;
import org.osariusz.Actors.Player;
import org.osariusz.Items.Weapon;

public class FightScenarios {
    public static void humanFightingAnomaly() {
        Monster anomaly =  Monster.builder().id("anomaly").name("Anomaly").hp(100).build();
        Monster human = Monster.builder().id("human").name("Human").hp(10).build();
        human.setWeapon(Weapon.builder().damage(50).shootChance(5).build());
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
        Weapon deadchatxd = Weapon.builder().damage(1).shootChance(50).build();
        Monster human1 = Monster.builder().id("human").name("Human1").hp(10).build();
        human1.setWeapon(deadchatxd);
        Monster human2 = Monster.builder().id("human").name("Human2").hp(10).build();
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

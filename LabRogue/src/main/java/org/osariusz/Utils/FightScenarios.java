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
}

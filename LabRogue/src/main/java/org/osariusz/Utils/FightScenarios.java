package org.osariusz.Utils;

import org.osariusz.Actors.Actor;
import org.osariusz.Actors.Monster;
import org.osariusz.Actors.Player;
import org.osariusz.Items.Weapon;

import static org.osariusz.Actors.ActorList.monsters;

public class FightScenarios {
    public static void humanFightingAnomaly() {
        Monster anomaly = (Monster) Monster.builder().hp(100).id("anomaly").name("Anomaly").build();
        Monster human = (Monster) Monster.builder().hp(10).id("human").name("Human").build();
        human.setWeapon(Weapon.builder().damage(50).shootChance(5).build());
        while (human.isAlive() && anomaly.isAlive()) {
            anomaly.attackActor(human);
            if (human.isAlive() && anomaly.isAlive()) {
                human.attackActor(anomaly);
            }
        }
        if (human.isAlive()) {
            System.out.println("Human won");
        } else {
            System.out.println("Anomaly won");
        }
    }

    public static int bothHumans() {
        Weapon deadchatxd = Weapon.builder().damage(1).shootChance(50).build();
        Monster human1 = (Monster) Monster.builder().hp(10).id("human").name("Human1").build();
        human1.setWeapon(deadchatxd);
        Monster human2 = (Monster) Monster.builder().hp(10).id("human").name("Human2").build();
        human2.setWeapon(deadchatxd);
        while (human1.isAlive() && human2.isAlive()) {
            human1.attackActor(human2);
            if (human1.isAlive() && human2.isAlive()) {
                human2.attackActor(human1);
            }
        }
        if (human1.isAlive()) {
            System.out.println("Human1 won");
            return 1;
        } else {
            System.out.println("Human2 won");
            return 2;
        }
    }

    public static int testFight(Actor attacker, Actor defender) {
        while (attacker.isAlive() && defender.isAlive()) {
            attacker.attackActor(defender);
            if (attacker.isAlive() && defender.isAlive()) {
                defender.attackActor(attacker);
            }
        }
        if (attacker.isAlive()) {
            //System.out.println(attacker.toString()+" won");
            return 1;
        } else {
            //System.out.println(defender.toString()+" won");
            return 2;
        }
    }

    public static void countPlayerWins(Monster.MonsterBuilder<?, ?> enemyBuilder) {
        int firstWins = 0;
        int secondWins = 0;
        for (int i = 0; i < 5000; ++i) {
            Player player = (Player) Player.builder().id("human").name("Player").build();
            int result = testFight(player, enemyBuilder.build());
            if (result == 1) {
                firstWins++;
            } else if (result == 2) {
                secondWins++;
            }
        }
        System.out.println("Player won " + firstWins + " times and " + enemyBuilder.build().toString() + " won " + secondWins);
    }

    public static void testNewEnemies() {
        for (Monster.MonsterBuilder<?, ?> monsterBuilder : monsters) {
            countPlayerWins(monsterBuilder);
        }
    }
}

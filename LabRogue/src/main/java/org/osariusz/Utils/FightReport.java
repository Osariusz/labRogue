package org.osariusz.Utils;

import lombok.experimental.SuperBuilder;
import org.osariusz.Actors.Actor;
import org.osariusz.Items.Weapon;

@SuperBuilder
public class FightReport {

    String report;

    int damage;
    int rolledShot;

    Actor attacker;
    Actor defender;

    Weapon weapon;

    public FightReport(FightReportBuilder<?, ?> builder) {
        report = String.format(
                "%s attacked %s, inflicting %d damage with a shot chance of %d while rolling %d",
                builder.attacker.toString(),
                builder.defender.toString(),
                builder.damage,
                builder.attacker.getRealShootChance(builder.defender, builder.weapon),
                builder.rolledShot
        );
    }

    public void showReport() {
        System.out.println(report);
    }

}

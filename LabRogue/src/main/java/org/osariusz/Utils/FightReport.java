package org.osariusz.Utils;

import org.osariusz.Actors.Actor;

public class FightReport {

    String report;

    public FightReport(FightReportBuilder builder) {
        report = String.format(
                "%s attacked %s, inflicting %d damage with a shot chance of %d while rolling %d",
                builder.attacker.toString(),
                builder.defender.toString(),
                builder.damage,
                builder.attacker.getShootThreshold(),
                builder.rolledShot
        );
    }

    public void showReport() {
        System.out.println(report);
    }

    public static class FightReportBuilder {
        int damage;
        int rolledShot;

        Actor attacker;
        Actor defender;

        public FightReportBuilder setDamage(int damage) {
            this.damage = damage;
            return this;
        }

        public FightReportBuilder rolledShot(int rolledShot) {
            this.rolledShot = rolledShot;
            return this;
        }

        public FightReportBuilder setAttacker(Actor attacker) {
            this.attacker = attacker;
            return this;
        }

        public FightReportBuilder setDefender(Actor defender) {
            this.defender = defender;
            return this;
        }

        public FightReport build() {
            return new FightReport(this);
        }
    }
}

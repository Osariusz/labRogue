package org.osariusz.Actors;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.osariusz.GameElements.GameElement;
import org.osariusz.Items.Weapon;
import org.osariusz.Utils.FightReport;

import java.util.Random;

@SuperBuilder
public abstract class Actor extends GameElement {

    private String id;

    @Getter
    private String name;

    @Getter
    private int hp;

    @Setter
    @Getter
    private Weapon weapon;

    public void dealDamage(int damage) {
        hp -= damage;
    }

    public void healHp(int heal) {
        hp += heal;
    }

    public boolean isAlive() {
        return hp > 0;
    }

    public int getShootThreshold() {
        if (weapon == null) {
            return 50;
        }
        return weapon.getShootChance();
    }

    public int getWeaponDamage() {
        if (weapon == null) {
            return 1;
        }
        return weapon.getDamage();
    }

    public void attackActor(Actor attacked) {
        Random random = new Random();
        FightReport.FightReportBuilder fightReportBuilder = new FightReport.FightReportBuilder().setAttacker(this).setDefender(attacked);
        int randomNumber = random.nextInt(1, 101);
        fightReportBuilder.rolledShot(randomNumber);
        if (randomNumber <= getShootThreshold()) { //will be replaced
            fightReportBuilder.setDamage(getWeaponDamage());
            attacked.dealDamage(getWeaponDamage());
        }
        fightReportBuilder.build().showReport();
    }

    @Override
    public String toString() {
        return this.name;
    }

}

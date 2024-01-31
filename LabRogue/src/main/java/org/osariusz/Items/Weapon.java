package org.osariusz.Items;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.osariusz.Actors.Actor;

import java.util.ArrayList;
import java.util.List;

@SuperBuilder(toBuilder = true)
@Getter
@NoArgsConstructor
public class Weapon extends Equipment {

    @Override
    protected void initializeDefaults() {
        super.initializeDefaults();
        super.id = "generic_weapon";
        super.name = "Generic Weapon";
        super.symbol = 'w';
        super.allowedSlots = new ArrayList<>(List.of(Actor.EquipmentSlots.WEAPON));
        this.damage = 1;
        this.shootChance = 50;
        this.range = 1;
    }

    int damage;

    int shootChance; //0-100

    int range;

    @Override
    public String toString() {
        return super.toString()+"(damage: "+damage+", shoot chance: "+shootChance+", range: "+range+")";
    }
}

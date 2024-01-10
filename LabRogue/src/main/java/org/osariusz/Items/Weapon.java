package org.osariusz.Items;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.osariusz.Actors.Actor;

@Getter
@SuperBuilder
public class Weapon extends Item {
    {
        super.name = "Weapon";
        super.symbol = 'w';
        this.damage = 1;
        this.shootChance = 50;
        this.range = 1;
    }


    int damage;

    int shootChance; //0-100

    int range;

}

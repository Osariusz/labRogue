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
    }

    @Builder.Default
    int damage = 1;

    @Builder.Default
    int shootChance = 50; //0-100

    @Builder.Default
    int range = 1;

}

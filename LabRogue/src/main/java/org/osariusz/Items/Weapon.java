package org.osariusz.Items;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.osariusz.Actors.Actor;

import java.util.ArrayList;
import java.util.List;

@Getter
@SuperBuilder(toBuilder = true)
public class Weapon extends Equipment {

    @Override
    protected void initializeDefaults() {
        super.initializeDefaults();
        super.id = "generic_weapon";
        super.name = "Generic Weapon";
        super.symbol = 'w';
        super.allowedSlots = new ArrayList<>(List.of(Actor.EquipmentSlots.WEAPON));
    }

    @Builder.Default
    int damage = 1;

    @Builder.Default
    int shootChance = 50; //0-100

    @Builder.Default
    int range = 1;

}

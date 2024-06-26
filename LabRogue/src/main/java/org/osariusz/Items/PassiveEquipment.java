package org.osariusz.Items;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.osariusz.Actors.Actor;

import java.util.ArrayList;
import java.util.List;

@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public class PassiveEquipment extends Equipment {

    @Override
    protected void initializeDefaults() {
        super.initializeDefaults();
        super.id = "passive_equipment";
        super.name = "Passive Equipment";
        super.symbol = 'p';
        super.allowedSlots = new ArrayList<>(List.of(Actor.EquipmentSlots.PASSIVE));
    }

}

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
public class Equipment extends Item{

    @Override
    protected void initializeDefaults() {
        super.initializeDefaults();
        super.id = "equipment";
        super.name = "Equipment";
        super.symbol = 'i';
        this.allowedSlots = new ArrayList<>();
    }

    protected int hpBonus;

    protected int agilityBonus;

    protected List<Actor.EquipmentSlots> allowedSlots;

}

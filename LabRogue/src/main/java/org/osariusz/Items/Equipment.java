package org.osariusz.Items;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.osariusz.Actors.Actor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public class Equipment extends Item{

    @Override
    protected void initializeDefaults() {
        super.initializeDefaults();
        super.id = "equipment";
        super.name = "Equipment";
        super.symbol = 'e';
        this.allowedSlots = new ArrayList<>();
    }

    protected int hpBonus;

    protected int agilityBonus;

    protected int movementSpeedBonus;

    protected List<Actor.EquipmentSlots> allowedSlots;

    public String getModifierString(AtomicBoolean isNextModifier, String label, int value) {
        String modifiersString = "";
        if(value != 0) {
            if(isNextModifier.get()) {
                modifiersString += ", ";
            }
            isNextModifier.set(true);
            modifiersString += label+hpBonus;
        }
        return modifiersString;
    }

    @Override
    public String toString() {
        String base = super.toString();

        AtomicBoolean modifiers = new AtomicBoolean(false);
        String modifiersString = "(" + getModifierString(modifiers, "hp: ", hpBonus) +
                getModifierString(modifiers, "agility: ", agilityBonus) +
                getModifierString(modifiers, "movement speed: ", movementSpeedBonus) +
                ") ";

        boolean slots = false;
        StringBuilder allowedSlotsString = new StringBuilder("allowed slots: ");
        for(Actor.EquipmentSlots equipmentSlot : allowedSlots) {
            if(slots) {
                allowedSlotsString.append(", ");
            }
            allowedSlotsString.append(equipmentSlot);
            slots = true;
        }

        if(allowedSlots.isEmpty()) {
            allowedSlotsString = new StringBuilder();
        }

        return base + modifiersString + allowedSlotsString;
    }

}

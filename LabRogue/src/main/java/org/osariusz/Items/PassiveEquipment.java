package org.osariusz.Items;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Getter
@SuperBuilder
public class PassiveEquipment extends Item {

    private int hpBonus;

    private int agilityBonus;

    private List<String> allowedSlots;

}

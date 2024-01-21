package org.osariusz.Items;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.osariusz.GameElements.GameElement;

@SuperBuilder(toBuilder = true)
@Getter
@NoArgsConstructor
public class Item extends GameElement {

    @Override
    protected void initializeDefaults() {
        super.initializeDefaults();
        super.name = "Generic Item";
        super.id = "generic_item";
        super.symbol = 'i';
        this.spawnChance = 10;
    }

    protected int spawnChance;

}

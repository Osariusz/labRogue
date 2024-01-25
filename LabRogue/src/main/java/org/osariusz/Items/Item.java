package org.osariusz.Items;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.osariusz.GameElements.GameElement;
import org.osariusz.GameElements.Spawnable;

import java.util.*;

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
        super.spawnChance = 10;
        this.transmutationChances = new ArrayList<>(List.of(new AbstractMap.SimpleEntry<>(1,super.id)));
    }

    protected List<Map.Entry<Integer, String>> transmutationChances;

}

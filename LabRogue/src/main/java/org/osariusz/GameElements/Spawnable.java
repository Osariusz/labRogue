package org.osariusz.GameElements;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder(toBuilder = true)
@Getter
@NoArgsConstructor
public class Spawnable {
    {
        initializeDefaults();
    }
    protected String id;

    protected int spawnChance;

    protected void initializeDefaults() {
        this.id = "generic_id";
        this.spawnChance = 10;
    }


}

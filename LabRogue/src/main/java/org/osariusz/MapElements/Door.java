package org.osariusz.MapElements;

import lombok.experimental.SuperBuilder;

@SuperBuilder(toBuilder = true)
public class Door extends MapElement {

    @Override
    protected void initializeDefaults() {
        super.initializeDefaults();
        super.name = "Door";
        super.id = "door";
        super.symbol = '▯';
    }
}

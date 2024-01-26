package org.osariusz.MapElements;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public class Door extends MapElement {

    @Override
    protected void initializeDefaults() {
        super.initializeDefaults();
        super.name = "Door";
        super.id = "door";
        super.symbol = '▯';
    }
}

package org.osariusz.MapElements;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.osariusz.Map.Map;

@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public class MapLeave extends Wall {

    protected int direction;

    @Override
    protected void initializeDefaults() {
        super.initializeDefaults();
        super.name = "Map Exit";
        super.id = "map_exit";
        super.symbol = 'ჲ';
        this.direction = 1;
    }
}

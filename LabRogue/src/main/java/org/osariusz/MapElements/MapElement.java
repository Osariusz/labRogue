package org.osariusz.MapElements;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.osariusz.Actors.Actor;
import org.osariusz.GameElements.GameElement;

@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public class MapElement extends GameElement {
    @Override
    protected void initializeDefaults() {
        super.initializeDefaults();
        super.name = "Generic Map Element";
        super.id = "generic_map_element";
        super.symbol = '#';
    }

}

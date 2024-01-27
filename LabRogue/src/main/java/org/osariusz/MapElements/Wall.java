package org.osariusz.MapElements;

import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.osariusz.Actors.Actor;
import org.osariusz.GameElements.GameElement;

import java.util.ArrayList;
import java.util.List;

@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public class Wall extends MapElement {

    @Override
    protected void initializeDefaults() {
        super.initializeDefaults();
        super.id = "wall";
        super.name = "Wall";
        super.symbol = '▒';
    }

}

package org.osariusz.MapElements;

import lombok.experimental.SuperBuilder;
import org.osariusz.GameElements.GameElement;

@SuperBuilder
public class Wall extends MapElement {
    {
        super.name = "Wall";
        super.symbol = '#';
    }

}

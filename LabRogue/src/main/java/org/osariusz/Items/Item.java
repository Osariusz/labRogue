package org.osariusz.Items;

import lombok.experimental.SuperBuilder;
import org.osariusz.GameElements.GameElement;

@SuperBuilder
public class Item extends GameElement {
    {
        super.name = "Item";
        super.symbol = 'i';
    }

}

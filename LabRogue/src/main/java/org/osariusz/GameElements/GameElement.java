package org.osariusz.GameElements;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.osariusz.Items.Item;

@SuperBuilder(toBuilder = true)
@Getter
@NoArgsConstructor
public class GameElement extends Spawnable {

    protected String name;

    protected char symbol;

    @Override
    protected void initializeDefaults() {
        super.initializeDefaults();
        super.id = "generic_element";
        this.name = "Generic Element";
        this.symbol = '~';
    }

    @Override
    public String toString() {
        return name;
    }

}

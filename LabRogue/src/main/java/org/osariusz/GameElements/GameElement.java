package org.osariusz.GameElements;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.osariusz.Items.Item;

@SuperBuilder(toBuilder = true)
@Getter
@NoArgsConstructor
public class GameElement {
    {
        initializeDefaults();
    }

    protected String name;
    protected String id;
    protected char symbol;

    protected void initializeDefaults() {
        this.name = "Generic Element";
        this.id = "generic_element";
        this.symbol = '~';

    }

    @Override
    public String toString() {
        return name;
    }

}

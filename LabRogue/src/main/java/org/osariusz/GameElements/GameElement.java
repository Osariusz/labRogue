package org.osariusz.GameElements;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public abstract class GameElement {
    protected String name;
    protected char symbol;

    @Override
    public String toString() {
        return name;
    }
}

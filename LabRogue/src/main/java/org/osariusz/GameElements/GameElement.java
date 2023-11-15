package org.osariusz.GameElements;

public abstract class GameElement {
    protected String name;
    protected char symbol;

    public char getSymbol() {
        return symbol;
    }

    @Override
    public String toString() {
        return name;
    }
}

package org.osariusz.Actors;

import lombok.experimental.SuperBuilder;

@SuperBuilder(toBuilder = true)
public class Player extends Actor {
    private int toxicity;
}

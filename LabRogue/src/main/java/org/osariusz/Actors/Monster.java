package org.osariusz.Actors;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class Monster extends Actor {
    private int spawnChance;
}

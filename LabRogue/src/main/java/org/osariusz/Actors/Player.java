package org.osariusz.Actors;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.osariusz.Map.Map;

@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public class Player extends Actor {
    private int toxicity;

    @Override
    protected void initializeDefaults() {
        super.initializeDefaults();
        super.name = "Player";
        super.id = "player";
        super.symbol = '@';
    }

    public void movePlayer(Map map, int xMovement, int yMovement) {
        map.moveActor(this, xMovement, yMovement);
    }
}

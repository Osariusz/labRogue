package org.osariusz.Actors;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.osariusz.Map.Map;

import java.util.Random;

@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public class Monster extends Actor {

    public void moveMonster(Map map) {
        int sightRange = 10;

        int playerDiffX = map.getPlayer().getPosition().getX()-getPosition().getX();
        int playerDiffY = map.getPlayer().getPosition().getY()-getPosition().getY();

        if(playerDiffX+playerDiffY <= sightRange) {
            //TODO: Rework AI so it follows the player via bfs
            if(playerDiffX != 0) {
                map.moveActor(this, playerDiffX/Math.abs(playerDiffX),0);
            }
            else {
                map.moveActor(this, playerDiffY/Math.abs(playerDiffY),0);
            }
        }
        else {
            //random movement if no player found
            Random random = new Random();
            int moveX = random.nextInt(0,2);
            int moveNegative = random.nextInt(0,2);
            if(moveX == 1) {
                map.moveActor(this, moveNegative,0);
            }
            else {
                map.moveActor(this, 0, moveNegative);
            }
        }
    }
}

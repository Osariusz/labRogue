package org.osariusz.Actors;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.osariusz.Map.Map;
import org.osariusz.Utils.Point;

import java.util.ArrayList;
import java.util.List;

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
        super.canPickItems = true;
        super.sightRange = 21;
    }

    public boolean shootMonster(Map map, String input) {
        List<Point> path = new ArrayList<>();
        Point newPoint = getPosition();
        for(char letter : input.toCharArray()) {
            switch (letter) {
                case 'w':
                    path.add(newPoint.offset(new Point(0,-1)));
                    break;
                case 's':
                    path.add(newPoint.offset(new Point(0,1)));
                    break;
                case 'a':
                    path.add(newPoint.offset(new Point(-1,0)));
                    break;
                case 'd':
                    path.add(newPoint.offset(new Point(1,0)));
                    break;
            }
        }
        Point lastPoint = path.get(path.size()-1);
        Actor attacked = map.getActorAtPosition(lastPoint);
        if(attacked != null && canShootAnyWeapon(map, attacked)) {
            map.getFightReports().addAll(attackActor(map, attacked));
            return true;
        }
        return false;
    }

    public List<Actor> actorsInSightRange(Map map) {
        return map.getAllActors().stream().filter(actor -> getPosition().distanceTo(actor.getPosition()) < getSightRange() && !actor.equals(this)).toList();
    }
}

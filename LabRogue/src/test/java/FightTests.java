import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.osariusz.Actors.Actor;
import org.osariusz.Items.Item;
import org.osariusz.Items.Weapon;
import org.osariusz.Map.Map;
import org.osariusz.MapElements.Tile;

public class FightTests {

    @Test
    public void kill() {
        Map map = new Map.MapBuilder().setWidth(4).setHeight(4).buildMap().build();
        Actor actor1 = new Actor(20);
        actor1.setWeapon(new Weapon(1, 100));
        Actor actor2 = new Actor(1);
        map.placeActor(actor1, 1, 1);
        map.placeActor(actor2, 2, 1);
        actor1.attackActor(actor2);
        assert (!actor2.isAlive());
    }

    @Test
    public void noWeaponAttack() {
        Map map = new Map.MapBuilder().setWidth(4).setHeight(4).buildMap().build();
        Actor actor1 = new Actor(20);
        Actor actor2 = new Actor(1);
        map.placeActor(actor1, 1, 1);
        map.placeActor(actor2, 2, 1);
        Assertions.assertDoesNotThrow(() -> {
            actor1.attackActor(actor2);
        });
    }
}

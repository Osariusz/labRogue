import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.osariusz.Actors.Actor;
import org.osariusz.Actors.Monster;
import org.osariusz.Items.Item;
import org.osariusz.Items.Weapon;
import org.osariusz.Map.Map;
import org.osariusz.MapElements.Tile;

public class FightTests {

    @Test
    public void kill() {
        Map map = new Map.MapBuilder().setWidth(4).setHeight(4).buildMap().build();
        Monster monster1 = (Monster) Monster.builder().hp(20).build();
        monster1.setWeapon(Weapon.builder().damage(1).shootChance(100).build());
        Monster monster2 = (Monster) Monster.builder().hp(1).build();
        map.placeActor(monster1, 1, 1);
        map.placeActor(monster2, 2, 1);
        monster1.attackActor(monster2);
        assert (!monster2.isAlive());
    }

    @Test
    public void noWeaponAttack() {
        Map map = new Map.MapBuilder().setWidth(4).setHeight(4).buildMap().build();
        Monster actor1 = (Monster) Monster.builder().hp(20).build();
        Monster actor2 = (Monster) Monster.builder().hp(1).build();
        map.placeActor(actor1, 1, 1);
        map.placeActor(actor2, 2, 1);
        Assertions.assertDoesNotThrow(() -> {
            actor1.attackActor(actor2);
        });
    }
}

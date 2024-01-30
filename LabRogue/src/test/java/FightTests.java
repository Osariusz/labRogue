import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.osariusz.Actors.Actor;
import org.osariusz.Actors.Monster;
import org.osariusz.Items.Item;
import org.osariusz.Items.Weapon;
import org.osariusz.Map.Map;
import org.osariusz.MapElements.Tile;
import org.osariusz.Utils.Point;

public class FightTests {

    @Test
    public void kill() {
        Map map = Map.builder().width(4).height(4).build();
        Monster monster1 = (Monster) Monster.builder().hp(20).build();
        monster1.getEquipment().get(Actor.EquipmentSlots.WEAPON).add(Weapon.builder().damage(1).shootChance(100).build());
        Monster monster2 = (Monster) Monster.builder().hp(1).build();
        map.placeActor(monster1, new Point(1, 1));
        map.placeActor(monster2, new Point(2, 1));
        monster1.attackActor(monster2);
        assert (!monster2.isAlive());
    }

    @Test
    public void noWeaponAttack() {
        Map map = Map.builder().width(4).height(4).build();
        Monster actor1 = (Monster) Monster.builder().hp(20).build();
        Monster actor2 = (Monster) Monster.builder().hp(1).build();
        map.placeActor(actor1, new Point(1, 1));
        map.placeActor(actor2, new Point(2, 1));
        Assertions.assertDoesNotThrow(() -> {
            actor1.attackActor(actor2);
        });
    }
}

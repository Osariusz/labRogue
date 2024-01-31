import org.junit.Test;
import org.osariusz.Actors.Monster;
import org.osariusz.Actors.Player;
import org.osariusz.Items.Equipment;
import org.osariusz.Items.Item;
import org.osariusz.Items.ItemList;
import org.osariusz.Items.Weapon;
import org.osariusz.Map.Map;

import java.util.Objects;

public class ItemTests {

    @Test
    public void itemBuildTest() {
        Item item = new Item().toBuilder().spawnChance(105).id("test").name("Test item").symbol('t').build();
        assert item.getSpawnChance() == 105;
        assert item.getName().equals("Test item");
        assert item.getId().equals("test");
        assert item.getSymbol() == 't';
    }

    @Test
    public void itemDefaultBuilderTest() {
        Item item = new Item().toBuilder().build();
        assert item.getSpawnChance() == 20;
        assert item.getName().equals("Generic Item");
        assert item.getId().equals("generic_item");
        assert item.getSymbol() == 'i';
    }

    @Test
    public void equipmentDefaultBuilderTest() {
        Equipment item = new Equipment().toBuilder().build();
        assert item.getSpawnChance() == 20;
        assert item.getName().equals("Equipment");
        assert item.getId().equals("equipment");
        assert item.getSymbol() == 'e';
    }

    @Test
    public void useSyringe() {
        Player player = new Player().toBuilder().build();
        ItemList.getItem("hp_syringe").build().useItem(player);

        Player secondPlayer = new Player().toBuilder().build();

        System.out.println(player);
        System.out.println(secondPlayer);

        System.out.println(player.getHp());
        System.out.println(secondPlayer.getHp());

        assert player.getHp()-secondPlayer.getHp() == 3;
    }
}

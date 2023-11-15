import org.junit.Test;
import org.osariusz.Actors.Actor;
import org.osariusz.Items.Item;
import org.osariusz.Map.Map;

public class MapTests {

    @Test
    public void testPlaceItem() {
        Map map = new Map.MapBuilder().setWidth(10).setHeight(10).buildMap().build();
        map.placeItem(new Item(),5,5);
        assert map.getMap().get(5).get(5) instanceof Item;
    }

    @Test
    public void testPlaceItemOnItem() {
        Map map = new Map.MapBuilder().setWidth(10).setHeight(10).buildMap().build();
        map.placeItem(new Item(),5,5);
        assert !map.canPlaceItem(new Item(),5,5);
    }

    @Test
    public void testPlaceActor() {
        Map map = new Map.MapBuilder().setWidth(10).setHeight(10).buildMap().build();
        map.placeActor(new Actor(),5,5);
        assert map.getMap().get(5).get(5) instanceof Actor;
    }

    @Test
    public void testPlaceActorOnActor() {
        Map map = new Map.MapBuilder().setWidth(10).setHeight(10).buildMap().build();
        map.placeActor(new Actor(),5,5);
        assert !map.canPlaceActor(new Actor(),5,5);
    }
}

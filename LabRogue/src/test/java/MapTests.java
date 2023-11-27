import org.junit.Test;
import org.osariusz.Actors.Actor;
import org.osariusz.Items.Item;
import org.osariusz.Map.Map;
import org.osariusz.MapElements.Tile;

public class MapTests {

    @Test
    public void testPlaceItem() {
        Map map = new Map.MapBuilder().setWidth(10).setHeight(10).buildMap().build();
        map.placeItem(new Item(),5,5);
        assert map.getMap().get(5).get(5) instanceof Tile;
        assert !((Tile) map.getMap().get(5).get(5)).getItems().isEmpty();
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
        map.placeActor(new Actor(20),5,5);
        assert map.getMap().get(5).get(5) instanceof Tile;
        Tile tile = (Tile)map.getMap().get(5).get(5);
        assert tile.hasActor();
    }

    @Test
    public void testPlaceActorOnActor() {
        Map map = new Map.MapBuilder().setWidth(10).setHeight(10).buildMap().build();
        map.placeActor(new Actor(20),5,5);
        assert !map.canPlaceActor(new Actor(20),5,5);
    }
}

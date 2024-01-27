import org.junit.Test;
import org.osariusz.Actors.Actor;
import org.osariusz.Actors.Monster;
import org.osariusz.Graphics.IO;
import org.osariusz.Items.Item;
import org.osariusz.Map.Map;
import org.osariusz.MapElements.Tile;
import org.osariusz.MapElements.Wall;
import org.osariusz.Utils.Point;

public class MapTests {

    @Test
    public void testPlaceItem() {
        Map map = Map.builder().width(10).height(10).build();
        map.placeItem(Item.builder().build(), 5, 5);
        assert map.getMap().get(5).get(5) instanceof Tile;
        assert !((Tile) map.getMap().get(5).get(5)).getItems().isEmpty();
    }

    @Test
    public void testPlaceItemOnItem() {
        Map map = Map.builder().width(10).height(10).build();
        map.placeItem(Item.builder().build(), new Point(5, 5));
        assert !map.canPlaceItem(Item.builder().build(), new Point(5, 5));
    }

    @Test
    public void testPlaceActor() {
        Map map = Map.builder().width(10).height(10).build();
        map.placeActor(Monster.builder().build(), 5, 5);
        assert map.getMap().get(5).get(5) instanceof Tile;
        Tile tile = (Tile) map.getMap().get(5).get(5);
        assert tile.hasActor();
    }

    @Test
    public void testPlaceActorOnActor() {
        Map map = Map.builder().width(10).height(10).build();
        map.placeActor(Monster.builder().build(), 5, 5);
        assert !map.canPlaceActor(Monster.builder().build(), 5, 5);
    }

    @Test
    public void SmallWallMap() {
        Map map = Map.builder().width(1000).height(1).build();
        for (int x = 0; x < map.getWidth(); ++x) {
            //assert map.getFeature(x, 0) instanceof Wall;
        }
    }

    @Test
    public void wallBuildTest() {
        Wall item = new Wall().toBuilder().id("test").name("Test item").symbol('t').build();
        assert item.getName().equals("Test item");
        assert item.getId().equals("test");
        assert item.getSymbol() == 't';
    }

    @Test
    public void wallDefaultBuilderTest() {
        Wall item = new Wall().toBuilder().build();
        assert item.getName().equals("Wall");
        assert item.getId().equals("wall");
        assert item.getSymbol() == '#';
    }
}

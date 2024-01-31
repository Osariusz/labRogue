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
    public void wallBuildTest() {
        Wall item = new Wall().toBuilder().id("test").name("Test item").symbol('t').build();
        assert item.getName().equals("Test item");
        assert item.getId().equals("test");
        assert item.getSymbol() == 't';
    }

}

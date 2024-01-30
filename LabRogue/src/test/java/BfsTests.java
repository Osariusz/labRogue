import org.junit.Test;
import org.osariusz.Actors.Actor;
import org.osariusz.Actors.Monster;
import org.osariusz.Items.Weapon;
import org.osariusz.MapElements.MapElement;
import org.osariusz.MapElements.Tile;
import org.osariusz.MapElements.Wall;
import org.osariusz.Utils.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BfsTests {
    @Test
    public void bfsNotNull() {
        Point a = new Point(0,0);
        Point b = new Point(3,3);
        List<Point> bfs = a.bfsTo(b, Integer.MAX_VALUE, (p) -> {return true;});
        System.out.println(bfs);
        assert bfs != null;
    }

    @Test
    public void pathPresentAndAbsent() {
        List<List<MapElement>> map = new ArrayList<>();
        for(int y = 0;y<3;++y) {
            map.add(new ArrayList<>());
            for(int x = 0;x<3;++x) {
                map.get(y).add(new Wall().toBuilder().build());
            }
        }
        map.get(1).set(0, new Tile().toBuilder().build());
        map.get(1).set(2, new Tile().toBuilder().build());

        Point a = new Point(0,1);
        Point b = new Point(2,1);

        assert a.bfsTo(b, Integer.MAX_VALUE, (p) -> {
            if(p.getY() < 0 || p.getY() >= 3 || p.getX() < 0 || p.getX() >= 3) {
                return false;
            }
            return map.get(p.getY()).get(p.getX()) instanceof Tile;
        }) == null;

        map.get(1).set(1, new Tile().toBuilder().build());

        assert a.bfsTo(b, Integer.MAX_VALUE, (p) -> {
            if(p.getY() < 0 || p.getY() >= 3 || p.getX() < 0 || p.getX() >= 3) {
                return false;
            }
            return map.get(p.getY()).get(p.getX()) instanceof Tile;
        }) != null;
    }
}

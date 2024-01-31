import org.junit.Test;
import org.osariusz.Actors.Monster;
import org.osariusz.Utils.Point;

import java.util.List;
import java.util.stream.Collectors;

public class ActorTests {
    @Test
    public void ratStartingItems() {
        Monster rat = new Monster().toBuilder().startingItems(List.of("rat_tail")).build();
        System.out.println(rat.getBackpack());
        assert rat.getBackpack().stream().anyMatch(i -> i.getId().equals("rat_tail"));
    }

    @Test
    public void defaultEnemyNoStartingItems() {
        Monster rat = new Monster().toBuilder().build();
        System.out.println(rat.getBackpack());
        assert rat.getBackpack().isEmpty();
    }

    @Test
    public void ratsIndependent() {
        Monster.MonsterBuilder<?, ?> r = new Monster().toBuilder().startingItems(List.of("rat_tail"));
        Monster rat = r.build();
        System.out.println(rat.getBackpack());
        assert rat.getBackpack().stream().filter(i -> i.getId().equals("rat_tail")).toList().size() == 1;

        rat = r.build();
        System.out.println(rat.getBackpack());
        assert rat.getBackpack().stream().filter(i -> i.getId().equals("rat_tail")).toList().size() == 1;

        rat = r.build();
        System.out.println(rat.getBackpack());
        assert rat.getBackpack().stream().filter(i -> i.getId().equals("rat_tail")).toList().size() == 1;
    }
}

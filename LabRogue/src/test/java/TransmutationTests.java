import org.junit.Test;
import org.osariusz.Items.Item;
import org.osariusz.Items.ItemList;
import org.osariusz.Utils.Point;

import java.util.*;

public class TransmutationTests {
    @Test
    public void testTransmutation() {
        Item.ItemBuilder<?, ?> milkBuilder = new Item().toBuilder().name("Milk").id("milk").transmutationChances(new ArrayList<>(List.of(
                new AbstractMap.SimpleEntry<Integer, String>(2, "cheese"),
                new AbstractMap.SimpleEntry<Integer, String>(1, "cream")
        )));
        Item.ItemBuilder<?, ?> cheeseBuilder = new Item().toBuilder().name("Cheese").id("cheese");
        Item.ItemBuilder<?, ?> creamBuilder = new Item().toBuilder().name("Cream").id("cream").transmutationChances(new ArrayList<>(List.of(
                new AbstractMap.SimpleEntry<Integer, String>(10, "milk")
        )));

        ItemList.items.add(milkBuilder);
        ItemList.items.add(cheeseBuilder);
        ItemList.items.add(creamBuilder);

        System.out.println(milkBuilder.build().transmuteItem());
        System.out.println(cheeseBuilder.build().transmuteItem());
        System.out.println(creamBuilder.build().transmuteItem());

        assert !milkBuilder.build().transmuteItem().getId().equals("milk");
        assert cheeseBuilder.build().transmuteItem().getId().equals("cheese");
        assert creamBuilder.build().transmuteItem().getId().equals("milk");
    }

    @Test
    public void testRandomTransmutation() {
        Item.ItemBuilder<?, ?> milkBuilder = new Item().toBuilder().name("Milk").id("milk").transmutationChances(new ArrayList<>(List.of(
                new AbstractMap.SimpleEntry<Integer, String>(2, "cheese"),
                new AbstractMap.SimpleEntry<Integer, String>(1, "cream")
        )));
        Item.ItemBuilder<?, ?> cheeseBuilder = new Item().toBuilder().name("Cheese").id("cheese");
        Item.ItemBuilder<?, ?> creamBuilder = new Item().toBuilder().name("Cream").id("cream").transmutationChances(new ArrayList<>(List.of(
                new AbstractMap.SimpleEntry<Integer, String>(10, "milk")
        )));

        ItemList.items.add(milkBuilder);
        ItemList.items.add(cheeseBuilder);
        ItemList.items.add(creamBuilder);

        int n = 1000000;

        Map<String, Integer> result = new HashMap<>();
        result.put("cheese", 0);
        result.put("cream", 0);
        for(int i = 0;i<n;++i) {
            Item t = milkBuilder.build().transmuteItem();
            result.put(t.getId(), result.get(t.getId())+1);
        }

        Double cheese = (double)result.get("cheese")/n;
        Double cream = (double)result.get("cream")/n;

        System.out.println(cheese);
        System.out.println(cream);
    }

}

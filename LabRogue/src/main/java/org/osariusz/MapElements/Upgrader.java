package org.osariusz.MapElements;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.osariusz.Actors.Actor;
import org.osariusz.Actors.Player;
import org.osariusz.Items.Item;
import org.osariusz.Utils.Logging;

import java.util.List;
import java.util.logging.Level;

@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public class Upgrader extends MapElement {

    @Override
    protected void initializeDefaults() {
        super.initializeDefaults();
        super.name = "Upgrader";
        super.id = "upgrader";
        super.symbol = 'U';
    }

    public void upgradeBackpackItem(Actor actor, Item item) {
        if(!actor.getBackpack().contains(item)) {
            Logging.logger.log(Level.WARNING, "Can't upgrade as "+actor+" has no "+item+" in backpack!");
            return;
        }
        actor.removeFromBackpack(item);
        actor.addToBackpack(List.of(item.transmuteItem()));
    }
}

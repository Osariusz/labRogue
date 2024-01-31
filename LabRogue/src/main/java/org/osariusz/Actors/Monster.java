package org.osariusz.Actors;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.osariusz.Items.Equipment;
import org.osariusz.Items.Item;
import org.osariusz.Items.ItemList;
import org.osariusz.Items.Weapon;
import org.osariusz.Map.Map;
import org.osariusz.Map.Rooms.Room;
import org.osariusz.Utils.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public class Monster extends Actor {

    protected List<String> startingItems;


    @Override
    protected void initializeDefaults() {
        super.initializeDefaults();
        super.canPickItems = false;
        this.startingItems = new ArrayList<>();
    }

    public void giveStartingItems() {
        List<Item> startingItems = getStartingItems().stream().map(id -> ItemList.getItem(id).build()).collect(Collectors.toList());
        this.getBackpack().clear();
        addToBackpack(startingItems);
    }

    public void moveMonster(Map map) {
        for(int i = 0;i<getRealMovementSpeed();++i) {
            moveMonsterOnce(map);
        }
    }

    public void moveMonsterOnce(Map map) {
        int sightRange = 10;

        for(int i = 0;i<getBackpack().size();++i) {
            Item item = getItemInBackpack(i);
            if(item instanceof Equipment equipmentItem) {
                for(EquipmentSlots equipmentSlots : equipmentItem.getAllowedSlots()) {
                    if(canEquip(equipmentSlots, 1, equipmentItem) && equipmentSlotReady(equipmentSlots)) {
                        equip(i, equipmentSlots, 1);
                        return;
                    }
                }
            }
        }


        for(Equipment weaponEquipment: getEquipment().get(EquipmentSlots.WEAPON)) {
            Weapon weapon = (Weapon) weaponEquipment;
            if(canShoot(map, map.getPlayer(), weapon)) {
                map.getFightReports().addAll(attackActor(map, map.getPlayer()));
                return;
            }
        }

        List<Point> path = position.bfsTo(map.getPlayer().getPosition(),sightRange,(Point p) -> {
            return map.canMoveThrough(this, p) || getPosition().equals(p) || map.getPlayer().position.equals(p);
        });
        if(position.distanceTo(map.getPlayer().getPosition()) <= sightRange && path != null && path.size() > 1) {
            map.moveActor(this, path.get(1));
        }
        else {
            //random movement if no player found
            Random random = new Random();
            int moveX = random.nextInt(0,2);
            int moveNegative = random.nextInt(0,2);
            if(moveX == 1) {
                map.moveActor(this, moveNegative,0);
            }
            else {
                map.moveActor(this, 0, moveNegative);
            }
        }
    }

    private static final class MonsterBuilderImpl extends Monster.MonsterBuilder<Monster, Monster.MonsterBuilderImpl> {
        public Monster build() {
            Monster result = new Monster(this);
            result.giveStartingItems();
            return result;
        }
    }
}

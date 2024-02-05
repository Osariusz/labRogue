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

    public void tryEquip(Equipment equipment, int backpackSlot) {
        for(EquipmentSlots equipmentSlots : equipment.getAllowedSlots()) {
            if(canEquip(equipmentSlots, 1, equipment) && equipmentSlotReady(equipmentSlots)) {
                equip(backpackSlot, equipmentSlots, 1);
                return;
            }
        }
    }

    public void moveInRandomDirection(Map map) {
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

    public void moveMonsterOnce(Map map) {
        int sightRange = 10;

        for(int backpackSlot = 0;backpackSlot<getBackpack().size();++backpackSlot) {
            Item item = getItemInBackpack(backpackSlot);
            if(item instanceof Equipment equipmentItem) {
                tryEquip(equipmentItem, backpackSlot);
            }
        }


        for(Equipment weaponEquipment: getEquipment().get(EquipmentSlots.WEAPON)) {
            Weapon weapon = (Weapon) weaponEquipment;
            if(canShoot(map, map.getPlayer(), weapon)) {
                map.getFightReports().addAll(attackActor(map, map.getPlayer()));
                return;
            }
        }

        List<Point> pathToPlayer = position.bfsTo(map.getPlayer().getPosition(),sightRange,
                (Point p) -> map.canMoveThrough(this, p) || getPosition().equals(p) || map.getPlayer().position.equals(p)
        );
        if(position.distanceTo(map.getPlayer().getPosition()) <= sightRange && pathToPlayer != null && pathToPlayer.size() > 1) {
            map.moveActor(this, pathToPlayer.get(1));
        }
        else {
            moveInRandomDirection(map);
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

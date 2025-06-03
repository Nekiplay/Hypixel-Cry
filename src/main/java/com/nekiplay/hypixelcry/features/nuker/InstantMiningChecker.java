package com.nekiplay.hypixelcry.features.nuker;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

import static com.nekiplay.hypixelcry.HypixelCry.mc;

public class InstantMiningChecker {
    private int ground_ticks = 0;

    private final List<Class<? extends Item>> allowedItems = new ArrayList<>();


    public void addAllowedItem(Class<? extends Item> itemClass) {
        if (!allowedItems.contains(itemClass)) {
            allowedItems.add(itemClass);
        }
    }


    public boolean AllowInstantMining() {
        if (mc.thePlayer.onGround && ground_ticks > 4) {
            return true;
        }

        ItemStack currentItem = mc.thePlayer.inventory.getCurrentItem();
        boolean isAllowedItem = allowedItems.stream()
                .anyMatch(clazz -> clazz.isInstance(currentItem.getItem()));


        if (isAllowedItem) {
            if (!mc.thePlayer.onGround) {
                ground_ticks = 0;
            } else {
                ground_ticks++;
            }
        }
        return false;
    }
}
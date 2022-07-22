package com.example.examplemod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;

public class FindHotbar {

    public int findSlotInHotbar(ItemStack stack) {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayerSP player = mc.thePlayer;

        ItemStack stack36 = player.inventory.getStackInSlot(36);
        ItemStack stack37 = player.inventory.getStackInSlot(37);
        ItemStack stack38 = player.inventory.getStackInSlot(38);
        ItemStack stack39 = player.inventory.getStackInSlot(39);
        ItemStack stack40 = player.inventory.getStackInSlot(40);
        ItemStack stack41 = player.inventory.getStackInSlot(41);
        ItemStack stack42 = player.inventory.getStackInSlot(42);
        ItemStack stack43 = player.inventory.getStackInSlot(43);
        ItemStack stack44 = player.inventory.getStackInSlot(44);
        if (stack36 == stack) { return 36; }
        else if (stack37 == stack) { return 37; }
        else if (stack38 == stack) { return 38; }
        else if (stack39 == stack) { return 39; }
        else if (stack40 == stack) { return 40; }
        else if (stack41 == stack) { return 41; }
        else if (stack42 == stack) { return 42; }
        else if (stack43 == stack) { return 43; }
        else if (stack44 == stack) { return 44; }
        return 0;
    }

    public int findSlotInHotbar(String name) {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayerSP player = mc.thePlayer;

        ItemStack stack36 = player.inventory.getStackInSlot(0);
        ItemStack stack37 = player.inventory.getStackInSlot(1);
        ItemStack stack38 = player.inventory.getStackInSlot(2);
        ItemStack stack39 = player.inventory.getStackInSlot(3);
        ItemStack stack40 = player.inventory.getStackInSlot(4);
        ItemStack stack41 = player.inventory.getStackInSlot(5);
        ItemStack stack42 = player.inventory.getStackInSlot(6);
        ItemStack stack43 = player.inventory.getStackInSlot(7);
        ItemStack stack44 = player.inventory.getStackInSlot(8);
        if (stack36 != null && stack36.hasDisplayName() && stack36.getDisplayName().contains(name)) { return 0; }
        else if (stack37 != null && stack37.hasDisplayName() && stack37.getDisplayName().contains(name)) { return 1; }
        else if (stack38 != null && stack38.hasDisplayName() && stack38.getDisplayName().contains(name)) { return 2; }
        else if (stack39 != null && stack39.hasDisplayName() && stack39.getDisplayName().contains(name)) { return 3; }
        else if (stack40 != null && stack40.hasDisplayName() && stack40.getDisplayName().contains(name)) { return 4; }
        else if (stack41 != null && stack41.hasDisplayName() && stack41.getDisplayName().contains(name)) { return 5; }
        else if (stack42 != null && stack42.hasDisplayName() && stack42.getDisplayName().contains(name)) { return 6; }
        else if (stack43 != null && stack43.hasDisplayName() && stack43.getDisplayName().contains(name)) { return 7; }
        else if (stack44 != null && stack44.hasDisplayName() && stack44.getDisplayName().contains(name)) { return 8; }
        return -1;
    }
}

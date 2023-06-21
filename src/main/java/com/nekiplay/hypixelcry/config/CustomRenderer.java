package com.nekiplay.hypixelcry.config;

import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.nekiplay.hypixelcry.Main.mc;

public class CustomRenderer {
    public static ItemStack item = null;
    public static int x = 0;
    public static int y = 0;
    @SubscribeEvent
    public void ItemRenderer(GuiScreenEvent.DrawScreenEvent.Post event) {
        if (item != null) {
            mc.getRenderItem().renderItemIntoGUI(item, x, y);
        }
    }
}

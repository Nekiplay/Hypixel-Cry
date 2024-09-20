package com.nekiplay.hypixelcry.features.macros;

import com.nekiplay.hypixelcry.FindHotbar;
import com.nekiplay.hypixelcry.Main;
import com.nekiplay.hypixelcry.mixins.MinecraftAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;
import java.awt.event.InputEvent;

public class WandofHealing {
    private int last_slot = 0;
    private int tick = 0;
    private int rogueSlot = -1;

    private int tick2 = 0;
    private int tick2_max = 0;
    private int tick3_max = 0;

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void TickEvent(TickEvent.ClientTickEvent event) {
        if (work) {
            Minecraft mc = Minecraft.getMinecraft();
            EntityPlayerSP player = mc.thePlayer;
            if (rogueSlot != player.inventory.currentItem && tick2 == 0) {
                last_slot = player.inventory.currentItem;
                mc.thePlayer.inventory.currentItem = rogueSlot;
                tick2++;
            }
            if (player.inventory.currentItem == rogueSlot && tick2 == 1) {
                try {
                    Robot robot = new Robot();
                    robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
                    robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
                }
                catch (Exception e) { }
                tick2++;
            } else if (player.inventory.currentItem == rogueSlot && tick2 == 2) {
                mc.thePlayer.inventory.currentItem = last_slot;
                tick2 = 0;
                work = false;
            }

            if (rogueSlot != player.inventory.currentItem) {
                tick2 = 0;
                work = false;
            }
        }
    }

    private boolean work;

    public void enable() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc != null) {
            EntityPlayerSP player = mc.thePlayer;
            if (player != null) {
                int slot = FindHotbar.findSlotInHotbar("Wand of Healing");
                int slot2 = FindHotbar.findSlotInHotbar("Wand of Mending");
                int slot3 = FindHotbar.findSlotInHotbar("Wand of Restoration");
                int slot4 = FindHotbar.findSlotInHotbar("Wand of Atonement");
                if (slot != -1 || slot2 != -1 || slot3 != -1 || slot4 != -1) {
                    if (slot != -1) {
                        rogueSlot = slot;
                    }
                    else if (slot2 != -1) {
                        rogueSlot = slot2;
                    }
                    else if (slot3 != -1) {
                        rogueSlot = slot3;
                    }
                    else if (slot4 != -1) {
                        rogueSlot = slot4;
                    }
                    work = true;
                }
                else {
                    tick = 0;
                    work = false;
                    player.addChatMessage(new ChatComponentText(Main.prefix + EnumChatFormatting.RED + "Wand of Healing not found"));
                }
            }
        }
    }
}

package com.nekiplay.hypixelcry.features.macros;

import com.nekiplay.hypixelcry.FindHotbar;
import com.nekiplay.hypixelcry.Main;
import com.nekiplay.hypixelcry.mixins.MinecraftAccessor;
import com.nekiplay.hypixelcry.utils.RandomUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;
import java.awt.event.InputEvent;

import static com.nekiplay.hypixelcry.Main.mc;
import static com.nekiplay.hypixelcry.Main.myConfigFile;

public class RogueSword {
    private int last_slot = 0;
    private int rogueSlot = -1;

    private int tick1 = 0;
    private boolean used = false;
    @SubscribeEvent
    public void TickEvent(TickEvent.ClientTickEvent event) {
        if (work) {
            Minecraft mc = Minecraft.getMinecraft();
            EntityPlayerSP player = mc.thePlayer;

            if (tick1 == 0) {
                last_slot = player.inventory.currentItem;
                mc.thePlayer.inventory.currentItem = rogueSlot;
                tick1++;
            }
            else if (tick1 == 1) {
                if (!used) {
                    try {
                        Robot robot = new Robot();
                        robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
                        robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
                        used = true;
                    }
                    catch (AWTException e) {
                        throw new RuntimeException(e);
                    }
                }
                else {
                    mc.thePlayer.inventory.currentItem = last_slot;
                    work = false;
                    resetTimings();
                }
            }
        }
    }

    private boolean work;

    public void resetTimings() {
        tick1 = 0;

        used = false;
    }

    public void enable() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc != null) {
            EntityPlayerSP player = mc.thePlayer;
            if (player != null) {
                int slot = FindHotbar.findSlotInHotbar("Rogue Sword");
                if (slot != -1) {
                    resetTimings();
                    rogueSlot = slot;
                    work = true;
                } else {
                    resetTimings();
                    work = false;
                    player.addChatMessage(new ChatComponentText(Main.prefix + EnumChatFormatting.RED + "Rogue Sword not found"));
                }
            }
        }
    }
}

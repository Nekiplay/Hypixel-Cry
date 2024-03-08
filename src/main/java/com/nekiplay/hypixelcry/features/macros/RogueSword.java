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
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static com.nekiplay.hypixelcry.Main.mc;
import static com.nekiplay.hypixelcry.Main.myConfigFile;

public class RogueSword {
    private int last_slot = 0;
    private int rogueSlot = -1;

    private int tick_timer1 = 0;
    private int tick_timer2 = 0;
    private int tick_timer3 = 0;

    private int tick1 = 0;
    private int tick2 = 0;
    private int tick3 = 0;
    private boolean used = false;
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void TickEvent(TickEvent.ClientTickEvent event) {
        if (work) {
            Minecraft mc = Minecraft.getMinecraft();
            EntityPlayerSP player = mc.thePlayer;

            if (rogueSlot != player.inventory.currentItem) {
                last_slot = player.inventory.currentItem;
            }

            if (tick1 <= tick_timer1) {
                mc.thePlayer.inventory.currentItem = rogueSlot;
                tick1++;
            }
            else if (tick2 <= tick_timer2) {
                if (player.inventory.currentItem == rogueSlot && !used) {
                    MinecraftAccessor minecraftAccessor = (MinecraftAccessor) mc;
                    minecraftAccessor.rightClickMouse();
                    used = true;
                }
                else if (player.inventory.currentItem != rogueSlot && !used) {
                    work = false;
                }
                tick2++;
            }
        }
        else {
            resetTimings();
        }
    }

    private boolean work;

    public void resetTimings() {
        tick1 = 0;
        tick2 = 0;
        tick3 = 0;

        used = false;

        tick_timer1 = RandomUtils.nextInt(5, 15);
        tick_timer2 = RandomUtils.nextInt(5, 15);
        tick_timer3 = RandomUtils.nextInt(5, 15);
    }

    public void enable() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc != null) {
            EntityPlayerSP player = mc.thePlayer;
            if (player != null) {
                FindHotbar findHotbar = new FindHotbar();
                int slot = findHotbar.findSlotInHotbar("Rogue Sword");
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

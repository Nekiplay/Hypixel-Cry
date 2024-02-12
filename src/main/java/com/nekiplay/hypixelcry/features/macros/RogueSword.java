package com.nekiplay.hypixelcry.features.macros;

import com.nekiplay.hypixelcry.FindHotbar;
import com.nekiplay.hypixelcry.Main;
import com.nekiplay.hypixelcry.mixins.MinecraftAccessor;
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
                MinecraftAccessor minecraftAccessor = (MinecraftAccessor)mc;
                minecraftAccessor.rightClickMouse();
                tick2++;
            }
            else if (player.inventory.currentItem == rogueSlot && tick2 == 2) {
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
                FindHotbar findHotbar = new FindHotbar();
                int slot = findHotbar.findSlotInHotbar("Rogue Sword");
                if (slot != -1) {
                    rogueSlot = slot;
                    work = true;
                } else {
                    tick = 0;
                    work = false;
                    player.addChatMessage(new ChatComponentText(Main.prefix + EnumChatFormatting.RED + "Rogue Sword not found"));
                }
            }
        }
    }
}

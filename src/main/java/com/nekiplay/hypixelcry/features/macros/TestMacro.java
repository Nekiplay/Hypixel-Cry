package com.nekiplay.hypixelcry.features.macros;

import com.nekiplay.hypixelcry.DataInterpretation.DataExtractor;
import com.nekiplay.hypixelcry.FindHotbar;
import com.nekiplay.hypixelcry.Main;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TestMacro {
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
            last_slot = player.inventory.currentItem;
            if (rogueSlot != player.inventory.currentItem && tick2 == 0) {
                mc.thePlayer.inventory.currentItem = rogueSlot;
            }
            if (player.inventory.currentItem == rogueSlot && tick2 == 3) {
                mc.getNetHandler().getNetworkManager().sendPacket(new C08PacketPlayerBlockPlacement(player.inventory.getCurrentItem()));
            }
            else if (player.inventory.currentItem == rogueSlot && tick2 == 6) {
                if (rogueSlot != player.inventory.currentItem) {
                    mc.thePlayer.inventory.currentItem = last_slot;
                    tick2 = 0;
                    work = false;
                }
            }
            else
            {
                tick2 = tick2 + 1;
            }
        }
    }

    private boolean work;

    @SubscribeEvent(priority= EventPriority.NORMAL, receiveCanceled=true)
    public void onEvent(InputEvent.KeyInputEvent event)
    {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayerSP player = mc.thePlayer;
        KeyBinding[] keyBindings = Main.keyBindings;
        if (keyBindings[10].isPressed()) {
            FindHotbar findHotbar = new FindHotbar();
            int slot = findHotbar.findSlotInHotbar("Stone");
            if (slot != -1) {
                rogueSlot = slot;
                work = true;
            } else {
                tick = 0;
                work = false;
                player.addChatMessage(new ChatComponentText(Main.prefix + EnumChatFormatting.RED +  "Rogue Sword not found"));
            }
        }
    }

    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
    public void onEventMouse(InputEvent.MouseInputEvent event)
    {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayerSP player = mc.thePlayer;
        KeyBinding[] keyBindings = Main.keyBindings;
        if (keyBindings[10].isPressed()) {
            FindHotbar findHotbar = new FindHotbar();
            int slot = findHotbar.findSlotInHotbar("Stone");
            if (slot != -1) {
                rogueSlot = slot;
                work = true;
            } else {
                tick = 0;
                work = false;
                player.addChatMessage(new ChatComponentText(Main.prefix + EnumChatFormatting.RED +  "Rogue Sword not found"));
            }
        }
    }
}

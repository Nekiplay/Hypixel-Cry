package com.example.examplemod.macros;

import com.example.examplemod.DataInterpretation.DataExtractor;
import com.example.examplemod.FindHotbar;
import com.example.examplemod.Main;
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

public class RogueSword {

    private int last_slot = 0;
    private int tick = 0;
    private int rogueSlot = -1;
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void TickEvent(TickEvent.ClientTickEvent event) {
        if (work) {
            Minecraft mc = Minecraft.getMinecraft();
            EntityPlayerSP player = mc.thePlayer;
            last_slot = player.inventory.currentItem;
            if (rogueSlot != player.inventory.currentItem) {
                mc.getNetHandler().getNetworkManager().sendPacket(new C09PacketHeldItemChange(rogueSlot));
            }
            mc.getNetHandler().getNetworkManager().sendPacket(new C08PacketPlayerBlockPlacement(player.inventory.getCurrentItem()));
            if (rogueSlot != player.inventory.currentItem) {
                mc.getNetHandler().getNetworkManager().sendPacket(new C09PacketHeldItemChange(last_slot));
            }
            work = false;
        }
    }

    private boolean work;

    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
    public void onEvent(InputEvent.KeyInputEvent event)
    {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayerSP player = mc.thePlayer;
        KeyBinding[] keyBindings = Main.keyBindings;
        if (keyBindings[0].isPressed()) {
            FindHotbar findHotbar = new FindHotbar();
            int slot = findHotbar.findSlotInHotbar("Rogue Sword");
            DataExtractor extractor = Main.getInstance().dataExtractor;
            if (extractor.getPlayerStats().Mp >= 50) {
                if (slot != -1) {
                    rogueSlot = slot;
                    work = true;
                } else {
                    tick = 0;
                    work = false;
                    player.addChatMessage(new ChatComponentText(Main.prefix + EnumChatFormatting.RED +  "Rogue Sword not found"));
                }
            }
            else
            {
                player.addChatMessage(new ChatComponentText(Main.prefix + EnumChatFormatting.RED +  "Not enough MP"));
            }
        }
    }

    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
    public void onEventMouse(InputEvent.MouseInputEvent event)
    {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayerSP player = mc.thePlayer;
        KeyBinding[] keyBindings = Main.keyBindings;
        if (keyBindings[0].isPressed()) {
            FindHotbar findHotbar = new FindHotbar();
            DataExtractor extractor = Main.getInstance().dataExtractor;
            if (extractor.getPlayerStats().Mp >= 50) {
                int slot = findHotbar.findSlotInHotbar("Rogue Sword");
                if (slot != -1) {
                    rogueSlot = slot;
                    work = true;
                } else {
                    tick = 0;
                    work = false;
                    player.addChatMessage(new ChatComponentText(Main.prefix + EnumChatFormatting.RED +  "Rogue Sword not found"));
                }
            }
            else
            {
                player.addChatMessage(new ChatComponentText(Main.prefix + EnumChatFormatting.RED + "Not enough MP"));
            }
        }
    }
}

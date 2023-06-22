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

public class WandofHealing {
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

    @SubscribeEvent(priority= EventPriority.NORMAL, receiveCanceled=true)
    public void onEvent(InputEvent.KeyInputEvent event)
    {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayerSP player = mc.thePlayer;
        KeyBinding[] keyBindings = Main.keyBindings;
        if (keyBindings[1].isPressed()) {
            FindHotbar findHotbar = new FindHotbar();
            DataExtractor extractor = Main.getInstance().dataExtractor;
            int slot = findHotbar.findSlotInHotbar("Wand of Healing");
            int slot2 = findHotbar.findSlotInHotbar("Wand of Mending");
            int slot3 = findHotbar.findSlotInHotbar("Wand of Restoration");
            int slot4 = findHotbar.findSlotInHotbar("Wand of Atonement");
            if (slot != -1 || slot2 != -1 || slot3 != -1 || slot4 != -1) {
                if (slot4 != -1) {
                    if (extractor.getPlayerStats().Mp >= 240) {
                        rogueSlot = slot4;
                        work = true;
                    }
                    else
                    {
                        player.addChatMessage(new ChatComponentText(Main.prefix + EnumChatFormatting.RED +  "Not enough MP"));
                    }
                }
                else if (slot3 != -1) {
                    if (extractor.getPlayerStats().Mp >= 200) {
                        rogueSlot = slot3;
                        work = true;
                    }
                    else
                    {
                        player.addChatMessage(new ChatComponentText(Main.prefix + EnumChatFormatting.RED +  "Not enough MP"));
                    }
                }
                else if (slot2 != -1) {
                    if (extractor.getPlayerStats().Mp >= 100) {
                        rogueSlot = slot2;
                        work = true;
                    }
                    else
                    {
                        player.addChatMessage(new ChatComponentText(Main.prefix + EnumChatFormatting.RED +  "Not enough MP"));
                    }
                }
                else if (slot != -1) {
                    if (extractor.getPlayerStats().Mp >= 60) {
                        rogueSlot = slot;
                        work = true;
                    }
                    else
                    {
                        player.addChatMessage(new ChatComponentText(Main.prefix + EnumChatFormatting.RED +  "Not enough MP"));
                    }
                }
            }
            else {
                tick = 0;
                work = false;
                player.addChatMessage(new ChatComponentText(Main.prefix + EnumChatFormatting.RED + "Wand of Healing not found"));
            }
        }
    }

    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
    public void onEventMouse(InputEvent.MouseInputEvent event)
    {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayerSP player = mc.thePlayer;
        KeyBinding[] keyBindings = Main.keyBindings;
        if (keyBindings[1].isPressed()) {
            FindHotbar findHotbar = new FindHotbar();
            DataExtractor extractor = Main.getInstance().dataExtractor;
            int slot = findHotbar.findSlotInHotbar("Wand of Healing");
            int slot2 = findHotbar.findSlotInHotbar("Wand of Mending");
            int slot3 = findHotbar.findSlotInHotbar("Wand of Restoration");
            int slot4 = findHotbar.findSlotInHotbar("Wand of Atonement");
            if (slot != -1 || slot2 != -1 || slot3 != -1 || slot4 != -1) {
                if (slot4 != -1) {
                    if (extractor.getPlayerStats().Mp >= 240) {
                        rogueSlot = slot4;
                        work = true;
                    }
                    else
                    {
                        player.addChatMessage(new ChatComponentText(Main.prefix + EnumChatFormatting.RED +  "Not enough MP"));
                    }
                }
                else if (slot3 != -1) {
                    if (extractor.getPlayerStats().Mp >= 200) {
                        rogueSlot = slot3;
                        work = true;
                    }
                    else
                    {
                        player.addChatMessage(new ChatComponentText(Main.prefix + EnumChatFormatting.RED +  "Not enough MP"));
                    }
                }
                else if (slot2 != -1) {
                    if (extractor.getPlayerStats().Mp >= 100) {
                        rogueSlot = slot2;
                        work = true;
                    }
                    else
                    {
                        player.addChatMessage(new ChatComponentText(Main.prefix + EnumChatFormatting.RED +  "Not enough MP"));
                    }
                }
                else if (slot != -1) {
                    if (extractor.getPlayerStats().Mp >= 60) {
                        rogueSlot = slot;
                        work = true;
                    }
                    else
                    {
                        player.addChatMessage(new ChatComponentText(Main.prefix + EnumChatFormatting.RED +  "Not enough MP"));
                    }
                }
            }
            else {
                tick = 0;
                work = false;
                player.addChatMessage(new ChatComponentText(Main.prefix + EnumChatFormatting.RED + "Wand of Healing not found"));
            }
        }
    }
}

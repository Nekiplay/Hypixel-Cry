package com.example.examplemod.macros;

import com.example.examplemod.DataInterpretation.DataExtractor;
import com.example.examplemod.FindHotbar;
import com.example.examplemod.Main;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class AspectoftheEnd {
    private int last_slot = 0;
    private int tick = 0;
    private int rogueSlot = -1;
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void TickEvent(TickEvent.ClientTickEvent event) {
        if (work) {
            Minecraft mc = Minecraft.getMinecraft();
            EntityPlayerSP player = mc.thePlayer;
            if (tick == 0) {
                last_slot = player.inventory.currentItem;
                player.inventory.currentItem = rogueSlot;
                tick++;
            }
            else if (tick == 1) {
                FindHotbar findHotbar = new FindHotbar();
                int slot = findHotbar.findSlotInHotbar("Aspect of the End");
                if (slot == player.inventory.currentItem) {
                    mc.playerController.sendUseItem(player, mc.theWorld, player.inventory.getCurrentItem());
                    tick++;
                }
            }
            else if (tick == 2) {
                player.inventory.currentItem = last_slot;
                work = false;
                tick = 0;
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
        DataExtractor extractor = Main.getInstance().dataExtractor;
        if (keyBindings[2].isPressed()) {
            if (extractor.getPlayerStats().Mp >= 50) {
                FindHotbar findHotbar = new FindHotbar();
                int slot = findHotbar.findSlotInHotbar("Aspect of the End");
                if (slot != -1) {
                    rogueSlot = slot;
                    work = true;
                } else {
                    tick = 0;
                    work = false;
                    player.addChatMessage(new ChatComponentText("Aspect of the End not found"));
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
        if (keyBindings[2].isPressed()) {
            FindHotbar findHotbar = new FindHotbar();
            int slot = findHotbar.findSlotInHotbar("Aspect of the End");
            if (slot != -1) {
                rogueSlot = slot;
                work = true;
            }
            else {
                tick = 0;
                work = false;
                player.addChatMessage(new ChatComponentText("Aspect of the End not found"));
            }
        }
    }
}

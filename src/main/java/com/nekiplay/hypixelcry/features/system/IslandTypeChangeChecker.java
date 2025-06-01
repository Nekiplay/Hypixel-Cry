package com.nekiplay.hypixelcry.features.system;

import com.nekiplay.hypixelcry.HypixelCry;
import com.nekiplay.hypixelcry.data.island.IslandType;
import com.nekiplay.hypixelcry.events.hypixel.IslandTypeChangeEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import static com.nekiplay.hypixelcry.HypixelCry.mc;

public class IslandTypeChangeChecker {
    private static IslandType lastDetected = IslandType.Unknown;

    public static IslandType getLastDetected() {
        return lastDetected;
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) return;

        IslandType current = IslandType.current();

        if (current != IslandType.Unknown && lastDetected != current) {
            if (HypixelCry.config.misc.debug.enabled && mc.thePlayer != null) {
                mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Location changed from: " + lastDetected + " to: " + current));
            }
            IslandTypeChangeEvent islandTypeChangeEvent = new IslandTypeChangeEvent(lastDetected, current);
            lastDetected = current;
            MinecraftForge.EVENT_BUS.post(islandTypeChangeEvent);
        }
        else if (lastDetected != current) {
            if (HypixelCry.config.misc.debug.enabled && mc.thePlayer != null) {
                mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Location changed from: " + lastDetected + " to: " + current));
            }
            lastDetected = current;
        }
    }
}

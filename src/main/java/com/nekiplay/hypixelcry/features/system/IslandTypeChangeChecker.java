package com.nekiplay.hypixelcry.features.system;

import com.nekiplay.hypixelcry.Main;
import com.nekiplay.hypixelcry.data.island.IslandType;
import com.nekiplay.hypixelcry.events.hypixel.IslandTypeChangeEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import static com.nekiplay.hypixelcry.Main.mc;

public class IslandTypeChangeChecker {
    public IslandType lastDetected = IslandType.Unknown;
    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) return;

        IslandType current = IslandType.current();

        if (current != IslandType.Unknown && lastDetected != current) {
            if (Main.config.misc.debug.enabled) {
                mc.thePlayer.addChatMessage(new ChatComponentText("Location changed from: " + lastDetected + " to: " + current));
            }
            IslandTypeChangeEvent islandTypeChangeEvent = new IslandTypeChangeEvent(lastDetected, current);
            lastDetected = current;
            MinecraftForge.EVENT_BUS.post(islandTypeChangeEvent);
        }
        else if (lastDetected != current) {
            if (Main.config.misc.debug.enabled) {
                mc.thePlayer.addChatMessage(new ChatComponentText("Location changed from: " + lastDetected + " to: " + current));
            }
            lastDetected = current;
        }
    }
}

package com.nekiplay.hypixelcry.data;

import com.nekiplay.hypixelcry.DataInterpretation.IslandType;
import com.nekiplay.hypixelcry.events.hypixel.IslandTypeChangeEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class IslandTypeChangeChecker {
    public IslandType lastDetected = IslandType.Unknown;
    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) return;

        IslandType current = IslandType.current();

        if (current != IslandType.Unknown && lastDetected != current) {
            IslandTypeChangeEvent islandTypeChangeEvent = new IslandTypeChangeEvent(lastDetected, current);
            MinecraftForge.EVENT_BUS.post(islandTypeChangeEvent);
        }
    }
}

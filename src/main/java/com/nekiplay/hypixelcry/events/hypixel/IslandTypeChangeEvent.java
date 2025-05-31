package com.nekiplay.hypixelcry.events.hypixel;

import com.nekiplay.hypixelcry.DataInterpretation.IslandType;
import net.minecraftforge.fml.common.eventhandler.Event;

public class IslandTypeChangeEvent extends Event {
    public IslandType old;
    public IslandType current;

    public IslandTypeChangeEvent(IslandType old, IslandType current) {
        this.old = old;
        this.current = current;
    }
}

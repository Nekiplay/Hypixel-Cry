package com.nekiplay.hypixelcry.events;

import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.eventhandler.Event;

public class MouseClick extends Event {
    public static class Left extends MouseClick { }
    public static class Right extends MouseClick { }
}


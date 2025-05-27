package com.nekiplay.hypixelcry.events.minecraft;

import net.minecraftforge.fml.common.eventhandler.Event;

public class KeyDownEvent extends Event {
    public int keyCode;

    public KeyDownEvent(int keyCode) {
        this.keyCode = keyCode;
    }
}

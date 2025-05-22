package com.nekiplay.hypixelcry.events.minecraft;

import net.minecraftforge.fml.common.eventhandler.Event;

public class KeyUpEvent extends Event {
    public int keyCode;

    public KeyUpEvent(int keyCode) {
        this.keyCode = keyCode;
    }
}

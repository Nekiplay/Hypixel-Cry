package com.nekiplay.hypixelcry.events.minecraft;

import net.minecraftforge.fml.common.eventhandler.Event;

public class KeyPressEvent extends Event {
    public int keyCode;

    public KeyPressEvent(int keyCode) {
        this.keyCode = keyCode;
    }
}

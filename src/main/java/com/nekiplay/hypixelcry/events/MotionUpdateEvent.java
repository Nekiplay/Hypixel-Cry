package com.nekiplay.hypixelcry.events;

import net.minecraftforge.fml.common.eventhandler.Event;

public class MotionUpdateEvent extends Event {
    public float yaw;
    public float pitch;

    public MotionUpdateEvent(float yaw, float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
    }
}
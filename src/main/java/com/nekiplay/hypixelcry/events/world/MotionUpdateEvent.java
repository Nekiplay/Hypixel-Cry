package com.nekiplay.hypixelcry.events.world;

public class MotionUpdateEvent {
    public float yaw;
    public float pitch;

    public MotionUpdateEvent(float yaw, float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
    }
}

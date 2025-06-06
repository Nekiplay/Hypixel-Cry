package com.nekiplay.hypixelcry.events.world;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public interface MotionUpdateCallback {
    Event<MotionUpdateCallback> EVENT = EventFactory.createArrayBacked(MotionUpdateCallback.class,
            (listeners) -> (yaw, pitch) -> {
                for (MotionUpdateCallback listener : listeners) {

                    return listener.update(yaw, pitch);
                }

                return null;
            });

    MotionUpdateEvent update(float yaw, float pitch);
}

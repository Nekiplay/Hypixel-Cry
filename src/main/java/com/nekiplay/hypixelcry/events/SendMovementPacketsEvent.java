package com.nekiplay.hypixelcry.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public class SendMovementPacketsEvent {
    public static final Event<Pre> PRE = EventFactory.createArrayBacked(Pre.class,
            (listeners) -> () -> {
                for (Pre listener : listeners) {
                    listener.onSendMovementPacketsPre();
                }
            }
    );

    public static final Event<Post> POST = EventFactory.createArrayBacked(Post.class,
            (listeners) -> () -> {
                for (Post listener : listeners) {
                    listener.onSendMovementPacketsPost();
                }
            }
    );

    @FunctionalInterface
    public interface Pre {
        void onSendMovementPacketsPre();
    }

    @FunctionalInterface
    public interface Post {
        void onSendMovementPacketsPost();
    }
}
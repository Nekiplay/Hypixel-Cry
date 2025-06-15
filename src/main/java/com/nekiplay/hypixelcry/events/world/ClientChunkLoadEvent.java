package com.nekiplay.hypixelcry.events.world;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.world.chunk.Chunk;

public class ClientChunkLoadEvent {
    public static final Event<ClientChunkLoadCallback> EVENT = EventFactory.createArrayBacked(
            ClientChunkLoadCallback.class,
            (listeners) -> (world, chunk) -> {
                for (ClientChunkLoadCallback listener : listeners) {
                    listener.onChunkLoad(world, chunk);
                }
            }
    );

    public interface ClientChunkLoadCallback {
        void onChunkLoad(ClientWorld world, Chunk chunk);
    }
}

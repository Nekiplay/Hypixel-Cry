package com.nekiplay.hypixelcry.mixins;

import com.nekiplay.hypixelcry.events.world.ClientChunkLoadEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientChunkManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.ChunkData;
import net.minecraft.world.Heightmap;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.function.Consumer;

@Mixin(ClientChunkManager.class)
public abstract class ClientChunkManagerMixin {
    @Inject(
            method = "loadChunkFromPacket",
            at = @At("TAIL")
    )
    private void onChunkLoad(int x, int z, PacketByteBuf buf, Map<Heightmap.Type, long[]> heightmaps, Consumer<ChunkData.BlockEntityVisitor> consumer, CallbackInfoReturnable<WorldChunk> cir) {
        Chunk chunk = cir.getReturnValue();
        if (chunk != null) {
            ClientWorld world = MinecraftClient.getInstance().world;
            if (world != null) {
                ClientChunkLoadEvent.EVENT.invoker().onChunkLoad(world, chunk);
            }
        }
    }
}
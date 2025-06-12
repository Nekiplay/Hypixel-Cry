package com.nekiplay.hypixelcry.features.esp;

import com.nekiplay.hypixelcry.HypixelCry;
import com.nekiplay.hypixelcry.annotations.Init;
import com.nekiplay.hypixelcry.config.enums.ESPFeatures;
import com.nekiplay.hypixelcry.utils.SpecialColor;
import com.nekiplay.hypixelcry.utils.render.RenderHelper;
import com.nekiplay.hypixelcry.utils.scheduler.Scheduler;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.WorldChunk;

import java.util.ArrayList;
import java.util.List;

import static com.nekiplay.hypixelcry.HypixelCry.mc;

public class ChestESP {
    private static final List<BlockPos> chestLocations = new ArrayList<>();

    @Init
    public static void init() {
        WorldRenderEvents.AFTER_TRANSLUCENT.register(ChestESP::render);
        Scheduler.INSTANCE.scheduleCyclic(ChestESP::onTick, 2);
    }

    public static void onTick() {
        chestLocations.clear();

        if (mc.world == null || mc.player == null) return;

        double maxRangeSq = Math.pow(HypixelCry.config.esp.chestEsp.maxRange, 2);
        int playerChunkX = mc.player.getChunkPos().x;
        int playerChunkZ = mc.player.getChunkPos().z;
        int renderDistance = mc.options.getViewDistance().getValue();

        // Iterate through chunks around the player
        for (int x = playerChunkX - renderDistance; x <= playerChunkX + renderDistance; x++) {
            for (int z = playerChunkZ - renderDistance; z <= playerChunkZ + renderDistance; z++) {
                WorldChunk chunk = mc.world.getChunk(x, z);
                if (chunk == null) continue;

                // Iterate through block entities in the chunk
                for (BlockEntity blockEntity : chunk.getBlockEntities().values()) {
                    if (blockEntity instanceof ChestBlockEntity) {
                        BlockPos pos = blockEntity.getPos();
                        if (HypixelCry.config.esp.chestEsp.maxRange == 0 ||
                                mc.player.squaredDistanceTo(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) <= maxRangeSq) {
                            chestLocations.add(pos);
                        }
                    }
                }
            }
        }
    }

    public static void render(WorldRenderContext context) {
        if (!HypixelCry.config.esp.chestEsp.enabled || chestLocations.isEmpty()) return;

        float[] colorComponents = SpecialColor.toSpecialColorFloatArray(HypixelCry.config.esp.chestEsp.colour);



        float alpha = colorComponents[3];
        float lineWidth = 1.5f; // Толщина линии
        boolean throughWalls = true; // Рендерить через стены

        for (BlockPos pos : chestLocations) {

            if (HypixelCry.config.esp.chestEsp.features.contains(ESPFeatures.Box)) {
                RenderHelper.renderFilled(context, pos, colorComponents, alpha, throughWalls);
                RenderHelper.renderOutline(context, pos, colorComponents, lineWidth, throughWalls);
            }
            if (HypixelCry.config.esp.chestEsp.features.contains(ESPFeatures.Text)) {
                RenderHelper.renderText(context, Text.of("Chest").asOrderedText(), pos.toCenterPos().add(0, 1, 0), SpecialColor.toSpecialColorIntNoAlpha(HypixelCry.config.esp.chestEsp.colour), 1, 0.5f, throughWalls);
            }
            if (HypixelCry.config.esp.chestEsp.features.contains(ESPFeatures.Tracer)) {
                RenderHelper.renderLineFromCursor(context, pos.toCenterPos(), colorComponents, 1, lineWidth);
            }
        }
    }
}

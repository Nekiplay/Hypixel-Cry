package com.nekiplay.hypixelcry.features.esp.pathFinders.detections.crystalhollows;

import com.nekiplay.hypixelcry.features.esp.pathFinders.PathFinderRenderer;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class JungleTemple {
    private static final ConcurrentHashMap<Long, Boolean> processedChunks = new ConcurrentHashMap<>();
    private static BlockPos currentTemplePos = null;
    private static final String TEMPLE_KEY = "JungleTemple_Unique";

    @SubscribeEvent
    public void onChunkLoad(ChunkEvent.Load event) {
        Chunk chunk = event.getChunk();
        long chunkKey = getChunkKey(chunk.xPosition, chunk.zPosition);

        if (!processedChunks.containsKey(chunkKey)) {
            processedChunks.put(chunkKey, true);
            scanChunkForTemple(event.world, chunk);
        }
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        removeTempleWaypoint();
        processedChunks.clear();
    }

    private static long getChunkKey(int x, int z) {
        return ((long)x << 32) | (z & 0xFFFFFFFFL);
    }

    private static void scanChunkForTemple(World world, Chunk chunk) {
        new Thread(() -> {
            try {
                for (int x = 0; x < 16; x++) {
                    for (int z = 0; z < 16; z++) {
                        for (int y = 0; y < 256; y++) {
                            BlockPos pos = new BlockPos(
                                    (chunk.xPosition << 4) + x,
                                    y,
                                    (chunk.zPosition << 4) + z
                            );

                            if (isTempleStructure(world, pos)) {
                                updateTemplePosition(pos);
                                return; // Прекращаем поиск после нахождения храма
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private static boolean isTempleStructure(World world, BlockPos pos) {
        Block block = world.getBlockState(pos).getBlock();
        if (block != Blocks.emerald_block) return false;

        BlockPos belowPos = pos.down();
        BlockPos abovePos1 = pos.up();
        BlockPos abovePos2 = abovePos1.up();

        return world.getBlockState(belowPos).getBlock() == Blocks.stone &&
                world.getBlockState(abovePos1).getBlock() == Blocks.emerald_block &&
                world.getBlockState(abovePos2).getBlock() == Blocks.emerald_block;
    }

    private static void updateTemplePosition(BlockPos newPos) {
        Minecraft.getMinecraft().addScheduledTask(() -> {
            // Если позиция изменилась или это первый храм
            if (currentTemplePos == null || !currentTemplePos.equals(newPos)) {
                // Удаляем старый вейпоинт
                if (currentTemplePos != null) {
                    PathFinderRenderer.removePath(TEMPLE_KEY);
                }

                // Сохраняем новую позицию и добавляем вейпоинт
                currentTemplePos = newPos;
                PathFinderRenderer.addOrUpdatePath(
                        TEMPLE_KEY,
                        newPos.up(), // Показываем на 1 блок выше основания
                        Color.GREEN,
                        "Jungle Temple"
                );
            }
        });
    }

    private static void removeTempleWaypoint() {
        Minecraft.getMinecraft().addScheduledTask(() -> {
            if (currentTemplePos != null) {
                PathFinderRenderer.removePath(TEMPLE_KEY);
                currentTemplePos = null;
            }
        });
    }
}

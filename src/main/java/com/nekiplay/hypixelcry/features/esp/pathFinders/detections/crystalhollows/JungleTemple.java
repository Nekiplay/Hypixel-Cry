package com.nekiplay.hypixelcry.features.esp.pathFinders.detections.crystalhollows;

import com.nekiplay.hypixelcry.HypixelCry;
import com.nekiplay.hypixelcry.features.esp.pathFinders.PathFinderRenderer;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;

public class JungleTemple {
    public static BlockPos jungleTemple = null;
    private static volatile boolean running = false;
    private static Thread detectionThread;
    private static boolean foundAndNotified = false; // Флаг для отслеживания отправки сообщения
    private static final int SEARCH_RADIUS = 200; // Радиус поиска

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        jungleTemple = null;
        foundAndNotified = false;
        stopDetection();
        if (PathFinderRenderer.hasPath("Temple")) {
            PathFinderRenderer.removePath("Temple");
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START &&
                event.type == TickEvent.Type.CLIENT &&
                Minecraft.getMinecraft().theWorld != null &&
                Minecraft.getMinecraft().thePlayer != null) {

            // Постоянный поиск (каждый тик)
            if (!running && !foundAndNotified) {
                startDetection(Minecraft.getMinecraft().theWorld,
                        Minecraft.getMinecraft().thePlayer.getPosition(),
                        SEARCH_RADIUS);
            }
        }
    }

    public static void startDetection(World world, BlockPos center, int radius) {
        if (running) return;

        running = true;
        detectionThread = new Thread(() -> {
            BlockPos foundPos = findEmeraldWithSmoothAndesiteBelow(world, center, radius);

            if (HypixelCry.config.esp.crystalHollows.pathFinder.enabledJungleTemple) {
                if (foundPos != null && !foundAndNotified) {
                    jungleTemple = foundPos;
                    foundAndNotified = true;

                    PathFinderRenderer.addOrUpdatePath("Temple", foundPos, Color.GREEN, "Temple");
                }
            }
            else {
                if (PathFinderRenderer.hasPath("Temple")) {
                    PathFinderRenderer.removePath("Temple");
                }
                foundAndNotified = false;
            }

            running = false;
        }, "JungleTempleDetector");
        detectionThread.start();
    }

    public static void stopDetection() {
        running = false;
        if (detectionThread != null && detectionThread.isAlive()) {
            detectionThread.interrupt();
        }
    }

    public static BlockPos findEmeraldWithSmoothAndesiteBelow(World world, BlockPos center, int radius) {
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                for (int y = -radius; y <= radius; y++) {
                    BlockPos checkPos = center.add(x, y, z);

                    IBlockState state = world.getBlockState(checkPos);
                    Block block = state.getBlock();

                    if (block == Blocks.emerald_block) {
                        BlockPos upPos = checkPos.up();
                        IBlockState upState = world.getBlockState(upPos);
                        Block upBlock = upState.getBlock();

                        BlockPos belowPos = checkPos.down();
                        IBlockState belowState = world.getBlockState(belowPos);
                        Block belowBlock = belowState.getBlock();

                        boolean isAndesite = belowBlock == Blocks.stone && upBlock == Blocks.emerald_block && 
                                belowState.getBlock().getMetaFromState(belowState) == 6;
                        if (isAndesite) {
                            return checkPos;
                        }
                    }
                }
            }
        }
        return null;
    }
}

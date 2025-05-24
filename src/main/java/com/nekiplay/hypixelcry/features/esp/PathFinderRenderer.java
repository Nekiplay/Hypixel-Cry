package com.nekiplay.hypixelcry.features.esp;

import com.nekiplay.hypixelcry.Main;
import com.nekiplay.hypixelcry.config.ESPFeatures;
import com.nekiplay.hypixelcry.utils.PathFinder;
import com.nekiplay.hypixelcry.utils.RenderUtils;
import com.nekiplay.hypixelcry.utils.SpecialColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.nekiplay.hypixelcry.Main.mc;

public class PathFinderRenderer {
    private static final ExecutorService pathFinderExecutor = Executors.newFixedThreadPool(2);
    private static final Map<String, PathData> paths = new ConcurrentHashMap<>();
    private static final Queue<PathResult> pathResults = new ConcurrentLinkedQueue<>();
    private static final int UPDATE_INTERVAL = 10;

    public static class PathData {
        public final BlockPos end;
        public final Color color;
        public final String endText;
        public List<BlockPos> blocks = Collections.emptyList();
        public BlockPos lastPlayerPos = null;
        public int updateCooldown = 0;
        public boolean needsUpdate = true;

        public PathData(BlockPos end, Color color, String endText) {
            this.end = end;
            this.color = color;
            this.endText = endText;
        }
    }

    private static class PathResult {
        public final String pathId;
        public final List<BlockPos> blocks;

        public PathResult(String pathId, List<BlockPos> blocks) {
            this.pathId = pathId;
            this.blocks = blocks;
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END || mc.thePlayer == null || mc.theWorld == null) {
            return;
        }

        // Обрабатываем результаты из других потоков
        while (!pathResults.isEmpty()) {
            PathResult result = pathResults.poll();
            PathData pathData = paths.get(result.pathId);
            if (pathData != null) {
                pathData.blocks = result.blocks;
            }
        }

        BlockPos currentPos = mc.thePlayer.getPosition();

        for (Map.Entry<String, PathData> entry : paths.entrySet()) {
            String pathId = entry.getKey();
            PathData pathData = entry.getValue();

            boolean playerMoved = !currentPos.equals(pathData.lastPlayerPos);
            boolean needsUpdate = pathData.needsUpdate || playerMoved || pathData.updateCooldown <= 0;

            if (needsUpdate) {
                pathData.updateCooldown = UPDATE_INTERVAL;
                pathData.lastPlayerPos = currentPos;
                pathData.needsUpdate = false;

                // Создаем копию данных для потока
                BlockPos startPos = currentPos;
                BlockPos endPos = pathData.end;

                pathFinderExecutor.submit(() -> {
                    // Создаем новый PathFinder для каждого потока
                    PathFinder pathFinder = new PathFinder(mc.theWorld, 256 * 2);
                    List<BlockPos> newPath = pathFinder.findPath(startPos, endPos);
                    List<BlockPos> simplifiedPath = newPath != null && !newPath.isEmpty()
                            ? pathFinder.getSimplifiedPath(newPath)
                            : Collections.emptyList();

                    // Добавляем результат в очередь для обработки в основном потоке
                    pathResults.add(new PathResult(pathId, simplifiedPath));
                });
            } else {
                pathData.updateCooldown--;
            }
        }
    }

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        if (mc.theWorld == null || paths.isEmpty()) {
            return;
        }

        for (PathData pathData : paths.values()) {
            if (pathData.blocks == null || pathData.blocks.isEmpty()) {
                continue;
            }

            // Рендерим линии пути
            BlockPos prevPos = pathData.blocks.get(0);
            for (int i = 1; i < pathData.blocks.size(); i++) {
                BlockPos currentPos = pathData.blocks.get(i);
                RenderUtils.drawLine(prevPos, currentPos, 1, pathData.color);
                prevPos = currentPos;
            }

            // Рендерим маркер конечной точки
            BlockPos endPos = pathData.blocks.get(pathData.blocks.size() - 1);
            RenderUtils.renderWaypointText(
                    pathData.endText,
                    new BlockPos(endPos.getX() + 0.5, endPos.getY() + 1.8, endPos.getZ() + 0.5),
                    event.partialTicks,
                    false,
                    pathData.color
            );
        }
    }

    // API для управления путями
    public static void addOrUpdatePath(String id, BlockPos end, Color color, String endText) {
        PathData newData = new PathData(end, color, endText);
        PathData existing = paths.put(id, newData);

        if (existing == null || !existing.end.equals(end)) {
            newData.needsUpdate = true;
        }
    }

    public static void removePath(String id) {
        paths.remove(id);
    }

    public static void clearAllPaths() {
        paths.clear();
    }

    public static boolean hasPath(String id) {
        return paths.containsKey(id);
    }

    public static List<BlockPos> getPathBlocks(String id) {
        PathData data = paths.get(id);
        return data != null ? new ArrayList<>(data.blocks) : Collections.emptyList();
    }

    public static void shutdown() {
        pathFinderExecutor.shutdownNow();
    }
}

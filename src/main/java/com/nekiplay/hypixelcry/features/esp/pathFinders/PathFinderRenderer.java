package com.nekiplay.hypixelcry.features.esp.pathFinders;

import com.nekiplay.hypixelcry.Main;
import com.nekiplay.hypixelcry.pathfinder.calculate.Path;
import com.nekiplay.hypixelcry.pathfinder.calculate.path.AStarPathFinder;
import com.nekiplay.hypixelcry.pathfinder.goal.Goal;
import com.nekiplay.hypixelcry.pathfinder.movement.CalculationContext;
import com.nekiplay.hypixelcry.utils.RenderUtils;
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
import java.util.stream.IntStream;

import static com.nekiplay.hypixelcry.Main.mc;

public class PathFinderRenderer {
    private static final ExecutorService PATH_FINDER_EXECUTOR = Executors.newFixedThreadPool(6);
    private static final Map<String, PathData> PATHS = new ConcurrentHashMap<>();
    private static final Queue<PathResult> PATH_RESULTS = new ConcurrentLinkedQueue<>();
    private static final double RECALCULATION_DISTANCE = 9.0;
    private static final int CHUNK_UPDATE_RADIUS = 1;

    public static class PathData {
        public final BlockPos end;
        public final Color color;
        public final String endText;
        public List<BlockPos> blocks = new ArrayList<>();
        public List<BlockPos> remainingPath = new ArrayList<>();
        public int furthestReachedIndex = 0;
        public int currentVisibleFromIndex = 0;
        public boolean needsUpdate = true;
        public int lastChunkX = Integer.MIN_VALUE;
        public int lastChunkZ = Integer.MIN_VALUE;
        public boolean chunksUpdated = false;

        public PathData(BlockPos end, Color color, String endText) {
            this.end = end;
            this.color = color;
            this.endText = endText;
        }
    }

    private static class PathResult {
        final String pathId;
        final List<BlockPos> blocks;

        PathResult(String pathId, List<BlockPos> blocks) {
            this.pathId = pathId;
            this.blocks = blocks;
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END || mc.thePlayer == null || mc.theWorld == null) return;

        BlockPos currentPos = mc.thePlayer.getPosition().add(0, -1, 0);
        processPathResults();

        if (Main.config.misc.pathFinderESP.enabled) {
            PATHS.values().forEach(pathData -> updatePath(currentPos, pathData));
        }
    }

    private void processPathResults() {
        while (!PATH_RESULTS.isEmpty()) {
            PathResult result = PATH_RESULTS.poll();
            Optional.ofNullable(PATHS.get(result.pathId)).ifPresent(data -> {
                data.blocks = result.blocks;
                data.furthestReachedIndex = 0;
                data.currentVisibleFromIndex = 0;
                data.chunksUpdated = false;
                data.needsUpdate = false;
            });
        }
    }

    private void updatePath(BlockPos currentPos, PathData pathData) {
        updateChunkData(currentPos, pathData);
        updateRemainingPath(currentPos, pathData);

        if (shouldRecalculatePath(currentPos, pathData)) {
            recalculatePath(currentPos, pathData);
        }
    }

    private void updateChunkData(BlockPos currentPos, PathData pathData) {
        int currentChunkX = currentPos.getX() >> 4;
        int currentChunkZ = currentPos.getZ() >> 4;

        if (Math.abs(currentChunkX - pathData.lastChunkX) > CHUNK_UPDATE_RADIUS ||
                Math.abs(currentChunkZ - pathData.lastChunkZ) > CHUNK_UPDATE_RADIUS) {
            pathData.chunksUpdated = true;
            pathData.lastChunkX = currentChunkX;
            pathData.lastChunkZ = currentChunkZ;
        }
    }

    private void recalculatePath(BlockPos currentPos, PathData pathData) {
        PATH_FINDER_EXECUTOR.submit(() -> {
            CalculationContext ctx = new CalculationContext();
            BlockPos targetPos = getNearestLoadedPos(pathData.end);

            AStarPathFinder finder = new AStarPathFinder(
                    currentPos.getX(), currentPos.getY(), currentPos.getZ(),
                    new Goal(targetPos.getX(), targetPos.getY(), targetPos.getZ(), ctx),
                    ctx
            );

            Optional.ofNullable(finder.calculatePath())
                    .map(Path::getSmoothedPath)
                    .ifPresent(path -> {
                        pathData.remainingPath = path;
                        PATH_RESULTS.add(new PathResult(getPathId(pathData), path));
                    });
        });
    }

    private String getPathId(PathData pathData) {
        return PATHS.entrySet().stream()
                .filter(entry -> entry.getValue() == pathData)
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse("");
    }

    private BlockPos getNearestLoadedPos(BlockPos target) {
        if (mc.theWorld.isBlockLoaded(target)) return target;

        BlockPos playerPos = mc.thePlayer.getPosition();
        int dx = target.getX() - playerPos.getX();
        int dz = target.getZ() - playerPos.getZ();

        double length = Math.sqrt(dx*dx + dz*dz);
        if (length > 0) {
            dx = (int)(dx / length);
            dz = (int)(dz / length);
        }

        for (int radius = 1; radius <= 100; radius++) {
            BlockPos checkPos = playerPos.add(dx * radius, 0, dz * radius);
            if (mc.theWorld.isBlockLoaded(checkPos)) return checkPos;

            for (int offset = 1; offset <= radius; offset++) {
                BlockPos pos1 = checkPos.add(-dz * offset, 0, dx * offset);
                BlockPos pos2 = checkPos.add(dz * offset, 0, -dx * offset);
                if (mc.theWorld.isBlockLoaded(pos1)) return pos1;
                if (mc.theWorld.isBlockLoaded(pos2)) return pos2;
            }
        }

        return target;
    }

    private boolean shouldRecalculatePath(BlockPos currentPos, PathData pathData) {
        if (pathData.needsUpdate || pathData.blocks.isEmpty()) return true;

        BlockPos endPos = pathData.blocks.get(pathData.blocks.size()-1);
        if (currentPos.distanceSq(endPos) < RECALCULATION_DISTANCE * RECALCULATION_DISTANCE) return true;

        BlockPos nearest = findNearestPathPoint(currentPos, pathData.blocks);
        if (nearest == null || currentPos.distanceSq(nearest) > RECALCULATION_DISTANCE * RECALCULATION_DISTANCE) return true;

        return pathData.chunksUpdated && isPotentialBetterPathAvailable(currentPos, pathData);
    }

    private BlockPos findNearestPathPoint(BlockPos playerPos, List<BlockPos> path) {
        if (path == null || path.isEmpty()) return null;
        if (path.size() < 2) return path.get(0);

        return IntStream.range(0, path.size() - 1)
                .mapToObj(i -> getClosestPointOnSegment(playerPos, path.get(i), path.get(i + 1)))
                .min(Comparator.comparingDouble(playerPos::distanceSq))
                .orElse(null);
    }

    private BlockPos getClosestPointOnSegment(BlockPos point, BlockPos start, BlockPos end) {
        double lineX = end.getX() - start.getX();
        double lineY = end.getY() - start.getY();
        double lineZ = end.getZ() - start.getZ();

        double pointX = point.getX() - start.getX();
        double pointY = point.getY() - start.getY();
        double pointZ = point.getZ() - start.getZ();

        double dot = pointX * lineX + pointY * lineY + pointZ * lineZ;
        double t = Math.max(0, Math.min(1, dot / (lineX*lineX + lineY*lineY + lineZ*lineZ)));

        return new BlockPos(
                start.getX() + t * lineX,
                start.getY() + t * lineY,
                start.getZ() + t * lineZ
        );
    }

    private void updateRemainingPath(BlockPos playerPos, PathData pathData) {
        if (pathData.remainingPath.isEmpty()) return;

        BlockPos nearest = findNearestPathPoint(playerPos, pathData.remainingPath);
        if (nearest == null) return;

        int nearestIndex = pathData.remainingPath.indexOf(nearest);
        if (nearestIndex > 0) {
            pathData.remainingPath = pathData.remainingPath.subList(nearestIndex, pathData.remainingPath.size());
        }
    }

    private boolean isPotentialBetterPathAvailable(BlockPos playerPos, PathData pathData) {
        if (pathData.blocks.isEmpty() || !mc.theWorld.isBlockLoaded(pathData.end)) return false;

        BlockPos currentPathEnd = pathData.blocks.get(pathData.blocks.size()-1);
        double currentDistance = currentPathEnd.distanceSq(pathData.end);

        return IntStream.rangeClosed(-3, 3)
                .anyMatch(x -> IntStream.rangeClosed(-3, 3)
                        .anyMatch(z -> {
                            BlockPos testPos = playerPos.add(x*16, 0, z*16);
                            return mc.theWorld.isBlockLoaded(testPos) && testPos.distanceSq(pathData.end) < currentDistance;
                        }));
    }

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        if (mc.theWorld == null || PATHS.isEmpty() || !Main.config.esp.pathFinderESP.enabled) return;

        PATHS.values().forEach(pathData -> renderPath(pathData, event.partialTicks));
    }

    private void renderPath(PathData pathData, float partialTicks) {
        if (pathData.blocks == null || pathData.blocks.isEmpty() || pathData.remainingPath.isEmpty()) return;

        BlockPos endPos = pathData.blocks.get(pathData.blocks.size() - 1);
        renderPathLines(pathData, endPos, partialTicks);
        renderEndPoint(pathData, endPos, partialTicks);
    }

    private void renderPathLines(PathData pathData, BlockPos endPos, float partialTicks) {
        BlockPos prevPos = pathData.remainingPath.get(0);
        for (int i = 1; i < pathData.remainingPath.size(); i++) {
            BlockPos currentPos = pathData.remainingPath.get(i);
            RenderUtils.drawLine(
                    prevPos.add(0, 1.5, 0),
                    currentPos.add(0, 1.5, 0),
                    4,
                    pathData.color
            );

            if (!currentPos.equals(endPos) && Main.config.esp.pathFinderESP.enableSubPoints) {
                RenderUtils.drawBlockBox(currentPos, pathData.color, 1, partialTicks);
            }
            prevPos = currentPos;
        }
    }

    private void renderEndPoint(PathData pathData, BlockPos endPos, float partialTicks) {
        RenderUtils.drawBlockBox(endPos, pathData.color, 4, partialTicks);
        RenderUtils.renderWaypointText(
                pathData.endText,
                new BlockPos(endPos.getX() + 0.5, endPos.getY() + 2.8, endPos.getZ() + 0.5),
                partialTicks,
                false,
                pathData.color
        );
    }

    // API methods
    public static void addOrUpdatePath(String id, BlockPos end, Color color, String endText) {
        PathData newData = new PathData(end, color, endText);
        if (!end.equals(Optional.ofNullable(PATHS.get(id)).map(data -> data.end).orElse(null))) {
            newData.needsUpdate = true;
        }
        PATHS.put(id, newData);
    }

    public static void removePath(String id) {
        PATHS.remove(id);
    }

    public static void clearAllPaths() {
        PATHS.clear();
    }

    public static boolean hasPath(String id) {
        return PATHS.containsKey(id);
    }

    public static List<BlockPos> getPathBlocks(String id) {
        PathData data = PATHS.get(id);
        return data != null ? new ArrayList<>(data.blocks) : Collections.emptyList();
    }

    public static void shutdown() {
        PATH_FINDER_EXECUTOR.shutdownNow();
    }
}
package com.nekiplay.hypixelcry.features.system;

import baritone.api.BaritoneAPI;
import baritone.api.IBaritone;
import baritone.api.behavior.IPathingBehavior;
import baritone.api.pathing.calc.IPath;
import baritone.api.pathing.goals.GoalBlock;
import com.nekiplay.hypixelcry.HypixelCry;
import com.nekiplay.hypixelcry.annotations.Init;
import com.nekiplay.hypixelcry.baritone.OnlyRenderProcess;
import com.nekiplay.hypixelcry.utils.render.RenderHelper;
import com.nekiplay.hypixelcry.utils.scheduler.Scheduler;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static com.nekiplay.hypixelcry.HypixelCry.mc;

public class PathFinder {
    private static final ExecutorService PATH_FINDER_EXECUTOR = Executors.newFixedThreadPool(6);
    private static final Map<String, PathData> PATHS = new ConcurrentHashMap<>();
    private static final Queue<PathResult> PATH_RESULTS = new ConcurrentLinkedQueue<>();
    private static final double RECALCULATION_DISTANCE = 4.0;
    private static final int CHUNK_UPDATE_RADIUS = 1;

    public static class PathData {
        public final BlockPos end;
        public final float[] color;
        public final String endText;
        public final IBaritone baritone;
        public OnlyRenderProcess process;
        public List<BlockPos> blocks = new ArrayList<>();
        public List<BlockPos> remainingPath = new ArrayList<>();
        public boolean needsUpdate = true;
        public int lastChunkX = Integer.MIN_VALUE;
        public int lastChunkZ = Integer.MIN_VALUE;
        public boolean chunksUpdated = false;

        public PathData(BlockPos end, float[] color, String endText, IBaritone baritone) {
            this.end = end;
            this.color = color;
            this.endText = endText;
            this.baritone = baritone;
        }
    }

    private record PathResult(String pathId, List<BlockPos> blocks) { }

    @Init
    public static void init() {
        WorldRenderEvents.AFTER_TRANSLUCENT.register(PathFinder::onRender);
        Scheduler.INSTANCE.scheduleCyclic(PathFinder::onClientTick, 2);
    }

    public static void onClientTick() {
        if (mc.player == null || mc.world == null) return;

        BlockPos currentPos = mc.player.getBlockPos().add(0, -1, 0);
        processPathResults();

        if (HypixelCry.config.misc.pathFinderESP.enabled) {
            PATHS.values().forEach(pathData -> updatePath(currentPos, pathData));
        }
    }

    private static void processPathResults() {
        while (!PATH_RESULTS.isEmpty()) {
            PathResult result = PATH_RESULTS.poll();
            Optional.ofNullable(PATHS.get(result.pathId)).ifPresent(data -> {
                data.blocks = result.blocks;
                data.remainingPath = new ArrayList<>(result.blocks);
                data.chunksUpdated = false;
                data.needsUpdate = false;
            });
        }
    }

    private static void updatePath(BlockPos currentPos, PathData pathData) {
        updateChunkData(currentPos, pathData);
        updateRemainingPath(currentPos, pathData);

        if (shouldRecalculatePath(currentPos, pathData)) {
            recalculatePath(pathData);
        }
    }

    private static void updateChunkData(BlockPos currentPos, PathData pathData) {
        int currentChunkX = currentPos.getX() >> 4;
        int currentChunkZ = currentPos.getZ() >> 4;

        if (Math.abs(currentChunkX - pathData.lastChunkX) > CHUNK_UPDATE_RADIUS ||
                Math.abs(currentChunkZ - pathData.lastChunkZ) > CHUNK_UPDATE_RADIUS) {
            pathData.chunksUpdated = true;
            pathData.lastChunkX = currentChunkX;
            pathData.lastChunkZ = currentChunkZ;
        }
    }

    private static void recalculatePath(PathData pathData) {
        PATH_FINDER_EXECUTOR.submit(() -> {
            pathData.process.setGoal(new GoalBlock(pathData.end));
            pathData.process.path();

            Optional<IPath> path = pathData.baritone.getPathingBehavior().getPath();
            if (path.isPresent()) {
                List<BlockPos> positions = path.get().positions().stream()
                        .map(bp -> new BlockPos(bp.x, bp.y, bp.z))
                        .collect(Collectors.toList());
                PATH_RESULTS.add(new PathResult(getPathId(pathData), positions));
            }
            if (pathData.baritone.getPathingBehavior().hasPath()) {
                pathData.baritone.getPathingBehavior().forceCancel();
            }
        });
    }

    private static String getPathId(PathData pathData) {
        return PATHS.entrySet().stream()
                .filter(entry -> entry.getValue() == pathData)
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse("");
    }

    private static boolean shouldRecalculatePath(BlockPos currentPos, PathData pathData) {
        if (pathData.needsUpdate || pathData.blocks.isEmpty()) return true;
        
        BlockPos nearest = findNearestPathPoint(currentPos, pathData.blocks);
        if (nearest == null || currentPos.getSquaredDistance(nearest) > RECALCULATION_DISTANCE * RECALCULATION_DISTANCE) return true;

        return pathData.chunksUpdated;
    }

    private static BlockPos findNearestPathPoint(BlockPos playerPos, List<BlockPos> path) {
        if (path == null || path.isEmpty()) return null;
        if (path.size() < 2) return path.getFirst();

        BlockPos nearest = path.getFirst();
        double minDistance = playerPos.getSquaredDistance(nearest);

        for (BlockPos pos : path) {
            double distance = playerPos.getSquaredDistance(pos);
            if (distance < minDistance) {
                minDistance = distance;
                nearest = pos;
            }
        }
        return nearest;
    }

    private static void updateRemainingPath(BlockPos playerPos, PathData pathData) {
        if (pathData.remainingPath.isEmpty()) return;

        BlockPos nearest = findNearestPathPoint(playerPos, pathData.remainingPath);
        if (nearest == null) return;

        int nearestIndex = pathData.remainingPath.indexOf(nearest);
        if (nearestIndex > 0) {
            pathData.remainingPath = pathData.remainingPath.subList(nearestIndex, pathData.remainingPath.size());
        }
    }

    public static void onRender(WorldRenderContext context) {
        if (mc.world == null || mc.player == null) return;

        PATHS.values().forEach(pathData -> {
            renderPath(pathData, context);
        });
    }

    private static void renderPath(PathData pathData, WorldRenderContext context) {
        if (pathData.blocks == null || pathData.blocks.isEmpty()) return;

        BlockPos endPos = pathData.blocks.getLast();
        renderPathLines(pathData, endPos, context);
        renderEndPoint(pathData, endPos, context);
    }

    private static void renderPathLines(PathData pathData, BlockPos endPos, WorldRenderContext context) {
        BlockPos prevPos = pathData.blocks.getFirst();
        renderStartPoint(pathData, prevPos, context);
        for (int i = 1; i < pathData.blocks.size(); i++) {
            BlockPos currentPos = pathData.blocks.get(i);
            Vec3d[] points = new Vec3d[] {
                    prevPos.toCenterPos(),
                    currentPos.toCenterPos()
            };
            RenderHelper.renderLinesFromPoints(context, points, pathData.color, pathData.color[3], 3f, true);
            prevPos = currentPos;
        }
    }

    private static void renderStartPoint(PathData pathData, BlockPos startPos, WorldRenderContext context) {
        RenderHelper.renderFilled(context, startPos, pathData.color, pathData.color[3], true);
        RenderHelper.renderOutline(context, startPos, pathData.color, 4f, true);

        int r = (int) (pathData.color[0] * 255);
        int g = (int) (pathData.color[1] * 255);
        int b = (int) (pathData.color[2] * 255);

        int color = (255 << 24) | (r << 16) | (g << 8) | b;

        RenderHelper.renderText(context, Text.of(pathData.endText).asOrderedText(), startPos.toCenterPos().add(0, 1, 0), color, 1, 0.5f, true);
        RenderHelper.renderText(context, Text.of("Start").asOrderedText(), startPos.toCenterPos().add(0, 1.2, 0), color, 1, 1.5f, true);
    }

    private static void renderEndPoint(PathData pathData, BlockPos endPos, WorldRenderContext context) {
        RenderHelper.renderFilled(context, endPos, pathData.color, pathData.color[3], true);
        RenderHelper.renderOutline(context, endPos, pathData.color, 4f, true);

        int r = (int) (pathData.color[0] * 255);
        int g = (int) (pathData.color[1] * 255);
        int b = (int) (pathData.color[2] * 255);

        int color = (255 << 24) | (r << 16) | (g << 8) | b;

        RenderHelper.renderText(context, Text.of(pathData.endText).asOrderedText(), endPos.toCenterPos().add(0, 1, 0), color, 1, 0.5f, true);
        RenderHelper.renderText(context, Text.of("End").asOrderedText(), endPos.toCenterPos().add(0, 1.2, 0), color, 1, 1.5f, true);
    }

    // API methods
    public static void addOrUpdatePath(String id, BlockPos end, float[] color, String endText) {
        IBaritone baritone = BaritoneAPI.getProvider().createBaritone(mc);
        BaritoneAPI.getSettings().allowPlace.value = false;
        BaritoneAPI.getSettings().allowBreak.value = false;
        BaritoneAPI.getSettings().renderPath.value = false;
        BaritoneAPI.getSettings().allowParkour.value = true;
        BaritoneAPI.getSettings().allowParkourAscend.value = true;
        BaritoneAPI.getSettings().maxFallHeightNoWater.value = 20;
        BaritoneAPI.getSettings().allowDiagonalAscend.value = true;
        BaritoneAPI.getSettings().allowDiagonalDescend.value = true;
        BaritoneAPI.getSettings().renderGoal.value = false;
        PathData newData = new PathData(end, color, endText, baritone);
        newData.process = new OnlyRenderProcess(baritone);
        newData.process.setGoal(new GoalBlock(end));
        baritone.getPathingControlManager().registerProcess(newData.process);

        if (!end.equals(Optional.ofNullable(PATHS.get(id)).map(data -> data.end).orElse(null))) {
            newData.needsUpdate = true;
        }
        PATHS.put(id, newData);
    }

    public static void removePath(String id) {
        PathData data = PATHS.get(id);
        if (data != null) {
            BaritoneAPI.getProvider().destroyBaritone(data.baritone);
            PATHS.remove(id);
        }
    }

    public static void clearAllPaths() {
        PATHS.values().forEach(data -> BaritoneAPI.getProvider().destroyBaritone(data.baritone));
        PATHS.clear();
    }

    public static boolean hasPath(String id) {
        return PATHS.containsKey(id);
    }

    public static List<BlockPos> getPathBlocks(String id) {
        PathData data = PATHS.get(id);
        if (data == null) {
            return Collections.emptyList();
        }

        return data.remainingPath;
    }

    public static void shutdown() {
        clearAllPaths();
        PATH_FINDER_EXECUTOR.shutdownNow();
    }
}
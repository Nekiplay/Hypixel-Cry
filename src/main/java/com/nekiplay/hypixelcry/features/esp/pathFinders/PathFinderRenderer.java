package com.nekiplay.hypixelcry.features.esp.pathFinders;

import com.nekiplay.hypixelcry.Main;
import com.nekiplay.hypixelcry.pathfinder.calculate.Path;
import com.nekiplay.hypixelcry.pathfinder.calculate.path.AStarPathFinder;
import com.nekiplay.hypixelcry.pathfinder.goal.Goal;
import com.nekiplay.hypixelcry.pathfinder.movement.CalculationContext;
import com.nekiplay.hypixelcry.utils.PathFinder;
import com.nekiplay.hypixelcry.utils.RenderUtils;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3i;
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
    private static final double RECALCULATION_DISTANCE = 9.0;
    private static final int CHUNK_UPDATE_RADIUS = 1;

    public static class PathData {
        public final BlockPos end;
        public final Color color;
        public final String endText;
        public List<BlockPos> blocks = Collections.emptyList();
        public BlockPos lastPlayerPos = null;
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

        BlockPos currentPos = mc.thePlayer.getPosition();
        
        // Обработка результатов из других потоков
        while (!pathResults.isEmpty()) {
            PathResult result = pathResults.poll();
            PathData pathData = paths.get(result.pathId);
            if (pathData != null) {
                pathData.blocks = result.blocks;
                pathData.furthestReachedIndex = 0;
                pathData.currentVisibleFromIndex = 0;
                pathData.chunksUpdated = false;
                pathData.needsUpdate = false;
            }
        }

        if (Main.config.esp.pathFinderESP.enabled) {
            for (Map.Entry<String, PathData> entry : paths.entrySet()) {
                String pathId = entry.getKey();
                PathData pathData = entry.getValue();

                // Проверка обновления чанков
                int currentChunkX = currentPos.getX() >> 4;
                int currentChunkZ = currentPos.getZ() >> 4;
                if (Math.abs(currentChunkX - pathData.lastChunkX) > CHUNK_UPDATE_RADIUS ||
                        Math.abs(currentChunkZ - pathData.lastChunkZ) > CHUNK_UPDATE_RADIUS) {
                    pathData.chunksUpdated = true;
                    pathData.lastChunkX = currentChunkX;
                    pathData.lastChunkZ = currentChunkZ;
                }

                // Обновление прогресса пути
                updatePathProgress(currentPos, pathData);

                // Проверка необходимости перерасчета
                if (shouldRecalculatePath(currentPos, pathData)) {
                    pathData.lastPlayerPos = currentPos;

                    pathFinderExecutor.submit(() -> {
                        CalculationContext calculationContext = new CalculationContext();
                        AStarPathFinder finder = new AStarPathFinder(currentPos.getX(), currentPos.getY(), currentPos.getZ(), new Goal(pathData.end.getX(), pathData.end.getY(), pathData.end.getZ(), calculationContext), calculationContext);

                        Path path = finder.calculatePath();
                        if (path != null) {
                            List<BlockPos> smoothed = path.getSmoothedPath();
                            //PathFinder pathFinder = new PathFinder(mc.theWorld, 130, 6000);
                            //List<BlockPos> newPath = pathFinder.findPath(currentPos, pathData.end);
                            //List<BlockPos> simplifiedPath = newPath != null && !newPath.isEmpty()
                            //        ? pathFinder.getSimplifiedPath(newPath)
                            //        : Collections.emptyList();
                            pathResults.add(new PathResult(pathId, smoothed));
                        }
                        else {
                            pathResults.add(new PathResult(pathId, new ArrayList<>()));
                        }
                    });
                }
            }
        }
    }

	private boolean shouldRecalculatePath(BlockPos currentPos, PathData pathData) {
        // Принудительное обновление
        if (pathData.needsUpdate) return true;
        
        // Первый расчет или сброс пути
        if (pathData.blocks.isEmpty()) return true;
		
		BlockPos endPos = pathData.blocks.get(pathData.blocks.size()-1);
        if (currentPos.distanceSq(endPos) < 32*32) return true;
        
        // Отклонение от маршрута
        BlockPos nearestPoint = findNearestPathPoint(currentPos, pathData.blocks);
        if (nearestPoint == null || 
            currentPos.distanceSq(nearestPoint) > RECALCULATION_DISTANCE * RECALCULATION_DISTANCE) {
            return true;
        }
        
        // Появление более оптимального пути
        return pathData.chunksUpdated && isPotentialBetterPathAvailable(currentPos, pathData);
    }

    private BlockPos findNearestPathPoint(BlockPos playerPos, List<BlockPos> path) {
		if (path == null || path.size() < 2) {
			return path != null && !path.isEmpty() ? path.get(0) : null;
		}
	
		BlockPos nearestPoint = null;
		double minDistance = Double.MAX_VALUE;
	
		// Проверяем расстояние до всех сегментов пути
		for (int i = 0; i < path.size() - 1; i++) {
			BlockPos start = path.get(i);
			BlockPos end = path.get(i + 1);
			
			// Вычисляем ближайшую точку на текущем сегменте пути
			BlockPos closestOnSegment = getClosestPointOnLine(playerPos, start, end);
			double distance = playerPos.distanceSq(closestOnSegment);
			
			if (distance < minDistance) {
				minDistance = distance;
				nearestPoint = distance == playerPos.distanceSq(start) ? start : 
							distance == playerPos.distanceSq(end) ? end : closestOnSegment;
			}
		}
	
		return nearestPoint;
	}
	
	private BlockPos getClosestPointOnLine(BlockPos point, BlockPos lineStart, BlockPos lineEnd) {
		// Вектор линии
		double lineX = lineEnd.getX() - lineStart.getX();
		double lineY = lineEnd.getY() - lineStart.getY();
		double lineZ = lineEnd.getZ() - lineStart.getZ();
		
		// Вектор от начала линии до точки
		double pointX = point.getX() - lineStart.getX();
		double pointY = point.getY() - lineStart.getY();
		double pointZ = point.getZ() - lineStart.getZ();
		
		// Длина линии в квадрате
		double lineLengthSq = lineX * lineX + lineY * lineY + lineZ * lineZ;
		
		// Скалярное произведение
		double dot = pointX * lineX + pointY * lineY + pointZ * lineZ;
		
		// Параметр положения проекции (0 = начало линии, 1 = конец линии)
		double t = Math.max(0, Math.min(1, dot / lineLengthSq));
		
		// Вычисляем ближайшую точку на линии
		return new BlockPos(
			lineStart.getX() + t * lineX,
			lineStart.getY() + t * lineY,
			lineStart.getZ() + t * lineZ
		);
	}

    private void updatePathProgress(BlockPos playerPos, PathData pathData) {
        if (pathData.blocks.isEmpty()) return;

        BlockPos nearest = findNearestPathPoint(playerPos, pathData.blocks);
        int nearestIndex = pathData.blocks.indexOf(nearest);

        // Обновление самого дальнего достигнутого индекса
        if (nearestIndex > pathData.furthestReachedIndex && 
            playerPos.distanceSq(nearest) < 9.0) {
            pathData.furthestReachedIndex = nearestIndex;
        }

        // Обновление видимой части пути
        if (nearestIndex < pathData.currentVisibleFromIndex) {
            pathData.currentVisibleFromIndex = nearestIndex;
        } else if (nearestIndex > pathData.currentVisibleFromIndex + 5) {
            pathData.currentVisibleFromIndex = Math.min(nearestIndex - 3, pathData.furthestReachedIndex);
        }
    }

    private boolean isPotentialBetterPathAvailable(BlockPos playerPos, PathData pathData) {
        if (pathData.blocks.isEmpty() || !mc.theWorld.isBlockLoaded(pathData.end)) {
            return false;
        }

        BlockPos currentPathEnd = pathData.blocks.get(pathData.blocks.size()-1);
        double currentDistance = currentPathEnd.distanceSq(pathData.end);

        for (int x = -3; x <= 3; x++) {
            for (int z = -3; z <= 3; z++) {
                BlockPos testPos = playerPos.add(x*16, 0, z*16);
                if (mc.theWorld.isBlockLoaded(testPos)) {
                    double testDistance = testPos.distanceSq(pathData.end);
                    if (testDistance < currentDistance) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        if (mc.theWorld == null || paths.isEmpty()) return;

        if (Main.config.esp.pathFinderESP.enabled) {
            for (PathData pathData : paths.values()) {
                if (pathData.blocks == null || pathData.blocks.isEmpty()) continue;

                int fromIndex = Math.max(0, pathData.currentVisibleFromIndex - 2);
                List<BlockPos> visiblePath = pathData.blocks.subList(fromIndex, pathData.blocks.size());

                if (visiblePath.size() < 2) continue;

                BlockPos prevPos = visiblePath.get(0);
                for (int i = 1; i < visiblePath.size(); i++) {
                    BlockPos currentPos = visiblePath.get(i);
                    RenderUtils.drawLine(prevPos, currentPos, 4, pathData.color);
                    prevPos = currentPos;
                }

                BlockPos endPos = pathData.blocks.get(pathData.blocks.size() - 1);

                RenderUtils.drawBlockBox(
                        endPos.subtract(new Vec3i(0, 1, 0)),
                        pathData.color, 4,
                        event.partialTicks
                );
                RenderUtils.renderWaypointText(
                        pathData.endText,
                        new BlockPos(endPos.getX() + 0.5, endPos.getY() + 1.8, endPos.getZ() + 0.5),
                        event.partialTicks,
                        false,
                        pathData.color
                );
            }
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

package com.nekiplay.hypixelcry.features.esp.pathFinders;

import com.nekiplay.hypixelcry.Main;
import com.nekiplay.hypixelcry.pathfinder.calculate.Path;
import com.nekiplay.hypixelcry.pathfinder.calculate.path.AStarPathFinder;
import com.nekiplay.hypixelcry.pathfinder.goal.Goal;
import com.nekiplay.hypixelcry.pathfinder.movement.CalculationContext;
import com.nekiplay.hypixelcry.utils.PathFinder;
import com.nekiplay.hypixelcry.utils.RenderUtils;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
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
        public List<BlockPos> blocks = new ArrayList<>();
        public int furthestReachedIndex = 0;
        public int currentVisibleFromIndex = 0;
        public boolean needsUpdate = true;
        public int lastChunkX = Integer.MIN_VALUE;
        public int lastChunkZ = Integer.MIN_VALUE;
        public boolean chunksUpdated = false;

        public List<BlockPos> remainingPath = new ArrayList<>();

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

        // Обработка результатов
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
                BlockPos endPos = pathData.end;

                // Проверка чанков
                int currentChunkX = currentPos.getX() >> 4;
                int currentChunkZ = currentPos.getZ() >> 4;
                if (Math.abs(currentChunkX - pathData.lastChunkX) > CHUNK_UPDATE_RADIUS ||
                        Math.abs(currentChunkZ - pathData.lastChunkZ) > CHUNK_UPDATE_RADIUS) {
                    pathData.chunksUpdated = true;
                    pathData.lastChunkX = currentChunkX;
                    pathData.lastChunkZ = currentChunkZ;
                }

                // Обновление прогресса
                updateRemainingPath(currentPos, pathData);

                if (shouldRecalculatePath(currentPos, pathData)) {
                    pathFinderExecutor.submit(() -> {
                        CalculationContext ctx = new CalculationContext();
                        BlockPos targetPos = getNearestLoadedPos(endPos);

                        AStarPathFinder finder = new AStarPathFinder(
                                currentPos.getX(), currentPos.getY(), currentPos.getZ(),
                                new Goal(targetPos.getX(), targetPos.getY(), targetPos.getZ(), ctx),
                                ctx
                        );

                        Path path = finder.calculatePath();
                        if (path != null) {
                            pathData.remainingPath = path.getSmoothedPath();
                            pathResults.add(new PathResult(pathId, pathData.remainingPath));
                        }
                    });
                }
            }
        }
    }

    private BlockPos getNearestLoadedPos(BlockPos target) {
        // Если целевой блок загружен - используем его
        if (mc.theWorld.isBlockLoaded(target)) {
            return target;
        }

        // Получаем позицию игрока
        BlockPos playerPos = mc.thePlayer.getPosition();

        // Вектор направления от игрока к цели
        int dx = target.getX() - playerPos.getX();
        int dz = target.getZ() - playerPos.getZ();

        // Нормализуем направление
        double length = Math.sqrt(dx*dx + dz*dz);
        if (length > 0) {
            dx = (int)(dx / length);
            dz = (int)(dz / length);
        }

        // Ищем по спирали от игрока в направлении цели
        for (int radius = 1; radius <= 100; radius++) {
            // Проверяем основное направление
            BlockPos checkPos = playerPos.add(dx * radius, 0, dz * radius);
            if (mc.theWorld.isBlockLoaded(checkPos)) {
                return checkPos;
            }

            // Проверяем соседние блоки перпендикулярно направлению
            for (int offset = 1; offset <= radius; offset++) {
                // Перпендикулярные смещения
                BlockPos pos1 = checkPos.add(-dz * offset, 0, dx * offset);
                BlockPos pos2 = checkPos.add(dz * offset, 0, -dx * offset);

                if (mc.theWorld.isBlockLoaded(pos1)) return pos1;
                if (mc.theWorld.isBlockLoaded(pos2)) return pos2;
            }
        }

        // Если ничего не нашли, возвращаем оригинальную позицию
        return target;
    }


    private boolean shouldRecalculatePath(BlockPos currentPos, PathData pathData) {
        // Принудительное обновление
        if (pathData.needsUpdate) return true;

        // Первый расчет или сброс пути
        if (pathData.blocks.isEmpty()) return true;

        // Проверяем, достигли ли мы конечной точки
        BlockPos endPos = pathData.blocks.get(pathData.blocks.size()-1);
        if (currentPos.distanceSq(endPos) < 4*4) {
            return true;
        }

        // Быстрая проверка - если игрок далеко от всего пути
        if (isFarFromEntirePath(currentPos, pathData.blocks)) {
            return true;
        }

        // Проверяем расстояние до ближайшей точки пути
        BlockPos nearest = findNearestPathPoint(currentPos, pathData.blocks);
        if (nearest == null || currentPos.distanceSq(nearest) > RECALCULATION_DISTANCE * RECALCULATION_DISTANCE) {
            return true;
        }

        // Проверяем обновление чанков
        if (pathData.chunksUpdated && isPotentialBetterPathAvailable(currentPos, pathData)) {
            return true;
        }

        return false;
    }

    private boolean isFarFromEntirePath(BlockPos playerPos, List<BlockPos> path) {
        // Быстрая проверка расстояния до всех точек пути
        double maxAllowedDistSq = RECALCULATION_DISTANCE * RECALCULATION_DISTANCE * 4; // Больший порог

        for (BlockPos pos : path) {
            if (playerPos.distanceSq(pos) <= maxAllowedDistSq) {
                return false;
            }
        }
        return true;
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

    private void updateRemainingPath(BlockPos playerPos, PathData pathData) {
        if (pathData.remainingPath.isEmpty()) return;

        // Находим ближайшую точку в оставшемся пути
        BlockPos nearest = findNearestPathPoint(playerPos, pathData.remainingPath);
        if (nearest == null) return;

        // Обрезаем путь до текущей позиции
        int nearestIndex = pathData.remainingPath.indexOf(nearest);
        if (nearestIndex > 0) {
            pathData.remainingPath = pathData.remainingPath.subList(
                    nearestIndex,
                    pathData.remainingPath.size()
            );
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
                if (pathData.remainingPath.isEmpty()) continue;

                BlockPos prevPos = pathData.remainingPath.get(0);
                for (int i = 1; i < pathData.remainingPath.size(); i++) {
                    BlockPos currentPos = pathData.remainingPath.get(i);
                    RenderUtils.drawLine(prevPos, currentPos.add(0, 1, 0), 4, pathData.color);
					
					RenderUtils.drawBlockBox(
                        currentPos,
                        pathData.color, 1,
                        event.partialTicks
					);
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

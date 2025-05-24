package com.nekiplay.hypixelcry.utils;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import java.util.*;

public class PathFinder {
    private final World world;
    private final int maxSearchDistance;
    private final int maxOperationsPerTick;
    private int operationsCount;

    // Кэш для проверки проходимости блоков
    private final Map<BlockPos, Boolean> walkableCache = new HashMap<>(1024 * 2);
    private final Map<BlockPos, Boolean> passableCache = new HashMap<>(1024 * 2);

    // Приоритетные направления для более прямых путей
    private static final int[][] DIRECTIONS = {
            {1, 0, 0}, {-1, 0, 0}, {0, 0, 1}, {0, 0, -1}, // Горизонтальные
            {1, 1, 0}, {-1, 1, 0}, {0, 1, 1}, {0, 1, -1},  // Вверх
            {1, -1, 0}, {-1, -1, 0}, {0, -1, 1}, {0, -1, -1} // Вниз
    };

    public PathFinder(World world, int maxSearchDistance) {
        this(world, maxSearchDistance, 2000);
    }

    public PathFinder(World world, int maxSearchDistance, int maxOperationsPerTick) {
        this.world = world;
        this.maxSearchDistance = maxSearchDistance;
        this.maxOperationsPerTick = maxOperationsPerTick;
    }

    public List<BlockPos> findPath(BlockPos start, BlockPos end) {
        operationsCount = 0;
        walkableCache.clear();
        passableCache.clear();

        if (start.equals(end)) return Collections.singletonList(start);

        // Быстрая проверка прямой видимости
        if (hasDirectPath(start, end)) {
            return Arrays.asList(start, end);
        }

        return findPathToNearestAvailable(start, end);
    }

    private boolean hasDirectPath(BlockPos from, BlockPos to) {
        // Быстрая проверка прямой видимости
        if (from.distanceSq(to) > 25) return false; // Ограничение расстояния

        // Проверяем, что конечная точка проходима
        if (!isWalkableFast(to)) return false;

        // Упрощенная проверка прямой видимости (алгоритм Брезенхема)
        return isLineWalkable(from, to);
    }

    private boolean isLineWalkable(BlockPos start, BlockPos end) {
        int dx = Math.abs(end.getX() - start.getX());
        int dy = Math.abs(end.getY() - start.getY());
        int dz = Math.abs(end.getZ() - start.getZ());

        int xs = start.getX() < end.getX() ? 1 : -1;
        int ys = start.getY() < end.getY() ? 1 : -1;
        int zs = start.getZ() < end.getZ() ? 1 : -1;

        // Доминирующее направление - X
        if (dx >= dy && dx >= dz) {
            int p1 = 2 * dy - dx;
            int p2 = 2 * dz - dx;
            int x = start.getX();
            int y = start.getY();
            int z = start.getZ();

            while (x != end.getX()) {
                x += xs;
                if (p1 >= 0) {
                    y += ys;
                    p1 -= 2 * dx;
                }
                if (p2 >= 0) {
                    z += zs;
                    p2 -= 2 * dx;
                }
                p1 += 2 * dy;
                p2 += 2 * dz;

                BlockPos current = new BlockPos(x, y, z);
                if (!isPassableFast(current)) return false;
            }
        }
        // Доминирующее направление - Y
        else if (dy >= dx && dy >= dz) {
            int p1 = 2 * dx - dy;
            int p2 = 2 * dz - dy;
            int x = start.getX();
            int y = start.getY();
            int z = start.getZ();

            while (y != end.getY()) {
                y += ys;
                if (p1 >= 0) {
                    x += xs;
                    p1 -= 2 * dy;
                }
                if (p2 >= 0) {
                    z += zs;
                    p2 -= 2 * dy;
                }
                p1 += 2 * dx;
                p2 += 2 * dz;

                BlockPos current = new BlockPos(x, y, z);
                if (!isPassableFast(current)) return false;
            }
        }
        // Доминирующее направление - Z
        else {
            int p1 = 2 * dy - dz;
            int p2 = 2 * dx - dz;
            int x = start.getX();
            int y = start.getY();
            int z = start.getZ();

            while (z != end.getZ()) {
                z += zs;
                if (p1 >= 0) {
                    y += ys;
                    p1 -= 2 * dz;
                }
                if (p2 >= 0) {
                    x += xs;
                    p2 -= 2 * dz;
                }
                p1 += 2 * dy;
                p2 += 2 * dx;

                BlockPos current = new BlockPos(x, y, z);
                if (!isPassableFast(current)) return false;
            }
        }

        return true;
    }

    private List<BlockPos> findPathToNearestAvailable(BlockPos start, BlockPos target) {
        PriorityQueue<Node> openSet = new PriorityQueue<>(64, Comparator.comparingDouble(n -> n.fCost));
        Set<BlockPos> closedSet = new HashSet<>(1024);
        Map<BlockPos, Node> allNodes = new HashMap<>(1024);
        Node bestNode = null;
        double bestDistance = Double.MAX_VALUE;
        final int maxDistSq = maxSearchDistance * maxSearchDistance;

        Node startNode = new Node(start, 0, heuristic(start, target), null);
        openSet.add(startNode);
        allNodes.put(start, startNode);

        while (!openSet.isEmpty() && operationsCount++ < maxOperationsPerTick) {
            Node current = openSet.poll();
            double currentDistance = current.pos.distanceSq(target);

            if (currentDistance < bestDistance) {
                bestDistance = currentDistance;
                bestNode = current;
            }

            if (currentDistance <= 9) {
                return reconstructPath(current);
            }

            closedSet.add(current.pos);

            for (int[] dir : DIRECTIONS) {
                BlockPos neighbor = current.pos.add(dir[0], dir[1], dir[2]);

                if (closedSet.contains(neighbor)) continue;
                if (start.distanceSq(neighbor) > maxDistSq) continue;
                if (!isWalkableFast(neighbor)) continue;
                if (!canMoveToFast(current.pos, neighbor)) continue;

                double tentativeGCost = current.gCost + getMovementCost(current.pos, neighbor);
                Node neighborNode = allNodes.get(neighbor);

                if (neighborNode == null) {
                    neighborNode = new Node(neighbor, tentativeGCost, heuristic(neighbor, target), current);
                    allNodes.put(neighbor, neighborNode);
                    openSet.add(neighborNode);
                } else if (tentativeGCost < neighborNode.gCost) {
                    openSet.remove(neighborNode);
                    neighborNode.gCost = tentativeGCost;
                    neighborNode.fCost = neighborNode.gCost + neighborNode.hCost;
                    neighborNode.parent = current;
                    openSet.add(neighborNode);
                }
            }
        }
        return bestNode != null ? reconstructPath(bestNode) : null;
    }

    private List<BlockPos> getNeighbors(BlockPos pos) {
        List<BlockPos> neighbors = new ArrayList<>(12); // 12 возможных направлений
        for (int[] dir : DIRECTIONS) {
            BlockPos neighbor = pos.add(dir[0], dir[1], dir[2]);
            if (isWalkableFast(neighbor) && canMoveToFast(pos, neighbor)) {
                neighbors.add(neighbor);
            }
        }
        return neighbors;
    }

    // Быстрые версии методов с кэшированием
    private boolean isWalkableFast(BlockPos pos) {
        return walkableCache.computeIfAbsent(pos, p ->
                world.isBlockLoaded(p) &&
                        isPassableFast(p) &&
                        isSolid(p.add(0, -1, 0)) &&
                        isPassableFast(p.add(0, 1, 0))
        );
    }

    private boolean isPassableFast(BlockPos pos) {
        return passableCache.computeIfAbsent(pos, p ->
                world.isBlockLoaded(p) &&
                        (world.getBlockState(p).getBlock().getMaterial().blocksMovement() == false ||
                                world.getBlockState(p).getBlock().getMaterial() == Material.air ||
                                world.getBlockState(p).getBlock().getMaterial() == Material.water)
        );
    }

    private boolean isSolid(BlockPos pos) {
        if (!world.isBlockLoaded(pos)) return false;
        return world.getBlockState(pos).getBlock().getMaterial().isSolid();
    }

    private boolean canMoveToFast(BlockPos from, BlockPos to) {
        int dx = to.getX() - from.getX();
        int dy = to.getY() - from.getY();
        int dz = to.getZ() - from.getZ();

        if ((Math.abs(dx) + Math.abs(dz)) > 1) return false;
        if (dy > 1) return false;
        if (dy < -3) return false;
        if (dy > 0) return world.isAirBlock(from.add(0, 2, 0));

        return true;
    }

    // Остальные методы без изменений
    private double getMovementCost(BlockPos from, BlockPos to) {
        double cost = from.distanceSq(to);
        if (Math.abs(to.getY() - from.getY()) > 0) cost += 0.5;
        if (world.getBlockState(to).getBlock().getMaterial() == Material.water) cost += 2.0;
        return cost;
    }

    private double heuristic(BlockPos from, BlockPos to) {
        return Math.abs(from.getX() - to.getX()) + Math.abs(from.getY() - to.getY()) + Math.abs(from.getZ() - to.getZ());
    }

    private List<BlockPos> reconstructPath(Node endNode) {
        LinkedList<BlockPos> path = new LinkedList<>();
        Node current = endNode;
        while (current != null) {
            path.addFirst(current.pos);
            current = current.parent;
        }
        return path;
    }

    public List<BlockPos> getSimplifiedPath(List<BlockPos> fullPath) {
        if (fullPath == null || fullPath.size() <= 2) return fullPath;

        List<BlockPos> simplified = new ArrayList<>();
        simplified.add(fullPath.get(0));

        for (int i = 1; i < fullPath.size() - 1; i++) {
            BlockPos prev = simplified.get(simplified.size()-1);
            BlockPos current = fullPath.get(i);
            BlockPos next = fullPath.get(i+1);

            if (!isStraightLine(prev, current, next)) {
                simplified.add(current);
            }
        }

        simplified.add(fullPath.get(fullPath.size()-1));
        return simplified;
    }

    private boolean isStraightLine(BlockPos a, BlockPos b, BlockPos c) {
        return (b.getX() - a.getX()) == (c.getX() - b.getX()) &&
                (b.getY() - a.getY()) == (c.getY() - b.getY()) &&
                (b.getZ() - a.getZ()) == (c.getZ() - b.getZ());
    }

    private static class Node {
        final BlockPos pos;
        double gCost;
        final double hCost;
        double fCost;
        Node parent;

        Node(BlockPos pos, double gCost, double hCost, Node parent) {
            this.pos = pos;
            this.gCost = gCost;
            this.hCost = hCost;
            this.fCost = gCost + hCost;
            this.parent = parent;
        }
    }
}

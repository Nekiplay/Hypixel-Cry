package com.nekiplay.hypixelcry.utils;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import java.util.*;

public class PathFinder {
    private final World world;
    private final int maxSearchDistance;
    private int maxOperationsPerTick = 1000;
    private int operationsCount;

    public PathFinder(World world, int maxSearchDistance) {
        this.world = world;
        this.maxSearchDistance = maxSearchDistance;
    }

    public PathFinder(World world, int maxSearchDistance, int maxOperationsPerTick) {
        this.world = world;
        this.maxSearchDistance = maxSearchDistance;
        this.maxOperationsPerTick = maxOperationsPerTick;
    }

    /**
     * Find a path from start to end using A* algorithm
     * @param start Starting position
     * @param end Target position
     * @return List of BlockPos representing the path, or null if no path found
     */
    public List<BlockPos> findPath(BlockPos start, BlockPos end) {
        operationsCount = 0;
        if (start.equals(end)) {
            return Arrays.asList(start);
        }

        // First try exact path
        if (world.isBlockLoaded(end)) {
            //List<BlockPos> path = findPathToExactPosition(start, end);
            //if (path != null) return path;
        }

        // If exact path not found, try find nearest
        return findPathToNearestAvailable(start, end);
    }

    private List<BlockPos> findPathToExactPosition(BlockPos start, BlockPos end) {
        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingDouble(n -> n.fCost));
        Set<BlockPos> closedSet = new HashSet<>();
        Map<BlockPos, Node> allNodes = new HashMap<>();

        Node startNode = new Node(start, 0, heuristic(start, end), null);
        openSet.add(startNode);
        allNodes.put(start, startNode);

        while (!openSet.isEmpty() && operationsCount < maxOperationsPerTick) {
            operationsCount++;
            Node current = openSet.poll();

            if (current.pos.equals(end)) {
                return reconstructPath(current);
            }

            closedSet.add(current.pos);

            for (BlockPos neighbor : getNeighbors(current.pos)) {
                if (closedSet.contains(neighbor)) continue;
                if (!isWalkable(neighbor)) continue;
                if (start.distanceSq(neighbor) > maxSearchDistance * maxSearchDistance) continue;

                double tentativeGCost = current.gCost + getMovementCost(current.pos, neighbor);

                Node neighborNode = allNodes.get(neighbor);
                if (neighborNode == null) {
                    neighborNode = new Node(neighbor, tentativeGCost, heuristic(neighbor, end), current);
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
        return null;
    }

    private List<BlockPos> findPathToNearestAvailable(BlockPos start, BlockPos target) {
		PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingDouble(n -> n.fCost));
		Set<BlockPos> closedSet = new HashSet<>();
		Map<BlockPos, Node> allNodes = new HashMap<>();
		Node bestNode = null;
		double bestDistance = Double.MAX_VALUE;
	
		Node startNode = new Node(start, 0, heuristic(start, target), null);
		openSet.add(startNode);
		allNodes.put(start, startNode);
	
		while (!openSet.isEmpty() && operationsCount < maxOperationsPerTick) {
			operationsCount++;
			Node current = openSet.poll();
	
			// Проверяем загружен ли чанк перед оценкой расстояния
			if (world.isBlockLoaded(current.pos)) {
				double currentDistance = current.pos.distanceSq(target);
				if (currentDistance < bestDistance) {
					bestDistance = currentDistance;
					bestNode = current;
				}
	
				// Если достигли точки достаточно близко к цели (даже если цель не загружена)
				if (currentDistance <= 16) { // Увеличил радиус с 9 до 16 (4 блока)
					return reconstructPath(current);
				}
			}
	
			closedSet.add(current.pos);
	
			for (BlockPos neighbor : getNeighbors(current.pos)) {
				if (closedSet.contains(neighbor)) continue;
	
				// Пропускаем незагруженные чанки, но сохраняем возможность найти путь к границе загруженной области
				if (!world.isBlockLoaded(neighbor)) {
					// Запоминаем граничную точку как возможную цель
					if (bestNode == null || current.pos.distanceSq(target) < bestDistance) {
						bestDistance = current.pos.distanceSq(target);
						bestNode = current;
					}
					continue;
				}
	
				if (!isWalkable(neighbor)) continue;
				if (start.distanceSq(neighbor) > maxSearchDistance * maxSearchDistance) continue;
	
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

    /**
     * Get all walkable neighboring positions
     */
    private List<BlockPos> getNeighbors(BlockPos pos) {
        List<BlockPos> neighbors = new ArrayList<>();
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

        for (int[] dir : directions) {
            BlockPos neighbor = pos.add(dir[0], 0, dir[1]);
            if (world.isBlockLoaded(neighbor) && canMoveTo(pos, neighbor)) {
                neighbors.add(neighbor);
            }

            // Проверка вертикального перемещения только для загруженных чанков
            BlockPos upNeighbor = pos.add(dir[0], 1, dir[1]);
            if (world.isBlockLoaded(upNeighbor) && canMoveTo(pos, upNeighbor)) {
                neighbors.add(upNeighbor);
            }

            BlockPos downNeighbor = pos.add(dir[0], -1, dir[1]);
            if (world.isBlockLoaded(downNeighbor) && canMoveTo(pos, downNeighbor)) {
                neighbors.add(downNeighbor);
            }
        }
        return neighbors;
    }

    /**
     * Check if movement from one position to another is possible
     */
    private boolean canMoveTo(BlockPos from, BlockPos to) {
        if (!isWalkable(to)) {
            return false;
        }

        int dx = to.getX() - from.getX();
        int dy = to.getY() - from.getY();
        int dz = to.getZ() - from.getZ();

        // Don't allow diagonal movement in this implementation
        if ((Math.abs(dx) + Math.abs(dz)) > 1) {
            return false;
        }

        // Check vertical movement constraints
        if (dy > 1) { // Can't jump more than 1 block
            return false;
        }

        if (dy < -3) { // Don't fall more than 3 blocks (to avoid fall damage)
            return false;
        }

        // If jumping up, make sure there's clearance
        if (dy > 0) {
            BlockPos clearanceCheck = from.add(0, 2, 0);
            return world.isAirBlock(clearanceCheck);
        }

        return true;
    }

    /**
     * Check if a position is walkable (solid ground, air above for entity)
     */
    private boolean isWalkable(BlockPos pos) {
        if (!world.isBlockLoaded(pos)) return false;

        BlockPos groundPos = pos.add(0, -1, 0);
        if (!world.isBlockLoaded(groundPos)) return false;

        Block groundBlock = world.getBlockState(groundPos).getBlock();
        if (!groundBlock.getMaterial().isSolid()) return false;

        if (!isPassable(pos)) return false;

        BlockPos abovePos = pos.add(0, 1, 0);
        return world.isBlockLoaded(abovePos) && isPassable(abovePos);
    }

    /**
     * Check if a block position is passable (air, water, etc.)
     */
    private boolean isPassable(BlockPos pos) {
        if (!world.isBlockLoaded(pos)) return false;

        Block block = world.getBlockState(pos).getBlock();
        Material material = block.getMaterial();
        return material == Material.air ||
                material == Material.water ||
                !material.blocksMovement();
    }

    /**
     * Calculate movement cost between two adjacent positions
     */
    private double getMovementCost(BlockPos from, BlockPos to) {
        double baseCost = from.distanceSq(to);

        // Add extra cost for vertical movement
        int dy = Math.abs(to.getY() - from.getY());
        if (dy > 0) {
            baseCost += dy * 0.5; // Make vertical movement slightly more expensive
        }

        // Add cost for moving through water
        if (world.getBlockState(to).getBlock().getMaterial() == Material.water) {
            baseCost += 2.0;
        }

        return baseCost;
    }

    /**
     * Heuristic function (Manhattan distance)
     */
    private double heuristic(BlockPos from, BlockPos to) {
        return Math.abs(from.getX() - to.getX()) +
                Math.abs(from.getY() - to.getY()) +
                Math.abs(from.getZ() - to.getZ());
    }

    /**
     * Reconstruct the path from the end node back to the start
     */
    private List<BlockPos> reconstructPath(Node endNode) {
        List<BlockPos> path = new ArrayList<>();
        Node current = endNode;

        while (current != null) {
            path.add(current.pos);
            current = current.parent;
        }

        Collections.reverse(path);
        return path;
    }

    /**
     * Node class for A* algorithm
     */
    private static class Node {
        final BlockPos pos;
        double gCost; // Cost from start
        final double hCost; // Heuristic cost to end
        double fCost; // Total cost
        Node parent;

        Node(BlockPos pos, double gCost, double hCost, Node parent) {
            this.pos = pos;
            this.gCost = gCost;
            this.hCost = hCost;
            this.fCost = gCost + hCost;
            this.parent = parent;
        }
    }

    /**
     * Utility method to get a simplified path (removes intermediate points in straight lines)
     */
    public List<BlockPos> getSimplifiedPath(List<BlockPos> fullPath) {
        if (fullPath == null || fullPath.size() <= 2) {
            return fullPath;
        }

        List<BlockPos> simplified = new ArrayList<>();
        simplified.add(fullPath.get(0));

        for (int i = 1; i < fullPath.size() - 1; i++) {
            BlockPos prev = fullPath.get(i - 1);
            BlockPos current = fullPath.get(i);
            BlockPos next = fullPath.get(i + 1);

            // Check if current point is necessary (not in a straight line)
            int dx1 = current.getX() - prev.getX();
            int dz1 = current.getZ() - prev.getZ();
            int dy1 = current.getY() - prev.getY();

            int dx2 = next.getX() - current.getX();
            int dz2 = next.getZ() - current.getZ();
            int dy2 = next.getY() - current.getY();

            // If direction changes, keep the point
            if (dx1 != dx2 || dz1 != dz2 || dy1 != dy2) {
                simplified.add(current);
            }
        }

        simplified.add(fullPath.get(fullPath.size() - 1));
        return simplified;
    }
}

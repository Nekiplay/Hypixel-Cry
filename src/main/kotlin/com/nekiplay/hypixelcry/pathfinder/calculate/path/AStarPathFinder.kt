package com.nekiplay.hypixelcry.pathfinder.calculate.path

import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap
import it.unimi.dsi.fastutil.longs.Long2ObjectMap
import com.nekiplay.hypixelcry.pathfinder.calculate.Path
import com.nekiplay.hypixelcry.pathfinder.calculate.PathNode
import com.nekiplay.hypixelcry.pathfinder.calculate.openset.BinaryHeapOpenSet
import com.nekiplay.hypixelcry.pathfinder.goal.Goal
import com.nekiplay.hypixelcry.pathfinder.movement.CalculationContext
import com.nekiplay.hypixelcry.pathfinder.movement.MovementResult
import com.nekiplay.hypixelcry.pathfinder.movement.Moves
import com.nekiplay.hypixelcry.pathfinder.util.mc
import net.minecraft.util.BlockPos

class AStarPathFinder(val startX: Int, val startY: Int, val startZ: Int, val goal: Goal, val ctx: CalculationContext) {
    private val closedSet: Long2ObjectMap<PathNode> = Long2ObjectOpenHashMap()
    private var calculating = false
	private var closestNode: PathNode? = null
    private var closestDistance = Double.MAX_VALUE
    private var iterations = 0

    fun calculatePath(): Path? {
        calculating = true
        iterations = 0
        val openSet = BinaryHeapOpenSet()
        val startNode = PathNode(startX, startY, startZ, goal)
        val res = MovementResult()
        val moves = Moves.values()
        startNode.costSoFar = 0.0
        startNode.totalCost = startNode.costToEnd
        openSet.add(startNode)

        closestNode = startNode
        closestDistance = startNode.costToEnd

        while (!openSet.isEmpty() && calculating && iterations < ctx.maxIterations) {
            iterations++

            // Пропускаем итерации согласно stepSize
            if (iterations % ctx.stepSize != 0) continue

            val currentNode = openSet.poll()

            if (currentNode.costToEnd < closestDistance) {
                closestNode = currentNode
                closestDistance = currentNode.costToEnd
            }

            if (goal.isAtGoal(currentNode.x, currentNode.y, currentNode.z)) {
                return Path(startNode, currentNode, goal, ctx)
            }

            if (!isChunkLoaded(currentNode.x, currentNode.y, currentNode.z)) {
                return Path(startNode, currentNode, goal, ctx)
            }

            for (move in moves) {
                res.reset()
                move.calculate(ctx, currentNode.x, currentNode.y, currentNode.z, res)

                if (!isChunkLoaded(res.x, res.y, res.z)) {
                    res.cost = ctx.cost.INF_COST
                }

                val cost = res.cost
                if (cost >= ctx.cost.INF_COST) continue

                val neighbourNode = getNode(res.x, res.y, res.z, PathNode.longHash(res.x, res.y, res.z))
                val neighbourCostSoFar = currentNode.costSoFar + cost

                if (neighbourNode.costSoFar > neighbourCostSoFar) {
                    neighbourNode.parentNode = currentNode
                    neighbourNode.costSoFar = neighbourCostSoFar
                    neighbourNode.totalCost = neighbourCostSoFar + neighbourNode.costToEnd

                    if (neighbourNode.heapPosition == -1) {
                        openSet.add(neighbourNode)
                    } else {
                        openSet.relocate(neighbourNode)
                    }
                }
            }
        }
        calculating = false

        closestNode?.let {
            if (it != startNode) {
                return Path(startNode, it, goal, ctx)
            }
        }
        return null
    }

    private fun isChunkLoaded(x: Int, y: Int, z: Int): Boolean {
        return mc.theWorld?.isBlockLoaded(BlockPos(x, y, z)) ?: false
    }

    fun getNode(x: Int, y: Int, z: Int, hash: Long): PathNode {
        var n: PathNode? = closedSet.get(hash)
        if (n == null) {
            n = PathNode(x, y, z, goal)
            closedSet.put(hash, n)
        }
        return n
    }

    fun requestStop() {
        if (!calculating) return
        calculating = false
    }
}
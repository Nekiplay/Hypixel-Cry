package com.nekiplay.hypixelcry.pathfinder.utils

import com.nekiplay.hypixelcry.pathfinder.movement.CalculationContext
import com.nekiplay.hypixelcry.pathfinder.movement.MovementHelper
import net.minecraft.block.*
import net.minecraft.block.enums.BlockHalf
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import kotlin.math.abs

object BlockUtil {
    fun canWalkOnBlock(world: World, pos: BlockPos): Boolean {
        val blockState = world.getBlockState(pos)
        val blockAboveState = world.getBlockState(pos.up())

        val block = blockState.getBlock()
        val blockAbove = blockAboveState.getBlock()
        return blockState.isSolidBlock(world, pos) && !blockState.isLiquid && blockAbove === Blocks.AIR
    }

    fun neighbourGenerator(
        mainBlock: BlockPos,
        xD1: Int, xD2: Int,
        yD1: Int, yD2: Int,
        zD1: Int, zD2: Int
    ): MutableList<BlockPos?> {
        val neighbours: MutableList<BlockPos?> = ArrayList<BlockPos?>()
        for (x in xD1..xD2) {
            for (y in yD1..yD2) {
                for (z in zD1..zD2) {
                    neighbours.add(mainBlock.add(x, y, z))
                }
            }
        }
        return neighbours
    }

    fun isStairSlab(world: World, block: BlockPos?): Boolean {
        val state = world.getBlockState(block)
        return state.getBlock() is StairsBlock ||
                state.getBlock() is SlabBlock
    }

    fun getDirectionToWalkOnStairs(state: BlockState): Direction? {
        if (state.getBlock() is StairsBlock) {
            val facing = state.get<Direction?>(StairsBlock.FACING)
            val half = state.get<BlockHalf?>(StairsBlock.HALF)

            if (half == BlockHalf.TOP) {
                return Direction.UP
            } else {
                return facing
            }
        }
        return Direction.UP
    }

    fun getPlayerDirectionToBeAbleToWalkOnBlock(startPos: BlockPos, endPos: BlockPos): Direction {
        val deltaX = endPos.getX() - startPos.getX()
        val deltaZ = endPos.getZ() - startPos.getZ()

        if (abs(deltaX) > abs(deltaZ)) {
            return if (deltaX > 0) Direction.EAST else Direction.WEST
        } else {
            return if (deltaZ > 0) Direction.SOUTH else Direction.NORTH
        }
    }

    fun canWalkOn(ctx: CalculationContext, startPos: BlockPos, endPos: BlockPos): Boolean {
        val world = world ?: return false
        val startState = world.getBlockState(startPos)
        val endState = world.getBlockState(endPos)

        // Check if end position is not solid
        if (!endState.isSolidBlock(world, endPos)) {
            return endPos.y - startPos.y <= 1
        }

        // Get collision shapes
        val sourceShape = startState.getCollisionShape(world, startPos)
        val destShape = endState.getCollisionShape(world, endPos)

        val sourceMaxY = if (sourceShape.isEmpty) 0.0 else sourceShape.boundingBox.maxY
        val destMaxY = if (destShape.isEmpty) 0.0 else destShape.boundingBox.maxY

        // Special case for stairs
        if (endState.block is StairsBlock && (destMaxY - sourceMaxY) > 1.0) {
            return MovementHelper.isValidStair(
                endState,
                endPos.x - startPos.x,
                endPos.z - startPos.z
            )
        }

        // Default case
        return (destMaxY - sourceMaxY) <= 0.5
    }

    fun bresenham(ctx: CalculationContext, start: BlockPos, end: BlockPos): Boolean {
        return bresenham(
            ctx,
            Vec3d.ofCenter(start),
            Vec3d.ofCenter(end)
        )
    }

    fun bresenham(ctx: CalculationContext, start: Vec3d, end: Vec3d): Boolean {
        var currentPos = start

        val x1 = MathHelper.floor(end.x)
        val y1 = MathHelper.floor(end.y)
        val z1 = MathHelper.floor(end.z)
        var x0 = MathHelper.floor(currentPos.x)
        var y0 = MathHelper.floor(currentPos.y)
        var z0 = MathHelper.floor(currentPos.z)

        var lastState: BlockState? = world?.getBlockState(BlockPos(x0, y0, z0))
        var lastPos = BlockPos(x0, y0, z0)

        var iterations = 200
        while (iterations-- >= 0) {
            if (x0 == x1 && y0 == y1 && z0 == z1) {
                return true
            }

            var hasNewX = true
            var hasNewY = true
            var hasNewZ = true
            var newX = 999.0
            var newY = 999.0
            var newZ = 999.0

            if (x1 > x0) {
                newX = x0 + 1.0
            } else if (x1 < x0) {
                newX = x0 + 0.0
            } else {
                hasNewX = false
            }

            if (y1 > y0) {
                newY = y0 + 1.0
            } else if (y1 < y0) {
                newY = y0 + 0.0
            } else {
                hasNewY = false
            }

            if (z1 > z0) {
                newZ = z0 + 1.0
            } else if (z1 < z0) {
                newZ = z0 + 0.0
            } else {
                hasNewZ = false
            }

            var stepX = 999.0
            var stepY = 999.0
            var stepZ = 999.0

            val dx = end.x - currentPos.x
            val dy = end.y - currentPos.y
            val dz = end.z - currentPos.z

            if (hasNewX) stepX = (newX - currentPos.x) / dx
            if (hasNewY) stepY = (newY - currentPos.y) / dy
            if (hasNewZ) stepZ = (newZ - currentPos.z) / dz

            if (stepX == -0.0) stepX = -1.0E-4
            if (stepY == -0.0) stepY = -1.0E-4
            if (stepZ == -0.0) stepZ = -1.0E-4

            val direction: Direction?
            if (stepX < stepY && stepX < stepZ) {
                direction = if (x1 > x0) Direction.WEST else Direction.EAST
                currentPos = Vec3d(newX, currentPos.y + dy * stepX, currentPos.z + dz * stepX)
            } else if (stepY < stepZ) {
                direction = if (y1 > y0) Direction.DOWN else Direction.UP
                currentPos = Vec3d(currentPos.x + dx * stepY, newY, currentPos.z + dz * stepY)
            } else {
                direction = if (z1 > z0) Direction.NORTH else Direction.SOUTH
                currentPos = Vec3d(currentPos.x + dx * stepZ, currentPos.y + dy * stepZ, newZ)
            }

            x0 = MathHelper.floor(currentPos.x) - (if (direction == Direction.EAST) 1 else 0)
            y0 = MathHelper.floor(currentPos.y) - (if (direction == Direction.UP) 1 else 0)
            z0 = MathHelper.floor(currentPos.z) - (if (direction == Direction.SOUTH) 1 else 0)

            var currState: BlockState? = world?.getBlockState(BlockPos(x0, y0, z0))
            var i = 0

            if (!MovementHelper.canStandOn(x0, y0, z0, ctx, currState) || !MovementHelper.canWalkThrough(
                    ctx,
                    x0,
                    y0 + 1,
                    z0
                ) || !MovementHelper.canWalkThrough(ctx, x0, y0 + 2, z0)
            ) {
                i = -3
                var foundValidBlock = false
                while (++i <= 3) {
                    if (i == 0) continue
                    currState = world?.getBlockState(BlockPos(x0, y0 + i, z0))
                    if (!MovementHelper.canStandOn(x0, y0 + i, z0, ctx, currState)) {
                        continue
                    }
                    if (!MovementHelper.canWalkThrough(ctx, x0, y0 + i + 1, z0)) {
                        continue
                    }
                    if (!MovementHelper.canWalkThrough(ctx, x0, y0 + i + 2, z0)) {
                        continue
                    }
                    foundValidBlock = true
                    break
                }
                if (!foundValidBlock) {
                    return false
                }
            }

            val delta = (y0 + i) - lastPos.getY()
            if (delta > 0) {
                if (delta > 1) {
                    return false
                }

                var sourceHeight = -1.0
                var destHeight = -1.0
                var snow = false

                if (lastState?.getBlock() is SnowBlock) {
                    sourceHeight = (lastState.get<Int?>(SnowBlock.LAYERS) - 1) * 0.125
                    snow = true
                }

                if (currState?.getBlock() is SnowBlock) {
                    destHeight = (currState.get<Int?>(SnowBlock.LAYERS) - 1) * 0.125
                    snow = true
                }

                if (!snow) {
                    val srcSmall: Boolean = MovementHelper.isBottomSlab(lastState)
                    val destSmall: Boolean = MovementHelper.isBottomSlab(currState)
                    val destSmallStair: Boolean =
                        MovementHelper.isValidStair(currState, x0 - lastPos.getX(), z0 - lastPos.getZ())

                    if (srcSmall != (destSmall || destSmallStair)) {
                        return false
                    } else if (srcSmall) {
                        return false
                    }
                } else {
                    if (sourceHeight == -1.0) {
                        sourceHeight = lastState?.getCollisionShape(world, lastPos)?.getMax(Direction.Axis.Y) ?: 0.0
                    }
                    if (destHeight == -1.0) {
                        destHeight =
                            currState?.getCollisionShape(world, BlockPos(x0, y0 + i, z0))?.getMax(Direction.Axis.Y) ?: 0.0
                    }
                    if (destHeight - sourceHeight > -0.5) {
                        return false
                    }
                }
            }

            lastState = currState
            lastPos = BlockPos(x0, y0 + i, z0)
        }
        return false
    }
}
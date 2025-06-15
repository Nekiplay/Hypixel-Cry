package com.nekiplay.hypixelcry.pathfinder.movement

import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.CarpetBlock
import net.minecraft.block.CauldronBlock
import net.minecraft.block.DoorBlock
import net.minecraft.block.FenceGateBlock
import net.minecraft.block.FluidBlock
import net.minecraft.block.LadderBlock
import net.minecraft.block.SkullBlock
import net.minecraft.block.SlabBlock
import net.minecraft.block.SnowBlock
import net.minecraft.block.StainedGlassBlock
import net.minecraft.block.StairsBlock
import net.minecraft.block.TrapdoorBlock
import net.minecraft.block.enums.BlockHalf
import net.minecraft.block.enums.SlabType
import net.minecraft.fluid.Fluids
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import kotlin.math.abs

object MovementHelper {

    fun canWalkThrough(
        ctx: CalculationContext,
        x: Int,
        y: Int,
        z: Int,
        state: BlockState? = ctx.world?.getBlockState(BlockPos(x, y, z))
    ): Boolean {
        val canWalk = canWalkThroughBlockState(state)
        if (canWalk != null) {
            return canWalk
        }
        return canWalkThroughPosition(x, y, z, state, ctx)
    }

    fun canWalkThroughBlockState(state: BlockState?): Boolean? {
        val block = state?.block
        return when (block) {
            Blocks.AIR -> true
            Blocks.FIRE, Blocks.TRIPWIRE, Blocks.COBWEB, Blocks.END_PORTAL, Blocks.COCOA, is SkullBlock, is TrapdoorBlock -> false
            is DoorBlock, is FenceGateBlock -> {
                // TODO this assumes that all doors in all mods are openable
                if (block == Blocks.IRON_DOOR) {
                    false
                } else {
                    state.get(DoorBlock.OPEN) // Check if the door is open
                }
            }
            is CarpetBlock -> null
            is SnowBlock -> null
            is FluidBlock -> { // Changed from BlockLiquid to FluidBlock
                if (state.get(FluidBlock.LEVEL) != 0) { // Changed property access
                    false
                } else {
                    null
                }
            }
            is CauldronBlock -> false
            Blocks.LADDER -> false
            else -> {
                try {
                    // Updated method call for Fabric 1.21.5
                    block == Blocks.AIR
                } catch (exception: Throwable) {
                    println("The block ${state?.block?.translationKey} requires a special case due to the exception ${exception.message}")
                    null
                }
            }
        }
    }

    fun canWalkThroughPosition(
        x: Int,
        y: Int,
        z: Int,
        state: BlockState?,
        ctx: CalculationContext
    ): Boolean {
        val block = state?.block

        if (block is CarpetBlock) {
            return canStandOn(x, y - 1, z, ctx)
        }

        if (block is SnowBlock) {
            ctx.world?.isChunkLoaded(x shr 4, z shr 4)?.let {
                if (!it) {  // Updated chunk check
                    return true
                }
            }
            if (state.get(SnowBlock.LAYERS) >= 1) {  // Updated property access
                return false
            }
            return canStandOn(x, y - 1, z, ctx)
        }

        if (block is FluidBlock) {
            if (isFlowing(x, y, z, state, ctx)) {
                return false
            }

            val up = ctx.world?.getBlockState(BlockPos(x, y + 1, z))
            if (up?.block is FluidBlock || up?.block == Blocks.LILY_PAD) {  // Updated block names
                return false
            }
            return state.fluidState.fluid == Fluids.WATER  // Updated fluid check
        }

        return state?.isAir == true
    }

    fun canStandOn(x: Int, y: Int, z: Int, ctx: CalculationContext, state: BlockState? = ctx.world?.getBlockState(BlockPos(x, y, z))): Boolean {
        val block = state?.block ?: return false
        return when {
            block.defaultState.isSolidBlock(null, null) -> true  // Replaces isNormalCube
            block == Blocks.LADDER -> true
            block == Blocks.FARMLAND || block == Blocks.GRASS_BLOCK || block == Blocks.DIRT_PATH -> true
            block == Blocks.ENDER_CHEST || block == Blocks.CHEST || block == Blocks.TRAPPED_CHEST -> true
            block == Blocks.GLASS || block is StainedGlassBlock -> true
            block is StairsBlock -> true  // Changed from BlockStairs to StairsBlock
            block == Blocks.SEA_LANTERN -> true
            isWater(state) -> {
                val up = ctx.world?.getBlockState(BlockPos(x, y + 1, z))?.block
                up == Blocks.LILY_PAD || up is CarpetBlock  // Changed from waterlily to LILY_PAD
            }
            isLava(state) -> false
            block is SlabBlock -> true  // Changed from BlockSlab to SlabBlock
            block is SnowBlock -> true  // Changed from BlockSnow to SnowBlock
            else -> false
        }
    }
    fun possiblyFlowing(state: BlockState?): Boolean {
        return state?.block is FluidBlock && state.get(FluidBlock.LEVEL) != 0
    }

    fun isFlowing(x: Int, y: Int, z: Int, state: BlockState, ctx: CalculationContext): Boolean {
        if (state.block !is FluidBlock) {
            return false
        }
        if (state.get(FluidBlock.LEVEL) != 0) {
            return true
        }
        return possiblyFlowing(ctx.world?.getBlockState(BlockPos(x + 1, y, z))) ||
                possiblyFlowing(ctx.world?.getBlockState(BlockPos(x - 1, y, z))) ||
                possiblyFlowing(ctx.world?.getBlockState(BlockPos(x, y, z + 1))) ||
                possiblyFlowing(ctx.world?.getBlockState(BlockPos(x, y, z - 1)))
    }

    fun isWater(state: BlockState?): Boolean {
        return state?.fluidState?.fluid == Fluids.WATER
    }

    fun isLava(state: BlockState): Boolean {
        return state.fluidState.fluid == Fluids.LAVA
    }

    fun isBottomSlab(state: BlockState?): Boolean {
        return state?.block is SlabBlock &&
                state.get(SlabBlock.TYPE) == SlabType.BOTTOM
    }

    fun isValidStair(state: BlockState?, dx: Int, dz: Int): Boolean {
        if (dx == dz) return false
        if (state?.block !is StairsBlock) return false
        if (state.get(StairsBlock.HALF) != BlockHalf.BOTTOM) return false

        val stairFacing = state.get(StairsBlock.FACING)

        return when {
            dz == -1 -> stairFacing == Direction.NORTH
            dz == 1 -> stairFacing == Direction.SOUTH
            dx == -1 -> stairFacing == Direction.WEST
            dx == 1 -> stairFacing == Direction.EAST
            else -> false
        }
    }
    
    fun getFacing(dx: Int, dz: Int): Direction {
        return if (dx == 0 && dz == 0) {
            Direction.UP
        } else {
            // Calculate index based on dx/dz values
            val index = abs(dx) * (2 + dx) + abs(dz) * (1 - dz)
            // Get horizontal directions
            val horizontals = Direction.entries.filter { it.axis.isHorizontal }
            horizontals.getOrNull(index) ?: Direction.NORTH
        }
    }

    fun isLadder(state: BlockState?): Boolean {
        return state?.block == Blocks.LADDER
    }

    fun canWalkIntoLadder(ladderState: BlockState?, dx: Int, dz: Int): Boolean {
        return isLadder(ladderState) && ladderState?.get(LadderBlock.FACING) != getFacing(
            dx,
            dz
        )
    }
}
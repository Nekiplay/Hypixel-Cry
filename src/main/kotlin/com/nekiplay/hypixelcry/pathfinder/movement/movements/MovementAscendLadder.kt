package com.nekiplay.hypixelcry.pathfinder.movement.movements

import com.nekiplay.hypixelcry.Main
import com.nekiplay.hypixelcry.pathfinder.movement.CalculationContext
import com.nekiplay.hypixelcry.pathfinder.movement.Movement
import com.nekiplay.hypixelcry.pathfinder.movement.MovementHelper
import com.nekiplay.hypixelcry.pathfinder.movement.MovementResult
import net.minecraft.util.BlockPos

class MovementAscendLadder(mm: Main, from: BlockPos, to: BlockPos) : Movement(mm, from, to) {
    override fun calculateCost(ctx: CalculationContext, res: MovementResult) {
        calculateCost(ctx, source.x, source.y, source.z, dest.x, dest.y, dest.z, res)
        costs = res.cost
    }

    companion object {
        fun calculateCost(
            ctx: CalculationContext,
            x: Int,
            y: Int,
            z: Int,
            destX: Int,
            destY: Int,
            destZ: Int,
            res: MovementResult
        ) {
            res.set(destX, destY, destZ)

            // Проверяем, что мы действительно поднимаемся по лестнице
            val sourceState = ctx.get(x, y, z)
            val destState = ctx.get(destX, destY, destZ)

            if (!MovementHelper.isLadder(sourceState)) {
                return
            }

            if (!MovementHelper.isLadder(destState) && !MovementHelper.canWalkThrough(ctx.bsa, destX, destY, destZ)) {
                return
            }

            // Проверяем пространство над целевым блоком
            if (!MovementHelper.canWalkThrough(ctx.bsa, destX, destY + 1, destZ)) {
                return
            }

            // Стоимость подъема по лестнице
            res.cost = ctx.cost.ONE_UP_LADDER_COST
        }
    }
}
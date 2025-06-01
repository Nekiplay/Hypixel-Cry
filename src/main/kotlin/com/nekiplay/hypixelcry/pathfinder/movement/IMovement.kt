package com.nekiplay.hypixelcry.pathfinder.movement

import com.nekiplay.hypixelcry.Main
import net.minecraft.util.BlockPos

interface IMovement {
    val mm: Main
    val source: BlockPos
    val dest: BlockPos
    val costs: Double // plural cuz kotlin gae

    fun getCost(): Double
    fun calculateCost(ctx: CalculationContext, res: MovementResult)
}
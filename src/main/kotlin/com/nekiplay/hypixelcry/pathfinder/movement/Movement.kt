package com.nekiplay.hypixelcry.pathfinder.movement

import com.nekiplay.hypixelcry.Main
import net.minecraft.util.BlockPos

abstract class Movement(override val mm: Main, override val source: BlockPos, override val dest: BlockPos) :
    IMovement {

    override var costs: Double = 1e6
    override fun getCost() = costs
}
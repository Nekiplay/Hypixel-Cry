package com.nekiplay.hypixelcry.pathfinder.util

import net.minecraft.util.BlockPos
import net.minecraft.util.Vec3


fun BlockPos.toVec3() = Vec3(x.toDouble() + 0.5, y.toDouble() + 0.5, z.toDouble() + 0.5)


package com.nekiplay.hypixelcry.pathfinder.utils

import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d

fun BlockPos.toVec3() = Vec3d(x.toDouble() + 0.5, y.toDouble() + 0.5, z.toDouble() + 0.5)
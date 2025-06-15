package com.nekiplay.hypixelcry.pathfinder.utils

import net.minecraft.client.MinecraftClient

val mc
    get() = MinecraftClient.getInstance()
val player
    get() = mc.player
val world
    get() = mc.world
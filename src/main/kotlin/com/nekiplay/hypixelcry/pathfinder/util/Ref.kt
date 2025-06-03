package com.nekiplay.hypixelcry.pathfinder.util

import net.minecraft.client.Minecraft

val mc
    get() = Minecraft.getMinecraft()
val player
    get() = mc.thePlayer
val world
    get() = mc.theWorld

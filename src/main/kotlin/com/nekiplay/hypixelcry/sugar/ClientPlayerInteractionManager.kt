package com.nekiplay.hypixelcry.sugar

import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayerInteractionManager
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand

fun ClientPlayerInteractionManager.silentUse(useSlot: Int) {
    val player = MinecraftClient.getInstance().player ?: return
    val inventory = player.inventory
    val originalSlot = inventory.selectedSlot

    if (useSlot in 1..9) {
        inventory.selectedSlot = useSlot
        val result = this.interactItem(player, Hand.MAIN_HAND)
        if (result is ActionResult.Success) {
            player.swingHand(Hand.MAIN_HAND)
        }
    }

    inventory.selectedSlot = originalSlot
}
package com.nekiplay.hypixelcry.sugar

import com.nekiplay.hypixelcry.utils.ItemUtils
import net.minecraft.entity.player.PlayerInventory

fun PlayerInventory.findSlotInHotbarByItemId(id: String): Int? {
    for (i in 0..8) {
        val stack = this.getStack(i)
        if (!stack.isEmpty && ItemUtils.getItemId(stack).equals(id, ignoreCase = true)) {
            return i
        }
    }
    return null
}
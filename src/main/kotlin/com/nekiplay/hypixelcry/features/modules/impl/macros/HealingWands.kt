package com.nekiplay.hypixelcry.features.modules.impl.macros

import com.nekiplay.hypixelcry.HypixelCry
import com.nekiplay.hypixelcry.features.modules.BindableClientModule
import com.nekiplay.hypixelcry.sugar.findSlotInHotbarByItemId
import com.nekiplay.hypixelcry.sugar.silentUse

object HealingWands : BindableClientModule() {
    private val WAND_IDS = setOf(
        "WAND_OF_ATONEMENT",
        "WAND_OF_RESTORATION",
        "WAND_OF_MENDING",
        "WAND_OF_HEALING"
    )

    override fun getKeybind(): Int {
        return HypixelCry.config.macros.items.healingWands.keybind
    }

    override fun press() {
        findWand()?.let { slot ->
            interaction?.silentUse(slot)
        }
    }

    private fun findWand(): Int? {
        return WAND_IDS.firstNotNullOfOrNull { player?.inventory?.findSlotInHotbarByItemId(it) }
    }
}
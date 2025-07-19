package com.nekiplay.hypixelcry.features.modules.impl.macros

import com.nekiplay.hypixelcry.HypixelCry
import com.nekiplay.hypixelcry.features.modules.BindableClientModule
import com.nekiplay.hypixelcry.sugar.findSlotInHotbarByItemId
import com.nekiplay.hypixelcry.sugar.silentUse
import com.nekiplay.hypixelcry.utils.ItemUtils
import net.minecraft.component.ComponentHolder

object RogueSword : BindableClientModule() {
    private val WAND_IDS = setOf(
        "ROGUE_SWORD"
    )

    override fun getKeybind(): Int {
        return HypixelCry.config.macros.items.rogueSword.keybind
    }

    override fun press() {
        findWand()?.let { slot ->
            interaction?.silentUse(slot)
        }
    }

    private fun findWand(): Int? {
        return WAND_IDS.firstNotNullOfOrNull { id ->
            player?.inventory?.findSlotInHotbarByItemId(id)?.takeIf { slot ->
                val stack = player?.inventory?.getStack(slot)
                stack != null && !stack.isEmpty && ItemUtils.getItemId(stack as ComponentHolder)
                    .equals(id, ignoreCase = true)
            }
        }
    }
}
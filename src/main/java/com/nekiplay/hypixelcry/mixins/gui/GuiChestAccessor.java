package com.nekiplay.hypixelcry.mixins.gui;

import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.IInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GuiChest.class)
public interface GuiChestAccessor {
    @Accessor("lowerChestInventory")
    IInventory GetLowerChestInventory();
}

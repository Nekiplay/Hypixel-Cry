package com.nekiplay.hypixelcry.features.macros;

import com.nekiplay.hypixelcry.DataInterpretation.DataExtractor;
import com.nekiplay.hypixelcry.HypixelCry;
import com.nekiplay.hypixelcry.mixins.gui.GuiChestAccessor;
import com.nekiplay.hypixelcry.utils.ApecUtils;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.IInventory;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.nekiplay.hypixelcry.HypixelCry.mc;

public class AutoChestClose {
    @SubscribeEvent
    public void tickEvent(RenderWorldLastEvent event) {
        if (mc.currentScreen instanceof GuiChest) {
            DataExtractor extractor = HypixelCry.dataExtractor;
            GuiChestAccessor accessor = (GuiChestAccessor) mc.currentScreen;
            IInventory lower = accessor.GetLowerChestInventory();
            String windowTitle = "";
            if (lower.hasCustomName()) {
                windowTitle = ApecUtils.removeAllCodes(lower.getDisplayName().getFormattedText());
            }
            if (HypixelCry.config.macros.dungeons.autoCloseChests.enable && (windowTitle.isEmpty() || windowTitle.equalsIgnoreCase("chest")) && extractor.isInTheCatacombs) {
                mc.thePlayer.closeScreen();
            }
        }
    }
}

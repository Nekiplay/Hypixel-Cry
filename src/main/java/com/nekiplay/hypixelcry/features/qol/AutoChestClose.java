package com.nekiplay.hypixelcry.features.qol;

import com.nekiplay.hypixelcry.DataInterpretation.DataExtractor;
import com.nekiplay.hypixelcry.Main;
import com.nekiplay.hypixelcry.mixins.GuiChestAccessor;
import com.nekiplay.hypixelcry.utils.ApecUtils;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.nekiplay.hypixelcry.Main.mc;
import static com.nekiplay.hypixelcry.Main.myConfigFile;

public class AutoChestClose {
    @SubscribeEvent
    public void tickEvent(RenderWorldLastEvent event) {
        if (mc.currentScreen instanceof GuiChest) {
            DataExtractor extractor = Main.getInstance().dataExtractor;
            GuiChestAccessor accessor = (GuiChestAccessor) mc.currentScreen;
            IInventory lower = accessor.GetLowerChestInventory();
            String windowTitle = "";
            if (lower.hasCustomName()) {
                windowTitle = ApecUtils.removeAllCodes(lower.getDisplayName().getFormattedText());
            }
            if (myConfigFile.isDebug) {
                mc.thePlayer.addChatMessage(new ChatComponentText("[DEBUG] [Auto Chest Close] Opened Chest: " + windowTitle));
            }
            if ((windowTitle.isEmpty() || windowTitle.equalsIgnoreCase("chest")) && extractor.isInTheCatacombs) {
                mc.thePlayer.closeScreen();
            }
        }
    }
}

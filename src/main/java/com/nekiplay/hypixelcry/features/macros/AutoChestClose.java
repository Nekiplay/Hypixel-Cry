package com.nekiplay.hypixelcry.features.macros;

import com.nekiplay.hypixelcry.HypixelCry;
import com.nekiplay.hypixelcry.annotations.Init;
import com.nekiplay.hypixelcry.utils.StringUtils;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.text.Text;

import static com.nekiplay.hypixelcry.HypixelCry.mc;

public class AutoChestClose {
    @Init
    public static void init() {
        WorldRenderEvents.AFTER_TRANSLUCENT.register(AutoChestClose::render);
    }

    private static void render(WorldRenderContext worldRenderContext) {
        if (mc.player == null) return;

        if (mc.currentScreen instanceof GenericContainerScreen screen) {
            Text containerName = screen.getTitle();

            String noColor = StringUtils.removeAllCodes(containerName.getString());
            mc.player.sendMessage(Text.of(noColor), false);
            if (HypixelCry.config.macros.dungeons.autoCloseChests.enable && (noColor.equalsIgnoreCase("chest") || noColor.equalsIgnoreCase("сундук"))) {
                mc.player.closeHandledScreen();
            }
        }
    }
}

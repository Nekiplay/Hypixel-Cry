package com.nekiplay.hypixelcry.features.macros;

import com.nekiplay.hypixelcry.HypixelCry;
import com.nekiplay.hypixelcry.annotations.Init;
import com.nekiplay.hypixelcry.events.SkyblockEvents;
import com.nekiplay.hypixelcry.utils.Location;
import com.nekiplay.hypixelcry.utils.StringUtils;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.text.Text;

import static com.nekiplay.hypixelcry.HypixelCry.mc;

public class AutoChestClose {
    @Init
    public static void init() {
        WorldRenderEvents.AFTER_TRANSLUCENT.register(AutoChestClose::render);
        SkyblockEvents.LOCATION_CHANGE.register(AutoChestClose::locationChange);
    }

    private static boolean allowWorking = false;

    private static void locationChange(Location location) {
        allowWorking = location == Location.DUNGEON;
    }

    private static void render(WorldRenderContext worldRenderContext) {
        if (mc.player == null) return;

        if (mc.currentScreen instanceof GenericContainerScreen screen && allowWorking) {
            Text containerName = screen.getTitle();

            String noColor = StringUtils.removeAllCodes(containerName.getString());
            if (HypixelCry.config.macros.dungeons.autoCloseChests.enable && (noColor.equalsIgnoreCase("large chest") || noColor.equalsIgnoreCase("chest") || noColor.equalsIgnoreCase("большой сундук") || noColor.equalsIgnoreCase("сундук"))) {
                mc.player.closeHandledScreen();
            }
        }
    }
}

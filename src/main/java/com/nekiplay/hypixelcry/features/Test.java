package com.nekiplay.hypixelcry.features;

import com.nekiplay.hypixelcry.HypixelCry;
import com.nekiplay.hypixelcry.annotations.Init;
import com.nekiplay.hypixelcry.events.SkyblockEvents;
import com.nekiplay.hypixelcry.utils.Area;
import com.nekiplay.hypixelcry.utils.Location;
import com.nekiplay.hypixelcry.utils.render.RenderHelper;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import static com.nekiplay.hypixelcry.HypixelCry.mc;

public class Test {

    @Init
    public static void init() {
        SkyblockEvents.LOCATION_CHANGE.register(Test::locationChange);
        SkyblockEvents.AREA_CHANGE.register(Test::areaChange);
    }

    private static void areaChange(Area area) {
        if (mc.player != null && HypixelCry.config.misc.debug.enabled) {
            mc.player.sendMessage(Text.of("New area: " + area.toString()), false);
        }
    }

    private static void locationChange(Location location) {
        if (mc.player != null && HypixelCry.config.misc.debug.enabled) {
            mc.player.sendMessage(Text.of("New location: " + location.toString()), false);
        }
    }
}

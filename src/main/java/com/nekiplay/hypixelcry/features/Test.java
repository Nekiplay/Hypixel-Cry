package com.nekiplay.hypixelcry.features;

import com.nekiplay.hypixelcry.annotations.Init;
import com.nekiplay.hypixelcry.events.render.WorldRenderEvent;
import com.nekiplay.hypixelcry.utils.render.RenderHelper;
import meteordevelopment.orbit.EventHandler;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.util.math.BlockPos;

public class Test {

    @Init
    public static void init() {
        WorldRenderEvents.AFTER_TRANSLUCENT.register(Test::render);
    }

    public static void render(WorldRenderContext context) {
        float[] colorComponents = new float[]{ 1.0f, 0.0f, 0.0f }; // Красный цвет (R, G, B)
        float alpha = 0.5f; // Прозрачность
        float lineWidth = 2.0f; // Толщина линии
        boolean throughWalls = true; // Рендерить через стены

        // Рендерим заполненный блок и его контур
        RenderHelper.renderFilled(context, new BlockPos(0, 0, 0), colorComponents, alpha, throughWalls);
        RenderHelper.renderOutline(context, new BlockPos(0, 0, 0), colorComponents, lineWidth, throughWalls);
    }
}

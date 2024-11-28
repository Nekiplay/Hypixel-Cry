package com.nekiplay.hypixelcry.config.pages.nukers;

import cc.polyfrost.oneconfig.config.annotations.*;
import cc.polyfrost.oneconfig.config.core.OneColor;
import cc.polyfrost.oneconfig.config.core.OneKeyBind;
import cc.polyfrost.oneconfig.config.data.InfoType;
import org.lwjgl.input.Keyboard;

public class GardenMainPage {
    @Info(
            text = "This nuker for clearing garden plots",
            type = InfoType.INFO,
            category = "Garden nuker",
            subcategory = "General"
    )
    public static boolean ignored; // Useless. Java limitations with @annotation.
    @Slider(
            name = "Horizontal distance",
            description = "Maximum horizontal distance",
            category = "Garden nuker",
            subcategory = "General",
            max = 5.4f,
            min = 1f
    )
    public float maximumNukerHorizontalDistance = 5.4f;

    @Slider(
            name = "Verical distance",
            description = "Maximum vertical distance",
            category = "Garden nuker",
            subcategory = "General",
            max = 7.5f,
            min = 1f
    )
    public float maximumNukerVericalDistance = 7.5f;
    @Slider(
            name = "Max boost",
            step = 1,
            min = 1.0F,
            max = 8.0F,
            description = "Boost max block per tick speed",
            category = "Garden nuker",
            subcategory = "General"
    )
    public int gardenNukerBlockPesTick = 4;

    @Slider(
            name = "Boost every ticks",
            step = 1,
            min = 0.0F,
            max = 64.0F,
            description = "Boost delay",
            category = "Garden nuker",
            subcategory = "General"
    )
    public int gardenNukerBoostTicks = 13;

    @Checkbox(
            name = "Tracer",
            description = "Render Tracer to break location",
            category = "Garden nuker",
            subcategory = "Visuals"
    )
    public boolean tracer = true;

    @Color(
            name = "Break block color",
            description = "The color of the block that is currently breaking",
            category = "Garden nuker",
            subcategory = "Colors"
    )
    public OneColor color = new OneColor(255, 255, 0);
}

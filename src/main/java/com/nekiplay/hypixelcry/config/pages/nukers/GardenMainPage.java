package com.nekiplay.hypixelcry.config.pages.nukers;

import cc.polyfrost.oneconfig.config.annotations.Color;
import cc.polyfrost.oneconfig.config.annotations.Info;
import cc.polyfrost.oneconfig.config.annotations.Slider;
import cc.polyfrost.oneconfig.config.core.OneColor;
import cc.polyfrost.oneconfig.config.data.InfoType;

public class GardenMainPage {
    @Info(
            text = "This nuker for clearing garden plots",
            type = InfoType.INFO,
            category = "Garden nuker",
            subcategory = "General"
    )
    public static boolean ignored; // Useless. Java limitations with @annotation.
    @Slider(
            name = "Max boost",
            step = 1,
            min = 1.0F,
            max = 8.0F,
            description = "Boost max block per tick speed",
            category = "Garden nuker",
            subcategory = "General"
    )
    public int GardenNukerBlockPesTick = 4;

    @Slider(
            name = "Boost every ticks",
            step = 1,
            min = 0.0F,
            max = 64.0F,
            description = "Boost delay",
            category = "Garden nuker",
            subcategory = "General"
    )
    public int GardenNukerBoostTicks = 13;

    @Color(
            name = "Break block color",
            description = "The color of the block that is currently breaking",
            category = "Garden nuker",
            subcategory = "Colors"
    )
    public OneColor color = new OneColor(255, 255, 0);
}

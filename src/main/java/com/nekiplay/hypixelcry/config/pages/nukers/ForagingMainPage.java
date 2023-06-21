package com.nekiplay.hypixelcry.config.pages.nukers;

import cc.polyfrost.oneconfig.config.annotations.Checkbox;
import cc.polyfrost.oneconfig.config.annotations.Color;
import cc.polyfrost.oneconfig.config.annotations.Info;
import cc.polyfrost.oneconfig.config.annotations.Slider;
import cc.polyfrost.oneconfig.config.core.OneColor;
import cc.polyfrost.oneconfig.config.data.InfoType;

public class ForagingMainPage {
    @Info(
            text = "This nuker for Foraging exp",
            type = InfoType.INFO,
            category = "Foraging nuker",
            subcategory = "General"
    )
    public static boolean ignored; // Useless. Java limitations with @annotation.
    @Info(
            text = "This nuker not recommended for AFK",
            type = InfoType.WARNING,
            category = "Foraging nuker",
            subcategory = "General"
    )
    public static boolean ignored2; // Useless. Java limitations with @annotation.
    @Slider(
            name = "Max boost",
            step = 1,
            min = 1.0F,
            max = 8.0F,
            description = "Boost max block per tick speed",
            category = "Foraging nuker",
            subcategory = "General"
    )
    public int ForagingNukerBlockPesTick = 4;

    @Slider(
            name = "Boost every ticks",
            step = 1,
            min = 0.0F,
            max = 64.0F,
            description = "Boost delay",
            category = "Foraging nuker",
            subcategory = "General"
    )
    public int ForagingNukerBoostTicks = 13;

    @Checkbox(
            name = "Ghost Axe",
            description = "Ghost switch axe to hand",
            category = "Foraging nuker",
            subcategory = "General"
    )
    public boolean ForagingNukerGhostAxe = false;

    @Color(
            name = "Break block color",
            description = "The color of the block that is currently breaking",
            category = "Foraging nuker",
            subcategory = "Colors"
    )
    public OneColor color = new OneColor(94, 58, 19);        // default color
}

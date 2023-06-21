package com.nekiplay.hypixelcry.config.pages.nukers;

import cc.polyfrost.oneconfig.config.annotations.*;
import cc.polyfrost.oneconfig.config.core.OneColor;
import cc.polyfrost.oneconfig.config.data.InfoType;

public class SandMainPage {
    @Info(
            text = "This nuker for making first money and easy for Mining exp",
            type = InfoType.INFO,
            category = "Sand nuker",
            subcategory = "General"
    )
    public static boolean ignored; // Useless. Java limitations with @annotation.
    @Info(
            text = "This nuker not recommended for AFK",
            type = InfoType.WARNING,
            category = "Sand nuker",
            subcategory = "General"
    )
    public static boolean ignored2; // Useless. Java limitations with @annotation.
    @Slider(
            name = "Max boost",
            step = 1,
            min = 1.0F,
            max = 8.0F,
            description = "Boost max block per tick speed",
            category = "Sand nuker",
            subcategory = "General"
    )
    public int SandNukerBlockPesTick = 4;

    @Slider(
            name = "Boost every ticks",
            step = 1,
            min = 0.0F,
            max = 64.0F,
            description = "Boost delay",
            category = "Sand nuker",
            subcategory = "General"
    )
    public int SandNukerBoostTicks = 13;

    @Dropdown(
            name = "Find mode",
            description = "Exposed mode",
            category = "Sand nuker",
            subcategory = "General",
            options = {"Hidden", "Visible", "All"}
    )
    public int SandExposedMode = 2;

    @Checkbox(
            name = "Ghost Shovel",
            description = "Ghost switch shovel to hand",
            category = "Sand nuker",
            subcategory = "General"
    )
    public boolean SandGhostShovel = false;

    @Color(
            name = "Break block color",
            description = "The color of the block that is currently breaking",
            category = "Sand nuker",
            subcategory = "Colors"
    )
    public OneColor color = new OneColor(255, 255, 0);
}

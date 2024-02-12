package com.nekiplay.hypixelcry.config.pages.nukers;

import cc.polyfrost.oneconfig.config.annotations.*;
import cc.polyfrost.oneconfig.config.core.OneColor;
import cc.polyfrost.oneconfig.config.core.OneKeyBind;
import cc.polyfrost.oneconfig.config.data.InfoType;
import org.lwjgl.input.Keyboard;

public class ForagingMainPage {
    @Info(
            text = "This nuker for Foraging exp",
            type = InfoType.INFO,
            category = "Foraging nuker",
            subcategory = "General"
    )
    public static boolean ignored; // Useless. Java limitations with @annotation.
    @KeyBind(
            name = "KeyBind", category = "Foraging nuker", subcategory = "General",
            description = "Toggles the macro on/off", size = 2
    )
    public OneKeyBind toggleMacro = new OneKeyBind(Keyboard.KEY_NONE);
    @Info(
            text = "This nuker not recommended for AFK",
            type = InfoType.WARNING,
            category = "Foraging nuker",
            subcategory = "General"
    )
    public static boolean ignored2; // Useless. Java limitations with @annotation.
    @Slider(
            name = "Horizontal distance",
            description = "Maximum horizontal distance",
            category = "Foraging nuker",
            subcategory = "General",
            max = 5.4f,
            min = 1f
    )
    public float MaximumNukerHorizontalDistance = 5.4f;

    @Slider(
            name = "Verical distance",
            description = "Maximum vertical distance",
            category = "Foraging nuker",
            subcategory = "General",
            max = 7.5f,
            min = 1f
    )
    public float MaximumNukerVericalDistance = 7.5f;
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
            name = "Tracer",
            description = "Render Tracer to break location",
            category = "Foraging nuker",
            subcategory = "Visuals"
    )
    public boolean Tracer = true;

    @Color(
            name = "Break block color",
            description = "The color of the block that is currently breaking",
            category = "Foraging nuker",
            subcategory = "Colors"
    )
    public OneColor color = new OneColor(94, 58, 19);        // default color
}

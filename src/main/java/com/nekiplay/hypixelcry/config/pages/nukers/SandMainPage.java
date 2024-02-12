package com.nekiplay.hypixelcry.config.pages.nukers;

import cc.polyfrost.oneconfig.config.annotations.*;
import cc.polyfrost.oneconfig.config.core.OneColor;
import cc.polyfrost.oneconfig.config.core.OneKeyBind;
import cc.polyfrost.oneconfig.config.data.InfoType;
import org.lwjgl.input.Keyboard;

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
    @KeyBind(
            name = "KeyBind", category = "Sand nuker", subcategory = "General",
            description = "Toggles the macro on/off", size = 2
    )
    public OneKeyBind toggleMacro = new OneKeyBind(Keyboard.KEY_NONE);
    @Slider(
            name = "Horizontal distance",
            description = "Maximum horizontal distance",
            category = "Sand nuker",
            subcategory = "General",
            max = 5.4f,
            min = 1f
    )
    public float MaximumNukerHorizontalDistance = 5.4f;

    @Slider(
            name = "Verical distance",
            description = "Maximum vertical distance",
            category = "Sand nuker",
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
            name = "Tracer",
            description = "Render Tracer to break location",
            category = "Sand nuker",
            subcategory = "Visuals"
    )
    public boolean Tracer = true;

    @Color(
            name = "Break block color",
            description = "The color of the block that is currently breaking",
            category = "Sand nuker",
            subcategory = "Colors"
    )
    public OneColor color = new OneColor(255, 255, 0);
}

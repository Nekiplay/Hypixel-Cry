package com.nekiplay.hypixelcry.config.pages.nukers;

import cc.polyfrost.oneconfig.config.annotations.*;
import cc.polyfrost.oneconfig.config.core.OneColor;
import cc.polyfrost.oneconfig.config.core.OneKeyBind;
import cc.polyfrost.oneconfig.config.data.InfoType;
import org.lwjgl.input.Keyboard;

public class MithrilMainPage {
    @Info(
            text = "This nuker not recommended for AFK",
            type = InfoType.WARNING,
            category = "Mithril nuker",
            subcategory = "General"
    )
    public static boolean ignored2; // Useless. Java limitations with @annotation.
    @KeyBind(
            name = "KeyBind", category = "Mithril nuker", subcategory = "General",
            description = "Toggles the macro on/off", size = 2
    )
    public OneKeyBind toggleMacro = new OneKeyBind(Keyboard.KEY_NONE);
    @Slider(
            name = "Horizontal distance",
            description = "Maximum horizontal distance",
            category = "Mithril nuker",
            subcategory = "Garden nuker",
            max = 5.4f,
            min = 1f
    )
    public float MaximumNukerHorizontalDistance = 5.4f;

    @Slider(
            name = "Verical distance",
            description = "Maximum vertical distance",
            category = "Mithril nuker",
            subcategory = "Garden nuker",
            max = 7.5f,
            min = 1f
    )
    public float MaximumNukerVericalDistance = 7.5f;
    @Dropdown(
            name = "Find mode",        // name of the component
            description = "Exposed mode",
            options = {"Hidden", "Visible", "All"},
            category = "Mithril nuker",
            subcategory = "General"
    )
    public int MithrilNukerExposedMode = 2;

    @Checkbox(
            name = "Tracer",
            description = "Render Tracer to break location",
            category = "Mithril nuker",
            subcategory = "Visuals"
    )
    public boolean Tracer = true;

    @Checkbox(
            name = "Skip Titanium",
            description = "Mithril nuker will ignore titanium",
            category = "Mithril nuker",
            subcategory = "General"
    )
    public boolean MithrilNukerIgnoreTitanium = false;

    @Color(
            name = "Break color",
            description = "The color of the breaking block",
            category = "Mithril nuker",
            subcategory = "Colors"
    )
    public OneColor Color = new OneColor(255, 192, 92);

}

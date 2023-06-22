package com.nekiplay.hypixelcry.config.pages.esp;

import cc.polyfrost.oneconfig.config.annotations.Checkbox;
import cc.polyfrost.oneconfig.config.annotations.Color;
import cc.polyfrost.oneconfig.config.annotations.Dropdown;
import cc.polyfrost.oneconfig.config.core.OneColor;

public class DarkMonolithMainPage {
    @Checkbox(
            name = "Dark Monolith ESP",
            description = "Render Dragon Egg in Dwarden Mines",
            category = "Dwarden Mines",
            subcategory = "General"
    )
    public boolean DwardenMinesDarkMonolithESP = true;

    @Color(
            name = "Dark Monolith color",
            description = "The color of the block dark monolith",
            category = "Dwarden Mines",
            subcategory = "Colors"
    )
    public OneColor Color = new OneColor(185, 255, 135);

    @Checkbox(
            name = "Text",
            description = "Render Text to location",
            category = "Dwarden Mines",
            subcategory = "Visuals"
    )
    public boolean Text = true;

    @Checkbox(
            name = "Tracer",
            description = "Render Tracer to location",
            category = "Dwarden Mines",
            subcategory = "Visuals"
    )
    public boolean Tracer = true;

    @Color(
            name = "Tracer Dark Monolith color",
            description = "The color of the block tracer treasure location",
            category = "Dwarden Mines",
            subcategory = "Colors"
    )
    public OneColor treasureTracerColor = new OneColor(255, 192, 92);

    @Dropdown(
            name = "Dark Monolith text color",
            description = "The color of the block dark monolith",
            category = "Dwarden Mines",
            subcategory = "Colors",
            options = { "BLACK", "DARK_BLUE", "DARK_GREEN", "DARK_AQUA", "DARK_RED", "DARK_PURPLE", "GOLD", "GRAY", "DARK_GRAY", "BLUE", "GREEN", "AQUA", "RED", "LIGHT_PURPLE", "YELLOW", "WHITE" }
    )
    public int TextColor = 5;
}

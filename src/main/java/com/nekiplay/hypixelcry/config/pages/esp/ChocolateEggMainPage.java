package com.nekiplay.hypixelcry.config.pages.esp;

import cc.polyfrost.oneconfig.config.annotations.Checkbox;
import cc.polyfrost.oneconfig.config.core.OneColor;

public class ChocolateEggMainPage {
    @Checkbox(
            name = "Chocolate Egg ESP",
            description = "Render eggs",
            category = "General",
            subcategory = "General"
    )
    public boolean EnableESP = true;

    @cc.polyfrost.oneconfig.config.annotations.Color(
            name = "Egg color",
            description = "The color of the eggs",
            category = "General",
            subcategory = "Colors"
    )
    public OneColor Color = new OneColor(255, 255, 0);

    @Checkbox(
            name = "Text",
            description = "Render text to location",
            category = "General",
            subcategory = "Visuals"
    )
    public boolean Text = true;

    @cc.polyfrost.oneconfig.config.annotations.Color(
            name = "Text color",
            description = "The color of the gifts",
            category = "General",
            subcategory = "Colors"
    )
    public OneColor TextColor = new OneColor(255, 255, 0);
}
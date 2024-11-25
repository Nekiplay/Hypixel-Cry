package com.nekiplay.hypixelcry.config.pages.esp;

import cc.polyfrost.oneconfig.config.annotations.Checkbox;
import cc.polyfrost.oneconfig.config.annotations.Color;
import cc.polyfrost.oneconfig.config.core.OneColor;

public class JerryGiftsMainPage {
    @Checkbox(
            name = "Gift ESP",
            description = "Render Gifts",
            category = "Jerry's Workshop",
            subcategory = "General"
    )
    public boolean enableESP = true;

    @cc.polyfrost.oneconfig.config.annotations.Color(
            name = "Gift color",
            description = "The color of the gifts",
            category = "Jerry's Workshop",
            subcategory = "Colors"
    )
    public OneColor color = new OneColor(255, 255, 0);

    @Checkbox(
            name = "Text",
            description = "Render Text to location",
            category = "Jerry's Workshop",
            subcategory = "Visuals"
    )
    public boolean text = true;

    @cc.polyfrost.oneconfig.config.annotations.Color(
            name = "Text color",
            description = "The color of the gifts",
            category = "Jerry's Workshop",
            subcategory = "Colors"
    )
    public OneColor textColor = new OneColor(255, 255, 0);
}

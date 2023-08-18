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
    public boolean EnableESP = true;

    @cc.polyfrost.oneconfig.config.annotations.Color(
            name = "Gift color",
            description = "The color of the gifts",
            category = "Jerry's Workshop",
            subcategory = "Colors"
    )
    public OneColor Color = new OneColor(255, 255, 0);
}

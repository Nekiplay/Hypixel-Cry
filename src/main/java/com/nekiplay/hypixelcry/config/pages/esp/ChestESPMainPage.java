package com.nekiplay.hypixelcry.config.pages.esp;

import cc.polyfrost.oneconfig.config.annotations.Checkbox;
import cc.polyfrost.oneconfig.config.core.OneColor;

public class ChestESPMainPage {
    @Checkbox(
            name = "Chest ESP",
            description = "Render Gifts",
            category = "General",
            subcategory = "General"
    )
    public boolean enableESP = true;

    @cc.polyfrost.oneconfig.config.annotations.Color(
            name = "Chest color",
            description = "The color of the chests",
            category = "General",
            subcategory = "Colors"
    )
    public OneColor color = new OneColor(255, 255, 0);
}

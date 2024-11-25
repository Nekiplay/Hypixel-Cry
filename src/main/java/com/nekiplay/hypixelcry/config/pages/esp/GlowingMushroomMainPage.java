package com.nekiplay.hypixelcry.config.pages.esp;

import cc.polyfrost.oneconfig.config.annotations.Checkbox;
import cc.polyfrost.oneconfig.config.annotations.Color;
import cc.polyfrost.oneconfig.config.core.OneColor;

public class GlowingMushroomMainPage {
    @Checkbox(
            name = "Glowing Mushroom ESP",
            description = "Render Glowing Mushrooms",
            category = "Glowing Mushroom",
            subcategory = "General"
    )
    public boolean glowingMushroomESP = true;

    @Color(
            name = "Glowing Mushroom color",
            description = "The color of the block glowing mushroom",
            category = "Glowing Mushroom",
            subcategory = "Colors"
    )
    public OneColor color = new OneColor(255, 255, 0);
}

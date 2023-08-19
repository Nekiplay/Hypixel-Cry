package com.nekiplay.hypixelcry.config.pages.esp;

import cc.polyfrost.oneconfig.config.annotations.Checkbox;
import cc.polyfrost.oneconfig.config.core.OneColor;

public class PeltMobMainPage {
    @Checkbox(
            name = "Pelt mob ESP",
            description = "Render pelt mobs",
            category = "Glowing Mushroom",
            subcategory = "General"
    )
    public boolean EnableESP = true;

    @Checkbox(
            name = "Tracer",
            description = "Render pelt mobs",
            category = "Glowing Mushroom",
            subcategory = "General"
    )
    public boolean EnableTracer = true;

    @cc.polyfrost.oneconfig.config.annotations.Color(
            name = "Pelt mob color",
            description = "The color of the gifts",
            category = "Glowing Mushroom",
            subcategory = "Colors"
    )
    public OneColor Color = new OneColor(255, 255, 0);
}

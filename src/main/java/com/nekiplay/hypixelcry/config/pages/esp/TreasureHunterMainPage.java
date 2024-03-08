package com.nekiplay.hypixelcry.config.pages.esp;

import cc.polyfrost.oneconfig.config.annotations.Checkbox;
import cc.polyfrost.oneconfig.config.annotations.Color;
import cc.polyfrost.oneconfig.config.annotations.Dropdown;
import cc.polyfrost.oneconfig.config.core.OneColor;
import net.minecraft.util.EnumChatFormatting;

import java.util.Arrays;

public class TreasureHunterMainPage {
    @Checkbox(
            name = "Treasure Location ESP",
            description = "Render Treasure location",
            category = "Treasure Hunter",
            subcategory = "General"
    )
    public boolean TreasureHunterESP = true;

    @Color(
            name = "Treasure color",
            description = "The color of the block treasure location",
            category = "Treasure Hunter",
            subcategory = "Colors"
    )
    public OneColor treasureColor = new OneColor(255, 192, 92);

    @Checkbox(
            name = "Text",
            description = "Render Text to location",
            category = "Treasure Hunter",
            subcategory = "Visuals"
    )
    public boolean Text = true;

    @Checkbox(
            name = "Tracer",
            description = "Render Tracer to location",
            category = "Treasure Hunter",
            subcategory = "Visuals"
    )
    public boolean Tracer = true;
    @Checkbox(
            name = "All locations",
            description = "Render all locations if dont know trasure location",
            category = "Treasure Hunter",
            subcategory = "Visuals"
    )
    public boolean AllLocations = true;

    @Color(
            name = "Tracer Treasure color",
            description = "The color of the block tracer treasure location",
            category = "Treasure Hunter",
            subcategory = "Colors"
    )
    public OneColor treasureTracerColor = new OneColor(255, 192, 92);
}

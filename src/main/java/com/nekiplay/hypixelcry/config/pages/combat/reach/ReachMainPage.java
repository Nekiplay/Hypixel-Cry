package com.nekiplay.hypixelcry.config.pages.combat.reach;

import cc.polyfrost.oneconfig.config.annotations.Checkbox;
import cc.polyfrost.oneconfig.config.annotations.Slider;

public class ReachMainPage {
    @Checkbox(
            name = "Enabled",
            description = "Enable reach",
            category = "Reach",
            subcategory = "General"
    )
    public boolean enabled = true;
    @Slider(
            name = "Combat Range",
            max = 16f,
            min = 3f,
            description = "Reach for attack",
            category = "Reach",
            subcategory = "General"
    )
    public float combatRange = 3.5f;
}
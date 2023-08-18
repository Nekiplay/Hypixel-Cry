package com.nekiplay.hypixelcry.config.pages.combat.noclickdelay;

import cc.polyfrost.oneconfig.config.annotations.Checkbox;

public class NoClickDelayMainPage {
    @Checkbox(
            name = "Enabled",
            description = "Remove left mouse click delay",
            category = "No Click Delay",
            subcategory = "General"
    )
    public boolean enabled = true;
}

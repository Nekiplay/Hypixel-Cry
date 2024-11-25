package com.nekiplay.hypixelcry.config.pages.qol;

import cc.polyfrost.oneconfig.config.annotations.Checkbox;

public class DungeonsMainPage {
    @Checkbox(
            name = "Auto close chests",
            description = "Auto close chests in dungeons",
            category = "General",
            subcategory = "General"
    )
    public boolean autCloseChests = false;
}

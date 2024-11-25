package com.nekiplay.hypixelcry.config.pages.esp;

import cc.polyfrost.oneconfig.config.annotations.Checkbox;

public class ResourceRespawnerMainPage {
    @Checkbox(
            name = "Enabled",
            description = "Render respawn procent",
            category = "General",
            subcategory = "General"
    )
    public boolean enableESP = true;
}

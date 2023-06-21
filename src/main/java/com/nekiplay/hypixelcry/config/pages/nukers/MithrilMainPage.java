package com.nekiplay.hypixelcry.config.pages.nukers;

import cc.polyfrost.oneconfig.config.annotations.Checkbox;
import cc.polyfrost.oneconfig.config.annotations.Dropdown;
import cc.polyfrost.oneconfig.config.annotations.Info;
import cc.polyfrost.oneconfig.config.data.InfoType;

public class MithrilMainPage {
    @Info(
            text = "This nuker not recommended for AFK",
            type = InfoType.WARNING,
            category = "Mithril nuker",
            subcategory = "General"
    )
    public static boolean ignored2; // Useless. Java limitations with @annotation.
    @Dropdown(
            name = "Find mode",        // name of the component
            description = "Exposed mode",
            options = {"Hidden", "Visible", "All"},
            category = "Mithril nuker", subcategory = "General"
    )
    public int MithrilNukerExposedMode = 2;

    @Checkbox(
            name = "Skip Titanium",
            description = "Mithril nuker will ignore titanium",
            category = "Mithril nuker",
            subcategory = "General"
    )
    public boolean MithrilNukerIgnoreTitanium = false;
}

package com.nekiplay.hypixelcry.config.pages.macros;

import cc.polyfrost.oneconfig.config.annotations.Checkbox;

public class AutoToolMacrosMainPage {
    @Checkbox(
            name = "Enabled",
            description = "Enabled Auto Tool?",
            category = "Auto Tool",
            subcategory = "General"
    )
    public boolean enabled = true;

    @Checkbox(
            name = "Ignore shit blocks",
            description = "Ignore stone, logs, and other trash?",
            category = "Auto Tool",
            subcategory = "General"
    )
    public boolean ignoreTrashBlock = true;
}

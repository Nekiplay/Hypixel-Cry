package com.nekiplay.hypixelcry.config;

import com.google.gson.annotations.Expose;
import com.nekiplay.hypixelcry.HypixelCry;
import com.nekiplay.hypixelcry.config.neupages.ESP;
import com.nekiplay.hypixelcry.config.neupages.Macros;
import com.nekiplay.hypixelcry.config.neupages.Misc;
import io.github.notenoughupdates.moulconfig.Config;
import io.github.notenoughupdates.moulconfig.annotations.Category;

public class NEUConfig extends Config {

    @Override
    public String getTitle() {
        return "§6Hypixel Cry §7v" + HypixelCry.VERSION;
    }

    @Override
    public void saveNow() {
        HypixelCry.getInstance().saveConfig();
    }

    @Expose
    @Category(
            name = "Visuals",
            desc = "World rendering features"
    )
    public ESP esp = new ESP();

    @Expose
    @Category(
            name = "Macros",
            desc = "Auto and semi-auto features"
    )
    public Macros macros = new Macros();

    @Expose
    @Category(
            name = "Misc",
            desc = "Options for sub features"
    )
    public Misc misc = new Misc();
}

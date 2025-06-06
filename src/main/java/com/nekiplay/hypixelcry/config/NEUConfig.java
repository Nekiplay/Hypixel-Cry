package com.nekiplay.hypixelcry.config;

import com.nekiplay.hypixelcry.config.neupages.Macros;
import com.nekiplay.hypixelcry.utils.Utils;
import io.github.notenoughupdates.moulconfig.Config;
import io.github.notenoughupdates.moulconfig.Social;
import io.github.notenoughupdates.moulconfig.annotations.Category;
import io.github.notenoughupdates.moulconfig.common.MyResourceLocation;

import java.util.Arrays;
import java.util.List;

public class NEUConfig extends Config {
    @Override
    public String getTitle() {
        return "§6Hypixel Cry §7v" + "1.1.1";
    }


    @Category(
            name = "Macros",
            desc = "Auto and semi-auto features"
    )
    public Macros macros = new Macros();

}

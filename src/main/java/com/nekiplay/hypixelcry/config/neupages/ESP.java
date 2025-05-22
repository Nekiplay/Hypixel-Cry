package com.nekiplay.hypixelcry.config.neupages;

import com.google.gson.annotations.Expose;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorBoolean;
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption;

public class ESP {
    @ConfigOption(
            name = "Glowing Mushrooms",
            desc = ""
    )
    @ConfigEditorBoolean
    @Expose
    public boolean glowingMushrooms = false;
}

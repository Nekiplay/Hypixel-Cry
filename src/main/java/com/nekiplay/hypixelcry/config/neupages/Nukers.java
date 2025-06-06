package com.nekiplay.hypixelcry.config.neupages;

import com.google.gson.annotations.Expose;
import io.github.notenoughupdates.moulconfig.annotations.Accordion;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorKeybind;
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption;

public class Nukers {
    @Accordion
    @ConfigOption(name = "Foraging", desc = "")
    @Expose
    public Foraging foraging = new Foraging();

    public static class Foraging {

    }
}

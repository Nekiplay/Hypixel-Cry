package com.nekiplay.hypixelcry.config.neupages;

import com.google.gson.annotations.Expose;
import io.github.notenoughupdates.moulconfig.annotations.Category;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorBoolean;
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption;

public class ESP {
    @Category(name = "Glowing Mushroom Island", desc = "Render features in glowing mushroom islands")
    @ConfigOption(
            name = "Glowing Mushrooms",
            desc = ""
    )
    @ConfigEditorBoolean
    @Expose
    public Glowing_Mushrooms glowingMushrooms = new Glowing_Mushrooms;

    @Category(name = "Dwarden Mines", desc = "Render features in dwarden mines")
    @ConfigOption(
            name = "Dark Monolith",
            desc = ""
    )
    @ConfigEditorBoolean
    @Expose
    public boolean darkMonolith = false;

    public class Glowing_Mushrooms {
        @ConfigOption(
                name = "Glowing Mushrooms",
                desc = ""
        )
        @ConfigEditorBoolean
        @Expose
        public boolean glowingMushrooms = false;

        @ConfigOption(
                name = "Treasure Hunter Fetcher",
                desc = ""
        )
        @ConfigEditorBoolean
        @Expose
        public boolean treasureHunter = false;
    }
}

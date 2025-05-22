package com.nekiplay.hypixelcry.config.neupages;

import com.google.gson.annotations.Expose;
import io.github.notenoughupdates.moulconfig.annotations.*;

public class ESP {
    @Category(name = "Glowing Mushroom Island", desc = "Render features in glowing mushroom islands")
    @ConfigOption(
            name = "Glowing Mushrooms",
            desc = ""
    )
    @Expose
    public Glowing_Mushrooms glowingMushrooms = new Glowing_Mushrooms();

    @Category(name = "Dwarden Mines", desc = "Render features in dwarden mines")
    @ConfigOption(
            name = "Dark Monolith",
            desc = ""
    )
    @Expose
    public Dwarden_Mines dwardenMines = new Dwarden_Mines();

    public class Dwarden_Mines {
        @ConfigOption(
                name = "Dark Monolith",
                desc = ""
        )
        @ConfigEditorAccordion(id = 0)
        public boolean searchAccordion = false;

        @ConfigOption(
                name = "Enable",
                desc = ""
        )
        @ConfigEditorAccordion(id = 0)
        @ConfigEditorBoolean
        @Expose
        public boolean darkMonolith = false;

        @ConfigOption(name = "Box color", desc = "ESP Box color")
        @ConfigEditorColour
        @Expose
        public String colour = "0:0:0:0:0";
    }

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

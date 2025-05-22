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
    @Expose
    public Dwarden_Mines dwardenMines = new Dwarden_Mines();

    public static class Dwarden_Mines {
        @Accordion
        @ConfigOption(
                name = "Dark Monolith",
                desc = ""
        )
        @Expose
        public Dark_Monolith darkMonolith = new Dark_Monolith();

		public static class Dark_Monolith {
            @ConfigOption(
                    name = "Enable",
                    desc = ""
            )
            @ConfigEditorBoolean
            @Expose
            public boolean enabled = false;

            @ConfigOption(name = "Box color", desc = "ESP Box color")
            @ConfigEditorColour
            @Expose
            public String colour = "0:0:0:0:0";
        }
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

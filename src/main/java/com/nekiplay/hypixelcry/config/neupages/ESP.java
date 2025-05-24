package com.nekiplay.hypixelcry.config.neupages;

import com.google.gson.annotations.Expose;
import com.nekiplay.hypixelcry.config.ESPFeatures;
import io.github.notenoughupdates.moulconfig.annotations.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ESP {

    @Accordion
    @ConfigOption(
            name = "Chest ESP",
            desc = ""
    )
    @Expose
    public Chest_ESP chestEsp = new Chest_ESP();

    @Category(name = "Desert Settlement", desc = "Render features in desert settlement")
    @Expose
    public Desert_Settlement desertSettlement = new Desert_Settlement();

	@Category(name = "Dwarden Mines", desc = "Render features in dwarden mines")
    @Expose
    public Dwarden_Mines dwardenMines = new Dwarden_Mines();

    public static class Chest_ESP {
        @ConfigOption(
                name = "Enable",
                desc = "Enable Chest ESP?"
        )
        @ConfigEditorBoolean
        @Expose
        public boolean enabled = false;

        @ConfigOption(name = "Color", desc = "ESP color")
        @ConfigEditorColour
        @Expose
        public String colour = "0:0:0:0:0";

        @ConfigOption(name = "Render features", desc = "")
        @Expose
        @ConfigEditorDraggableList(requireNonEmpty = false)
        public List<ESPFeatures> features = new ArrayList<>(Collections.singletonList(ESPFeatures.Box));
    }

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
                    desc = "Enable Dark Monolith ESP?"
            )
            @ConfigEditorBoolean
            @Expose
            public boolean enabled = false;

            @ConfigOption(name = "Color", desc = "ESP color")
            @ConfigEditorColour
            @Expose
            public String colour = "0:0:0:0:0";

            @ConfigOption(name = "Render features", desc = "")
            @Expose
            @ConfigEditorDraggableList(requireNonEmpty = false)
            public List<ESPFeatures> features = new ArrayList<>(Collections.singletonList(ESPFeatures.Box));
        }
    }

    public static class Desert_Settlement {

        @Accordion
        @ConfigOption(
                name = "Treasure Hunter Fetcher",
                desc = "Get position of treasure on desert settlement"
        )
        @Expose
        public Treasure_Hunter_Fetcher treasureHunterFetcher = new Treasure_Hunter_Fetcher();

        @Accordion
        @ConfigOption(
                name = "Glowing Mushrooms",
                desc = "Show glowing mushrooms in desert settlement"
        )
        @Expose
        public Glowing_Mushrooms glowingMushrooms = new Glowing_Mushrooms();

        public static class Treasure_Hunter_Fetcher {
            @ConfigOption(
                    name = "Enable",
                    desc = "Enable Treasure Fetcher?"
            )
            @ConfigEditorBoolean
            @Expose
            public boolean enabled = false;

            @ConfigOption(name = "Color", desc = "ESP color")
            @ConfigEditorColour
            @Expose
            public String colour = "0:0:0:0:0";

            @ConfigOption(name = "Render features", desc = "")
            @Expose
            @ConfigEditorDraggableList
            public List<ESPFeatures> features = new ArrayList<>(Collections.singletonList(ESPFeatures.Box));
        }

        public static class Glowing_Mushrooms {
            @ConfigOption(
                    name = "Enable",
                    desc = "Enable Glowing Mushrooms ESP?"
            )
            @ConfigEditorBoolean
            @Expose
            public boolean enabled = false;

            @ConfigOption(name = "Color", desc = "ESP color")
            @ConfigEditorColour
            @Expose
            public String colour = "0:0:0:0:0";

            @ConfigOption(name = "Render features", desc = "")
            @Expose
            @ConfigEditorDraggableList
            public List<ESPFeatures> features = new ArrayList<>(Collections.singletonList(ESPFeatures.Box));
        }
    }
}

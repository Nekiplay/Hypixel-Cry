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

    public static class Chest_ESP {
        @ConfigOption(
                name = "Enable",
                desc = "Enable Chest ESP?"
        )
        @ConfigEditorBoolean
        @Expose
        public boolean enabled = false;

        @ConfigOption(name = "Range", desc = "Max range, 0 = unlimited")
        @ConfigEditorSlider(minValue = 0, maxValue = 50, minStep = 1)
        @Expose
        public int maxRange = 0;

        @ConfigOption(name = "Color", desc = "ESP color")
        @ConfigEditorColour
        @Expose
        public String colour = "0:255:255:0:13";

        @ConfigOption(name = "Render features", desc = "")
        @Expose
        @ConfigEditorDraggableList(requireNonEmpty = false)
        public List<ESPFeatures> features = new ArrayList<>(Collections.singletonList(ESPFeatures.Box));
    }

    @Category(name = "Desert Settlement", desc = "Render features in desert settlement")
    @Expose
    public Desert_Settlement desertSettlement = new Desert_Settlement();

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
            public String colour = "0:255:64:255:30";

            @ConfigOption(name = "Render features", desc = "")
            @Expose
            @ConfigEditorDraggableList
            public List<ESPFeatures> features = new ArrayList<>(Collections.singletonList(ESPFeatures.Box));
        }
    }

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
                    desc = "Enable Dark Monolith ESP?"
            )
            @ConfigEditorBoolean
            @Expose
            public boolean enabled = false;

            @ConfigOption(name = "Color", desc = "ESP color")
            @ConfigEditorColour
            @Expose
            public String colour = "0:255:131:0:255";

            @ConfigOption(name = "Render features", desc = "")
            @Expose
            @ConfigEditorDraggableList(requireNonEmpty = false)
            public List<ESPFeatures> features = new ArrayList<>(Collections.singletonList(ESPFeatures.Box));
        }
    }

    @Category(name = "Crystal Hollows", desc = "Render features in crystall hollows")
    @Expose
    public Crystal_Hollows crystalHollows = new Crystal_Hollows();

    public static class Crystal_Hollows {
        @Accordion
        @ConfigOption(
                name = "Yog ESP",
                desc = ""
        )
        @Expose
        public Yog yog = new Yog();

        public static class Yog {
            @ConfigOption(
                    name = "Enable",
                    desc = "Enable Yog ESP?"
            )
            @ConfigEditorBoolean
            @Expose
            public boolean enabled = false;

            @ConfigOption(name = "Color", desc = "ESP color")
            @ConfigEditorColour
            @Expose
            public String colour = "0:255:255:19:0";

            @ConfigOption(name = "Render features", desc = "")
            @Expose
            @ConfigEditorDraggableList(requireNonEmpty = false)
            public List<ESPFeatures> features = new ArrayList<>(Collections.singletonList(ESPFeatures.Box));
        }
		
		@Accordion
        @ConfigOption(
                name = "Automaton ESP",
                desc = ""
        )
        @Expose
        public Automaton automaton = new Automaton();

        public static class Automaton {
            @ConfigOption(
                    name = "Enable",
                    desc = "Enable Authomaton ESP?"
            )
            @ConfigEditorBoolean
            @Expose
            public boolean enabled = false;

            @ConfigOption(name = "Color", desc = "ESP color")
            @ConfigEditorColour
            @Expose
            public String colour = "0:255:255:255:255";

            @ConfigOption(name = "Render features", desc = "")
            @Expose
            @ConfigEditorDraggableList(requireNonEmpty = false)
            public List<ESPFeatures> features = new ArrayList<>(Collections.singletonList(ESPFeatures.Box));
        }

        @Accordion
        @ConfigOption(
                name = "Jungle Temple PathFinder",
                desc = ""
        )
        @Expose
        public PathFinder pathFinder = new PathFinder();

        public static class PathFinder {
            @ConfigOption(
                    name = "Enable",
                    desc = "Enable Jungle Temple path finder?"
            )
            @ConfigEditorBoolean
            @Expose
            public boolean enabledJungleTemple = false;
        }
    }

    @Category(name = "Glacite Mineshafts", desc = "Render features in glacite mineshafts")
    @Expose
    public Glacite_Mineshafts glaciteMineshafts = new Glacite_Mineshafts();

    public static class Glacite_Mineshafts {
        @Accordion
        @ConfigOption(
                name = "Frozen Courpes ESP",
                desc = ""
        )
        @Expose
        public Frozen_Courpes frozenCourpes = new Frozen_Courpes();

        public static class Frozen_Courpes {
            @ConfigOption(
                    name = "Enable",
                    desc = "Enable Frozen Courpes ESP?"
            )
            @ConfigEditorBoolean
            @Expose
            public boolean enabled = true;

            @ConfigOption(name = "Color", desc = "ESP color")
            @ConfigEditorColour
            @Expose
            public String colour = "0:255:25:6:255";

            @ConfigOption(name = "Render features", desc = "")
            @Expose
            @ConfigEditorDraggableList(requireNonEmpty = false)
            public List<ESPFeatures> features = new ArrayList<>(Collections.singletonList(ESPFeatures.Box));

            @ConfigOption(
                    name = "Enable PathFinder",
                    desc = "Enable Frozen Courpes PathFinder?"
            )
            @ConfigEditorBoolean
            @Expose
            public boolean enabledPathFinder = true;

            @ConfigOption(name = "PathFinder type", desc = "Render path to nearest or all courpes")
            @Expose
            @ConfigEditorDropdown()
            public Priority priority = Priority.Nearest;

            public enum Priority {
                Nearest, All;

                @Override
                public String toString() {
                    return name();
                }
            }
        }
    }

    @Accordion
    @ConfigOption(
            name = "PathFinder",
            desc = ""
    )
    @Expose
    public PathFinderESP pathFinderESP = new PathFinderESP();

    public static class PathFinderESP {
        @ConfigOption(
                name = "Enable",
                desc = "Enable render PathFinder?"
        )
        @ConfigEditorBoolean
        @Expose
        public boolean enabled = true;

        @ConfigOption(
                name = "Enable",
                desc = "Enable render sub points?"
        )
        @ConfigEditorBoolean
        @Expose
        public boolean enableSubPoints = false;
    }
}

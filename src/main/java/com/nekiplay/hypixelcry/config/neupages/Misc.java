package com.nekiplay.hypixelcry.config.neupages;

import com.google.gson.annotations.Expose;
import io.github.notenoughupdates.moulconfig.annotations.Accordion;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorBoolean;
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption;

public class Misc {
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
                desc = "Enable PathFinder features?"
        )
        @ConfigEditorBoolean
        @Expose
        public boolean enabled = true;
    }

    @Accordion
    @ConfigOption(
            name = "Debug",
            desc = ""
    )
    @Expose
    public Debug debug = new Debug();

    public static class Debug {
        @ConfigOption(
                name = "Enable",
                desc = "Enable debug?"
        )
        @ConfigEditorBoolean
        @Expose
        public boolean enabled = false;
    }
}

package com.nekiplay.hypixelcry.config.neupages;

import com.google.gson.annotations.Expose;
import com.nekiplay.hypixelcry.config.enums.ESPFeatures;
import com.nekiplay.hypixelcry.config.enums.PathFinderPriority;
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

        @ConfigOption(name = "Features", desc = "Render features")
        @Expose
        @ConfigEditorDraggableList(requireNonEmpty = false)
        public List<ESPFeatures> features = new ArrayList<>(Collections.singletonList(ESPFeatures.Box));
    }
}

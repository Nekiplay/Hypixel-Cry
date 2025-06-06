package com.nekiplay.hypixelcry.config.neupages;

import com.nekiplay.hypixelcry.config.enums.AutoRightClickBlocks;
import com.nekiplay.hypixelcry.config.enums.AutoRightClickOpenFeatures;
import io.github.notenoughupdates.moulconfig.annotations.*;
import java.util.ArrayList;
import java.util.List;

public class Macros {
    @Accordion
    @ConfigOption(name = "Auto RightClick", desc = "Auto click to selected blocks")
    public AutoRightClick autoRightClick = new AutoRightClick();

    public static class AutoRightClick {
        @ConfigOption(name = "Enabled", desc = "Enable RightClick?")
        @ConfigEditorBoolean()
        public boolean enabled = false;

        @ConfigOption(name = "Blocks", desc = "Click for blocks")
        @ConfigEditorDraggableList(requireNonEmpty = false)
        public List<AutoRightClickBlocks> blocks = new ArrayList<AutoRightClickBlocks>() {{
            add(AutoRightClickBlocks.Chest);
            add(AutoRightClickBlocks.Lever);
        }};

        @ConfigOption(name = "Features", desc = "Additional features")
        @ConfigEditorDraggableList(requireNonEmpty = false)
        public List<AutoRightClickOpenFeatures> features = new ArrayList<AutoRightClickOpenFeatures>() {{
            add(AutoRightClickOpenFeatures.Air);
            add(AutoRightClickOpenFeatures.GhostHand);
        }};

        @ConfigOption(name = "Rotation time", desc = "")
        @ConfigEditorSlider(minValue = 25, maxValue = 750, minStep = 25)
        public int rotationTime = 100;
    }
}

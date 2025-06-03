package com.nekiplay.hypixelcry.config.neupages;

import com.google.gson.annotations.Expose;
import com.nekiplay.hypixelcry.config.enums.AutoRightClickBlocks;
import com.nekiplay.hypixelcry.config.enums.AutoRightClickOpenFeatures;
import com.nekiplay.hypixelcry.data.island.IslandType;
import io.github.notenoughupdates.moulconfig.annotations.*;
import org.lwjgl.input.Keyboard;
import java.util.ArrayList;
import java.util.List;

public class Macros {
    @Accordion
    @ConfigOption(name = "Auto RightClick", desc = "Auto click to selected blocks")
    @Expose
    public AutoRightClick autoRightClick = new AutoRightClick();

    public static class AutoRightClick {
        @ConfigOption(name = "Enabled", desc = "Enable RightClick?")
        @ConfigEditorBoolean()
        @Expose
        public boolean enabled = false;

        @ConfigOption(name = "Islands", desc = "Allow islands for working")
        @Expose
        @ConfigEditorDraggableList(requireNonEmpty = false)
        public List<IslandType> allowedIslands = new ArrayList<IslandType>() {{
            add(IslandType.Catacombs);
            add(IslandType.Crystal_Hollows);
        }};

        @ConfigOption(name = "Blocks", desc = "Click for blocks")
        @Expose
        @ConfigEditorDraggableList(requireNonEmpty = false)
        public List<AutoRightClickBlocks> blocks = new ArrayList<AutoRightClickBlocks>() {{
            add(AutoRightClickBlocks.Chest);
            add(AutoRightClickBlocks.Lever);
        }};

        @ConfigOption(name = "Features", desc = "Additional features")
        @Expose
        @ConfigEditorDraggableList(requireNonEmpty = false)
        public List<AutoRightClickOpenFeatures> features = new ArrayList<AutoRightClickOpenFeatures>() {{
            add(AutoRightClickOpenFeatures.Air);
            add(AutoRightClickOpenFeatures.GhostHand);
        }};

        @ConfigOption(name = "Rotation time", desc = "")
        @ConfigEditorSlider(minValue = 25, maxValue = 750, minStep = 25)
        @Expose
        public int rotationTime = 100;
    }

    @Accordion
    @ConfigOption(name = "Ghost Blocks", desc = "Set looking blocks to air")
    @Expose
    public Ghost_Blocks ghostBlocks = new Ghost_Blocks();

    public static class Ghost_Blocks {
        @ConfigOption(name = "Key binding", desc = "Activation key bind")
        @ConfigEditorKeybind(defaultKey = Keyboard.KEY_NONE)
        @Expose
        public int ghostBlocksKeyBind = Keyboard.KEY_NONE;

        @ConfigOption(name = "Range", desc = "Raycast range")
        @ConfigEditorSlider(minValue = 2, maxValue = 32, minStep = 1)
        @Expose
        public int range = 8;
    }

    @Category(name = "Dungeons", desc = "Macros in dungeons")
    @Expose
    public Dungeons dungeons = new Dungeons();

    public static class Dungeons {
        @Accordion
        @ConfigOption(name = "Auto chest close", desc = "Auto close dungeon chests")
        @Expose
        public Auto_Close_Chests autoCloseChests = new Auto_Close_Chests();

        public static class Auto_Close_Chests {
            @ConfigOption(name = "Auto close chests", desc = "Auto close dungeon chests?")
            @ConfigEditorBoolean()
            @Expose
            public boolean enable = true;
        }
    }
}

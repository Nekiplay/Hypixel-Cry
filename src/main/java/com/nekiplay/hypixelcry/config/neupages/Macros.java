package com.nekiplay.hypixelcry.config.neupages;

import com.google.gson.annotations.Expose;
import io.github.notenoughupdates.moulconfig.annotations.*;
import org.lwjgl.input.Keyboard;

public class Macros {
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

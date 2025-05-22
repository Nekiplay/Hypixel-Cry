package com.nekiplay.hypixelcry.config.neupages;

import com.google.gson.annotations.Expose;
import io.github.notenoughupdates.moulconfig.annotations.Category;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorBoolean;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorKeybind;
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption;
import io.github.notenoughupdates.moulconfig.observer.Property;
import org.lwjgl.input.Keyboard;

public class Macros {
    @Category(name = "Ghost Blocks", desc = "Remove blocks in world")
    @ConfigOption(name = "Key Binding", desc = "Key bind")
    @Expose
    public Ghost_Blocks ghostBlocks = new Ghost_Blocks();

    public class Ghost_Blocks {
        @ConfigOption(name = "Key Binding", desc = "Key bind")
        @ConfigEditorKeybind(defaultKey = Keyboard.KEY_NONE)
        @Expose
        public int ghostBlocksKeyBind = Keyboard.KEY_NONE;
    }
}

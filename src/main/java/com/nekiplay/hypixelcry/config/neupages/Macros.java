package com.nekiplay.hypixelcry.config.neupages;

import com.google.gson.annotations.Expose;
import io.github.notenoughupdates.moulconfig.annotations.Category;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorKeybind;
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption;
import org.lwjgl.input.Keyboard;

public class Macros {
    @Category(name = "Ghost Blocks", desc = "Remove blocks in world")
    @ConfigOption(name = "Key Binding", desc = "Key Binding")
    @ConfigEditorKeybind(defaultKey = Keyboard.KEY_NONE)
    @Expose
    public int ghostBlocksKeyBind = Keyboard.KEY_NONE;
}

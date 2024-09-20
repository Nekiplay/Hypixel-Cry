package com.nekiplay.hypixelcry.config.pages.macros;

import cc.polyfrost.oneconfig.config.annotations.Checkbox;
import cc.polyfrost.oneconfig.config.annotations.KeyBind;
import cc.polyfrost.oneconfig.config.core.OneKeyBind;
import org.lwjgl.input.Keyboard;

public class GhostBlocksMacrosPage {
    @KeyBind(
            name = "KeyBind", category = "Ghost Blocks", subcategory = "General",
            description = "Use the macro", size = 2
    )
    public OneKeyBind toggleMacro = new OneKeyBind(Keyboard.KEY_NONE);
}

package com.nekiplay.hypixelcry.config.pages.macros;

import cc.polyfrost.oneconfig.config.annotations.KeyBind;
import cc.polyfrost.oneconfig.config.core.OneKeyBind;
import org.lwjgl.input.Keyboard;

public class RogueSwordMacrosMainPage {
    @KeyBind(
            name = "KeyBind", category = "Rogue Sword", subcategory = "General",
            description = "Use the macro", size = 2
    )
    public OneKeyBind toggleMacro = new OneKeyBind(Keyboard.KEY_NONE);
}

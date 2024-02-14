package com.nekiplay.hypixelcry.config.pages.macros;

import cc.polyfrost.oneconfig.config.annotations.KeyBind;
import cc.polyfrost.oneconfig.config.core.OneKeyBind;
import org.lwjgl.input.Keyboard;

public class WandOfHealingMacrosMainPage {
    @KeyBind(
            name = "KeyBind", category = "Wand of healing", subcategory = "General",
            description = "Use the macro", size = 2
    )
    public OneKeyBind toggleMacro = new OneKeyBind(Keyboard.KEY_NONE);
}

package com.nekiplay.hypixelcry.config.pages.combat.autoclicker;

import cc.polyfrost.oneconfig.config.annotations.Checkbox;
import cc.polyfrost.oneconfig.config.annotations.KeyBind;
import cc.polyfrost.oneconfig.config.annotations.Slider;
import cc.polyfrost.oneconfig.config.core.OneKeyBind;
import org.lwjgl.input.Keyboard;

public class AutoClickerMainPage {
    @KeyBind(
            name = "KeyBind", category = "AutoClicker", subcategory = "General",
            description = "Toggles the macro on/off", size = 2
    )
    public OneKeyBind toggleMacro = new OneKeyBind(Keyboard.KEY_NONE);
    @Slider(
            name = "CPS",
            step = 1,
            max = 28,
            min = 1,
            description = "Click Per Second",
            category = "AutoClicker",
            subcategory = "General"
    )
    public int cps = 11;

    @Slider(
            name = "Randomization",
            step = 1,
            max = 128,
            min = 0,
            description = "Click Per Second",
            category = "AutoClicker",
            subcategory = "General"
    )
    public int randomization = 64;

    @Checkbox(
            name = "Only on Weapon",
            description = "Work only if equip weapon",
            category = "AutoClicker",
            subcategory = "General"
    )
    public boolean onlyOnWeapon = true;
}

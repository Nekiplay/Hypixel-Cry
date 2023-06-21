package com.nekiplay.hypixelcry.config.pages.combat;

import cc.polyfrost.oneconfig.config.annotations.Slider;

public class AutoClickerMainPage {
    @Slider(
            name = "CPS",
            step = 1,
            max = 28,
            min = 1,
            description = "Click Per Second",
            category = "AutoClicker",
            subcategory = "General"
    )
    public int CPS = 16;
}

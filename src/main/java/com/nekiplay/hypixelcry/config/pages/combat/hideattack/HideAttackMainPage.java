package com.nekiplay.hypixelcry.config.pages.combat.hideattack;

import cc.polyfrost.oneconfig.config.annotations.Checkbox;
import cc.polyfrost.oneconfig.config.annotations.Slider;

public class HideAttackMainPage {
    @Checkbox(
            name = "Enabled",
            description = "Ghost switch weapon to hand",
            category = "Hide Attack",
            subcategory = "General"
    )
    public boolean HideAttack = true;

    @Slider(
            name = "Weapon slot",
            step = 1,
            max = 9,
            min = 1,
            description = "Weapon hotbar slot",
            category = "Hide Attack",
            subcategory = "General"
    )
    public int HideAttackWeaponSlot = 1;

    @Checkbox(
            name = "Check hurt time",
            description = "Dont swap item if entity has damage resistant",
            category = "Hide Attack",
            subcategory = "General"
    )
    public boolean CheckHurtTime = true;

    @Slider(
            name = "Minimum hurt time",
            step = 1,
            max = 20,
            min = 1,
            description = "Minimum hurt time for swap item",
            category = "Hide Attack",
            subcategory = "General"
    )
    public int MaximumHurtTime = 12;
}

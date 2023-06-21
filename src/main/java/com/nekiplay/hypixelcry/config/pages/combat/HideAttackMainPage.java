package com.nekiplay.hypixelcry.config.pages.combat;

import cc.polyfrost.oneconfig.config.annotations.Checkbox;
import cc.polyfrost.oneconfig.config.annotations.Number;

public class HideAttackMainPage {
    @Checkbox(
            name = "Hide Attack",
            description = "Ghost switch weapon to hand",
            category = "Hide Attack",
            subcategory = "General"
    )
    public boolean HideAttack = true;

    @Number(
            name = "Weapon slot",
            step = 1,
            max = 9,
            min = 1,
            description = "Weapon hotbar slot",
            category = "Hide Attack",
            subcategory = "General"
    )
    public int HideAttackWeaponSlot = 1;
}

package com.nekiplay.hypixelcry.config.pages.nukers;

import cc.polyfrost.oneconfig.config.annotations.Checkbox;
import cc.polyfrost.oneconfig.config.annotations.Color;
import cc.polyfrost.oneconfig.config.annotations.Info;
import cc.polyfrost.oneconfig.config.annotations.Slider;
import cc.polyfrost.oneconfig.config.core.OneColor;
import cc.polyfrost.oneconfig.config.data.InfoType;

public class CropMainPage {
    @Info(
            text = "This nuker for making money and easy for Farming exp",
            type = InfoType.INFO,
            category = "Crop nuker",
            subcategory = "General"
    )
    public static boolean ignored; // Useless. Java limitations with @annotation.
    @Info(
            text = "This nuker not recommended for AFK in public hubs",
            type = InfoType.WARNING,
            category = "Crop nuker",
            subcategory = "General"

    )
    public static boolean ignored2; // Useless. Java limitations with @annotation.
    @Checkbox(
            name = "Replanish support",
            description = "Better work replanish",
            category = "Crop nuker",
            subcategory = "General"
    )
    public boolean CropNukerReplanish = true;

    @Checkbox(
            name = "Only mathematical hoe",
            description = "Breaks only plants that are intended for your hoe and dicer",
            category = "Crop nuker",
            subcategory = "General"
    )
    public boolean CropNukerOnlyMathematicalHoe = false;

    @Slider(
            name = "Max boost",
            step = 1,
            min = 1.0F,
            max = 8.0F,
            description = "Boost max block per tick speed",
            category = "Crop nuker",
            subcategory = "General"
    )
    public int CropNukerBlockPesTick = 4;

    @Slider(
            name = "Boost every ticks",
            step = 1,
            min = 0.0F,
            max = 64.0F,
            description = "Boost delay",
            category = "Crop nuker",
            subcategory = "General"
    )
    public int CropNukerBoostTicks = 13;

    @Checkbox(
            name = "Remover",
            description = "Remove planted crops",
            category = "Crop nuker",
            subcategory = "General"
    )
    public boolean CropNukerRemover = false;

    @Color(
            name = "Wheat color",
            description = "The color of the wheat",
            category = "Crop nuker",
            subcategory = "Colors"
    )
    public OneColor wheatColor = new OneColor(255, 248, 204);

    @Color(
            name = "Carrot color",
            description = "The color of the carrots",
            category = "Crop nuker",
            subcategory = "Colors"
    )
    public OneColor carrotColor = new OneColor(149, 255, 122);

    @Color(
            name = "Potatoes color",
            description = "The color of the potatoes",
            category = "Crop nuker",
            subcategory = "Colors"
    )
    public OneColor potatoesColor = new OneColor(255, 235, 105);
    @Color(
            name = "Reeds color",
            description = "The color of the reeds",
            category = "Crop nuker",
            subcategory = "Colors"
    )
    public OneColor reedsColor = new OneColor(149, 255, 122);
    @Color(
            name = "Cactus color",
            description = "The color of the cactus",
            category = "Crop nuker",
            subcategory = "Colors"
    )
    public OneColor cactusColor = new OneColor(149, 255, 122);


    @Color(
            name = "Melon color",
            description = "The color of the melon",
            category = "Crop nuker",
            subcategory = "Colors"
    )
    public OneColor melonColor = new OneColor(149, 255, 122);

    @Color(
            name = "Pumpkin color",
            description = "The color of the pumpkin",
            category = "Crop nuker",
            subcategory = "Colors"
    )
    public OneColor pumpkinColor = new OneColor(255, 235, 105);

    @Color(
            name = "Cocoa color",
            description = "The color of the cocoa",
            category = "Crop nuker",
            subcategory = "Colors"
    )
    public OneColor cocoaColor = new OneColor(117, 70, 0);

    @Color(
            name = "Nether wart color",
            description = "The color of the nether warts",
            category = "Crop nuker",
            subcategory = "Colors"
    )
    public OneColor netherWartsColor = new OneColor(117, 0, 18);
}
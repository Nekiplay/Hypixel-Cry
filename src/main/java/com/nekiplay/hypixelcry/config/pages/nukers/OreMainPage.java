package com.nekiplay.hypixelcry.config.pages.nukers;

import cc.polyfrost.oneconfig.config.annotations.*;
import cc.polyfrost.oneconfig.config.core.OneColor;
import cc.polyfrost.oneconfig.config.core.OneKeyBind;
import cc.polyfrost.oneconfig.config.data.InfoType;
import org.lwjgl.input.Keyboard;

public class OreMainPage {
    @Info(
            text = "This nuker not recommended for AFK",
            type = InfoType.WARNING,
            category = "Ore nuker",
            subcategory = "General"
    )
    public static boolean ignored2; // Useless. Java limitations with @annotation.
    @KeyBind(
            name = "KeyBind", category = "Ore nuker", subcategory = "General",
            description = "Toggles the macro on/off", size = 2
    )
    public OneKeyBind toggleMacro = new OneKeyBind(Keyboard.KEY_NONE);

    @Slider(
            name = "Horizontal distance",
            description = "Maximum horizontal distance",
            category = "Ore nuker",
            subcategory = "General",
            max = 5.4f,
            min = 1f
    )
    public float MaximumNukerHorizontalDistance = 5.4f;

    @Slider(
            name = "Verical distance",
            description = "Maximum vertical distance",
            category = "Ore nuker",
            subcategory = "General",
            max = 7.5f,
            min = 1f
    )
    public float MaximumNukerVericalDistance = 7.5f;
    @DualOption(
            name = "Break mode",
            description = "Mining mode",
            category = "Ore nuker",
            subcategory = "General",
            left = "Default",
            right = "Instant"
            //options = {"Default", "Instant"}
    )
    public boolean  OreNukerMode = false;

    @Dropdown(
            name = "Find mode",
            description = "Exposed mode",
            category = "Ore nuker",
            subcategory = "General",
            options = {"Hidden", "Visible", "All"}
    )
    public int OreNukerExposedMode = 2;
    @Checkbox(
            name = "Coal",
            description = "Mine coal",
            category = "Ore nuker",
            subcategory = "Blocks"
    )
    public boolean OreNukerCoal = true;

    @Checkbox(
            name = "Iron",
            description = "Mine iron",
            category = "Ore nuker",
            subcategory = "Blocks"
    )
    public boolean OreNukerIron = true;

    @Checkbox(
            name = "Gold",
            description = "Mine gold",
            category = "Ore nuker",
            subcategory = "Blocks"
    )
    public boolean OreNukerGold = true;

    @Checkbox(
            name = "Lapis",
            description = "Mine lapis",
            category = "Ore nuker",
            subcategory = "Blocks"
    )
    public boolean OreNukerLapis = true;

    @Checkbox(
            name = "Redstone",
            description = "Mine redstone",
            category = "Ore nuker",
            subcategory = "Blocks"
    )
    public boolean OreNukerRedstone = true;

    @Checkbox(
            name = "Emerald",
            description = "Mine emeralds",
            category = "Ore nuker",
            subcategory = "Blocks"
    )
    public boolean OreNukerEmerald = true;

    @Checkbox(
            name = "Diamond",
            description = "Mine diamonds",
            category = "Ore nuker",
            subcategory = "Blocks"
    )
    public boolean OreNukerDiamond = true;

    @Checkbox(
            name = "Stone",
            description = "Mine stone",
            category = "Ore nuker",
            subcategory = "Blocks"
    )

    public boolean OreNukerStone = false;

    @Checkbox(
            name = "Cobblestone",
            description = "Mine cobblestone",
            category = "Ore nuker",
            subcategory = "Blocks"
    )
    public boolean OreNukerCobblestone = false;

    @Checkbox(
            name = "End stone",
            description = "Mine end stone",
            category = "Ore nuker",
            subcategory = "Blocks"
    )
    public boolean OreNukerEndstone = false;

    @Checkbox(
            name = "Obsidian",
            description = "Mine Obsidian",
            category = "Ore nuker",
            subcategory = "Blocks"
    )
    public boolean OreNukerObsidian = false;

    @Checkbox(
            name = "Ice",
            description = "Mine ice",
            category = "Ore nuker",
            subcategory = "Blocks"
    )
    public boolean OreNukerIce = false;

    @Checkbox(
            name = "Tracer",
            description = "Render Tracer to break location",
            category = "Ore nuker",
            subcategory = "Visuals"
    )
    public boolean Tracer = true;

    @Color(
            name = "Coal color",
            description = "The color of the coal block and coal ore",
            category = "Ore nuker",
            subcategory = "Colors"
    )
    public OneColor coalColor = new OneColor(99, 99, 99);

    @Color(
            name = "Iron color",
            description = "The color of the iron block and iron ore",
            category = "Ore nuker",
            subcategory = "Colors"
    )
    public OneColor ironColor = new OneColor(255, 255, 255);

    @Color(
            name = "Gold color",
            description = "The color of the gold block and gold ore",
            category = "Ore nuker",
            subcategory = "Colors"
    )
    public OneColor goldColor = new OneColor(251, 255, 0);

    @Color(
            name = "Lapis color",
            description = "The color of the lapis block and lapis ore",
            category = "Ore nuker",
            subcategory = "Colors"
    )
    public OneColor lapisColor = new OneColor(0, 48, 143);

    @Color(
            name = "Redstone color",
            description = "The color of the redstone block and redstone ore",
            category = "Ore nuker",
            subcategory = "Colors"
    )
    public OneColor redstoneColor = new OneColor(110, 0, 0);

    @Color(
            name = "Emerald color",
            description = "The color of the emerald block and emerald ore",
            category = "Ore nuker",
            subcategory = "Colors"
    )
    public OneColor emeraldColor = new OneColor(50, 168, 0);

    @Color(
            name = "Diamond color",
            description = "The color of the diamond block and diamond ore",
            category = "Ore nuker",
            subcategory = "Colors"
    )
    public OneColor diamondColor = new OneColor(0, 227, 227);

    @Color(
            name = "Stone color",
            description = "The color of the stone",
            category = "Ore nuker",
            subcategory = "Colors"
    )
    public OneColor stoneColor = new OneColor(82, 82, 82);

    @Color(
            name = "Cobblestone color",
            description = "The color of the cobblestone",
            category = "Ore nuker",
            subcategory = "Colors"
    )
    public OneColor cobblestoneColor = new OneColor(82, 82, 82);

    @Color(
            name = "End stone color",
            description = "The color of the end stone",
            category = "Ore nuker",
            subcategory = "Colors"
    )
    public OneColor endStoneColor = new OneColor(244, 255, 181);

    @Color(
            name = "Obsidian color",
            description = "The color of the obsidian",
            category = "Ore nuker",
            subcategory = "Colors"
    )
    public OneColor obsidianColor = new OneColor(13, 15, 2);

    @Color(
            name = "Ice color",
            description = "The color of the ice",
            category = "Ore nuker",
            subcategory = "Colors"
    )
    public OneColor iceColor = new OneColor(150, 211, 255);
}

package com.nekiplay.hypixelcry;

import com.nekiplay.hypixelcry.config.CustomRenderer;
import com.nekiplay.hypixelcry.features.esp.*;
import com.nekiplay.hypixelcry.features.macros.AutoTool;
import com.nekiplay.hypixelcry.features.macros.RogueSword;
import com.nekiplay.hypixelcry.features.macros.WandofHealing;
import com.nekiplay.hypixelcry.features.nuker.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

public class FeatureRegister {
    public static WandofHealing wandOfHealingMacros = new WandofHealing();
    public static RogueSword rogueSwordMacros = new RogueSword();
    public static AutoTool autoToolMacros = new AutoTool();

    public static Crop cropNuker = new Crop();
    public static Foraging foragingNuker = new Foraging();
    public static GardenMiner gardenNuker = new GardenMiner();
    public static Mithril mithrilNuker = new Mithril();
    public static Ore oreNuker = new Ore();
    public static Sand sandNuker = new Sand();
    public void register(FMLInitializationEvent event) {
        /* Events */
        MinecraftForge.EVENT_BUS.register(new CustomRenderer());

        /* Macros */
        MinecraftForge.EVENT_BUS.register(new RogueSword());
		MinecraftForge.EVENT_BUS.register(new AutoClicker());

        MinecraftForge.EVENT_BUS.register(cropNuker);
        MinecraftForge.EVENT_BUS.register(foragingNuker);
        MinecraftForge.EVENT_BUS.register(oreNuker);
        MinecraftForge.EVENT_BUS.register(mithrilNuker);
        MinecraftForge.EVENT_BUS.register(sandNuker);
        MinecraftForge.EVENT_BUS.register(gardenNuker);


        /* ESP */
        MinecraftForge.EVENT_BUS.register(new ResourceRespawnerESP());
        MinecraftForge.EVENT_BUS.register(new ChestESP());
        MinecraftForge.EVENT_BUS.register(new Treasure_Hunter());
        MinecraftForge.EVENT_BUS.register(new Dark_Monolith());
        MinecraftForge.EVENT_BUS.register(new Glowing_Mushroom());
        MinecraftForge.EVENT_BUS.register(new PeltMobEsp());
        MinecraftForge.EVENT_BUS.register(new Gifts());
    }
}

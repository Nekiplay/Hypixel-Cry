package com.nekiplay.hypixelcry;

import com.nekiplay.hypixelcry.auto.HideAttack;
import com.nekiplay.hypixelcry.auto.RemoteAccess;
import com.nekiplay.hypixelcry.config.CustomRenderer;
import com.nekiplay.hypixelcry.esp.Dark_Monolith;
import com.nekiplay.hypixelcry.esp.Glowing_Mushroom;
import com.nekiplay.hypixelcry.esp.Treasure_Hunter;
import com.nekiplay.hypixelcry.macros.AspectoftheEnd;
import com.nekiplay.hypixelcry.macros.AutoClicker;
import com.nekiplay.hypixelcry.macros.RogueSword;
import com.nekiplay.hypixelcry.macros.WandofHealing;
import com.nekiplay.hypixelcry.nuker.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

public class MacrosRegister {
    public void register(FMLInitializationEvent event) {
        /* Events */
        MinecraftForge.EVENT_BUS.register(new CustomRenderer());

        /* Macros */
        MinecraftForge.EVENT_BUS.register(new RogueSword());
        MinecraftForge.EVENT_BUS.register(new WandofHealing());
        MinecraftForge.EVENT_BUS.register(new AspectoftheEnd());

        /* ESP */
        MinecraftForge.EVENT_BUS.register(new Treasure_Hunter());
        MinecraftForge.EVENT_BUS.register(new Dark_Monolith());
        MinecraftForge.EVENT_BUS.register(new Glowing_Mushroom());

        /* Nuker */
        MinecraftForge.EVENT_BUS.register(new Mithril());
        MinecraftForge.EVENT_BUS.register(new Foraging());
        MinecraftForge.EVENT_BUS.register(new Ore());
        MinecraftForge.EVENT_BUS.register(new Crop());
        MinecraftForge.EVENT_BUS.register(new Sand());
        MinecraftForge.EVENT_BUS.register(new GardenMiner());

        MinecraftForge.EVENT_BUS.register(new RemoteAccess());
        MinecraftForge.EVENT_BUS.register(new HideAttack());
        MinecraftForge.EVENT_BUS.register(new AutoClicker());
    }
}
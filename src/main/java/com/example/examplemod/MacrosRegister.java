package com.example.examplemod;

import com.example.examplemod.auto.HideAttack;
import com.example.examplemod.auto.RemoteAccess;
import com.example.examplemod.esp.Dark_Monolith;
import com.example.examplemod.esp.Glowing_Mushroom;
import com.example.examplemod.esp.Treasure_Hunter;
import com.example.examplemod.macros.AspectoftheEnd;
import com.example.examplemod.macros.AutoClicker;
import com.example.examplemod.macros.RogueSword;
import com.example.examplemod.macros.WandofHealing;
import com.example.examplemod.nuker.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

public class MacrosRegister {
    public void register(FMLInitializationEvent event) {
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

        MinecraftForge.EVENT_BUS.register(new RemoteAccess());
        MinecraftForge.EVENT_BUS.register(new HideAttack());
        MinecraftForge.EVENT_BUS.register(new AutoClicker());
    }
}

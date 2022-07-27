package com.example.examplemod;

import com.example.examplemod.DataInterpretation.DataExtractor;
import com.example.examplemod.proxy.CommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.io.File;
import java.io.IOException;

@Mod(modid = Main.MODID, version = Main.VERSION)

public class Main
{
    private static Main instance;
    public static final Main getInstance() {
        return instance;
    }

    public static final Minecraft mc = Minecraft.getMinecraft();

    public static File configPath = null;
    public static GuiScreen display = null;
    public static Config configFile = Config.INSTANCE;

    public static KeyBinding[] keyBindings;
    @SidedProxy(clientSide = "com.example.examplemod.proxy.ClientProxy")
    public static CommonProxy proxy;

    public DataExtractor dataExtractor = new DataExtractor();

    public static final String MODID = "examplemod";
    public static final String VERSION = "1.0";

    public static final String prefix = EnumChatFormatting.GRAY + "[" + EnumChatFormatting.GOLD + "Hypixel Cry" + EnumChatFormatting.GRAY + "] ";

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        try {
            File directory = new File(event.getModConfigurationDirectory(), "hypixelcry");
            if (!directory.exists()) {
                directory.mkdirs();
            }
            File directory2 = new File(directory, "config.toml");
            configPath = directory2;
            if (!directory2.exists()) {
                directory2.createNewFile();
            }
        }
        catch (IOException ignored) {

        }
        instance = this;
        configFile.initialize();
        System.setProperty("java.awt.headless", "false");
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(dataExtractor);
        MinecraftForge.EVENT_BUS.register(this);
        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        proxy.postInit(event);
    }

    @SubscribeEvent
    public void tick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;
        if (display != null) {
            try {
                mc.displayGuiScreen(display);
            } catch (Exception e) {
                e.printStackTrace();
            }
            display = null;
        }
    }
}

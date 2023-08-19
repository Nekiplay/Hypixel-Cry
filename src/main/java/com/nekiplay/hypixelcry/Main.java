package com.nekiplay.hypixelcry;

import com.nekiplay.hypixelcry.DataInterpretation.DataExtractor;
import com.nekiplay.hypixelcry.config.MyConfig;
import com.nekiplay.hypixelcry.events.MillisecondEvent;
import com.nekiplay.hypixelcry.features.hud.TPSHud;
import com.nekiplay.hypixelcry.features.hud.TimeSinceLastTickHud;
import com.nekiplay.hypixelcry.proxy.CommonProxy;
import com.nekiplay.hypixelcry.utils.world.TickRate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.lwjgl.opengl.Display;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Mod(modid = Main.MODID, version = Main.VERSION)

public class Main
{
    private static Main instance;
    public static final Main getInstance() {
        return instance;
    }

    public static final Minecraft mc = Minecraft.getMinecraft();

    public static MyConfig myConfigFile;

    public static KeyBinding[] keyBindings;
    @SidedProxy(clientSide = "com.nekiplay.hypixelcry.proxy.ClientProxy")
    public static CommonProxy proxy;

    public DataExtractor dataExtractor = new DataExtractor();

    public static final String MODID = "hypixelcry";
    public static final String VERSION = "2.0";

    public static final String prefix = EnumChatFormatting.GRAY + "[" + EnumChatFormatting.GOLD + "Hypixel Cry" + EnumChatFormatting.GRAY + "] ";
    public static final String serverprefix = EnumChatFormatting.GRAY + "[" + EnumChatFormatting.YELLOW + "Remote Server" + EnumChatFormatting.GRAY + "] ";

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        myConfigFile = new MyConfig();
        TPSHud tpsHud = new TPSHud();
        TimeSinceLastTickHud timeSinceLastTickHud = new TimeSinceLastTickHud();
        instance = this;
        System.setProperty("java.awt.headless", "false");
        proxy.preInit(event);
    }


    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        Display.setTitle("Minecraft 1.8.9 | Hypixel Cry v" + VERSION);
        MinecraftForge.EVENT_BUS.register(dataExtractor);
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(TickRate.INSTANCE);
        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        LocalDateTime now = LocalDateTime.now();
        Duration initialDelay = Duration.between(now, now);
        long initialDelaySeconds = initialDelay.getSeconds();

        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(() -> MinecraftForge.EVENT_BUS.post(new MillisecondEvent()), initialDelaySeconds, 1, TimeUnit.MILLISECONDS);
        proxy.postInit(event);
    }
}

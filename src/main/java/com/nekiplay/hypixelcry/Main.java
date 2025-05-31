package com.nekiplay.hypixelcry;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nekiplay.hypixelcry.DataInterpretation.DataExtractor;
import com.nekiplay.hypixelcry.commands.*;
import com.nekiplay.hypixelcry.config.NEUConfig;
import com.nekiplay.hypixelcry.events.MillisecondEvent;
import com.nekiplay.hypixelcry.utils.ConfigUtil;
import io.github.notenoughupdates.moulconfig.observer.PropertyTypeAdapterFactory;
import io.github.notenoughupdates.moulconfig.processor.BuiltinMoulConfigGuis;
import io.github.notenoughupdates.moulconfig.processor.ConfigProcessorDriver;
import io.github.notenoughupdates.moulconfig.processor.MoulConfigProcessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.opengl.Display;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Mod(modid = Main.MODID, version = Main.VERSION, clientSideOnly = true)

public class Main
{
    private static Main instance;
    public static final Main getInstance() {
        return instance;
    }
    public static final Logger LOG = LogManager.getLogger("HypixelCry");
    public static final String PREFIX = "Hypixel Cry";

    public static final Minecraft mc = Minecraft.getMinecraft();

    public GuiScreen screenToOpen = null;
    public static NEUConfig config;
    private File configFile;
    private File neuDir;

    public static FeatureRegister features = new FeatureRegister();

    public static DataExtractor dataExtractor = new DataExtractor();

    public static final String MODID = "AntiCheat";
    public static final String VERSION = "1.1.0";

    public static final String prefix = EnumChatFormatting.GRAY + "[" + EnumChatFormatting.GOLD + "Hypixel Cry" + EnumChatFormatting.GRAY + "] " + EnumChatFormatting.RESET;

    private final Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation()
            .registerTypeAdapterFactory(new PropertyTypeAdapterFactory()).create();

    public File getConfigFile() {
        return this.configFile;
    }

    public void newConfigFile() {
        this.configFile = new File(getNeuDir(), "configNew.json");
    }

    public File getNeuDir() {
        return this.neuDir;
    }

    public MoulConfigProcessor<NEUConfig> processor = null;

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) return;
        if (screenToOpen != null) {
            Minecraft.getMinecraft().displayGuiScreen(screenToOpen);
            screenToOpen = null;
        }
    }

    public void saveConfig() {
        ConfigUtil.saveConfig(config, configFile, gson);
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        instance = this;

        neuDir = new File(event.getModConfigurationDirectory(), "hypixelcry");
        neuDir.mkdirs();

        configFile = new File(neuDir, "config.json");

        if (configFile.exists()) {
            config = ConfigUtil.loadConfig(NEUConfig.class, configFile, gson);
        }

        if (config == null) {
            config = new NEUConfig();
            saveConfig();
        }

        System.setProperty("java.awt.headless", "false");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        Display.setTitle("Minecraft 1.8.9 | Hypixel Cry " + VERSION);
        MinecraftForge.EVENT_BUS.register(this);

        processor = new MoulConfigProcessor<>(config);
        BuiltinMoulConfigGuis.addProcessors(processor);
        ConfigProcessorDriver driver = new ConfigProcessorDriver(processor);
        driver.checkExpose = false;
        driver.warnForPrivateFields = false;
        driver.processConfig(config);

        MinecraftForge.EVENT_BUS.register(dataExtractor);
        MinecraftForge.EVENT_BUS.register(this);
        //MinecraftForge.EVENT_BUS.register(TickRate.INSTANCE);

        features.register(event);

        ClientCommandHandler.instance.registerCommand(new LocationCommand());
        ClientCommandHandler.instance.registerCommand(new BlockInfoCommand());
        ClientCommandHandler.instance.registerCommand(new TestCommand());
        ClientCommandHandler.instance.registerCommand(new PathCommand());
        ClientCommandHandler.instance.registerCommand(new EntityInfoCommand());
        ClientCommandHandler.instance.registerCommand(new SetAngle());
        ClientCommandHandler.instance.registerCommand(new OpenSettings());
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        LocalDateTime now = LocalDateTime.now();
        Duration initialDelay = Duration.between(now, now);
        long initialDelaySeconds = initialDelay.getSeconds();

        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(() -> MinecraftForge.EVENT_BUS.post(new MillisecondEvent()), initialDelaySeconds, 1, TimeUnit.MILLISECONDS);
    }
}

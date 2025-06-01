package com.nekiplay.hypixelcry;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nekiplay.hypixelcry.DataInterpretation.DataExtractor;
import com.nekiplay.hypixelcry.commands.*;
import com.nekiplay.hypixelcry.config.NEUConfig;
import com.nekiplay.hypixelcry.events.MillisecondEvent;
import com.nekiplay.hypixelcry.utils.ConfigUtil;
import io.github.notenoughupdates.moulconfig.gui.GuiScreenElementWrapper;
import io.github.notenoughupdates.moulconfig.gui.MoulConfigEditor;
import io.github.notenoughupdates.moulconfig.observer.PropertyTypeAdapterFactory;
import io.github.notenoughupdates.moulconfig.processor.BuiltinMoulConfigGuis;
import io.github.notenoughupdates.moulconfig.processor.ConfigProcessorDriver;
import io.github.notenoughupdates.moulconfig.processor.MoulConfigProcessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.event.GuiScreenEvent;
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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Mod(modid = HypixelCry.MODID, version = HypixelCry.VERSION, clientSideOnly = true)

public class HypixelCry
{
    private static HypixelCry instance;
    public static final HypixelCry getInstance() {
        return instance;
    }
    public static final Logger LOG = LogManager.getLogger("HypixelCry");
    public static final String PREFIX = "Hypixel Cry";

    public static final Minecraft mc = Minecraft.getMinecraft();

    public static GuiScreen screenToOpen = null;
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

    public static void openGui() {
        MoulConfigEditor<NEUConfig> gui = new MoulConfigEditor<>(HypixelCry.getInstance().processor);
        screenToOpen = new GuiScreenElementWrapper(gui);
    }

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

        features.registerSystemFeatures(event);
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

    @SubscribeEvent
    public void onGuiInitPost(GuiScreenEvent.InitGuiEvent.Post event) {
        if (event.gui instanceof GuiIngameMenu) {
            int x = event.gui.width - 105;
            int x2 = x + 100;
            int y = event.gui.height - 22;
            int y2 = y + 20;

            List<GuiButton> sorted = new ArrayList<>(event.buttonList);
            sorted.sort((a, b) -> (b.yPosition + b.height) - (a.yPosition + a.height));

            for (GuiButton button : sorted) {
                int otherX = button.xPosition;
                int otherX2 = button.xPosition + button.width;
                int otherY = button.yPosition;
                int otherY2 = button.yPosition + button.height;

                if (otherX2 > x && otherX < x2 && otherY2 > y && otherY < y2) {
                    y = otherY - 20 - 2;
                    y2 = y + 20;
                }
            }

            event.buttonList.add(new GuiButton(6969422, x, Math.max(0, y), 100, 20, "Hypixel Cry"));
        }
    }

    @SubscribeEvent
    public void onGuiAction(GuiScreenEvent.ActionPerformedEvent.Post event) {
        if (event.gui instanceof GuiIngameMenu && event.button.id == 6969422) {
            openGui();
        }
    }
}

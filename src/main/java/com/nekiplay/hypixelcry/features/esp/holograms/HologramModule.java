package com.nekiplay.hypixelcry.features.esp.holograms;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.nekiplay.hypixelcry.Main;
import com.nekiplay.hypixelcry.utils.RenderUtils;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Collectors;

import static com.nekiplay.hypixelcry.Main.myConfigFile;

public class HologramModule {
    public Gson gson = new Gson();

    public ArrayList<HologramDataListed> allHolograms = new ArrayList<HologramDataListed>();


    public void reload() {
        File dir = new File(Main.mc.mcDataDir.getPath());
        File dir2 = new File(dir.getPath() + "/holograms");
        if (!dir2.exists()) {
            dir2.mkdir();
        }

        if (dir2.exists()) {
            allHolograms.clear();
            File[] files = dir2.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.exists()) {
                        try {
                            BufferedReader reader = Files.newBufferedReader(Paths.get(file.getPath()), StandardCharsets.UTF_8);
                            try {
                                String json = reader.lines().collect(Collectors.joining());
                                HologramDataListed hologramData = gson.fromJson(json, HologramDataListed.class);
                                if (hologramData != null) {
                                    allHolograms.add(hologramData);
                                    Main.LOG.info(Main.PREFIX + " Success loaded hologram: " + file.getName());
                                }

                            } catch (JsonSyntaxException e) {
                                Main.LOG.error(Main.PREFIX + " Error in hologram: " + e);

                            }
                        } catch (IOException e) {
                            Main.LOG.error(Main.PREFIX + " Error in hologram: " + e);
                        }
                    }
                }
            }
        }

    }

    private void render(RenderWorldLastEvent event) {
        for (Object hologramDataListedObject : allHolograms.toArray()) {
            HologramDataListed hologramDataListed = (HologramDataListed) hologramDataListedObject;

            RenderUtils.renderWaypointText(hologramDataListed.text, new BlockPos(hologramDataListed.x, hologramDataListed.y, hologramDataListed.z), event.partialTicks, hologramDataListed.background, new Color(hologramDataListed.red, hologramDataListed.green, hologramDataListed.blue));
            for (HologramData hologramData : hologramDataListed.other_holograms) {
                render2(hologramData, event);
            }
        }
    }

    private void render2(HologramData hologramData, RenderWorldLastEvent event) {
        RenderUtils.renderWaypointText(hologramData.text, new BlockPos(hologramData.x, hologramData.y, hologramData.z), event.partialTicks, hologramData.background, new Color(hologramData.red, hologramData.green, hologramData.blue));
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        render(event);
    }
}

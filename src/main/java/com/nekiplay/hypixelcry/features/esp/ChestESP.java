package com.nekiplay.hypixelcry.features.esp;

import com.nekiplay.hypixelcry.DataInterpretation.DataExtractor;
import com.nekiplay.hypixelcry.Main;
import com.nekiplay.hypixelcry.config.ESPFeatures;
import com.nekiplay.hypixelcry.utils.RenderUtils;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;

import static com.nekiplay.hypixelcry.Main.mc;
import static com.nekiplay.hypixelcry.utils.SpecialColor.toSpecialColor;

public class ChestESP {
    private static final ArrayList<BlockPos> locations = new ArrayList<BlockPos>();
    @SubscribeEvent
    public void TickEvent(TickEvent.ClientTickEvent clientTickEvent) {
        if (clientTickEvent.phase == TickEvent.Phase.START) {
            return;
        }
        locations.clear();
        DataExtractor extractor = Main.dataExtractor;
        if (extractor.isInSkyblock) {
            if (mc.theWorld != null && Main.config.esp.chestEsp.enabled) {
                for (TileEntity tileEntity : mc.theWorld.loadedTileEntityList) {
                    if (tileEntity instanceof TileEntityChest) {
                        if (Main.config.esp.chestEsp.maxRange == 0) {
                            locations.add(tileEntity.getPos());
                        }
                        else {
                            double dist = mc.thePlayer.getDistanceSq(tileEntity.getPos());
                            if (dist <= Main.config.esp.chestEsp.maxRange * Main.config.esp.chestEsp.maxRange) {
                                locations.add(tileEntity.getPos());
                            }
                        }
                    }
                }
            }
        }
    }

    @SuppressWarnings("deprecation")
    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (Main.config.esp.chestEsp.enabled)
        {
            for (BlockPos pos: locations) {
                if (Main.config.esp.chestEsp.features.contains(ESPFeatures.Box)) {
                    RenderUtils.drawBlockBox(pos, toSpecialColor(Main.config.esp.chestEsp.colour), 1, event.partialTicks);
                }
                if (Main.config.esp.chestEsp.features.contains(ESPFeatures.Text)) {
                    RenderUtils.renderWaypointText("Chest", new BlockPos(pos.getX() + 0.5, pos.getY() + 1.8, pos.getZ() + 0.5), event.partialTicks, false, toSpecialColor(Main.config.esp.chestEsp.colour));
                }
                if (Main.config.esp.chestEsp.features.contains(ESPFeatures.Tracer)) {
                    RenderUtils.drawTracer(pos, toSpecialColor(Main.config.esp.chestEsp.colour), 1, event.partialTicks);
                }
            }
        }
    }
}

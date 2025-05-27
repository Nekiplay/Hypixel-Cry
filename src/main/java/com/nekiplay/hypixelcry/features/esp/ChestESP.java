package com.nekiplay.hypixelcry.features.esp;

import com.nekiplay.hypixelcry.Main;
import com.nekiplay.hypixelcry.config.ESPFeatures;
import com.nekiplay.hypixelcry.utils.RenderUtils;
import com.nekiplay.hypixelcry.utils.SpecialColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;

public class ChestESP {
    private static final ArrayList<BlockPos> locations = new ArrayList<BlockPos>();
    @SubscribeEvent
    public void TickEvent(TickEvent.ClientTickEvent clientTickEvent) {
        if (clientTickEvent.phase == TickEvent.Phase.START) {
            return;
        }
        if (Main.mc.theWorld != null && Main.getInstance().config.esp.chestEsp.enabled) {
            locations.clear();
            for (TileEntity tileEntity : Main.mc.theWorld.loadedTileEntityList) {
                if (tileEntity instanceof TileEntityChest) {
                    locations.add(tileEntity.getPos());
                }
            }
        }
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (Main.getInstance().config.esp.chestEsp.enabled)
        {
            for (BlockPos pos: locations) {
                if (Main.getInstance().config.esp.chestEsp.features.contains(ESPFeatures.Box)) {
                    RenderUtils.drawBlockBox(pos, SpecialColor.toSpecialColor(Main.getInstance().config.esp.chestEsp.colour), 1, event.partialTicks);
                }
                if (Main.getInstance().config.esp.chestEsp.features.contains(ESPFeatures.Text)) {
                    RenderUtils.renderWaypointText("Chest", new BlockPos(pos.getX() + 0.5, pos.getY() + 1.8, pos.getZ() + 0.5), event.partialTicks, false, SpecialColor.toSpecialColor(Main.getInstance().config.esp.chestEsp.colour));
                }
                if (Main.getInstance().config.esp.chestEsp.features.contains(ESPFeatures.Tracer)) {
                    RenderUtils.drawTracer(pos, SpecialColor.toSpecialColor(Main.getInstance().config.esp.chestEsp.colour), 1, event.partialTicks);
                }
            }
        }
    }
}

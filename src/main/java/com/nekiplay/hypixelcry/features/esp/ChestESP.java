package com.nekiplay.hypixelcry.features.esp;

import com.nekiplay.hypixelcry.Main;
import com.nekiplay.hypixelcry.config.MyConfig;
import com.nekiplay.hypixelcry.utils.RenderUtils;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class ChestESP {
    private static final ArrayList<BlockPos> collected = new ArrayList<BlockPos>();
    private static final ArrayList<BlockPos> locations = new ArrayList<BlockPos>();
    @SubscribeEvent
    public void TickEvent(TickEvent.ClientTickEvent clientTickEvent) {
        if (Main.mc.theWorld != null && false) {
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
        if (false)
        {
            for (BlockPos pos: locations) {
                if (!collected.contains(pos)) {
                    //RenderUtils.renderWaypointText("Chest", new BlockPos(pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5), event.partialTicks, false, myConfigFile.chestESPMainPage.color.toJavaColor());
                    //RenderUtils.drawBlockBox(pos, myConfigFile.chestESPMainPage.color.toJavaColor(), 1, event.partialTicks);
                }
            }
        }
    }
}

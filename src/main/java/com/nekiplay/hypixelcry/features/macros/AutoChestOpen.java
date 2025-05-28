package com.nekiplay.hypixelcry.features.macros;

import com.nekiplay.hypixelcry.Main;
import com.nekiplay.hypixelcry.events.world.BlockUpdateEvent;
import com.nekiplay.hypixelcry.utils.KeyBindUtils;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.nekiplay.hypixelcry.Main.mc;

public class AutoChestOpen {
    public BlockPos lastUsed = null;
    public List<BlockPos> opened = new ArrayList<BlockPos>();
    @SubscribeEvent
    public void TickEvent(TickEvent.ClientTickEvent clientTickEvent) {
        if (clientTickEvent.phase == TickEvent.Phase.START) {
            return;
        }
        if (mc.theWorld == null || mc.thePlayer == null) {
            return;
        }
        if (!Main.config.macros.autoChestOpen.enabled) {
            return;
        }
        if (mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            BlockPos pos = mc.objectMouseOver.getBlockPos();
            IBlockState blockState = mc.theWorld.getBlockState(pos);
            if (mc.currentScreen == null && blockState.getBlock() == Blocks.chest && (lastUsed == null || !lastUsed.equals(pos))) {
                lastUsed = pos;
                if (!opened.contains(pos)) {
                    opened.add(pos);
                }
                KeyBindUtils.rightClick();
                if (Main.config.macros.autoChestOpen.rageMode) {
                    mc.theWorld.setBlockState(pos, Blocks.air.getDefaultState());
                }
            }
        }
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event)
    {
        opened.clear();
    }

    @SubscribeEvent
    public void onBlockUpdate(BlockUpdateEvent event) {
        if (Main.config.macros.autoChestOpen.rageMode) {
            if (event.newState.getBlock() == Blocks.chest) {
                if (opened.contains(event.pos)) {
                    event.setCanceled(true);
                }
            }
        }
    }
}

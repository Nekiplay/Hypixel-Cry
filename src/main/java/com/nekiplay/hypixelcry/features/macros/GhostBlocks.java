package com.nekiplay.hypixelcry.features.macros;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;

public class GhostBlocks {
    public void enable() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc != null) {
            EntityPlayerSP player = mc.thePlayer;
            if (player != null && mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                BlockPos pos = mc.objectMouseOver.getBlockPos();
                mc.theWorld.setBlockState(pos, Blocks.air.getDefaultState());
            }
        }
    }
}

package com.nekiplay.hypixelcry.features.macros;

import com.nekiplay.hypixelcry.Main;
import com.nekiplay.hypixelcry.utils.KeyBindUtils;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import static com.nekiplay.hypixelcry.Main.mc;

public class AutoChestOpen {
    public BlockPos lastUsed = null;
    @SubscribeEvent
    public void TickEvent(TickEvent.ClientTickEvent clientTickEvent) {
        if (clientTickEvent.phase == TickEvent.Phase.START) {
            return;
        }
        if (mc.theWorld == null || mc.thePlayer == null) {
            return;
        }
        if (!Main.getInstance().config.macros.autoChestOpen.enabled) {
            return;
        }
        if (mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            BlockPos pos = new BlockPos(mc.objectMouseOver.hitVec.xCoord, mc.objectMouseOver.hitVec.yCoord, mc.objectMouseOver.hitVec.zCoord);
            IBlockState blockState = mc.theWorld.getBlockState(pos);
            if (blockState.getBlock() == Blocks.chest && (lastUsed == null || !lastUsed.equals(pos))) {
                lastUsed = pos;
                KeyBindUtils.rightClick();
            }
        }
    }
}

package com.nekiplay.hypixelcry.features.esp;

import cc.polyfrost.oneconfig.renderer.asset.Icon;
import cc.polyfrost.oneconfig.utils.Notifications;
import com.nekiplay.hypixelcry.Main;
import com.nekiplay.hypixelcry.utils.RenderUtils;
import com.nekiplay.hypixelcry.utils.SpecialColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Vec3i;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;


public class Dark_Monolith {
    BlockPos egg = null;
    boolean found = false;


    @SubscribeEvent
    public void TickEvent(TickEvent.ClientTickEvent clientTickEvent) {
        if (Main.mc.theWorld != null && false) {
            BlockPos pos1 = new BlockPos(-15, 236, -92);
            BlockPos pos2 = new BlockPos(49, 202, -162);
            BlockPos pos3 = new BlockPos(56, 214, -25);
            BlockPos pos4 = new BlockPos(128, 187, 58);
            BlockPos pos5 = new BlockPos(150, 196, 190);
            BlockPos pos6 = new BlockPos(61, 204, 181);
            BlockPos pos7 = new BlockPos(91, 187, 131);
            BlockPos pos8 = new BlockPos(77, 160, 162);
            BlockPos pos9 = new BlockPos(-10, 162, 109);
            BlockPos pos10 = new BlockPos(1, 183, 25);
            BlockPos pos11 = new BlockPos(0, 170, 0);
            BlockPos pos12 = new BlockPos(-94, 201, -30);
            BlockPos pos13 = new BlockPos(-91, 221, -53);
            BlockPos pos14 = new BlockPos(-64, 206, -63);

            BlockPos egg1 = findEgg(pos1);
            BlockPos egg2 = findEgg(pos2);
            BlockPos egg3 = findEgg(pos3);
            BlockPos egg4 = findEgg(pos4);
            BlockPos egg5 = findEgg(pos5);
            BlockPos egg6 = findEgg(pos6);
            BlockPos egg7 = findEgg(pos7);
            BlockPos egg8 = findEgg(pos8);
            BlockPos egg9 = findEgg(pos9);
            BlockPos egg10 = findEgg(pos10);
            BlockPos egg11 = findEgg(pos11);
            BlockPos egg12 = findEgg(pos12);
            BlockPos egg13 = findEgg(pos13);
            BlockPos egg14 = findEgg(pos14);

            if (egg1 != null) {
                egg = egg1;
            }
            else if (egg2 != null) {
                egg = egg2;
            }
            else if (egg3 != null) {
                egg = egg3;
            }
            else if (egg4 != null) {
                egg = egg4;
            }
            else if (egg5 != null) {
                egg = egg5;
            }
            else if (egg6 != null) {
                egg = egg6;
            }
            else if (egg7 != null) {
                egg = egg7;
            }
            else if (egg8 != null) {
                egg = egg8;
            }
            else if (egg9 != null) {
                egg = egg9;
            }
            else if (egg10 != null) {
                egg = egg10;
            }
            else if (egg11 != null) {
                egg = egg11;
            }
            else if (egg12 != null) {
                egg = egg12;
            }
            else if (egg13 != null) {
                egg = egg13;
            }
            else if (egg14 != null) {
                egg = egg14;
            }
            else
            {
                egg = null;
            }

            if (egg == null && found) {
                found = false;
            }
            else if (egg != null && !found){
                found = true;
            }
        }
    }

    private BlockPos findEgg(BlockPos start) {
        Vec3i vec3i = new Vec3i(5, 5, 5);
        Iterable<BlockPos> blocks = BlockPos.getAllInBox(start.add(vec3i), start.subtract(vec3i));
        for (BlockPos block : blocks) {
            IBlockState state = Main.mc.theWorld.getBlockState(block);
            if (state != null && state.getBlock() == Blocks.dragon_egg) {
                return block;
            }
        }
        return null;
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (Main.getInstance().config.esp.dwardenMines.darkMonolith && egg !=null)
        {
            RenderUtils.drawBlockBox(egg, SpecialColor.toSpecialColor(Main.getInstance().config.esp.dwardenMines.colour), 1, event.partialTicks);
            if (false) {
                //RenderUtils.renderWaypointText("Dark Monolith", new BlockPos(egg.getX() + 0.5, egg.getY() + 1.8, egg.getZ() + 0.5), event.partialTicks, false, myConfigFile.darkMonolithMainPage.color.toJavaColor());
            }
            if (false) {
                //RenderUtils.drawTracer(egg, myConfigFile.darkMonolithMainPage.treasureTracerColor.toJavaColor(), 1, event.partialTicks);
            }
        }
    }
}

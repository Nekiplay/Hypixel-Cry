package com.nekiplay.hypixelcry.features.nuker;

import com.nekiplay.hypixelcry.Main;
import com.nekiplay.hypixelcry.events.world.BlockUpdateEvent;
import com.nekiplay.hypixelcry.utils.PlayerUtils;
import com.nekiplay.hypixelcry.utils.RenderUtils;
import net.minecraft.block.BlockStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.*;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;

import static com.nekiplay.hypixelcry.Main.myConfigFile;

public class Mithril extends GeneralNuker {
    private static BlockPos blockPos;
    private static int currentDamage;
    public boolean work = false;
    private int damage = 220;
    private final int damageReset = 220;

    @Override
    public boolean isBlockToBreak(IBlockState state, BlockPos pos) {
        if(state.getBlock() == Blocks.prismarine) {
            return true;
        } else if(state.getBlock() == Blocks.wool) {
            return true;
        } else if(state.getBlock() == Blocks.stained_hardened_clay) {
            return true;
        } else if(!myConfigFile.mithrilMainPage.MithrilNukerIgnoreTitanium && state.getBlock() == Blocks.stone && state.getValue(BlockStone.VARIANT) == BlockStone.EnumType.DIORITE_SMOOTH) {
            return true;
        } else if(state.getBlock() == Blocks.gold_block) {
            return true;
        }
        return false;
    }

    @SubscribeEvent
    public void TickEvent(TickEvent.ClientTickEvent clientTickEvent) {
        if (work && Main.mc.theWorld != null && Main.mc.thePlayer != null) {
            myConfigFile.ChangeExposedMode(this, myConfigFile.mithrilMainPage.MithrilNukerExposedMode);

            if(currentDamage > damage) {
                currentDamage = 0;
                damage = damageReset;
            }
            if (!Main.mc.thePlayer.onGround && currentDamage != 0) {
                damage += 1;
            }
            if(blockPos != null) {
                IBlockState blockState = Main.mc.theWorld.getBlockState(blockPos);
                Vec3 playerVec = Main.mc.thePlayer.getPositionVector();
                Vec3 vec = new Vec3(blockPos.getX(), blockPos.getY(), blockPos.getZ());
                if (playerVec.distanceTo(vec) > 12) {
                    currentDamage = 0;
                    damage = damageReset;
                    blockPos = null;
                }
                else if (blockState.getBlock() == Blocks.bedrock || blockState.getBlock() == Blocks.air || !isBlockToBreak(blockState, blockPos)) {
                    currentDamage = 0;
                    damage = damageReset;
                    blockPos = null;
                }
            }
            else{
                currentDamage = 0;
                damage = damageReset;
            }


            if (currentDamage == 0) {
                SetDistance(myConfigFile.mithrilMainPage.MaximumNukerHorizontalDistance, myConfigFile.mithrilMainPage.MaximumNukerVericalDistance);
                BlockPos near = getClosestBlock(getBlocks());
                if (near != null) {
                    blockPos = near;
                    Main.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, blockPos, EnumFacing.DOWN));
                    PlayerUtils.swingItem();
                    damage = damageReset;
                }
            }
            //if (Main.myConfigFile.OreNukerMode == 0) {
                currentDamage++;
            //}
        }
        else {
            damage = damageReset;
        }
    }
    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (work && blockPos != null) {
            RenderUtils.drawBlockBox(blockPos, myConfigFile.mithrilMainPage.Color.toJavaColor(), 1, event.partialTicks);
            if (myConfigFile.mithrilMainPage.Tracer) {
                RenderUtils.drawTracer(blockPos, myConfigFile.mithrilMainPage.Color.toJavaColor(), 1, event.partialTicks);
            }
        }
    }
    @SubscribeEvent
    public void onBlockUpdate(BlockUpdateEvent event) {
        if (work) {
            if (isBlockToBreak(event.oldState, event.pos) && !isBlockToBreak(event.newState, event.pos) && Main.mc.thePlayer != null) {
                Vec3 playerVec = Main.mc.thePlayer.getPositionVector();
                Vec3 vec = new Vec3(event.pos.getX(), event.pos.getY(), event.pos.getZ());
                if (playerVec.distanceTo(vec) <= 12) {
                    Main.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, event.pos, EnumFacing.DOWN));
                    currentDamage = 0;
                    damage = damageReset;
                    blockPos = null;
                }
            }
        }
    }

    @SubscribeEvent(priority= EventPriority.NORMAL, receiveCanceled=true)
    public void onEvent(InputEvent.KeyInputEvent event)
    {
        KeyBinding[] keyBindings = Main.keyBindings;
        if (keyBindings[9].isPressed()) {
            if (!work) {
                work = true;
                currentDamage = 0;
                damage = damageReset;
                blockPos = null;
                Main.mc.thePlayer.addChatMessage(new ChatComponentText(Main.prefix + EnumChatFormatting.GREEN + "Mithril nuker enabled"));
            }
            else {
                work = false;
                currentDamage = 0;
                damage = damageReset;
                blockPos = null;
                Main.mc.thePlayer.addChatMessage(new ChatComponentText(Main.prefix + EnumChatFormatting.RED + "Mithril nuker disabled"));
            }
        }
    }
}

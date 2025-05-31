package com.nekiplay.hypixelcry.features.macros;

import com.nekiplay.hypixelcry.Main;
import com.nekiplay.hypixelcry.config.neupages.Macros;
import com.nekiplay.hypixelcry.events.world.BlockUpdateEvent;
import com.nekiplay.hypixelcry.utils.RaycastUtils;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.*;

import static com.nekiplay.hypixelcry.Main.mc;

public class AutoChestOpen {
    private final Map<BlockPos, Integer> openedChests = new LinkedHashMap<>();
    private int tickCounter = 0;
    private static final int CHEST_COOLDOWN = 20, MAX_REMOVALS_PER_TICK = 20;
    private static final double SEARCH_DISTANCE = 4.4;

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (shouldSkipTick(event)) return;

        if (++tickCounter % MAX_REMOVALS_PER_TICK == 0) cleanUpOldChests();
        if (mc.currentScreen == null) handleChestOpening();
    }

    private boolean shouldSkipTick(TickEvent.ClientTickEvent event) {
        return event.phase == TickEvent.Phase.START || mc.theWorld == null ||
                mc.thePlayer == null || !Main.config.macros.autoChestOpen.enabled;
    }

    private void cleanUpOldChests() {
        openedChests.entrySet().removeIf(entry -> {
            if (entry.getValue() >= CHEST_COOLDOWN) return true;
            entry.setValue(entry.getValue() + 20);
            return false;
        });
    }

    private void handleChestOpening() {
        boolean ghostHand = Main.config.macros.autoChestOpen.features.contains(Macros.AutoChestOpen.Features.GhostHand);

        if (ghostHand) {
            MovingObjectPosition mouseOver = RaycastUtils.rayTraceToChest(
                    getEyePosition(),
                    getLookEndPos()
            );
            tryOpenChest(mouseOver);
        } else if (isLookingAtChest()) {
            tryOpenChest(mc.objectMouseOver);
        }
    }

    private Vec3 getEyePosition() {
        return new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ);
    }

    private Vec3 getLookEndPos() {
        Vec3 look = mc.thePlayer.getLook(1.0f);
        return getEyePosition().addVector(look.xCoord * AutoChestOpen.SEARCH_DISTANCE, look.yCoord * AutoChestOpen.SEARCH_DISTANCE, look.zCoord * AutoChestOpen.SEARCH_DISTANCE);
    }

    private boolean isLookingAtChest() {
        return mc.objectMouseOver != null &&
                mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK &&
                mc.theWorld.getBlockState(mc.objectMouseOver.getBlockPos()).getBlock() == Blocks.chest;
    }

    private void tryOpenChest(MovingObjectPosition mouseOver) {
        if (mouseOver == null || mouseOver.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK ||
                openedChests.containsKey(mouseOver.getBlockPos())) return;

        simulateHumanClick(mouseOver);
        openedChests.put(mouseOver.getBlockPos(), 0);
    }

    private void simulateHumanClick(MovingObjectPosition mop) {
        mc.playerController.onPlayerRightClick(
                mc.thePlayer, mc.theWorld,
                mc.thePlayer.inventory.getCurrentItem(),
                mop.getBlockPos(), mop.sideHit, mop.hitVec
        );
        mc.thePlayer.swingItem();

        if (Main.config.macros.autoChestOpen.features.contains(Macros.AutoChestOpen.Features.Air)) {
            mc.theWorld.setBlockState(mop.getBlockPos(), Blocks.air.getDefaultState());
        }
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        openedChests.clear();
    }

    @SubscribeEvent
    public void onBlockUpdate(BlockUpdateEvent event) {
        if (Main.config.macros.autoChestOpen.features.contains(Macros.AutoChestOpen.Features.Air) &&
                event.newState.getBlock() == Blocks.chest && openedChests.containsKey(event.pos)) {
            event.setCanceled(true);
        }
    }
}
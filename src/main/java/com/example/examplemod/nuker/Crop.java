package com.example.examplemod.nuker;

import com.example.examplemod.Main;
import com.example.examplemod.utils.BlockUtils;
import com.example.examplemod.utils.Perlin2D;
import com.example.examplemod.utils.PlayerUtils;
import com.example.examplemod.utils.RenderUtils;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.*;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static com.example.examplemod.utils.Perlin2D.PerlinNoice;

public class Crop {
    private BlockPos breakCrop;
    private List<BlockPos> farmlandsBad = new ArrayList<>();
    public boolean work = false;
    private int hoeTick = 0;
    private static final ArrayList<BlockPos> broken = new ArrayList<>();

    private boolean isCropGrow(int max_age, IBlockState state) {
        int age = state.getValue(BlockCrops.AGE);
        return age == max_age;
    }

    private boolean isNetherWarsGrow(int max_age, IBlockState state) {
        int age = state.getValue(BlockNetherWart.AGE);
        return age == max_age;
    }

    private BlockPos findEgg(BlockPos start, int range) {
        Vec3i vec3i = new Vec3i(range, range, range);
        Iterable<BlockPos> blocks = BlockPos.getAllInBox(start.add(vec3i), start.subtract(vec3i));
        for (BlockPos block : blocks) {
            IBlockState state = Main.mc.theWorld.getBlockState(block);
            if (state != null && state.getBlock() == Blocks.dragon_egg) {
                return block;
            }
        }
        return null;
    }
    private void breakCrop(BlockPos crop) {
        InventoryPlayer inventory = Main.mc.thePlayer.inventory;
        ItemStack currentItem = inventory.getCurrentItem();
        breakCrop = crop;
        if (crop != null) {
            Main.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, crop, EnumFacing.DOWN));
            if (Main.configFile.CropNukerReplanish) {
                if (currentItem.hasTagCompound()) {
                    NBTTagCompound lore = currentItem.getTagCompound().getCompoundTag("ExtraAttributes").getCompoundTag("enchantments");
                    if (lore != null && lore.hasKey("replenish")) {
                        Block block2 = Main.mc.theWorld.getBlockState(crop).getBlock();
                        Main.mc.theWorld.setBlockState(crop, block2.getDefaultState());
                    }
                }
            }
            PlayerUtils.swingItem();
            broken.add(crop);
            hoeTick = 10;
        }
    }
    @SubscribeEvent
    public void TickEvent(TickEvent.ClientTickEvent clientTickEvent) {
        if (Main.mc.thePlayer == null) {
            broken.clear();
        }

        if (work && Main.mc.theWorld != null) {
            InventoryPlayer inventory = Main.mc.thePlayer.inventory;
            ItemStack currentItem = inventory.getCurrentItem();
            if (currentItem != null) {
                if (currentItem.getItem() instanceof ItemHoe && hoeTick > 4) {
                    double noice = PerlinNoice(2);
                    if (noice >= Main.configFile.CropNukerBoostChance) {
                        for (int i = 0; i < Main.configFile.CropNukerBlockPesTick; i++) {
                            BlockPos near = getNearblyCrop();
                            breakCrop(near);
                        }
                    }
                    else
                    {
                        BlockPos near = getNearblyCrop();
                        breakCrop(near);
                    }
                }
                else if (currentItem.getItem() instanceof ItemSeeds || (currentItem.hasDisplayName() && currentItem.getDisplayName().contains("Seeds"))) {
                    farmlandsBad = getBadFarmLand();
                }
                else if (currentItem.getItem() instanceof ItemHoe) {
                    hoeTick++;
                }
                else
                {
                    hoeTick = 0;
                }
            }
            else {
                hoeTick = 0;
            }
        }
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (work) {
            InventoryPlayer inventory = Minecraft.getMinecraft().thePlayer.inventory;
            ItemStack currentItem = inventory.getCurrentItem();
            if (currentItem != null) {
                if (farmlandsBad != null && currentItem.getItem() instanceof ItemSeeds || (currentItem.hasDisplayName() && currentItem.getDisplayName().contains("Seeds"))) {
                    for (BlockPos blockPos : farmlandsBad) {
                        RenderUtils.drawBlockBox(blockPos, new Color(255, 0, 0), 2, event.partialTicks);
                    }
                } else if (breakCrop != null && currentItem.getItem() instanceof ItemHoe) {
                    RenderUtils.drawBlockBox(breakCrop, new Color(45, 165, 45), 1, event.partialTicks);
                }
            }
        }
    }

    private List<BlockPos> getBadFarmLand() {
        List<BlockPos> done = new ArrayList<>();
        ArrayList<Block> blocks = new ArrayList<>();
        blocks.add(Blocks.farmland);
        ArrayList<BlockPos> farmlands = BlockUtils.getNearestBlocks(blocks, 75);
        for (BlockPos blockPos : farmlands) {
            Block block = Main.mc.theWorld.getBlockState(blockPos.up()).getBlock();
            if (block instanceof net.minecraftforge.common.IPlantable)
            {
            }
            else
            {
                done.add(new BlockPos(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5));
            }
        }
        return done;
    }

    private BlockPos getNearblyCrop() {
        if (broken.size() > 40) {
            broken.clear();
        }

        Vec3 playerVec = Main.mc.thePlayer.getPositionVector();
        ArrayList<Vec3> warts = new ArrayList<>();
        double r = 6;
        double r2 = 6;
        BlockPos playerPos = Main.mc.thePlayer.getPosition();
        playerPos = playerPos.add(0, 1, 0);
        Vec3i vec3i = new Vec3i(r, r2, r);
        Iterable<BlockPos> blocks = BlockPos.getAllInBox(playerPos.add(vec3i), playerPos.subtract(vec3i));
        for (BlockPos blockPos : blocks) {
            IBlockState blockState = Main.mc.theWorld.getBlockState(blockPos);
            Block block = blockState.getBlock();
            if (!broken.contains(blockPos)) {
                if (block == Blocks.wheat) {
                    if (isCropGrow(7, blockState)) {
                        warts.add(new Vec3(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5));
                    }
                } else if (block == Blocks.carrots) {
                    if (isCropGrow(7, blockState)) {
                        warts.add(new Vec3(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5));
                    }
                } else if (block == Blocks.potatoes) {
                    if (isCropGrow(7, blockState)) {
                        warts.add(new Vec3(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5));
                    }
                } else if (block == Blocks.nether_wart) {
                    if (isNetherWarsGrow(3, blockState)) {
                        warts.add(new Vec3(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5));
                    }
                }
            }
        }


        double smallest = 9999;
        Vec3 closest = null;
        for (Vec3 wart : warts) {
            double dist = wart.distanceTo(playerVec);
            if (dist < smallest) {
                smallest = dist;
                closest = wart;
            }
        }
        if (closest != null && smallest < 5) {
            return new BlockPos(closest.xCoord, closest.yCoord, closest.zCoord);
        }
        return null;
    }
    @SubscribeEvent(priority= EventPriority.NORMAL, receiveCanceled=true)
    public void onEvent(InputEvent.KeyInputEvent event)
    {
        Minecraft mc = Minecraft.getMinecraft();
        KeyBinding[] keyBindings = Main.keyBindings;
        if (keyBindings[4].isPressed()) {
            if (!work) {
                work = true;
                broken.clear();
                farmlandsBad.clear();
                Main.mc.thePlayer.addChatMessage(new ChatComponentText("Crop nuker enabled"));
            }
            else {
                work = false;
                Main.mc.thePlayer.addChatMessage(new ChatComponentText("Crop nuker disabled"));
            }
        }
    }
}

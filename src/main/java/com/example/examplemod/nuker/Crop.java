package com.example.examplemod.nuker;

import com.example.examplemod.DataInterpretation.DataExtractor;
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
import net.minecraft.item.ItemAxe;
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

    private boolean isValidCactus(BlockPos cactus)
    {
        IBlockState sand = Main.mc.theWorld.getBlockState(new BlockPos(cactus.getX(), cactus.getY() - 2, cactus.getZ()));
        if (sand.getBlock() == Blocks.sand) {
            return true;
        }
        else
        {
            IBlockState air = Main.mc.theWorld.getBlockState(new BlockPos(cactus.getX(), cactus.getY() - 1, cactus.getZ()));
            return air.getBlock() == Blocks.air;

        }
    }

    private void breakCrop(BlockPos crop) {
        InventoryPlayer inventory = Main.mc.thePlayer.inventory;
        ItemStack currentItem = inventory.getCurrentItem();
        breakCrop = crop;
        if (crop != null) {
            boolean valid = false;
            Block block2 = Main.mc.theWorld.getBlockState(crop).getBlock();
            if (block2 == Blocks.melon_block || block2 == Blocks.pumpkin) {
                if (Main.mc.thePlayer.onGround) {
                    Main.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, crop, EnumFacing.DOWN));
                    PlayerUtils.swingItem();
                    broken.add(crop);
                    valid = true;
                }
            }
            else
            {
                Main.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, crop, EnumFacing.DOWN));
                PlayerUtils.swingItem();
                broken.add(crop);
                valid = true;
            }
            if (Main.configFile.CropNukerReplanish && valid) {
                if (currentItem.hasTagCompound()) {
                    NBTTagCompound lore = currentItem.getTagCompound().getCompoundTag("ExtraAttributes").getCompoundTag("enchantments");
                    if (lore != null && lore.hasKey("replenish")) {
                        if (block2 != Blocks.melon_block && block2 != Blocks.pumpkin && block2 != Blocks.cactus && block2 != Blocks.reeds) {
                            Main.mc.theWorld.setBlockState(crop, block2.getDefaultState());
                        }
                        else
                        {
                            Block block3 = Main.mc.theWorld.getBlockState(crop.add(0, 1, 0)).getBlock();
                            if (block3 == Blocks.cactus || block3 == Blocks.reeds) {
                                Main.mc.theWorld.setBlockState(crop.add(0, 1, 0), Blocks.air.getDefaultState());
                            }
                            Main.mc.theWorld.setBlockState(crop, Blocks.air.getDefaultState());
                        }
                    }
                    else {
                        if (block2 != Blocks.melon_block && block2 != Blocks.pumpkin && block2 != Blocks.cactus && block2 != Blocks.reeds) {

                        }
                        else
                        {
                            Block block3 = Main.mc.theWorld.getBlockState(crop.add(0, 1, 0)).getBlock();
                            if (block3 == Blocks.cactus || block3 == Blocks.reeds) {
                                Main.mc.theWorld.setBlockState(crop.add(0, 1, 0), Blocks.air.getDefaultState());
                            }
                            Main.mc.theWorld.setBlockState(crop, Blocks.air.getDefaultState());
                        }
                    }
                }
            }
            hoeTick = 10;
        }
    }
    private int boostTicks = 0;
    @SubscribeEvent
    public void TickEvent(TickEvent.ClientTickEvent clientTickEvent) {
        if (Main.mc.thePlayer == null) {
            broken.clear();
        }

        if (work && Main.mc.theWorld != null) {
            InventoryPlayer inventory = Main.mc.thePlayer.inventory;
            ItemStack currentItem = inventory.getCurrentItem();
            if (currentItem != null) {
                if ((currentItem.getItem() instanceof ItemHoe || currentItem.getItem() instanceof ItemAxe) && hoeTick > 7) {
                    if (boostTicks > Main.configFile.CropNukerBoostTicks)
                    {
                        for (int i = 0; i < Main.configFile.CropNukerBlockPesTick; i++) {
                            BlockPos near = getNearblyCrop();
                            breakCrop(near);
                        }
                        boostTicks = 0;
                    }
                    else
                    {
                        BlockPos near = getNearblyCrop();
                        breakCrop(near);
                        boostTicks++;
                    }


                    //double noice = PerlinNoice(2);
                    //if (noice >= Main.configFile.CropNukerBoostChance) {
                    //    for (int i = 0; i < Main.configFile.CropNukerBlockPesTick; i++) {
                    //        BlockPos near = getNearblyCrop();
                    //        breakCrop(near);
                    //    }
                    //}
                    //else
                    //{
                    //    BlockPos near = getNearblyCrop();
                    //    breakCrop(near);
                    //}
                }
                else if (currentItem.getItem() instanceof ItemSeeds || (currentItem.hasDisplayName() && currentItem.getDisplayName().contains("Seeds"))) {
                    farmlandsBad = getBadFarmLand();
                }
                else if (currentItem.getItem() instanceof ItemHoe || currentItem.getItem() instanceof ItemAxe) {
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
        ArrayList<BlockPos> farmlands = BlockUtils.getNearestBlocks(blocks, 15);
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

    private boolean isMathHoe(String hoe) {
        InventoryPlayer inventory = Main.mc.thePlayer.inventory;
        ItemStack currentItem = inventory.getCurrentItem();
        if (currentItem != null) {
            if (currentItem.getTagCompound() != null && currentItem.getTagCompound().getCompoundTag("display") != null) {
                return (currentItem.getTagCompound().getCompoundTag("display").getString("Name").contains(hoe));
            }
        }
        return false;
    }

    private BlockPos getNearblyCrop() {
        if (broken.size() > 40) {
            broken.clear();
        }

        Vec3 playerVec = Main.mc.thePlayer.getPositionVector();
        ArrayList<Vec3> warts = new ArrayList<>();
        double r = 6;
        double r2 = Main.configFile.CropNukerMaxYRange;
        BlockPos playerPos = Main.mc.thePlayer.getPosition();
        playerPos = playerPos.add(0, 1, 0);
        Vec3i vec3i = new Vec3i(r, r2, r);
        Iterable<BlockPos> blocks = BlockPos.getAllInBox(playerPos.add(vec3i), playerPos.subtract(vec3i));
        for (BlockPos blockPos : blocks) {
            IBlockState blockState = Main.mc.theWorld.getBlockState(blockPos);
            Block block = blockState.getBlock();
            if (!broken.contains(blockPos)) {
                if (block == Blocks.wheat) {
                    if (Main.configFile.CropNukerOnlyMathematicalHoe && isMathHoe("Wheat Hoe")) {
                        if (isCropGrow(7, blockState) || Main.configFile.CropNukerRemover) {
                            warts.add(new Vec3(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5));
                        }
                    } else if (!Main.configFile.CropNukerOnlyMathematicalHoe) {
                        if (isCropGrow(7, blockState) || Main.configFile.CropNukerRemover) {
                            warts.add(new Vec3(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5));
                        }
                    }
                } else if (block == Blocks.carrots) {
                    if (Main.configFile.CropNukerOnlyMathematicalHoe && isMathHoe("Carrot Hoe")) {
                        if (isCropGrow(7, blockState) || Main.configFile.CropNukerRemover) {
                            warts.add(new Vec3(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5));
                        }
                    }
                    else if (!Main.configFile.CropNukerOnlyMathematicalHoe) {
                        if (isCropGrow(7, blockState) || Main.configFile.CropNukerRemover) {
                            warts.add(new Vec3(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5));
                        }
                    }
                } else if (block == Blocks.potatoes) {
                    if (Main.configFile.CropNukerOnlyMathematicalHoe && isMathHoe("Potato Hoe")) {
                        if (isCropGrow(7, blockState) || Main.configFile.CropNukerRemover) {
                            warts.add(new Vec3(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5));
                        }
                    }
                    else if (!Main.configFile.CropNukerOnlyMathematicalHoe) {
                        if (isCropGrow(7, blockState) || Main.configFile.CropNukerRemover) {
                            warts.add(new Vec3(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5));
                        }
                    }
                } else if (block == Blocks.nether_wart) {
                    if (Main.configFile.CropNukerOnlyMathematicalHoe && isMathHoe("Nether Warts Hoe")) {
                        if (isNetherWarsGrow(3, blockState) || Main.configFile.CropNukerRemover) {
                            warts.add(new Vec3(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5));
                        }
                    }
                    else if (!Main.configFile.CropNukerOnlyMathematicalHoe) {
                        if (isNetherWarsGrow(3, blockState) || Main.configFile.CropNukerRemover) {
                            warts.add(new Vec3(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5));
                        }
                    }
                }
                else if (block == Blocks.cactus) {
                    if (Main.configFile.CropNukerOnlyMathematicalHoe && isMathHoe("Cactus Knife")) {
                        if (isValidCactus(blockPos)) {
                            warts.add(new Vec3(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5));
                        }
                    }
                    else if (!Main.configFile.CropNukerOnlyMathematicalHoe) {
                        if (isValidCactus(blockPos)) {
                            warts.add(new Vec3(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5));
                        }
                    }
                }
                else if (block == Blocks.reeds) {
                    if (Main.configFile.CropNukerOnlyMathematicalHoe && isMathHoe("Sugar Cane Hoe")) {
                        if (isValidCactus(blockPos)) {
                            warts.add(new Vec3(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5));
                        }
                    }
                    else if (!Main.configFile.CropNukerOnlyMathematicalHoe) {
                        if (isValidCactus(blockPos)) {
                            warts.add(new Vec3(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5));
                        }
                    }
                }
                else if (block == Blocks.melon_block) {
                    warts.add(new Vec3(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5));
                }
                else if (block == Blocks.pumpkin) {
                    warts.add(new Vec3(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5));
                }
                else if (block == Blocks.melon_stem && Main.configFile.CropNukerRemover) {
                    warts.add(new Vec3(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5));
                }
                else if (block == Blocks.pumpkin_stem && Main.configFile.CropNukerRemover) {
                    warts.add(new Vec3(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5));
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
                Main.mc.thePlayer.addChatMessage(new ChatComponentText(Main.prefix + EnumChatFormatting.GREEN + "Crop nuker enabled"));
            }
            else {
                work = false;
                Main.mc.thePlayer.addChatMessage(new ChatComponentText(Main.prefix + EnumChatFormatting.RED + "Crop nuker disabled"));
            }
        }
    }
}

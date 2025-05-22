package com.nekiplay.hypixelcry.features.nuker;

import com.nekiplay.hypixelcry.FindHotbar;
import com.nekiplay.hypixelcry.Main;
import com.nekiplay.hypixelcry.features.esp.Glowing_Mushroom;
import com.nekiplay.hypixelcry.utils.BlockUtils;
import com.nekiplay.hypixelcry.utils.PlayerUtils;
import com.nekiplay.hypixelcry.utils.RenderUtils;
import com.nekiplay.hypixelcry.utils.world.TickRate;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.*;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static com.nekiplay.hypixelcry.Main.mc;
import static net.minecraft.block.BlockDirectional.FACING;

public class Crop {
    private BlockPos breakCrop;
    private Block breakBlock;
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

    private boolean isCocoaGrow(int max_age, IBlockState state) {
        int age = state.getValue(BlockCocoa.AGE);
        return age == max_age;
    }

    private boolean isValidCactus(BlockPos cactus)
    {
        IBlockState sand = mc.theWorld.getBlockState(new BlockPos(cactus.getX(), cactus.getY() - 2, cactus.getZ()));
        if (sand.getBlock() == Blocks.sand) {
            return true;
        }
        else
        {
            IBlockState air = mc.theWorld.getBlockState(new BlockPos(cactus.getX(), cactus.getY() - 1, cactus.getZ()));
            return air.getBlock() == Blocks.air;

        }
    }

    private boolean isValidReeds(BlockPos cactus)
    {
        Block reeds = mc.theWorld.getBlockState(new BlockPos(cactus.getX(), cactus.getY() - 1, cactus.getZ())).getBlock();
        Block reeds2 = mc.theWorld.getBlockState(new BlockPos(cactus.getX(), cactus.getY() - 2, cactus.getZ())).getBlock();
        return reeds == Blocks.reeds && (reeds2 == Blocks.dirt || reeds2 == Blocks.sand);
    }

    private void breakCrop(BlockPos crop) {
        if (TickRate.INSTANCE.getTimeSinceLastTick() > 1 && false) {
            return;
        }
        InventoryPlayer inventory = mc.thePlayer.inventory;
        ItemStack currentItem = inventory.getCurrentItem();
        breakCrop = crop;
        if (crop != null) {
            breakBlock = mc.theWorld.getBlockState(crop).getBlock();
            boolean valid = false;
            IBlockState block2 = mc.theWorld.getBlockState(crop);
            if (block2.getBlock() == Blocks.melon_block || block2.getBlock() == Blocks.pumpkin) {
                if (mc.thePlayer.onGround) {
                    mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, crop, EnumFacing.DOWN));
                    PlayerUtils.swingItem();
                    broken.add(crop);
                    valid = true;
                }
            }
            else
            {
                mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, crop, EnumFacing.DOWN));
                PlayerUtils.swingItem();
                broken.add(crop);
                valid = true;
            }
            if (false && valid) {
                if (currentItem.hasTagCompound()) {
                    NBTTagCompound lore = currentItem.getTagCompound().getCompoundTag("ExtraAttributes").getCompoundTag("enchantments");
                    if (lore != null && lore.hasKey("replenish")) {
                        if (block2.getBlock() == Blocks.cocoa) {
                            EnumFacing facing = block2.getValue(FACING);
                            IBlockState newstate = block2.getBlock().getDefaultState();
                            mc.theWorld.setBlockState(crop, newstate.withProperty(FACING, facing));
                        }
                        else if (block2.getBlock() != Blocks.melon_block && block2.getBlock() != Blocks.pumpkin && block2.getBlock() != Blocks.cactus && block2.getBlock() != Blocks.reeds) {
                            mc.theWorld.setBlockState(crop, block2.getBlock().getDefaultState());
                        }
                        else
                        {
                            Block block3 = mc.theWorld.getBlockState(crop.add(0, 1, 0)).getBlock();
                            if (block3 == Blocks.cactus || block3 == Blocks.reeds) {
                                mc.theWorld.setBlockState(crop.add(0, 1, 0), Blocks.air.getDefaultState());
                            }
                            mc.theWorld.setBlockState(crop, Blocks.air.getDefaultState());
                        }
                    }
                    else {
                        if (block2.getBlock() != Blocks.melon_block && block2.getBlock() != Blocks.pumpkin && block2.getBlock() != Blocks.cactus && block2.getBlock() != Blocks.reeds) {

                        }
                        else
                        {
                            Block block3 = mc.theWorld.getBlockState(crop.add(0, 1, 0)).getBlock();
                            if (block3 == Blocks.cactus || block3 == Blocks.reeds) {
                                mc.theWorld.setBlockState(crop.add(0, 1, 0), Blocks.air.getDefaultState());
                            }
                            mc.theWorld.setBlockState(crop, Blocks.air.getDefaultState());
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
        if (mc.thePlayer == null) {
            broken.clear();
            return;
        }
        
        if (work && mc.theWorld != null) {
            InventoryPlayer inventory = mc.thePlayer.inventory;
            ItemStack currentItem = inventory.getCurrentItem();
            if (currentItem != null) {
                if (((currentItem.getItem() instanceof ItemHoe || currentItem.getItem() instanceof ItemAxe || currentItem.getItem() instanceof ItemShears) && hoeTick > 7) || false) {
                    if (boostTicks > 1)
                    {
                        for (int i = 0; i < 1; i++) {
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
                }
                else if (currentItem.getItem() instanceof ItemHoe || currentItem.getItem() instanceof ItemAxe || currentItem.getItem() instanceof ItemShears) {
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
                } else if (breakCrop != null && (currentItem.getItem() instanceof ItemHoe || currentItem.getItem() instanceof ItemAxe || currentItem.getItem() instanceof ItemShears)) {
                    if (breakBlock == Blocks.wheat) {
                        //RenderUtils.drawBlockBox(breakCrop, myConfigFile.cropMainPage.wheatColor.toJavaColor(), 1, event.partialTicks);
                        if (false) {
                           //RenderUtils.drawTracer(breakCrop, myConfigFile.cropMainPage.wheatColor.toJavaColor(), 1, event.partialTicks);
                        }
                    }
                    else if (breakBlock == Blocks.carrots) {
                        //RenderUtils.drawBlockBox(breakCrop, myConfigFile.cropMainPage.carrotColor.toJavaColor(), 1, event.partialTicks);
                        if (false) {
                            //RenderUtils.drawTracer(breakCrop, myConfigFile.cropMainPage.carrotColor.toJavaColor(), 1, event.partialTicks);
                        }
                    }
                    else if (breakBlock == Blocks.potatoes) {
                        //RenderUtils.drawBlockBox(breakCrop, myConfigFile.cropMainPage.potatoesColor.toJavaColor(), 1, event.partialTicks);
                        if (false) {
                            //RenderUtils.drawTracer(breakCrop, myConfigFile.cropMainPage.potatoesColor.toJavaColor(), 1, event.partialTicks);
                        }
                    }
                    else if (breakBlock == Blocks.reeds) {
                        //RenderUtils.drawBlockBox(breakCrop, myConfigFile.cropMainPage.reedsColor.toJavaColor(), 1, event.partialTicks);
                        if (false) {
                            //RenderUtils.drawTracer(breakCrop, myConfigFile.cropMainPage.reedsColor.toJavaColor(), 1, event.partialTicks);
                        }
                    }
                    else if (breakBlock == Blocks.cactus) {
                        //RenderUtils.drawBlockBox(breakCrop, myConfigFile.cropMainPage.cactusColor.toJavaColor(), 1, event.partialTicks);
                        if (false) {
                            //RenderUtils.drawTracer(breakCrop, myConfigFile.cropMainPage.cactusColor.toJavaColor(), 1, event.partialTicks);
                        }
                    }
                    else if (breakBlock == Blocks.melon_block || breakBlock == Blocks.melon_stem) {
                        //RenderUtils.drawBlockBox(breakCrop, myConfigFile.cropMainPage.melonColor.toJavaColor(), 1, event.partialTicks);
                        if (false) {
                            //RenderUtils.drawTracer(breakCrop, myConfigFile.cropMainPage.melonColor.toJavaColor(), 1, event.partialTicks);
                        }
                    }
                    else if (breakBlock == Blocks.pumpkin || breakBlock == Blocks.pumpkin_stem) {
                        //RenderUtils.drawBlockBox(breakCrop, myConfigFile.cropMainPage.pumpkinColor.toJavaColor(), 1, event.partialTicks);
                        if (false) {
                            //RenderUtils.drawTracer(breakCrop, myConfigFile.cropMainPage.pumpkinColor.toJavaColor(), 1, event.partialTicks);
                        }
                    }
                    else if (breakBlock == Blocks.cocoa) {
                        //RenderUtils.drawBlockBox(breakCrop, myConfigFile.cropMainPage.cocoaColor.toJavaColor(), 1, event.partialTicks);
                        if (false) {
                            //RenderUtils.drawTracer(breakCrop, myConfigFile.cropMainPage.cocoaColor.toJavaColor(), 1, event.partialTicks);
                        }
                    }
                    else if (breakBlock == Blocks.nether_wart) {
                        //RenderUtils.drawBlockBox(breakCrop, myConfigFile.cropMainPage.netherWartsColor.toJavaColor(), 1, event.partialTicks);
                        if (false) {
                            //RenderUtils.drawTracer(breakCrop, myConfigFile.cropMainPage.netherWartsColor.toJavaColor(), 1, event.partialTicks);
                        }
                    }
                    else if (breakBlock == Blocks.brown_mushroom || breakBlock == Blocks.red_mushroom) {
                        //RenderUtils.drawBlockBox(breakCrop, myConfigFile.cropMainPage.mushRoomColor.toJavaColor(), 1, event.partialTicks);
                        if (false) {
                            //RenderUtils.drawTracer(breakCrop, myConfigFile.cropMainPage.mushRoomColor.toJavaColor(), 1, event.partialTicks);
                        }
                    }
                    else {
                        //RenderUtils.drawBlockBox(breakCrop, new Color(45, 165, 45), 1, event.partialTicks);
                        if (false) {
                            //RenderUtils.drawTracer(breakCrop, new Color(45, 165, 45), 1, event.partialTicks);
                        }
                    }
                }
            }
        }
    }

    private boolean isMathHoe(String hoe) {
        InventoryPlayer inventory = mc.thePlayer.inventory;
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

        Vec3 playerVec = mc.thePlayer.getPositionVector();
        ArrayList<Vec3> warts = new ArrayList<>();
        double r = 4;
        double r2 = 4;
        BlockPos playerPos = mc.thePlayer.getPosition();
        playerPos = playerPos.add(0, 1, 0);
        Vec3i vec3i = new Vec3i(r, r2, r);
        Iterable<BlockPos> blocks = BlockPos.getAllInBox(playerPos.add(vec3i), playerPos.subtract(vec3i));
        for (BlockPos blockPos : blocks) {
            IBlockState blockState = mc.theWorld.getBlockState(blockPos);
            Block block = blockState.getBlock();
            if (!broken.contains(blockPos)) {
                if (block == Blocks.wheat) {
                    if (isCropGrow(7, blockState) || false) {
                        if (false && isMathHoe("Wheat Hoe")) {
                            warts.add(new Vec3(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5));
                        } else if (!false) {
                            warts.add(new Vec3(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5));
                        }
                    }
                }
                else if (block == Blocks.carrots) {
                    if (isCropGrow(7, blockState) || false) {
                        if (false && isMathHoe("Carrot Hoe")) {
                            warts.add(new Vec3(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5));
                        } else if (!false) {
                            warts.add(new Vec3(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5));
                        }
                    }
                }
                else if (block == Blocks.potatoes) {
                    if (isCropGrow(7, blockState) || false) {
                        if (false && isMathHoe("Potato Hoe")) {
                            warts.add(new Vec3(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5));
                        } else if (!false) {
                            warts.add(new Vec3(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5));
                        }
                    }
                }
                else if (block == Blocks.nether_wart) {
                    if (isNetherWarsGrow(3, blockState) || false) {
                        if (false && isMathHoe("Nether Warts Hoe")) {
                            warts.add(new Vec3(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5));
                        } else if (!false) {
                            warts.add(new Vec3(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5));
                        }
                    }
                }
                else if (block == Blocks.cactus) {
                    if (isValidCactus(blockPos)) {
                        if (false && isMathHoe("Cactus Knife")) {
                            warts.add(new Vec3(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5));
                        } else if (!false) {
                            warts.add(new Vec3(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5));
                        }
                    }
                }
                else if (block == Blocks.reeds) {
                    if (isValidReeds(blockPos)) {
                        if (false && isMathHoe("Sugar Cane Hoe")) {
                            warts.add(new Vec3(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5));
                        } else if (!false) {
                            warts.add(new Vec3(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5));
                        }
                    }
                }
                else if (block == Blocks.melon_block) {
                    if (false && isMathHoe("Melon Dicer")) {
                        warts.add(new Vec3(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5));
                    } else if (!false) {
                        warts.add(new Vec3(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5));
                    }
                }
                else if (block == Blocks.pumpkin) {
                    if (false && isMathHoe("Pumpkin Dicer")) {
                        warts.add(new Vec3(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5));
                    } else if (!false) {
                        warts.add(new Vec3(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5));
                    }
                }
                else if (block == Blocks.red_mushroom || block == Blocks.brown_mushroom) {
                    if (false && isMathHoe("Moby")) {
                        if (Glowing_Mushroom.isGlowingMushroom(blockPos)) {
                            warts.add(new Vec3(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5));
                        }
                    }
                    else if (!false) {
                        warts.add(new Vec3(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5));
                    }
                }
                else if (block == Blocks.cocoa) {
                    if (isCocoaGrow(2, blockState) || false) {
                        if (false && isMathHoe("Coco Chopper")) {
                            warts.add(new Vec3(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5));
                        } else if (!false) {
                            warts.add(new Vec3(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5));
                        }
                    }
                }
                else if (block == Blocks.melon_stem && false) {
                    warts.add(new Vec3(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5));
                }
                else if (block == Blocks.pumpkin_stem && false) {
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
    private void enable() {
        KeyBinding[] keyBindings = Main.keyBindings;
        if (keyBindings[0].isPressed()) {
            if (!work) {
                work = true;
                broken.clear();
                farmlandsBad.clear();
                mc.thePlayer.addChatMessage(new ChatComponentText(Main.prefix + EnumChatFormatting.GREEN + "Crop nuker enabled"));
            }
            else {
                work = false;
                mc.thePlayer.addChatMessage(new ChatComponentText(Main.prefix + EnumChatFormatting.RED + "Crop nuker disabled"));
            }
        }
    }

    @SubscribeEvent(priority= EventPriority.NORMAL, receiveCanceled=true)
    public void onEvent(InputEvent.KeyInputEvent event)
    {

    }
    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
    public void onEventMouse(InputEvent.MouseInputEvent event)
    {

    }
}

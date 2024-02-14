package com.nekiplay.hypixelcry.features.macros;

import com.nekiplay.hypixelcry.events.AttackEntity;
import com.nekiplay.hypixelcry.events.BlockClick;
import com.nekiplay.hypixelcry.utils.InventoryUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.nekiplay.hypixelcry.Main.mc;
import static com.nekiplay.hypixelcry.Main.myConfigFile;

public class AutoTool {
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void tickEvent(BlockClick event) {
        if (mc.objectMouseOver != null) {
            BlockPos pos = mc.objectMouseOver.getBlockPos();
            IBlockState state = mc.theWorld.getBlockState(pos);

            if (!myConfigFile.autoToolMacrosMainPage.enabled) {
                return;
            }

            Block block = state.getBlock();
            if (block != Blocks.redstone_ore && block != Blocks.lit_redstone_ore && block != Blocks.lapis_block && block != Blocks.coal_ore && block != Blocks.iron_ore && block != Blocks.gold_ore && block != Blocks.diamond_ore && block != Blocks.obsidian && block != Blocks.gold_block && block != Blocks.iron_block && block != Blocks.coal_block && block != Blocks.diamond_block && block != Blocks.emerald_block && block != Blocks.emerald_ore && block != Blocks.redstone_block) {
                if (myConfigFile.autoToolMacrosMainPage.ignoreTrashBlock) {
                    return;
                }
            }
            int pick = getFastestPickaxe();
            if (pick != -1) {
                mc.thePlayer.inventory.currentItem = pick;
            }
        }
    }
    private final Pattern pestsFromVacuumPattern = Pattern.compile("Mining Speed: \\+(\\d+)?");
    private int getFastestPickaxe() {
        int lowerSpeed = 0;
        int lowerPick = -1;

        for (int i = 0; i <= 8; i++) {
            ItemStack slot = mc.thePlayer.inventory.getStackInSlot(i);
            if (slot != null) {
                ArrayList<String> lore = InventoryUtils.getItemLore(slot);
                if (!lore.isEmpty()) {
                    for (String line : lore) {
                        if (line.contains("Mining Speed:")) {
                            Matcher matcher = pestsFromVacuumPattern.matcher(line);
                            if (matcher.find()) {
                                int speed = Integer.parseInt( matcher.group(1));
                                if (speed >= lowerSpeed) {
                                    lowerSpeed = speed;
                                    lowerPick = i;
                                }
                            }
                        }
                    }
                }
            }
        }
        return lowerPick;
    }
}

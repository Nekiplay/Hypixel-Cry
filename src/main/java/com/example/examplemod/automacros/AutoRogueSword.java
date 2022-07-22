package com.example.examplemod.automacros;

import com.example.examplemod.DataInterpretation.DataExtractor;
import com.example.examplemod.FindHotbar;
import com.example.examplemod.Main;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class AutoRogueSword {
    private int last_slot = 0;
    private int tick = 0;

    private int notUsedTicks = 20 * Main.configFile.AutoRogueSwordDelay - 4;
    private int resetDelay = 20 * Main.configFile.AutoRogueSwordDelay - 4;

    @SubscribeEvent
    public void onWorldChange(WorldEvent.Unload event) {
        resetDelay = 20 * Main.configFile.AutoRogueSwordDelay - 4;
        notUsedTicks = resetDelay;
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void TickEvent(TickEvent.ClientTickEvent event) {
        notUsedTicks = 20 * Main.configFile.AutoRogueSwordDelay - 4;
        resetDelay = 20 * Main.configFile.AutoRogueSwordDelay - 4;
        DataExtractor extractor = Main.getInstance().dataExtractor;
        if (notUsedTicks >= resetDelay && extractor.isInSkyblock && extractor.getPlayerStats().Mp >= 50 && Main.configFile.AutoRogueSword) {
            FindHotbar findHotbar = new FindHotbar();
            Minecraft mc = Minecraft.getMinecraft();
            int slot = findHotbar.findSlotInHotbar("Rogue Sword");
            EntityPlayerSP player = mc.thePlayer;
            if (tick == 0 || tick >= 3) {
                last_slot = player.inventory.currentItem;
                player.inventory.currentItem = slot;
                tick++;
                if (tick >= 3) {
                    tick = 1;
                }
            }
            else if (tick == 1) {
                if (slot == player.inventory.currentItem) {
                    mc.playerController.sendUseItem(player, mc.theWorld, player.inventory.getCurrentItem());
                    tick++;
                }
            }
            else if (tick == 2) {
                player.inventory.currentItem = last_slot;
                tick = 0;
                notUsedTicks = 20 * Main.configFile.AutoRogueSwordDelay - 4;
                resetDelay = 20 * Main.configFile.AutoRogueSwordDelay - 4;
                notUsedTicks = 0;
            }
            else
            {
                tick++;
            }
        }
        else if (!extractor.isInSkyblock || !Main.configFile.AutoRogueSword) {
            notUsedTicks = 20 * Main.configFile.AutoRogueSwordDelay - 4;
            resetDelay = 20 * Main.configFile.AutoRogueSwordDelay - 4;
            notUsedTicks = resetDelay;
        }
        else if (notUsedTicks < resetDelay) {
            notUsedTicks++;
        }
    }
}

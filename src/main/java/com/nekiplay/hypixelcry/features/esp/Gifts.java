package com.nekiplay.hypixelcry.features.esp;

import com.nekiplay.hypixelcry.DataInterpretation.DataExtractor;
import com.nekiplay.hypixelcry.Main;
import com.nekiplay.hypixelcry.events.AttackEntity;
import com.nekiplay.hypixelcry.utils.ApecUtils;
import com.nekiplay.hypixelcry.utils.RenderUtils;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;

import static com.nekiplay.hypixelcry.Main.mc;
import static com.nekiplay.hypixelcry.Main.myConfigFile;

public class Gifts {
    private static final ArrayList<BlockPos> allPositions = new ArrayList<BlockPos>(){{
        add(new BlockPos(-1, 76, 81));
        add(new BlockPos(-11,80,108));
        add(new BlockPos(-28,77,92));
        add(new BlockPos(10,76,70));
        add(new BlockPos(13,76,60));
        add(new BlockPos(-26,78,42));
        add(new BlockPos(15,76,26));
        add(new BlockPos(17,77,41));
        add(new BlockPos(19,86,71));
        add(new BlockPos(15,88,105));
        add(new BlockPos(-34,84,74));
        add(new BlockPos(-25,76,69));
        add(new BlockPos(-18,85,53));
        add(new BlockPos(-17,82,30));
        add(new BlockPos(-13,86,96));
        add(new BlockPos(17,77,90));
        add(new BlockPos(-13,76,9));
        add(new BlockPos(9,77,9));
        add(new BlockPos(4,82,100));
    }};
    private static final ArrayList<BlockPos> collected = new ArrayList<BlockPos>();
    private boolean allowRender = false;
    @SubscribeEvent
    public void OnTick(TickEvent.ClientTickEvent event) {
        DataExtractor extractor = Main.getInstance().dataExtractor;
        String zone = extractor.getScoreBoardData().Zone;
        if (zone.contains("Jerry's") || zone.contains("Sherry's")) {
            allowRender = true;
        }
        else {
            allowRender = false;
        }
    }
    @SubscribeEvent
    public void onAttackEntity(WorldEvent.Unload event)
    {
        collected.clear();
    }
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onChatMsg(ClientChatReceivedEvent event) {
        if (event.message.getUnformattedText().contains("You have already found this Gift this year!")) {
            double distance = 9999999;
            BlockPos closest = null;
            for (BlockPos pos : allPositions) {
                double distanceCurrent = mc.thePlayer.getDistanceSq(pos);
                if (distanceCurrent <= distance) {
                    closest = pos;
                    distance = distanceCurrent;
                }
            }

            if (closest != null) {
                if (!collected.contains(closest)) {
                    collected.add(closest);
                }
            }
        }
    }
    @SubscribeEvent
    public void onAttackEntity(AttackEntity event)
    {
        if (event.attacked instanceof EntityArmorStand) {
            if (myConfigFile != null && myConfigFile.jerryGiftsMainPage.EnableESP && allowRender) {

                for (BlockPos pos : allPositions) {
                    double distance = event.attacked.getDistanceSq(pos);
                    if (distance <= 2.68) {
                        if (!collected.contains(pos)) {
                            collected.add(pos);
                        }
                        break;
                    }
                }
            }
        }
    }
    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (myConfigFile != null && myConfigFile.jerryGiftsMainPage.EnableESP && allowRender)
        {
            for (BlockPos pos: allPositions) {
                if (!collected.contains(pos)) {
                    RenderUtils.drawBlockBox(pos, myConfigFile.jerryGiftsMainPage.Color.toJavaColor(), 1, event.partialTicks);
                }
            }
        }
    }
}

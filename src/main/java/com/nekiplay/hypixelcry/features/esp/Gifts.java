package com.nekiplay.hypixelcry.features.esp;

import com.nekiplay.hypixelcry.DataInterpretation.DataExtractor;
import com.nekiplay.hypixelcry.Main;
import com.nekiplay.hypixelcry.events.AttackEntity;
import com.nekiplay.hypixelcry.utils.EntityUtils;
import com.nekiplay.hypixelcry.utils.RenderUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.util.BlockPos;
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
    private static final ArrayList<BlockPos> collected = new ArrayList<BlockPos>();
    private boolean allowRender = false;
    @SubscribeEvent
    public void OnTick(TickEvent.ClientTickEvent event) {
        DataExtractor extractor = Main.getInstance().dataExtractor;
        String zone = extractor.getScoreBoardData().Zone;
        if (zone.contains("Jerry") || zone.contains("Sherry's") || zone.contains("Reflective")) {
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
    @SubscribeEvent
    public void onAttackEntity(AttackEntity event)
    {
        if (event.attacked instanceof EntityArmorStand) {
            if (myConfigFile != null && myConfigFile.jerryGiftsMainPage.enableESP && allowRender) {

                EntityArmorStand armorStand = (EntityArmorStand) event.attacked;
                String head = EntityUtils.getArmorStandHeadId(armorStand);
                if (head != null && !head.isEmpty()) {
                    if (head.equals("7732c5e4-1800-3b90-a70f-727d2969254b")) {
                        if (!collected.contains(event.attacked.getPosition())) {
                            collected.add(event.attacked.getPosition());
                        }
                    }
                }
            }
        }
    }
    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (myConfigFile != null && myConfigFile.jerryGiftsMainPage.enableESP && allowRender)
        {
            for (Entity entity : mc.theWorld.getLoadedEntityList()) {
                if (entity instanceof EntityArmorStand) {
                    EntityArmorStand armorStand = (EntityArmorStand) entity;
                    String head = EntityUtils.getArmorStandHeadId(armorStand);
                    if (head != null && !head.isEmpty()) {
                        if (head.equals("7732c5e4-1800-3b90-a70f-727d2969254b") && !collected.contains(entity.getPosition())) {
                            RenderUtils.drawBlockBox(entity.getPosition().add(0, 1, 0), myConfigFile.jerryGiftsMainPage.color.toJavaColor(), 1, event.partialTicks);
                            if (myConfigFile.jerryGiftsMainPage.text) {
                                RenderUtils.renderWaypointText("Gift", entity.getPosition().add(0, 2.8, 0), event.partialTicks, false, myConfigFile.jerryGiftsMainPage.textColor.toJavaColor());
                            }
                        }
                    }
                }
            }
        }
    }
}

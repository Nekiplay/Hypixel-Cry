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

public class Gifts {
    private static final ArrayList<BlockPos> gifts = new ArrayList<BlockPos>();
    private boolean allowRender = false;
    @SubscribeEvent
    public void OnTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            return;
        }
        DataExtractor extractor = Main.dataExtractor;
        String zone = extractor.getScoreBoardData().Zone;
        allowRender = zone.contains("Jerry") || zone.contains("Sherry's") || zone.contains("Reflective");


    }
    @SubscribeEvent
    public void onUnloadWorld(WorldEvent.Unload event)
    {
        gifts.clear();
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (false && allowRender)
        {
            for (Entity entity : mc.theWorld.getLoadedEntityList()) {
                if (entity instanceof EntityArmorStand) {
                    EntityArmorStand armorStand = (EntityArmorStand) entity;
                    String head = EntityUtils.getArmorStandSkullOwner(armorStand);
                    if (head != null && !head.isEmpty()) {
                        if ((head.equals("7732c5e4-1800-3b90-a70f-727d2969254b") || head.equals("3047a516-415b-3bf4-b597-b78fd2a9ccf4"))) {
                            //RenderUtils.drawBlockBox(entity.getPosition().add(0, 1, 0), myConfigFile.jerryGiftsMainPage.color.toJavaColor(), 1, event.partialTicks);
                            if (false) {
                                //RenderUtils.renderWaypointText("Gift", entity.getPosition().add(0, 2.8, 0), event.partialTicks, false, myConfigFile.jerryGiftsMainPage.textColor.toJavaColor());
                            }
                        }
                    }
                }
            }
        }
    }
}

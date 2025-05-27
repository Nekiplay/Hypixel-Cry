package com.nekiplay.hypixelcry.features.esp;

import com.nekiplay.hypixelcry.DataInterpretation.DataExtractor;
import com.nekiplay.hypixelcry.Main;
import com.nekiplay.hypixelcry.config.ESPFeatures;
import com.nekiplay.hypixelcry.events.AttackEntity;
import com.nekiplay.hypixelcry.utils.EntityUtils;
import com.nekiplay.hypixelcry.utils.RenderUtils;
import com.nekiplay.hypixelcry.utils.SpecialColor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;

import static com.nekiplay.hypixelcry.Main.mc;

public class FrozenCourpes {
    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (Main.getInstance().config.esp.glaciteTunnels.frozenCourpes.enabled)
        {
            for (Entity entity : mc.theWorld.getLoadedEntityList()) {
                if (entity instanceof EntityArmorStand) {
                    EntityArmorStand armorStand = (EntityArmorStand) entity;
                    String head = EntityUtils.getArmorStandHeadName(armorStand);
                    if (head != null && !head.isEmpty()) {
                        if (head.contains("Lapis Armor Helmet")) {
                            if (Main.getInstance().config.esp.chestEsp.features.contains(ESPFeatures.Box)) {
                                RenderUtils.drawEntityBox(entity, SpecialColor.toSpecialColor(Main.getInstance().config.esp.glaciteTunnels.frozenCourpes.colour), 1, event.partialTicks);
                            }
                            if (Main.getInstance().config.esp.chestEsp.features.contains(ESPFeatures.Text)) {
                                RenderUtils.renderWaypointText("Courpe", new BlockPos(entity.posX + 0.5, entity.posY + 1.8, entity.posZ + 0.5), event.partialTicks, false, SpecialColor.toSpecialColor(Main.getInstance().config.esp.glaciteTunnels.frozenCourpes.colour));
                            }
                            if (Main.getInstance().config.esp.chestEsp.features.contains(ESPFeatures.Tracer)) {
                                RenderUtils.drawTracer(entity.getPosition(), SpecialColor.toSpecialColor(Main.getInstance().config.esp.glaciteTunnels.frozenCourpes.colour), 1, event.partialTicks);
                            }
                        }
                    }
                }
            }
        }
    }
}

package com.nekiplay.hypixelcry.features.esp.mining.crystalhollows;

import com.nekiplay.hypixelcry.Main;
import com.nekiplay.hypixelcry.config.ESPFeatures;
import com.nekiplay.hypixelcry.utils.RenderUtils;
import com.nekiplay.hypixelcry.utils.SpecialColor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.List;

import static com.nekiplay.hypixelcry.Main.mc;
import static com.nekiplay.hypixelcry.utils.SpecialColor.toSpecialColor;

public class YogESP {
    public List<Entity> yogs = new ArrayList<>();
    @SubscribeEvent
    public void TickEvent(TickEvent.ClientTickEvent event) {
        yogs.clear();
        if (mc.theWorld != null && Main.config.esp.crystalHollows.yog.enabled) {
            List<Entity> entityList = mc.theWorld.getLoadedEntityList();
            for (Entity entity : entityList) {
                if (entity instanceof EntityMagmaCube) {
                    yogs.add(entity);
                }
            }
        }
    }
    @SuppressWarnings("deprecation")
    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (Main.config.esp.crystalHollows.yog.enabled)
        {
            for (Entity entity : yogs) {
                if (Main.config.esp.crystalHollows.yog.features.contains(ESPFeatures.Box)) {
                    RenderUtils.drawEntityBox(entity, toSpecialColor(Main.config.esp.crystalHollows.yog.colour), 1, event.partialTicks);
                }
                if (Main.config.esp.crystalHollows.yog.features.contains(ESPFeatures.Text)) {
                    RenderUtils.renderWaypointText("Yog", new BlockPos(entity.getPosition().getX() + 0.5, entity.getPosition().getY() + 1.8, entity.getPosition().getZ() + 0.5), event.partialTicks, false, toSpecialColor(Main.config.esp.crystalHollows.yog.colour));
                }
                if (Main.config.esp.crystalHollows.yog.features.contains(ESPFeatures.Tracer)) {
                    RenderUtils.drawTracer(entity.getPosition(), toSpecialColor(Main.config.esp.crystalHollows.yog.colour), 1, event.partialTicks);
                }
            }
        }
    }
}

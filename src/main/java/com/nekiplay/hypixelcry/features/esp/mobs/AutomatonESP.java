package com.nekiplay.hypixelcry.features.esp.mobs;

import com.nekiplay.hypixelcry.Main;
import com.nekiplay.hypixelcry.config.ESPFeatures;
import com.nekiplay.hypixelcry.utils.RenderUtils;
import com.nekiplay.hypixelcry.utils.SpecialColor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.List;

import static com.nekiplay.hypixelcry.Main.mc;

public class AutomatonESP {
    public List<Entity> automatons = new ArrayList<>();
    @SubscribeEvent
    public void TickEvent(TickEvent.ClientTickEvent event) {
        automatons.clear();
        if (mc.theWorld != null && Main.getInstance().config.esp.crystalHollows.automaton.enabled) {
            List<Entity> entityList = mc.theWorld.getLoadedEntityList();
            for (Entity entity : entityList) {
                if (entity instanceof EntityIronGolem) {
                    automatons.add(entity);
                }
            }
        }
    }
    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (Main.getInstance().config.esp.crystalHollows.automaton.enabled)
        {
            for (Entity entity : automatons) {
                if (Main.getInstance().config.esp.crystalHollows.automaton.features.contains(ESPFeatures.Box)) {
                    RenderUtils.drawEntityBox(entity, SpecialColor.toSpecialColor(Main.getInstance().config.esp.crystalHollows.automaton.colour), 1, event.partialTicks);
                }
                if (Main.getInstance().config.esp.crystalHollows.automaton.features.contains(ESPFeatures.Text)) {
                    RenderUtils.renderWaypointText("Automaton", new BlockPos(entity.getPosition().getX() + 0.5, entity.getPosition().getY() + 1.8, entity.getPosition().getZ() + 0.5), event.partialTicks, false, SpecialColor.toSpecialColor(Main.getInstance().config.esp.crystalHollows.automaton.colour));
                }
                if (Main.getInstance().config.esp.crystalHollows.automaton.features.contains(ESPFeatures.Tracer)) {
                    RenderUtils.drawTracer(entity.getPosition(), SpecialColor.toSpecialColor(Main.getInstance().config.esp.crystalHollows.automaton.colour), 1, event.partialTicks);
                }
            }
        }
    }
}

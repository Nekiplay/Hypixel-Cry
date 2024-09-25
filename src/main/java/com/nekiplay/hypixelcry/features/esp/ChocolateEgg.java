package com.nekiplay.hypixelcry.features.esp;

import com.nekiplay.hypixelcry.utils.EntityUtils;
import com.nekiplay.hypixelcry.utils.RenderUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.nekiplay.hypixelcry.Main.mc;
import static com.nekiplay.hypixelcry.Main.myConfigFile;

public class ChocolateEgg {
    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (myConfigFile != null && myConfigFile.chocolateEggMainPage.EnableESP)
        {
            for (Entity entity : mc.theWorld.getLoadedEntityList()) {
                if (entity instanceof EntityArmorStand) {
                    EntityArmorStand armorStand = (EntityArmorStand) entity;
                    String head = EntityUtils.getArmorStandHeadId(armorStand);
                    if (head != null && !head.isEmpty()) {
                        if (head.equals("e67f7c89-3a19-3f30-ada2-43a3856e5028")) {
                            RenderUtils.drawBlockBox(entity.getPosition().add(0, 1, 0), myConfigFile.chocolateEggMainPage.Color.toJavaColor(), 1, event.partialTicks);
                            if (myConfigFile.chocolateEggMainPage.Text) {
                                RenderUtils.renderWaypointText("Egg", entity.getPosition().add(0, 2.8, 0), event.partialTicks, false, myConfigFile.chocolateEggMainPage.TextColor.toJavaColor());
                            }
                        }
                    }
                }
            }
        }
    }
}

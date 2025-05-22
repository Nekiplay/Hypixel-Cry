package com.nekiplay.hypixelcry.features.esp;

import com.nekiplay.hypixelcry.utils.EntityUtils;
import com.nekiplay.hypixelcry.utils.RenderUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.nekiplay.hypixelcry.Main.mc;

public class ChocolateEgg {
    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (false)
        {
            for (Entity entity : mc.theWorld.getLoadedEntityList()) {
                if (entity instanceof EntityArmorStand) {
                    EntityArmorStand armorStand = (EntityArmorStand) entity;
                    String head = EntityUtils.getArmorStandHeadId(armorStand);
                    if (head != null && !head.isEmpty()) {
                        if (head.equals("e67f7c89-3a19-3f30-ada2-43a3856e5028") || head.equals("015adc61-0aba-3d4d-b3d1-ca47a68a154b") || head.equals("55ae5624-c86b-359f-be54-e0ec7c175403")) {
                            //RenderUtils.drawBlockBox(entity.getPosition().add(0, 1, 0), myConfigFile.chocolateEggMainPage.color.toJavaColor(), 1, event.partialTicks);
                            if (false) {
                               // RenderUtils.renderWaypointText("Egg", entity.getPosition().add(0, 2.8, 0), event.partialTicks, false, myConfigFile.chocolateEggMainPage.textColor.toJavaColor());
                            }
                        }
                    }
                }
            }
        }
    }
}

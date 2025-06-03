package com.nekiplay.hypixelcry.utils;

import com.nekiplay.hypixelcry.features.system.RotationHandler;
import com.nekiplay.hypixelcry.utils.helper.Angle;
import net.minecraft.util.Vec3;

import static com.nekiplay.hypixelcry.HypixelCry.mc;

public class PlayerUtils {
    public static Vec3 getEyePosition() {
        return new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ);
    }

    public static Vec3 getLookEndPos(Vec3 target, float distance) {
        Angle angle = AngleUtils.getRotation(target);
        Vec3 look = AngleUtils.getVectorForRotation(angle.pitch, angle.yaw);
        return getEyePosition().addVector(look.xCoord * distance, look.yCoord * distance, look.zCoord * distance);
    }

    public static Vec3 getLookEndPos(float distance, boolean serverSide) {
        Vec3 look = mc.thePlayer.getLook(1.0f);
        if (serverSide) {
            RotationHandler rotationHandler = RotationHandler.getInstance();
            float serverSideYaw = rotationHandler.getServerSideYaw();
            float serverSidePitch = rotationHandler.getServerSidePitch();
            if (serverSideYaw != 0 || serverSidePitch != 0) {
                look = AngleUtils.getVectorForRotation(serverSidePitch, serverSideYaw);
            }
        }
        return getEyePosition().addVector(look.xCoord * distance, look.yCoord * distance, look.zCoord * distance);
    }
}

package com.nekiplay.hypixelcry.utils;

import com.nekiplay.hypixelcry.utils.helper.Angle;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;

import static com.nekiplay.hypixelcry.HypixelCry.mc;

public class PlayerUtils {
    public static Vec3d getEyePosition() {
        return new Vec3d(mc.player.getX(), mc.player.getY() + mc.player.getEyeHeight(mc.player.getPose()), mc.player.getZ());
    }

    public static Vec3d getLookEndPos(Vec3d target, float distance) {
        Angle angle = AngleUtils.getRotation(target);
        Vec3d look = AngleUtils.getVectorForRotation(angle.pitch, angle.yaw);
        return getEyePosition().add(look.x * distance, look.y * distance, look.z * distance);
    }

    public static Vec3d getLookEndPos(float distance) {
        Vec3d look = AngleUtils.getVectorForRotation(mc.player.getPitch(), mc.player.getYaw());
        return getEyePosition().add(look.x * distance, look.y * distance, look.z * distance);
    }
}


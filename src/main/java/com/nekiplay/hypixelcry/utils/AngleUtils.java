package com.nekiplay.hypixelcry.utils;

import com.nekiplay.hypixelcry.utils.helper.Angle;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import static com.nekiplay.hypixelcry.HypixelCry.mc;

public class AngleUtils {
    private static final double randomAddition = (Math.random() * 0.3 - 0.15);

    public static float get360RotationYaw(float yaw) {
        return (yaw % 360 + 360) % 360;
    }

    public static float normalizeAngle(float angle) {
        while (angle > 180) {
            angle -= 360;
        }
        while (angle <= -180) {
            angle += 360;
        }
        return angle;
    }

    public static float normalizeYaw(float yaw) {
        float newYaw = yaw % 360F;
        if (newYaw < -180F) {
            newYaw += 360F;
        }
        if (newYaw > 180F) {
            newYaw -= 360F;
        }
        return newYaw;
    }

    public static float get360RotationYaw() {
        if (mc.player == null)
            return 0;
        return get360RotationYaw(mc.player.getYaw());
    }

    public static float clockwiseDifference(float initialYaw360, float targetYaw360) {
        return get360RotationYaw(targetYaw360 - initialYaw360);
    }

    public static Vec3d getVectorForRotation(float pitch, float yaw) {
        float f = MathHelper.cos(-yaw * 0.017453292F - 3.1415927F);
        float f1 = MathHelper.sin(-yaw * 0.017453292F - 3.1415927F);
        float f2 = -MathHelper.cos(-pitch * 0.017453292F);
        float f3 = MathHelper.sin(-pitch * 0.017453292F);
        return new Vec3d(f1 * f2, f3, f * f2);
    }

    public static float antiClockwiseDifference(float initialYaw360, float targetYaw360) {
        return get360RotationYaw(initialYaw360 - targetYaw360);
    }

    public static float smallestAngleDifference(float initialYaw360, float targetYaw360) {
        return Math.min(clockwiseDifference(initialYaw360, targetYaw360), antiClockwiseDifference(initialYaw360, targetYaw360));
    }

    public static float getActualYawFrom360(float yaw360) {
        float currentYaw = yaw360;
        if (mc.player.getYaw() > yaw360) {
            while (mc.player.getYaw() - currentYaw < 180 || mc.player.getYaw() - currentYaw > 0) {
                if (Math.abs(currentYaw + 360 - mc.player.getYaw()) < Math.abs(currentYaw - mc.player.getYaw()))
                    currentYaw = currentYaw + 360;
                else break;
            }
        }
        if (mc.player.getYaw() < yaw360) {
            while (currentYaw - mc.player.getYaw() > 180 || mc.player.getYaw() - currentYaw < 0) {
                if (Math.abs(currentYaw - 360 - mc.player.getYaw()) < Math.abs(currentYaw - mc.player.getYaw()))
                    currentYaw = currentYaw - 360;
                else break;
            }
        }
        return currentYaw;


    }

    public static float getClosestDiagonal() {
        return getClosestDiagonal(get360RotationYaw());
    }

    public static float getClosestDiagonal(float yaw) {
        if (get360RotationYaw(yaw) < 90 && get360RotationYaw(yaw) > 0) {
            return 45;
        } else if (get360RotationYaw(yaw) < 180) {
            return 135f;
        } else if (get360RotationYaw(yaw) < 270) {
            return 225f;
        } else {
            return 315f;
        }
    }

    public static float getClosest30() {
        if (get360RotationYaw() < 45) {
            return 30f;
        } else if (get360RotationYaw() < 90) {
            return 60f;
        } else if (get360RotationYaw() < 135) {
            return 120f;
        } else if (get360RotationYaw() < 180) {
            return 150f;
        } else if (get360RotationYaw() < 225) {
            return 210f;
        } else if (get360RotationYaw() < 270) {
            return 240f;
        } else if (get360RotationYaw() < 315) {
            return 300f;
        } else {
            return 330f;
        }
    }

    public static float getClosest45(float inputAngle) {
        float normalizedAngle = (inputAngle % 360 + 360) % 360;
        float remainder = normalizedAngle % 45;
        if (remainder <= 22.5) {
            return (float) (Math.floor(normalizedAngle / 45) * 45);
        } else {
            return (float) (Math.ceil(normalizedAngle / 45) * 45);
        }
    }

    public static float getClosest() {
        if (get360RotationYaw() < 45 || get360RotationYaw() > 315) {
            return 0f;
        } else if (get360RotationYaw() < 135) {
            return 90f;
        } else if (get360RotationYaw() < 225) {
            return 180f;
        } else {
            return 270f;
        }
    }

    public static float getClosest(float yaw) {
        if (get360RotationYaw(yaw) < 45 || get360RotationYaw(yaw) > 315) {
            return 0f;
        } else if (get360RotationYaw(yaw) < 135) {
            return 90f;
        } else if (get360RotationYaw(yaw) < 225) {
            return 180f;
        } else {
            return 270f;
        }
    }

    public static Angle getRotation(Vec3d to) {
        return getRotation(mc.player.getEyePos(), to);
    }


    public static Angle getRotation(BlockPos pos) {
        return getRotation(mc.player.getEyePos(), new Vec3d(pos).add(0.5, 0.5, 0.5));
    }

    public static Angle getRotation(Vec3d from, BlockPos pos) {
        return getRotation(from, new Vec3d(pos).add(0.5, 0.5, 0.5));
    }

    public static Angle getRotation(Vec3d from, Vec3d to) {
        double xDiff = to.x - from.x;
        double yDiff = to.y - from.y;
        double zDiff = to.z - from.z;

        double dist = Math.sqrt(xDiff * xDiff + zDiff * zDiff);

        float yaw = (float) Math.toDegrees(Math.atan2(zDiff, xDiff)) - 90F;
        float pitch = (float) -Math.toDegrees(Math.atan2(yDiff, dist));

//    if (randomness) {
//      yaw += (float) ((Math.random() - 1) * 4);
//      pitch += (float) ((Math.random() - 1) * 4);
//    }

        return new Angle(yaw, pitch);
    }

    public static Angle getNeededChange(Angle startAngle, Angle endAngle) {
        float yawChange = normalizeAngle(normalizeAngle(endAngle.getYaw()) - normalizeAngle(startAngle.getYaw()));
        return new Angle(yawChange, endAngle.getPitch() - startAngle.getPitch());
    }

    public static Angle getPlayerAngle() {
        return new Angle(get360RotationYaw(), mc.player.getPitch());
    }
}

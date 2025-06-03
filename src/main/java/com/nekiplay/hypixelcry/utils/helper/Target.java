package com.nekiplay.hypixelcry.utils.helper;

import com.nekiplay.hypixelcry.utils.AngleUtils;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

public class Target {

    private Vec3 vec;
    private Entity entity;
    private BlockPos blockPos;
    private Angle angle;
    private float additionalY = (float) (1 + Math.random()) * 0.75f;

    public Target(Vec3 vec) {
        this.vec = vec;
    }

    public Target(Entity entity) {
        this.entity = entity;
    }

    public Target(BlockPos blockPos) {
        this.blockPos = blockPos;
    }

    public Target(Angle angle) {
        this.angle = angle;
    }

    // Ensures Rotation Always Ends
    public Angle getTargetAngle() {
        if (blockPos != null) {
            return AngleUtils.getRotation(blockPos);
        }

        if (vec != null) {
            return AngleUtils.getRotation(vec);
        }

        if (entity != null) {
            return AngleUtils.getRotation(entity.getPositionVector().addVector(0, additionalY, 0));
        }

        return angle;
    }

    @Override
    public String toString() {
        return "Vec3: " + this.vec + ", Ent: " + (this.entity != null ? this.entity.getEntityId() : "null") + ", Pos: " + this.blockPos + ", Angle: " + this.angle;
    }
}
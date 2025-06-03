package com.nekiplay.hypixelcry.features.system;

import com.nekiplay.hypixelcry.events.MotionUpdateEvent;
import com.nekiplay.hypixelcry.utils.AngleUtils;
import com.nekiplay.hypixelcry.utils.helper.Angle;
import com.nekiplay.hypixelcry.utils.helper.RotationConfiguration;
import com.nekiplay.hypixelcry.utils.helper.Target;
import lombok.Getter;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import static com.nekiplay.hypixelcry.HypixelCry.mc;

public class RotationHandler {
    private static RotationHandler instance;
    private final Queue<RotationConfiguration> rotations = new LinkedList<>();
    private final Angle startRotation = new Angle(0f, 0f);
    private final Random random = new Random();
    private boolean enabled;
    private long startTime;
    private long endTime;
    private Target target = new Target(new Angle(0, 0));

    private float lastBezierYaw = 0;
    private float lastBezierPitch = 0;

    private float serverSideYaw = 0;
    private float serverSidePitch = 0;

    private int randomMultiplier1 = 1;
    private int randomMultiplier2 = 1;

    private boolean followingTarget = false;
    private boolean stopRequested = false;

    public float getServerSidePitch() {
        return serverSidePitch;
    }

    public float getServerSideYaw() {
        return serverSideYaw;
    }

    @Getter
    private RotationConfiguration configuration;

    public static RotationHandler getInstance() {
        if (instance == null) {
            instance = new RotationHandler();
        }
        return instance;
    }

    public RotationHandler queueRotation(RotationConfiguration... configs) {
        this.rotations.addAll(Arrays.asList(configs));
        return instance;
    }

    public void start() {
        if (this.rotations.isEmpty() || this.enabled) {
            return;
        }
        this.easeTo(rotations.poll());
    }

    public void easeTo(RotationConfiguration configuration) {
        this.configuration = configuration;
        this.startTime = System.currentTimeMillis();
        this.startRotation.setRotation(configuration.from().orElse(AngleUtils.getPlayerAngle()));
        this.target = configuration.target().get();

        Angle change = AngleUtils.getNeededChange(this.startRotation, this.target.getTargetAngle());
        this.endTime = this.startTime + getTime(pythagoras(change.getYaw(), change.getPitch()), configuration.time());

        this.randomMultiplier1 = randomMultiplier2 = random.nextBoolean() ? 4 : -7;

        this.lastBezierYaw = 0;
        this.lastBezierPitch = 0;

        if (configuration.rotationType() == RotationConfiguration.RotationType.SERVER) {
            if (serverSideYaw == 0 && serverSidePitch == 0) {
                serverSideYaw = mc.thePlayer.rotationYaw;
                serverSidePitch = mc.thePlayer.rotationPitch;
            } else {
                this.startRotation.setYaw(AngleUtils.get360RotationYaw(serverSideYaw));
                this.startRotation.setPitch(serverSidePitch);
            }
        }

        this.stopRequested = false;
        this.enabled = true;
    }

    private void reset() {
        if (this.stopRequested) {
            this.configuration = null;
            this.target = null;
            this.startTime = this.endTime = 0L;
            this.serverSideYaw = this.serverSidePitch = this.lastBezierYaw = this.lastBezierPitch = 0;
        }
        this.enabled = false;
        this.followingTarget = false;
        this.stopRequested = false;
    }

    //so that we can stop rotation from anywhere, not JUST a tick (crashes if you call it from other events that aren't synchronized properly)
    public void stop() {
        this.rotations.clear();
        this.stopRequested = true;
        this.enabled = false;
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (!enabled || this.configuration == null || this.configuration.rotationType() != RotationConfiguration.RotationType.CLIENT) {
            return;
        }

        Angle bezierAngle = getBezierAngle();

        mc.thePlayer.rotationYaw += bezierAngle.getYaw() - lastBezierYaw;
        mc.thePlayer.rotationPitch += bezierAngle.getPitch() - lastBezierPitch;
        lastBezierYaw = bezierAngle.getYaw();
        lastBezierPitch = bezierAngle.getPitch();
        if (System.currentTimeMillis() > this.endTime || this.stopRequested) {
            handleRotationEnd();
        }
    }

    @SubscribeEvent(receiveCanceled = true)
    public void onMotionUpdate(MotionUpdateEvent event) {
        if (!enabled || this.configuration == null || this.configuration.rotationType() != RotationConfiguration.RotationType.SERVER) {
            return;
        }

        Angle bezierAngle = getBezierAngle();

        serverSideYaw += bezierAngle.getYaw() - lastBezierYaw;
        serverSidePitch += bezierAngle.getPitch() - lastBezierPitch;
        event.yaw = serverSideYaw;
        event.pitch = serverSidePitch;

        lastBezierYaw = bezierAngle.getYaw();
        lastBezierPitch = bezierAngle.getPitch();
        if (System.currentTimeMillis() > this.endTime || this.stopRequested) {
            handleRotationEnd();
        }
    }

    private Angle getBezierAngle() {
        float totalTime = (float) (this.endTime - this.startTime);
        float timeProgress = Math.min(totalTime, System.currentTimeMillis() - this.startTime) / totalTime;
        float rotationProgress = configuration.easeFunction().invoke(timeProgress);

        Angle bezierEnd = AngleUtils.getNeededChange(this.startRotation, this.target.getTargetAngle());
        Angle control1 = new Angle(bezierEnd.getYaw() * 0.05f * this.randomMultiplier1, bezierEnd.getYaw() * 0.1f * this.randomMultiplier2);
        Angle control2 = new Angle(bezierEnd.getYaw() - bezierEnd.getYaw() * 0.05f * this.randomMultiplier2, bezierEnd.getPitch() - bezierEnd.getYaw() * 0.1f * this.randomMultiplier1);

        double bezierYawSoFar = bezier(rotationProgress, control1.getYaw(), control2.getYaw(), bezierEnd.getYaw());
        double bezierPitchSoFar = bezier(rotationProgress, control1.getPitch(), control2.getPitch(), bezierEnd.getPitch());
        return new Angle((float) bezierYawSoFar, (float) bezierPitchSoFar);
    }

    private double bezier(float t, float c1, float c2, float end) {
        return 3 * Math.pow((1 - t), 2) * t * c1 + 3 * (1 - t) * Math.pow(t, 2) * c2 + Math.pow(t, 3) * end;
    }

    private void handleRotationEnd() {
        if (!this.stopRequested) {
            if (this.configuration.followTarget()) {
                System.out.println("Following Target");
                this.easeTo(configuration);
                this.followingTarget = true;
                return;
            }

            configuration.callback().ifPresent(Runnable::run);

            if (!this.rotations.isEmpty()) {
                this.easeTo(this.rotations.poll());
                return;
            }

            if (this.configuration.rotationType() == RotationConfiguration.RotationType.SERVER && this.configuration.easeBackToClientSide()) {
                RotationConfiguration newConf = new RotationConfiguration(AngleUtils.getPlayerAngle(), this.configuration.time(),
                        RotationConfiguration.RotationType.SERVER, () -> {
                });
                this.easeTo(newConf);
                return;
            }
        }
        this.reset();
    }

    private double pythagoras(float yaw, float pitch) {
        return Math.sqrt(yaw * yaw + pitch * pitch);
    }

    private long getTime(double pythagoras, long time) {
        if (time <= 0) {
            return 1;
        }
        if (pythagoras < 25) {
            return (long) (time * 0.65);
        }
        if (pythagoras < 45) {
            return (long) (time * 0.77);
        }
        if (pythagoras < 80) {
            return (long) (time * 0.9);
        }
        if (pythagoras > 100) {
            return (long) (time * 1.1);
        }
        return (long) (time * 1.0);
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public boolean isFollowingTarget() {
        return this.followingTarget;
    }

    public void stopFollowingTarget() {
        if (this.configuration != null) {
            this.configuration.followTarget(false);
        }
    }
}
package com.nekiplay.hypixelcry.utils;

import com.nekiplay.hypixelcry.utils.helper.Ease;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

public class SmoothRotator {
    private boolean isRotating = false;
    private Thread rotationThread;
    private Runnable callback;

    public static SmoothRotator INSTANCE;

    public static SmoothRotator getInstance() {
        return INSTANCE;
    }

    /**
     * Плавно поворачивает взгляд к указанным углам
     * @param targetYaw целевой угол поворота по горизонтали
     * @param targetPitch целевой угол поворота по вертикали
     * @param duration время поворота в миллисекундах
     * @param easing функция плавности
     * @param callback действие после завершения поворота
     */
    public void smoothLook(double targetYaw, double targetPitch, long duration, Ease easing, boolean clientSide, Runnable callback) {
        if (isRotating) {
            stop();
        }

        isRotating = true;
        this.callback = callback;

        double startYaw = Rotations.serverYaw;
        double startPitch = Rotations.serverPitch;

        rotationThread = new Thread(() -> {
            try {
                long startTime = System.currentTimeMillis();
                long elapsedTime = 0;

                while (elapsedTime < duration && isRotating) {
                    double progress = easing.invoke((float) elapsedTime / duration);

                    double currentYaw = startYaw + (targetYaw - startYaw) * progress;
                    double currentPitch = startPitch + (targetPitch - startPitch) * progress;

                    Rotations.setCamRotation(currentYaw, currentPitch);
                    Rotations.rotate(currentYaw, currentPitch);

                    Thread.sleep(1);
                    elapsedTime = System.currentTimeMillis() - startTime;
                }

                if (isRotating) {
                    // Финализируем поворот к точным значениям
                    Rotations.setCamRotation(targetYaw, targetPitch);
                    Rotations.rotate(targetYaw, targetPitch, 1, clientSide, null);

                    if (this.callback != null) {
                        this.callback.run();
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                isRotating = false;
            }
        });

        rotationThread.start();
    }

    /**
     * Плавно поворачивает взгляд к указанным углам (с упрощенными параметрами)
     * @param targetYaw целевой угол поворота по горизонтали
     * @param targetPitch целевой угол поворота по вертикали
     * @param duration время поворота в миллисекундах
     */
    public void smoothLook(double targetYaw, double targetPitch, long duration, boolean clientSide) {
        smoothLook(targetYaw, targetPitch, duration, Ease.EASE_OUT_QUAD, clientSide, null);
    }


    /**
     * Плавно поворачивает взгляд к указанной сущности
     * @param entity целевая сущность
     * @param duration время поворота в миллисекундах
     * @param easing функция плавности
     * @param callback действие после завершения поворота
     */
    public void smoothLookToEntity(Entity entity, long duration, Ease easing, boolean clientSide, Runnable callback) {
        double yaw = Rotations.getYaw(entity);
        double pitch = Rotations.getPitch(entity);
        smoothLook(yaw, pitch, duration, easing, clientSide, callback);
    }

    /**
     * Плавно поворачивает взгляд к указанной позиции
     * @param pos целевая позиция
     * @param duration время поворота в миллисекундах
     * @param easing функция плавности
     * @param callback действие после завершения поворота
     */
    public void smoothLookToPos(Vec3d pos, long duration, Ease easing, boolean clientSide, Runnable callback) {
        double yaw = Rotations.getYaw(pos);
        double pitch = Rotations.getPitch(pos);
        smoothLook(yaw, pitch, duration, easing, clientSide, callback);
    }

    /**
     * Проверяет, выполняется ли в данный момент поворот
     * @return true если поворот выполняется
     */
    public boolean isRotating() {
        return isRotating;
    }

    /**
     * Прерывает текущий поворот
     */
    public void stop() {
        isRotating = false;
        if (rotationThread != null && rotationThread.isAlive()) {
            rotationThread.interrupt();
        }
    }
}
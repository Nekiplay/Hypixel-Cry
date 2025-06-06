package com.nekiplay.hypixelcry.mixins;

import com.nekiplay.hypixelcry.events.MouseButtonEvent;
import com.nekiplay.hypixelcry.utils.misc.input.Input;
import com.nekiplay.hypixelcry.utils.misc.input.KeyAction;
import net.minecraft.client.Mouse;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

@Mixin(Mouse.class)
public abstract class MouseMixin {
    @Inject(method = "onMouseButton", at = @At("HEAD"), cancellable = true)
    private void onMouseButton(long window, int button, int action, int mods, CallbackInfo info) {
        Input.setButtonState(button, action != GLFW_RELEASE);

        ActionResult result = MouseButtonEvent.EVENT.invoker().onKeyEvent(new MouseButtonEvent(button, KeyAction.get(action)));

        if (result == ActionResult.FAIL) {
            info.cancel();
        }
    }
}
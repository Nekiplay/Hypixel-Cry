package com.nekiplay.hypixelcry.mixins.minecraft;

import com.nekiplay.hypixelcry.events.minecraft.KeyDownEvent;
import com.nekiplay.hypixelcry.events.minecraft.KeyPressEvent;
import com.nekiplay.hypixelcry.events.minecraft.KeyUpEvent;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

@Mixin(Keyboard.class)
public class MixinKeyboard {
    @Inject(method = "onKey", at = @At("HEAD"))
    private void onKey(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
        if (Minecraft.getMinecraft().thePlayer == null) return;
        //System.out.println("Key: " + key + " Scancode: " + scancode + " Action: " + action + " Modifiers: " + modifiers);
        /*
         * action = 0: Key released
         * action = 1: Key pressed
         * action = 2: Key held
         * key = keycode
         * not entirely sure what scancode means
         * modifiers = 0: No modifier
         * modifiers = 1: Shift
         * modifiers = 2: Control
         * modifiers = 4: Alt
         */
        // todo on 1.8 it first checks TextInput.isActive() before posting, however im not sure if this is needed
        // and as of now that file would need to be recoded to work with 1.21 so it hasn't been put here
        // there is also an onChar method we could mixin to and use for typing fields and replace TextInput.isActive() with that somehow
        // the extension functions such as isActive() and isKeyHeld() still work from keyboard manager
        // this only replaces the posting of events
        if (action == 0) MinecraftForge.EVENT_BUS.post(new KeyUpEvent(key));
        if (action == 1) MinecraftForge.EVENT_BUS.post(new KeyDownEvent(key));
        if (action == 2) MinecraftForge.EVENT_BUS.post(new KeyPressEvent(key));
    }
}

package com.nekiplay.hypixelcry.events;

import com.nekiplay.hypixelcry.utils.misc.input.KeyAction;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.ActionResult;

public class KeyEvent {
    public static final Event<KeyCallback> EVENT = EventFactory.createArrayBacked(
            KeyCallback.class,
            (listeners) -> (event) -> {
                for (KeyCallback listener : listeners) {
                    ActionResult result = listener.onKeyEvent(event);

                    if(result != ActionResult.PASS) {
                        return result;
                    }
                }

                return ActionResult.PASS;
            }
    );

    private int key;
    private int modifiers;
    private KeyAction action;

    public KeyEvent(int key, int modifiers, KeyAction action) {
        this.key = key;
        this.modifiers = modifiers;
        this.action = action;
    }

    public int getKey() {
        return key;
    }

    public int getModifiers() {
        return modifiers;
    }

    public KeyAction getAction() {
        return action;
    }

    public interface KeyCallback {
        ActionResult onKeyEvent(KeyEvent event);
    }
}

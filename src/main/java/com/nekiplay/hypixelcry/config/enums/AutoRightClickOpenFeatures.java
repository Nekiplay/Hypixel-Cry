package com.nekiplay.hypixelcry.config.enums;

public enum AutoRightClickOpenFeatures {
    Air("Set to air after click"),
    GhostHand("Click thought blocks"),
    AutoLook("Auto look to block"),

    ;
    final String label;

    AutoRightClickOpenFeatures(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}

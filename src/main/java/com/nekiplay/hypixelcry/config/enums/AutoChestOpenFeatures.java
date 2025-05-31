package com.nekiplay.hypixelcry.config.enums;

public enum AutoChestOpenFeatures {
    Air("Set to air after open"),
    GhostHand("Open thought blocks"),

    ;
    final String label;

    AutoChestOpenFeatures(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}

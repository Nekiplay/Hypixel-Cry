package com.nekiplay.hypixelcry.config.enums;

public enum AutoRightClickBlocks {
    Chest("Chest"),
    Lever("Lever"),
    Skull("Skull"),

            ;
    final String label;

    AutoRightClickBlocks(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}

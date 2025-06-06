package com.nekiplay.hypixelcry.config.enums;

public enum ESPFeatures {
    Box("Box"),
    Text("Text"),
    Tracer("Tracer"),

    ;
    final String label;

    ESPFeatures(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}

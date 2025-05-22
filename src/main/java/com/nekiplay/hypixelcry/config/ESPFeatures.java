package com.nekiplay.hypixelcry.config;

public enum ESPFeatures {
    Box("Box"),
    Text("Text"),
    Tracer("Tracer"),

    ;
    String label;

    ESPFeatures(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}

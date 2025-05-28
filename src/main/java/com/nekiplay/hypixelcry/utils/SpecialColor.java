package com.nekiplay.hypixelcry.utils;

import java.awt.*;

@SuppressWarnings("AvoidBritishSpelling")
public class SpecialColor {
    private static final int MIN_CHROMA_SECS = 1;
    private static final int MAX_CHROMA_SECS = 60;
    private static final SimpleTimeMark startTime = SimpleTimeMark.now();
    private static final Color DEFAULT_COLOR = new Color(0, 0, 0, 0); // Transparent black as default

    public static Color toSpecialColor(String str) {
        if (str == null || str.isEmpty()) {
            return DEFAULT_COLOR;
        }
        return new Color(toSpecialColorInt(str), true);
    }

    public static int toSpecialColorInt(String str) {
        if (str == null || str.isEmpty()) {
            return DEFAULT_COLOR.getRGB();
        }

        int[] components = decompose(str);
        int chroma = components[0];
        int alpha = components[1];
        int red = components[2];
        int green = components[3];
        int blue = components[4];

        float[] hsb = Color.RGBtoHSB(red, green, blue, null);
        float hue = hsb[0];
        float sat = hsb[1];
        float bri = hsb[2];

        float adjustedHue;
        if (chroma > 0) {
            float hueChange = (startTime.passedSince().toMillis() / 1000f / chromaSpeed(chroma) % 1);
            adjustedHue = hue + hueChange;
            if (adjustedHue < 0) {
                adjustedHue += 1f;
            }
        } else {
            adjustedHue = hue;
        }

        return (alpha & 0xFF) << 24 | (Color.HSBtoRGB(adjustedHue, sat, bri) & 0x00FFFFFF);
    }

    private static int[] decompose(String csv) {
        if (csv == null || csv.isEmpty()) {
            return new int[5]; // Returns array of zeros
        }

        String[] parts = csv.split(":");
        int[] result = new int[5];
        for (int i = 0; i < parts.length && i < 5; i++) {
            try {
                result[i] = Integer.parseInt(parts[i]);
            } catch (NumberFormatException e) {
                result[i] = 0;
            }
        }
        return result;
    }

    private static float chromaSpeed(int speed) {
        return (255 - speed) / 254f * (MAX_CHROMA_SECS - MIN_CHROMA_SECS) + MIN_CHROMA_SECS;
    }
}
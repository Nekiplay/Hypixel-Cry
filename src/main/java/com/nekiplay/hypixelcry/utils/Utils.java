package com.nekiplay.hypixelcry.utils;

import net.minecraft.client.MinecraftClient;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class Utils {
    public static boolean openUrl(String url) {
        try {
            Desktop desk = Desktop.getDesktop();
            desk.browse(new URI(url));
            return true;
        } catch (UnsupportedOperationException | IOException | URISyntaxException ignored) {
            Runtime runtime = Runtime.getRuntime();
            try {
                runtime.exec("xdg-open " + url);
                return true;
            } catch (IOException e) {
                return false;
            }
        }
    }

    public static void update() {
        MinecraftClient client = MinecraftClient.getInstance();


    }
}

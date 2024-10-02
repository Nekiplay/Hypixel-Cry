package com.nekiplay.hypixelcry.features.esp.holograms;

import net.minecraft.util.BlockPos;

import java.awt.*;

public class HologramData {
    public double x;
    public double y;
    public double z;
    public String text;
    public String world;
    public int red;
    public int green;
    public int blue;
    public double max_render_distance = 16;
    public boolean background;

    public HologramData() {

    }
    public HologramData(double x, double y, double z, String text, String world, double max_render_distance) {
        this.x = x;
        this.y = y;
        this.z = z;

        this.text = text;
        this.world = world;
        this.max_render_distance = max_render_distance;
    }

    public HologramData(BlockPos pos, String text, String world, double max_render_distance) {
        this.x = pos.getX();
        this.y = pos.getY();
        this.z = pos.getZ();

        this.text = text;
        this.world = world;
        this.max_render_distance = max_render_distance;
    }
}
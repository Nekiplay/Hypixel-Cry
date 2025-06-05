package com.nekiplay.hypixelcry.utils.render;

import it.unimi.dsi.fastutil.doubles.Double2ObjectMap;
import it.unimi.dsi.fastutil.doubles.Double2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.util.Identifier;
import net.minecraft.util.TriState;

import java.util.OptionalDouble;
import java.util.function.DoubleFunction;
import java.util.function.Function;

public class SkyblockerRenderLayers {
    private static final Double2ObjectMap<RenderLayer.MultiPhase> LINES_LAYERS = new Double2ObjectOpenHashMap<>();
    private static final Double2ObjectMap<RenderLayer.MultiPhase> LINES_THROUGH_WALLS_LAYERS = new Double2ObjectOpenHashMap<>();
    private static final Object2ObjectMap<Identifier, RenderLayer.MultiPhase> TEXTURE_LAYERS = new Object2ObjectOpenHashMap<>();
    private static final Object2ObjectMap<Identifier, RenderLayer.MultiPhase> TEXTURE_THROUGH_WALLS_LAYERS = new Object2ObjectOpenHashMap<>();

    public static final RenderLayer.MultiPhase FILLED = RenderLayer.of("filled", RenderLayer.DEFAULT_BUFFER_SIZE, false, true, RenderPipelines.DEBUG_FILLED_BOX, RenderLayer.MultiPhaseParameters.builder()
            .layering(RenderPhase.VIEW_OFFSET_Z_LAYERING)
            .build(false));

    public static final RenderLayer.MultiPhase FILLED_THROUGH_WALLS = RenderLayer.of("filled_through_walls", RenderLayer.DEFAULT_BUFFER_SIZE, false, true, SkyblockerRenderPipelines.FILLED_THROUGH_WALLS, RenderLayer.MultiPhaseParameters.builder()
            .layering(RenderPhase.VIEW_OFFSET_Z_LAYERING)
            .build(false));

    private static final DoubleFunction<RenderLayer.MultiPhase> LINES = lineWidth -> RenderLayer.of("lines", RenderLayer.DEFAULT_BUFFER_SIZE, false, false, RenderPipelines.LINES, RenderLayer.MultiPhaseParameters.builder()
            .lineWidth(new RenderPhase.LineWidth(OptionalDouble.of(lineWidth)))
            .layering(RenderPhase.VIEW_OFFSET_Z_LAYERING)
            .build(false));

    private static final DoubleFunction<RenderLayer.MultiPhase> LINES_THROUGH_WALLS = lineWidth -> RenderLayer.of("lines_through_walls", RenderLayer.DEFAULT_BUFFER_SIZE, false, false, SkyblockerRenderPipelines.LINES_THROUGH_WALLS, RenderLayer.MultiPhaseParameters.builder()
            .lineWidth(new RenderPhase.LineWidth(OptionalDouble.of(lineWidth)))
            .layering(RenderPhase.VIEW_OFFSET_Z_LAYERING)
            .build(false));

    public static final RenderLayer.MultiPhase QUADS = RenderLayer.of("quad", RenderLayer.DEFAULT_BUFFER_SIZE, false, true, RenderPipelines.DEBUG_QUADS, RenderLayer.MultiPhaseParameters.builder()
            .layering(RenderPhase.VIEW_OFFSET_Z_LAYERING)
            .build(false));

    public static final RenderLayer.MultiPhase QUADS_THROUGH_WALLS = RenderLayer.of("quad_through_walls", RenderLayer.DEFAULT_BUFFER_SIZE, false, true, SkyblockerRenderPipelines.QUADS_THROUGH_WALLS, RenderLayer.MultiPhaseParameters.builder()
            .layering(RenderPhase.VIEW_OFFSET_Z_LAYERING)
            .build(false));

    private static final Function<Identifier, RenderLayer.MultiPhase> TEXTURE = texture -> RenderLayer.of("texture", RenderLayer.DEFAULT_BUFFER_SIZE, false, true, SkyblockerRenderPipelines.TEXTURE, RenderLayer.MultiPhaseParameters.builder()
            .texture(new RenderPhase.Texture(texture, TriState.FALSE, false))
            .layering(RenderPhase.VIEW_OFFSET_Z_LAYERING)
            .build(false));

    private static final Function<Identifier, RenderLayer.MultiPhase> TEXTURE_THROUGH_WALLS = texture -> RenderLayer.of("texture_through_walls", RenderLayer.DEFAULT_BUFFER_SIZE, false, true, SkyblockerRenderPipelines.TEXTURE_THROUGH_WALLS, RenderLayer.MultiPhaseParameters.builder()
            .texture(new RenderPhase.Texture(texture, TriState.FALSE, false))
            .layering(RenderPhase.VIEW_OFFSET_Z_LAYERING)
            .build(false));
    public static final RenderLayer.MultiPhase CYLINDER = RenderLayer.of("cylinder", RenderLayer.DEFAULT_BUFFER_SIZE, false, true, SkyblockerRenderPipelines.CYLINDER, RenderLayer.MultiPhaseParameters.builder()
            .layering(RenderPhase.VIEW_OFFSET_Z_LAYERING)
            .build(false));

    public static RenderLayer.MultiPhase getLines(double lineWidth) {
        return LINES_LAYERS.computeIfAbsent(lineWidth, LINES);
    }

    public static RenderLayer.MultiPhase getLinesThroughWalls(double lineWidth) {
        return LINES_THROUGH_WALLS_LAYERS.computeIfAbsent(lineWidth, LINES_THROUGH_WALLS);
    }

    public static RenderLayer.MultiPhase getTexture(Identifier texture) {
        return TEXTURE_LAYERS.computeIfAbsent(texture, TEXTURE);
    }

    public static RenderLayer.MultiPhase getTextureThroughWalls(Identifier texture) {
        return TEXTURE_THROUGH_WALLS_LAYERS.computeIfAbsent(texture, TEXTURE_THROUGH_WALLS);
    }
}
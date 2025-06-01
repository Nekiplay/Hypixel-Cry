package com.nekiplay.hypixelcry.utils;

import com.nekiplay.hypixelcry.events.minecraft.render.RenderEntityModelEvent;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.EXTPackedDepthStencil;

import static com.nekiplay.hypixelcry.HypixelCry.mc;
import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;

public final class OutlineUtils {
    private OutlineUtils() {} // Private constructor to prevent instantiation

    public static void outlineEntity(RenderEntityModelEvent event) {
        outlineEntity(event, Color.WHITE, 2f, true, true);
    }

    public static void outlineEntity(RenderEntityModelEvent event, Color color) {
        outlineEntity(event, color, 2f, true, true);
    }

    public static void outlineEntity(RenderEntityModelEvent event, Color color, float lineWidth) {
        outlineEntity(event, color, lineWidth, true, true);
    }

    public static void outlineEntity(RenderEntityModelEvent event, Color color, float lineWidth, boolean depth) {
        outlineEntity(event, color, lineWidth, depth, true);
    }

    public static void outlineEntity(
            RenderEntityModelEvent event,
            Color color,
            float lineWidth,
            boolean depth,
            boolean shouldCancelHurt
    ) {
        if (shouldCancelHurt) event.getEntity().hurtTime = 0;

        boolean fancyGraphics = mc.gameSettings.fancyGraphics;
        float gamma = mc.gameSettings.gammaSetting;
        mc.gameSettings.fancyGraphics = false;
        mc.gameSettings.gammaSetting = 100000f;

        glPushMatrix();
        glPushAttrib(GL_ALL_ATTRIB_BITS);

        checkSetupFBO();

        if (!depth) {
            glDisable(GL_DEPTH_TEST);
            glDepthMask(false);
        }

        glDisable(GL_ALPHA_TEST);
        glDisable(GL_TEXTURE_2D);
        glDisable(GL_LIGHTING);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        glEnable(GL_STENCIL_TEST);
        glClear(GL_STENCIL_BUFFER_BIT);
        glClearStencil(0xF);

        glStencilFunc(GL_ALWAYS, 1, 0xFF);
        glStencilOp(GL_KEEP, GL_KEEP, GL_REPLACE);
        glColorMask(false, false, false, false);
        render(event);

        glStencilFunc(GL_NOTEQUAL, 1, 0xFF);
        glStencilOp(GL_KEEP, GL_KEEP, GL_KEEP);
        glColorMask(true, true, true, true);

        if (!depth) {
            glEnable(GL_POLYGON_OFFSET_LINE);
            glPolygonOffset(1.0f, -2000000f);
        }
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0f, 240.0f);

        glLineWidth(lineWidth);
        glEnable(GL_LINE_SMOOTH);
        glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);

        glColor4d(color.getRed() / 255.0, color.getGreen() / 255.0, color.getBlue() / 255.0, color.getAlpha() / 255.0);

        render(event);

        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        glDisable(GL_STENCIL_TEST);
        glLineWidth(1f);
        glPopAttrib();
        glPopMatrix();

        mc.gameSettings.fancyGraphics = fancyGraphics;
        mc.gameSettings.gammaSetting = gamma;
    }

    private static void render(RenderEntityModelEvent event) {
        event.getModel().render(
                event.getEntity(),
                event.getLimbSwing(),
                event.getLimbSwingAmount(),
                event.getAgeInTicks(),
                event.getHeadYaw(),
                event.getHeadPitch(),
                event.getScaleFactor()
        );
    }

    private static void checkSetupFBO() {
        Framebuffer fbo = mc.getFramebuffer();
        if (fbo == null || fbo.depthBuffer <= -1) return;
        setupFBO(fbo);
        fbo.depthBuffer = -1;
    }

    private static void setupFBO(Framebuffer fbo) {
        EXTFramebufferObject.glDeleteRenderbuffersEXT(fbo.depthBuffer);
        int stencilDepthBufferId = EXTFramebufferObject.glGenRenderbuffersEXT();
        EXTFramebufferObject.glBindRenderbufferEXT(EXTFramebufferObject.GL_RENDERBUFFER_EXT, stencilDepthBufferId);
        EXTFramebufferObject.glRenderbufferStorageEXT(
                EXTFramebufferObject.GL_RENDERBUFFER_EXT,
                EXTPackedDepthStencil.GL_DEPTH_STENCIL_EXT,
                mc.displayWidth,
                mc.displayHeight
        );
        EXTFramebufferObject.glFramebufferRenderbufferEXT(
                EXTFramebufferObject.GL_FRAMEBUFFER_EXT,
                EXTFramebufferObject.GL_STENCIL_ATTACHMENT_EXT,
                EXTFramebufferObject.GL_RENDERBUFFER_EXT,
                stencilDepthBufferId
        );
        EXTFramebufferObject.glFramebufferRenderbufferEXT(
                EXTFramebufferObject.GL_FRAMEBUFFER_EXT,
                EXTFramebufferObject.GL_DEPTH_ATTACHMENT_EXT,
                EXTFramebufferObject.GL_RENDERBUFFER_EXT,
                stencilDepthBufferId
        );
    }
}
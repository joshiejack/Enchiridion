package joshie.enchiridion.helpers;

import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glScalef;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import net.minecraftforge.client.ForgeHooksClient;

import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.ReflectionHelper;

public class OpenGLHelper {
    public static void start() {
        glPushMatrix();
    }

    public static void end() {
        glPopMatrix();
    }

    public static void scale(float scale) {
        glScalef(scale, scale, 1.0F);
    }

    public static void scale(float scaleX, float scaleY) {
        glScalef(scaleX, scaleY, 1.0F);
    }

    public static void scaleAll(float scale) {
        glScalef(scale, scale, scale);
    }

    public static void scaleZ(float scale, float scaleZ) {
        glScalef(scale, scale, scaleZ);
    }

    public static void scaleZ(float scale) {
        glScalef(1.0F, 1.0F, scale);
    }

    public static void enable(int what) {
        glEnable(what);
    }

    public static void disable(int what) {
        glDisable(what);
    }

    public static void clear(int what) {
        glClear(what);
    }

    public static void fixColors() {
        glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public static void color(int color) {
        float red = (color >> 16 & 255) / 255.0F;
        float green = (color >> 8 & 255) / 255.0F;
        float blue = (color & 255) / 255.0F;
        color(red, green, blue);
    }

    public static void color(float red, float green, float blue) {
        glColor4f(red, green, blue, 1.0F);
    }

    public static void resetZ() {
        clear(GL_DEPTH_BUFFER_BIT);
    }

    public static boolean FIXED = false;

    public static void fixShitForThePedia() {
        if (!Boolean.parseBoolean(System.getProperty("forge.forceDisplayStencil", "false"))) {
            try {
                Framebuffer buffer = Minecraft.getMinecraft().getFramebuffer();
                buffer.createBindFramebuffer(buffer.framebufferWidth, buffer.framebufferHeight);
                int stencilBits = GL11.glGetInteger(GL11.GL_STENCIL_BITS);
                ReflectionHelper.findField(ForgeHooksClient.class, "stencilBits").setInt(null, stencilBits);

                if (ReflectionHelper.findField(OpenGlHelper.class, "field_153212_w").getInt(null) == 2) {
                    if (EXTFramebufferObject.glCheckFramebufferStatusEXT(buffer.framebufferObject) != EXTFramebufferObject.GL_FRAMEBUFFER_COMPLETE_EXT) {
                        ReflectionHelper.findField(ForgeHooksClient.class, "stencilBits").setInt(null, 0);
                        buffer.createBindFramebuffer(buffer.framebufferWidth, buffer.framebufferHeight);
                    }
                }
            } catch (Exception e) {}
        }
    }
}

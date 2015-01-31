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
        Minecraft mc = ClientHelper.getMinecraft();
        OpenGlHelper.func_153186_a(OpenGlHelper.field_153199_f, org.lwjgl.opengl.EXTPackedDepthStencil.GL_DEPTH24_STENCIL8_EXT, Minecraft.getMinecraft().getFramebuffer().framebufferTextureWidth, Minecraft.getMinecraft().getFramebuffer().framebufferHeight);
        OpenGlHelper.func_153190_b(OpenGlHelper.field_153198_e, org.lwjgl.opengl.EXTFramebufferObject.GL_STENCIL_ATTACHMENT_EXT, OpenGlHelper.field_153199_f, Minecraft.getMinecraft().getFramebuffer().depthBuffer);

        if (!FIXED) {
            mc.getFramebuffer().createBindFramebuffer(mc.displayWidth, mc.displayHeight);
            FIXED = true;
        }
    }
}

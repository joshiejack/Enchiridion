package joshie.enchiridion.helpers;

import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glScalef;

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
    
    public static void scaleAll(float scale) {
    	glScalef(scale, scale, scale);
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
    
    public static void fixColors() {
        glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
    
    public static void color(int color) {
    	float red = (color >> 16 & 255) / 255.0F;
        float green = (color >> 8 & 255) / 255.0F;
        float blue = (color & 255) / 255.0F;
        glColor4f(red, green, blue, 1.0F);
    }
    
    public static void resetZ() {
		glClear(GL_DEPTH_BUFFER_BIT);
    }
}

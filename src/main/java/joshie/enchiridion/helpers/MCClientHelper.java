package joshie.enchiridion.helpers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.Language;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.lwjgl.glfw.GLFW;

public class MCClientHelper {
    public static boolean isShiftPressed() {
        long handle = Minecraft.getInstance().mainWindow.getHandle();
        return InputMappings.isKeyDown(handle, GLFW.GLFW_KEY_LEFT_SHIFT) || InputMappings.isKeyDown(handle, GLFW.GLFW_KEY_RIGHT_SHIFT);
    }

    public static Language getLang() {
        return Minecraft.getInstance().getLanguageManager().getCurrentLanguage();
    }

    public static boolean isCtrlPressed() {
        long handle = Minecraft.getInstance().mainWindow.getHandle();
        return InputMappings.isKeyDown(handle, GLFW.GLFW_KEY_LEFT_CONTROL) || InputMappings.isKeyDown(handle, GLFW.GLFW_KEY_RIGHT_CONTROL);
    }

    public static PlayerEntity getPlayer() {
        return Minecraft.getInstance().player;
    }

    public static World getWorld() {
        return Minecraft.getInstance().world;
    }
}
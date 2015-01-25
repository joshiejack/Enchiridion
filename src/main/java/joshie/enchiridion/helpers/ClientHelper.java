package joshie.enchiridion.helpers;

import java.awt.Dimension;
import java.awt.Point;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientHelper {
    /** Returns the client Minecraft instance **/
    public static Minecraft getMinecraft() {
        return FMLClientHandler.instance().getClient();
    }

    /** Returns the client player **/
    public static EntityPlayer getPlayer() {
        return getMinecraft().thePlayer;
    }

    /** Returns the world the player is in **/
    public static World getWorld() {
        return getMinecraft().theWorld;
    }
    
    public static boolean isShiftPressed() {
        return Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
    }
    
    //Binds a texture
    public static void bindTexture(ResourceLocation texture) {
        getMinecraft().getTextureManager().bindTexture(texture);
    }

    //returns the lang currently in use
    public static String getLang() {
        return FMLClientHandler.instance().getCurrentLanguage();
    }
}

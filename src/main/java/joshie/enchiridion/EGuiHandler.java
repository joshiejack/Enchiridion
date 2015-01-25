package joshie.enchiridion;

import static joshie.enchiridion.EInfo.WIKI_ID;
import joshie.enchiridion.wiki.WikiHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class EGuiHandler implements IGuiHandler {
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case WIKI_ID:
                return WikiHelper.gui;
            default:
                return null;
        }
    }
}

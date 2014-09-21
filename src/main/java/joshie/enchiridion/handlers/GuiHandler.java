package joshie.enchiridion.handlers;

import static joshie.enchiridion.lib.EnchiridionInfo.WIKI_ID;
import joshie.enchiridion.wiki.WikiHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {
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

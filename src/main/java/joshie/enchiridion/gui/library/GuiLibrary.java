package joshie.enchiridion.gui.library;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.util.ELocation;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class GuiLibrary extends GuiContainer {
    private static final ResourceLocation location = new ELocation("rustic.png");
    public IInventory library;

    public GuiLibrary(InventoryPlayer playerInventory, IInventory library) {
        super(new ContainerLibrary(playerInventory, library));
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        EnchiridionAPI.draw.drawImage(location, -10, -10, 440, 240);
    }
}

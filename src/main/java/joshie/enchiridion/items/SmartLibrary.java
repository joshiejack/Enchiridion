package joshie.enchiridion.items;

import java.util.ArrayList;
import java.util.List;

import joshie.enchiridion.EClientProxy;
import joshie.enchiridion.data.book.BookRegistry;
import joshie.enchiridion.library.LibraryHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.ISmartItemModel;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SmartLibrary implements ISmartItemModel {
    private ItemStack broken = new ItemStack(Items.enchanted_book);
    private IBakedModel library;
    private ItemModelMesher mesher;

    @SubscribeEvent
    public void onCookery(ModelBakeEvent event) {
        event.modelRegistry.putObject(EClientProxy.libraryItem, this);
    }

    @Override
    public IBakedModel handleItemState(ItemStack stack) {
        IBakedModel ret = null;
        //Setup
        if (mesher == null) mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
        library = mesher.getModelManager().getModel(EClientProxy.library);
        if (stack.getItemDamage() == 0) { //If we're a book
            ret = mesher.getModelManager().getModel(BookRegistry.INSTANCE.getModelLocation(stack));
        } else {
            ItemStack book = LibraryHelper.getClientLibraryContents().getCurrentBookItem();
            ret = book == null ? library : mesher.getItemModel(book);
        }

        return ret == null ? mesher.getItemModel(broken) : ret;
    }

    /** Redundant crap below :/ **/
    @Override
    public List<BakedQuad> getFaceQuads(EnumFacing facing) {
        return new ArrayList();
    }

    @Override
    public List<BakedQuad> getGeneralQuads() {
        return new ArrayList();
    }

    @Override
    public boolean isAmbientOcclusion() {
        return false;
    }

    @Override
    public boolean isGui3d() {
        return false;
    }

    @Override
    public boolean isBuiltInRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return library.getParticleTexture();
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return ItemCameraTransforms.DEFAULT;
    }
}
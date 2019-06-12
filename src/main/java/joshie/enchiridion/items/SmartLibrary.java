package joshie.enchiridion.items;

import joshie.enchiridion.EClientHandler;
import joshie.enchiridion.EConfig;
import joshie.enchiridion.data.book.BookMeshDefinition;
import joshie.enchiridion.lib.EInfo;
import joshie.enchiridion.library.LibraryHelper;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@EventBusSubscriber(modid = EInfo.MODID, value = Dist.CLIENT)
public class SmartLibrary implements IBakedModel {
    private static IBakedModel library;

    @SubscribeEvent
    public static void onCookery(ModelBakeEvent event) {
        if (EConfig.SETTINGS.libraryAsItem.get()) {
            event.getModelRegistry().put(EClientHandler.libraryItem, new SmartLibrary());
        }
    }

    @Override
    @Nonnull
    public ItemOverrideList getOverrides() {
        return LibraryOverride.INSTANCE;
    }

    private static class LibraryOverride extends ItemOverrideList {
        private static LibraryOverride INSTANCE = new LibraryOverride();
        private ItemStack broken = new ItemStack(Items.ENCHANTED_BOOK);

        private ItemModelMesher mesher;

        public LibraryOverride() {
            super();
        }

        @Override
        @Nonnull
        public IBakedModel getModelWithOverrides(@Nonnull IBakedModel originalModel, @Nonnull ItemStack stack, @Nullable World world, @Nullable LivingEntity entity) {
            IBakedModel ret;
            //Setup
            if (mesher == null) mesher = Minecraft.getInstance().getItemRenderer().getItemModelMesher();
            library = mesher.getModelManager().getModel(EClientHandler.library);
            if (stack.getDamage() == 0) { //If we're a book
                ret = mesher.getModelManager().getModel(BookMeshDefinition.INSTANCE.getModelLocation(stack));
            } else {
                ItemStack book = LibraryHelper.getClientLibraryContents().getCurrentBookItem();
                ret = book.isEmpty() ? library : mesher.getItemModel(book);
            }

            return ret == null ? mesher.getItemModel(broken) : ret;
        }
    }

    /**
     * Redundant crap below :/
     **/
    @Override
    @Nonnull
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand) {
        return new ArrayList<>();
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
    @Nonnull
    public TextureAtlasSprite getParticleTexture() {
        return library.getParticleTexture();
    }
}
package joshie.enchiridion.library;

import com.google.gson.JsonObject;
import joshie.enchiridion.EConfig;
import net.minecraft.util.JSONUtils;
import net.minecraftforge.common.crafting.IConditionSerializer;

import javax.annotation.Nonnull;
import java.util.function.BooleanSupplier;

public class LibraryConditionFactory implements IConditionSerializer {

    @Override
    @Nonnull
    public BooleanSupplier parse(@Nonnull JsonObject json) {
        boolean value = JSONUtils.getBoolean(json, "value", true);
        return () -> EConfig.SETTINGS.addWrittenBookRecipeForLibrary.get() == value;
    }
}
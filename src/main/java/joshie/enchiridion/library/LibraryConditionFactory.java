package joshie.enchiridion.library;

import com.google.gson.JsonObject;
import joshie.enchiridion.EConfig;
import net.minecraft.util.JSONUtils;
import net.minecraftforge.common.crafting.IConditionFactory;
import net.minecraftforge.common.crafting.JsonContext;

import java.util.function.BooleanSupplier;

public class LibraryConditionFactory implements IConditionFactory {
    @Override
    public BooleanSupplier parse(JsonContext context, JsonObject json) {
        boolean value = JSONUtils.getBoolean(json, "value", true);
        return () -> EConfig.SETTINGS.addWrittenBookRecipeForLibrary == value;
    }
}
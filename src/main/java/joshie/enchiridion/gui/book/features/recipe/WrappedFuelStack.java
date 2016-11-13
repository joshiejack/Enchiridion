package joshie.enchiridion.gui.book.features.recipe;

import joshie.enchiridion.helpers.ItemListHelper;
import net.minecraft.tileentity.TileEntityFurnace;

import java.util.stream.Collectors;

public class WrappedFuelStack extends WrappedStack {
    public WrappedFuelStack(double x, double y, float scale) {
        super(null, x, y, scale);

        permutations.addAll(ItemListHelper.items().stream().filter(stack -> TileEntityFurnace.getItemBurnTime(stack) > 0).collect(Collectors.toList()));

        hasPermutations = permutations.size() > 1;
        stack = permutations.get(RAND.nextInt(permutations.size()));
    }
}
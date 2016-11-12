package joshie.enchiridion.gui.book.features.recipe;

import joshie.enchiridion.helpers.ItemListHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;

public class WrappedFuelStack extends WrappedStack {
    public WrappedFuelStack(double x, double y, float scale) {
        super(null, x, y, scale);
               
        for (ItemStack stack: ItemListHelper.items()) {
            if (TileEntityFurnace.getItemBurnTime(stack) > 0) {
                permutations.add(stack);
            }
        }
               
        hasPermutations = permutations.size() > 1;
        stack = permutations.get(RAND.nextInt(permutations.size()));
    }
}

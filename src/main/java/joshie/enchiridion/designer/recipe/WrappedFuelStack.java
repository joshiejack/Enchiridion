package joshie.enchiridion.designer.recipe;

import joshie.enchiridion.helpers.ItemHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;

public class WrappedFuelStack extends WrappedStack {
    public WrappedFuelStack(double x, double y, float scale) {
        super(null, x, y, scale);
        
        for (ItemStack stack: ItemHelper.items()) {
            if (TileEntityFurnace.getItemBurnTime(stack) > 0) {
                permutations.add(stack);
            }
        }
        
        hasPermutations = permutations.size() > 1;
        stack = permutations.get(rand.nextInt(permutations.size()));
    }
}

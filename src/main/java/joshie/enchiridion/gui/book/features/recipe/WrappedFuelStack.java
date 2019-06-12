package joshie.enchiridion.gui.book.features.recipe;

import joshie.enchiridion.helpers.ItemListHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.FurnaceTileEntity;

import java.util.stream.Collectors;

public class WrappedFuelStack extends WrappedStack {
    public WrappedFuelStack(double x, double y, float scale) {
        super(null, x, y, scale);

        permutations.addAll(ItemListHelper.items().stream().filter(stack -> getBurnTime(stack) > 0).collect(Collectors.toList()));

        hasPermutations = permutations.size() > 1;
        stack = permutations.get(RAND.nextInt(permutations.size()));
    }

    private int getBurnTime(ItemStack stack) {
        if (stack.isEmpty()) {
            return 0;
        } else {
            Item item = stack.getItem();
            int ret = stack.getBurnTime();
            return net.minecraftforge.event.ForgeEventFactory.getItemBurnTime(stack, ret == -1 ? FurnaceTileEntity.getBurnTimes().getOrDefault(item, 0) : ret);
        }
    }
}
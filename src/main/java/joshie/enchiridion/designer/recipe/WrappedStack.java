package joshie.enchiridion.designer.recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import joshie.enchiridion.api.IItemStack;
import joshie.enchiridion.helpers.ItemHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class WrappedStack implements IItemStack {
    protected static final Random rand = new Random();
    protected List<ItemStack> permutations = new ArrayList();
    protected boolean hasPermutations = false;
    protected ItemStack stack;
    private int ticker = 0;
    private final double x;
    private final double y;
    private final float scale;

    public WrappedStack(Object object, double x, double y, float scale) {
        this.x = x;
        this.y = y;
        this.scale = scale;

        if (object instanceof ItemStack) {
            stack = ((ItemStack) object).copy();
            if (stack.getItemDamage() == OreDictionary.WILDCARD_VALUE) {
                ArrayList<Integer> metaList = new ArrayList();
                for (ItemStack aStack : ItemHelper.items()) {
                    if (aStack.getItem() == stack.getItem()) {
                        permutations.add(aStack);
                    }
                }
            } else permutations.add(stack);
        } else if (object instanceof List) {
            List<ItemStack> stacks = (ArrayList<ItemStack>) object;
            for (ItemStack stacky: stacks) {
                if (stacky.getItemDamage() == OreDictionary.WILDCARD_VALUE) {
                    for (ItemStack aStack : ItemHelper.items()) {
                        if (aStack.getItem() == stacky.getItem()) {
                            permutations.add(aStack);
                        }
                    }
                } else permutations.add(stacky);
            }
        }

        if (object != null) {
            hasPermutations = permutations.size() > 1;
            stack = permutations.get(rand.nextInt(permutations.size()));
        }
    }

    @Override
    public ItemStack getItemStack() {
        if (hasPermutations) {
            ticker--;

            if (ticker <= 0) {
                stack = permutations.get(rand.nextInt(permutations.size()));
                ticker = 100;
            }
        }

        return stack;
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public float getScale() {
        return scale;
    }
}

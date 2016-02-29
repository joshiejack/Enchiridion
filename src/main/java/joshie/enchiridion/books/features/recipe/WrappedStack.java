package joshie.enchiridion.books.features.recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import joshie.enchiridion.api.IItemStack;
import joshie.lib.helpers.ItemListHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class WrappedStack implements IItemStack {
    protected static final Random rand = new Random();
    protected List<ItemStack> permutations = new ArrayList();
    protected boolean hasPermutations = false;
    protected ItemStack stack;
    private int ticker = 0;
    private double x;
    private double y;
    private float scale;

    public WrappedStack(Object object, double x, double y, float scale) {
        this.x = x;
        this.y = y;
        this.scale = scale;
        
        if (object == null) stack = null;
        else {
            if (object instanceof String) {
                object = OreDictionary.getOres((String) object);
            }
            
            if (object instanceof ItemStack) {
                stack = ((ItemStack) object).copy();
                if (stack.getItemDamage() == OreDictionary.WILDCARD_VALUE) {
                    ArrayList<Integer> metaList = new ArrayList();
                    for (ItemStack aStack : ItemListHelper.items()) {
                        if (aStack.getItem() == stack.getItem()) {
                            permutations.add(aStack);
                        }
                    }
                } else permutations.add(stack);
            } else if (object instanceof List) {
                List<ItemStack> stacks = new ArrayList((List)object);
                for (ItemStack stacky : stacks) {
                    if (stacky.getItemDamage() == OreDictionary.WILDCARD_VALUE) {
                        for (ItemStack aStack : ItemListHelper.items()) {
                            if (aStack.getItem() == stacky.getItem()) {
                                permutations.add(aStack);
                            }
                        }
                    } else permutations.add(stacky);
                }
            }

            hasPermutations = permutations.size() > 1;
            stack = permutations.get(rand.nextInt(permutations.size()));
        }
    }

    @Override
    public ItemStack getItemStack() {
        return stack;
    }
    
    @Override
    public void onDisplayTick() {
        if (hasPermutations) {
            ticker--;

            if (ticker <= 0) {
                ticker = 100;
                stack = permutations.get(rand.nextInt(permutations.size()));
            }
        }
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

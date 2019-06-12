package joshie.enchiridion.gui.book.features.recipe;

import joshie.enchiridion.api.recipe.IItemStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Random;

public class WrappedStack implements IItemStack {
    protected static final Random RAND = new Random();
    protected NonNullList<ItemStack> permutations = NonNullList.create();
    protected boolean hasPermutations = false;
    @Nonnull
    protected ItemStack stack;
    private int ticker = 0;
    private double x;
    private double y;
    private float scale;

    public WrappedStack(Ingredient ingredient, double x, double y, float scale) {
        this.x = x;
        this.y = y;
        this.scale = scale;

        if (ingredient == null) {
            stack = ItemStack.EMPTY;
        } else {

            if (ingredient == Ingredient.EMPTY) {
                permutations.add(ItemStack.EMPTY);
            } else {
                Collections.addAll(permutations, ingredient.getMatchingStacks());
            }

            hasPermutations = permutations.size() > 1;
            stack = permutations.get(RAND.nextInt(permutations.size()));
        }
    }

    @Override
    @Nonnull
    public ItemStack getItemStack() {
        return stack;
    }

    @Override
    public void onDisplayTick() {
        if (hasPermutations) {
            ticker--;

            if (ticker <= 0) {
                ticker = 100;
                stack = permutations.get(RAND.nextInt(permutations.size()));
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
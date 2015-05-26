package joshie.enchiridion.wiki.elements;

import static joshie.enchiridion.helpers.OpenGLHelper.end;
import static joshie.enchiridion.helpers.OpenGLHelper.scaleAll;
import static joshie.enchiridion.helpers.OpenGLHelper.start;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import joshie.enchiridion.helpers.StackHelper;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import com.google.gson.annotations.Expose;

public class ElementRecipe extends ElementItem {
    public IRecipe recipe;
    @Expose
    public int stackSize;
    @Expose
    public String[] items;
    @Expose
    public boolean[] ores;
    public ItemStack[] stacks;
    private int tick;

    @Override
    public ElementRecipe setToDefault() {
        setStack(new ItemStack(Blocks.anvil));
        this.width = (int) (32 * size);
        this.height = (int) (32 * size);
        this.size = (float) (width / 32D);
        return this;
    }

    @Override
    public void updateHeight(int change) {
        height += change;
        width += change;
        this.size = (float) (width / 32D);
    }

    @Override
    public ElementRecipe setStack(ItemStack stack) {
        this.stack = stack;
        this.item = StackHelper.getStringFromStack(stack);
        this.items = new String[9];
        this.stacks = null;
        this.ores = null;
        for (IRecipe r : (List<IRecipe>) CraftingManager.getInstance().getRecipeList()) {
            if (r.getRecipeOutput() != null) {
                if (stack.isItemEqual(r.getRecipeOutput())) {
                    recipe = r;
                    stackSize = r.getRecipeOutput().stackSize;
                    break;
                }
            }
        }

        if (recipe instanceof ShapedOreRecipe) {
            Object[] input = ((ShapedOreRecipe) recipe).getInput();
            if (input != null) {
                for (int i = 0; i < input.length; i++) {
                    items[i] = StackHelper.getStringFromObject(input[i]);
                }
            }
        } else if (recipe instanceof ShapelessOreRecipe) {
            ArrayList<Object> input = ((ShapelessOreRecipe) recipe).getInput();
            for (int i = 0; i < input.size(); i++) {
                if (input != null) {
                    items[i] = StackHelper.getStringFromObject(input.get(i));
                }
            }
        }

        return this;
    }

    @Override
    public void display(boolean isEditMode) {
        start();
        scaleAll(size);
        if (stack == null) {
            stack = StackHelper.getStackFromString(item);
        }

        //If the stack isn't null render it
        renderStack(stack, getX((int) (65 * size)), getY((int) (25 * size)));
        wiki.mc.fontRenderer.drawSplitString("x" + stackSize, getX((int) (85 * size)), getY((int) (30 * size)), width, 0xFFFFFFFF);
        if (stacks == null) {
            stacks = new ItemStack[9];
            ores = new boolean[9];
            if (items != null) {
                for (int i = 0; i < 9; i++) {
                    String item = items[i];
                    if (item == null || item.equals("")) continue;
                    ArrayList<ItemStack> dic = OreDictionary.getOres(item);
                    if (dic != null && dic.size() > 0) {
                        ores[i] = true;
                        stacks[i] = dic.get(new Random().nextInt(dic.size()));
                    } else {
                        stacks[i] = StackHelper.getStackFromString(items[i]);
                    }
                }
            }
        } else {
            tick++;
            int j = 2, k = -1;

            for (int i = 0; i < stacks.length; i++) {
                renderStack(stacks[i], getX((int) (((-18 * size) + (55 * size) + (-18 * j * size)))), getY((int) ((k * 18 * size) + (25 * size))));
                j--;
                if (j < 0) {
                    j = 2;
                    k++;
                }
            }

            if (tick % 300 == 0) {
                for (int i = 0; i < ores.length; i++) {
                    if (ores[i]) {
                        ArrayList<ItemStack> dic = OreDictionary.getOres(item);
                        stacks[i] = dic.get(new Random().nextInt(dic.size()));
                    }
                }
            }
        }

        end();
    }
}

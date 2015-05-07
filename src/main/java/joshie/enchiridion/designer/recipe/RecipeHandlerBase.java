package joshie.enchiridion.designer.recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.IItemStack;
import joshie.enchiridion.api.IRecipeHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;

public abstract class RecipeHandlerBase implements IRecipeHandler {
    private static final ResourceLocation location = new ResourceLocation("books", "textures/gui/guide_elements.png");
    protected ArrayList<IItemStack> stackList = new ArrayList();
    private String unique;

    public RecipeHandlerBase() {}

    private Object getObject(ArrayList<Object> input, int i) {
        if (i >= input.size()) return null;
        else return input.get(i);
    }

    protected void init(ItemStack output, ArrayList<Object> input, int width) {
        int length = input.size();
        stackList.add(new WrappedStack(output, 115D, 35D, 1.75F));
        if (length == 1) {
            stackList.add(new WrappedStack(getObject(input, 0), 29D, 48D, 1F));
        } else if (length == 2) {
            if (width == 1) {
                stackList.add(new WrappedStack(getObject(input, 0), 29D, 2D, 1F));
                stackList.add(new WrappedStack(getObject(input, 1), 29D, 48D, 1F));
            } else {
                stackList.add(new WrappedStack(getObject(input, 0), 29D, 48D, 1F));
                stackList.add(new WrappedStack(getObject(input, 1), 56D, 48D, 1F));
            }
        } else if (length == 3) {
            stackList.add(new WrappedStack(getObject(input, 0), 1D, 48D, 1F));
            stackList.add(new WrappedStack(getObject(input, 1), 29D, 48D, 1F));
            stackList.add(new WrappedStack(getObject(input, 2), 56D, 48D, 1F));
        } else if (length == 4) {
            stackList.add(new WrappedStack(getObject(input, 0), 1D, 2D, 1F));
            stackList.add(new WrappedStack(getObject(input, 1), 29D, 2D, 1F));
            stackList.add(new WrappedStack(getObject(input, 2), 1D, 48D, 1F));
            stackList.add(new WrappedStack(getObject(input, 3), 29D, 48D, 1F));
        } else {
            stackList.add(new WrappedStack(getObject(input, 0), 1D, 2D, 1F));
            stackList.add(new WrappedStack(getObject(input, 1), 29D, 2D, 1F));
            stackList.add(new WrappedStack(getObject(input, 2), 56D, 2D, 1F));

            stackList.add(new WrappedStack(getObject(input, 3), 1D, 48D, 1F));
            stackList.add(new WrappedStack(getObject(input, 4), 29D, 48D, 1F));
            stackList.add(new WrappedStack(getObject(input, 5), 56D, 48D, 1F));

            stackList.add(new WrappedStack(getObject(input, 6), 1D, 92D, 1F));
            stackList.add(new WrappedStack(getObject(input, 7), 29D, 92D, 1F));
            stackList.add(new WrappedStack(getObject(input, 8), 56D, 92D, 1F));
        }

        unique = "";
        for (Object o : input) {
            if (o instanceof List) {
                unique += ":" + getMostCommonName((ArrayList<ItemStack>) o);
            } else if (o instanceof ItemStack) {
                unique += ":" + Item.itemRegistry.getNameForObject(((ItemStack) o).getItem());
                unique += ":" + ((ItemStack) o).getItemDamage();
            }
        }
    }

    public String getMostCommonName(ArrayList<ItemStack> stacks) {
        HashMap<String, Integer> map = new HashMap();
        for (ItemStack stack : stacks) {
            int[] ids = OreDictionary.getOreIDs(stack);
            for (int i : ids) {
                String name = OreDictionary.getOreName(i);
                if (map.containsKey(name)) {
                    int value = map.get(name) + 1;
                    map.put(name, value);
                } else map.put(name, 1);
            }
        }

        String highest = "";
        int highest_value = 0;
        for (String string : map.keySet()) {
            int value = map.get(string);
            if (value > highest_value) {
                highest_value = value;
                highest = string;
            }
        }

        return highest;
    }

    @Override
    public String getUniqueName() {
        return getRecipeName() + unique;
    }

    protected String getRecipeName() {
        return "ShapedOre";
    }

    @Override
    public void addRecipes(ItemStack output, List<IRecipeHandler> list) {
        for (IRecipe check : (ArrayList<IRecipe>) CraftingManager.getInstance().getRecipeList()) {
            ItemStack stack = check.getRecipeOutput();
            if (stack == null || (!(check.getClass() != getRecipeClass()))) continue;
            if (stack.isItemEqual(output)) {
                try {
                    list.add((IRecipeHandler) Class.forName(getRecipeClass().getName()).getConstructor(IRecipe.class).newInstance(check));
                } catch (Exception e) {}
            }
        }
    }

    protected Class getRecipeClass() {
        return null;
    }

    @Override
    public double getHeight(double width) {
        return width / 2.5D;
    }

    @Override
    public double getWidth(double width) {
        return width;
    }

    @Override
    public float getSize(double width) {
        return (float) (width / 110D);
    }

    @Override
    public void draw() {
        drawBackground();
        for (IItemStack stack : stackList) {
            EnchiridionAPI.draw.drawStack(stack, stack.getX(), stack.getY(), stack.getScale());
        }
    }

    protected void drawBackground() {
        Minecraft.getMinecraft().getTextureManager().bindTexture(location);
        EnchiridionAPI.draw.drawTexturedRect(0D, 0D, 0, 0, 58, 58);
        EnchiridionAPI.draw.drawTexturedRect(84D, 50D, 1, 63, 20, 14);
    }
}

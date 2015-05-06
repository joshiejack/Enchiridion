package joshie.enchiridion.designer.recipe;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import joshie.enchiridion.api.IRecipeHandler;
import joshie.enchiridion.designer.DesignerHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class RecipeHandlerShapedOre implements IRecipeHandler {
    private ShapedOreRecipe recipe;

    public RecipeHandlerShapedOre() {}
    public RecipeHandlerShapedOre(ShapedOreRecipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public void addRecipes(ItemStack output, List<IRecipeHandler> list) {
        for (IRecipe check : (ArrayList<IRecipe>) CraftingManager.getInstance().getRecipeList()) {
            ItemStack stack = check.getRecipeOutput();
            if (stack == null || (!(check instanceof ShapedOreRecipe))) continue;

            if (stack.isItemEqual(output)) {
                list.add(new RecipeHandlerShapedOre((ShapedOreRecipe) check));
            }
        }
    }

    private static Field input;
    static {
        try {
            input = ShapedOreRecipe.class.getDeclaredField("input");
            input.setAccessible(true);
        } catch (Exception e) {}
    }

    private static ItemStack getStack(Object object) {       
        return (ItemStack) (object == null ? null : (object instanceof List) ? ((List)object).get(0) : (ItemStack) object);
    }

    @Override
    public void draw(int left, int top, double height, double width, float size) {
        DesignerHelper.drawStack(recipe.getRecipeOutput(), (int) (left + (width / 1.3D)), (int) (top + (height / 3.5D)), size * 1.75F);
        try {
            Object[] input = (Object[]) this.input.get(recipe);
            if (input.length == 9) {
                DesignerHelper.drawStack(getStack(input[0]), (int) (left + (width / 15D)), (int) (top + (height / 15D)), size);
                DesignerHelper.drawStack(getStack(input[1]), (int) (left + (width / 3.5D)), (int) (top + (height / 15D)), size);
                DesignerHelper.drawStack(getStack(input[2]), (int) (left + (width / 1.95D)), (int) (top + (height / 15D)), size);
                
                DesignerHelper.drawStack(getStack(input[3]), (int) (left + (width / 15D)), (int) (top + (width / 3.5D)), size);
                DesignerHelper.drawStack(getStack(input[4]), (int) (left + (width / 3.5D)), (int) (top + (width / 3.5D)), size);
                DesignerHelper.drawStack(getStack(input[5]), (int) (left + (width / 1.95D)), (int) (top + (width / 3.5D)), size);
                
                DesignerHelper.drawStack(getStack(input[6]), (int) (left + (width / 15D)), (int) (top + (width / 1.95D)), size);
                DesignerHelper.drawStack(getStack(input[7]), (int) (left + (width / 3.5D)), (int) (top + (width / 1.95D)), size);
                DesignerHelper.drawStack(getStack(input[8]), (int) (left + (width / 1.95D)), (int) (top + (width / 1.95D)), size);
            } else if (input.length == 4) {
                DesignerHelper.drawStack(getStack(input[0]), (int) (left + (width / 15D)), (int) (top + (height / 15D)), size * 1.5F);
                DesignerHelper.drawStack(getStack(input[1]), (int) (left + (width / 2.5D)), (int) (top + (height / 15D)), size * 1.5F);
                
                DesignerHelper.drawStack(getStack(input[2]), (int) (left + (width / 15D)), (int) (top + (width / 2.5D)), size * 1.5F);
                DesignerHelper.drawStack(getStack(input[3]), (int) (left + (width / 2.5D)), (int) (top + (width / 2.5D)), size * 1.5F);
            }

        } catch (Exception e) {}
    }
}

package joshie.enchiridion.api;

import java.util.List;

import net.minecraft.item.ItemStack;

public interface IRecipeHandler {
    /** Add recipes that are valid for this output item **/
    public void addRecipes(ItemStack output, List<IRecipeHandler> list);

    /** Draw this recipe in the book 
     * @param size 
     * @param width 
     * @param height 
     * @param top 
     * @param left **/
    public void draw(int left, int top, double height, double width, float size);
}

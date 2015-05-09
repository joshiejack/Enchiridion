package joshie.enchiridion.api;

import java.util.List;

import net.minecraft.item.ItemStack;

public interface IRecipeHandler {
    /** Add recipes that are valid for this output item **/
    public void addRecipes(ItemStack output, List<IRecipeHandler> list);

    /** Draw this recipe in the book, You can make use of the
     *  the helper functions in @IDrawHelper with access to an instance
     *  from @EnchiridionAPI **/
    public void draw();

    /** Return the height of this recipe handler, based on the width **/
    public double getHeight(double width);
    
    /** Return an adjusted width, based on the original **/
    public double getWidth(double width);

    /** Return the scale size, based on the width, for this recipe handler **/
    public float getSize(double width);

    /** Returns a unique name for this recipe, saved in the json **/
    public String getUniqueName();

    /** Add Tooltip **/
    public void addTooltip(List list);
}

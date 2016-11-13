package joshie.enchiridion.api.recipe;

import net.minecraft.item.ItemStack;

public interface IItemStack {
    /** @return Returns the current item stack **/
    ItemStack getItemStack();

    /** @return the xPosition of this stack **/
    double getX();

    /** @return the yPosition of this stack **/
    double getY();

    /** @return the scale of this stack **/
    float getScale();

    /** Called when rendered, to update the stack if necessary **/
    void onDisplayTick();
}
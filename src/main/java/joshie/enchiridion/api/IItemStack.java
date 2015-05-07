package joshie.enchiridion.api;

import net.minecraft.item.ItemStack;

public interface IItemStack {
    /** Returns the current item stack **/
    public ItemStack getItemStack();

    /** Return the xPosition of this stack **/
    public double getX();

    /** Return the yPosition of this stack **/
    public double getY();

    /** Return the scale of this stack **/
    public float getScale();
}

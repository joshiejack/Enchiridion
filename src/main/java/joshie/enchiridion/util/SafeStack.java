package joshie.enchiridion.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;

public class SafeStack {
    public ResourceLocation location;

    protected SafeStack(@Nonnull ItemStack stack) {
        if (!stack.isEmpty()) {
            location = ForgeRegistries.ITEMS.getKey(stack.getItem());
        }
    }

    @Nonnull
    public ItemStack toStack() {
        return new ItemStack(ForgeRegistries.ITEMS.getValue(location));
    }

    public static NonNullList<SafeStack> allInstances(@Nonnull ItemStack stack) {
        NonNullList<SafeStack> safe = NonNullList.create();
        safe.add(new SafeStackMod(stack));
        safe.add(new SafeStackNBT(stack));
        safe.add(new SafeStack(stack));
        return safe;
    }

    public static SafeStack newInstance(String modid, @Nonnull ItemStack stack, boolean matchNBT) {
        if (!modid.equals("IGNORE")) {
            return new SafeStackMod(stack);
        } else if (matchNBT) {
            return new SafeStackNBT(stack);
        } else return new SafeStack(stack);
    }

    public static class SafeStackMod extends SafeStack {
        public String modid;

        protected SafeStackMod(@Nonnull ItemStack stack) {
            super(stack);

            ResourceLocation key = ForgeRegistries.ITEMS.getKey(stack.getItem());
            modid = key.getPath();
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            return prime * ((modid == null) ? 0 : modid.hashCode());
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (getClass() != obj.getClass()) return false;
            SafeStackMod other = (SafeStackMod) obj;
            if (modid == null) {
                if (other.modid != null) return false;
            } else if (!modid.equals(other.modid)) return false;
            return true;
        }
    }

    public static class SafeStackNBT extends SafeStack {
        CompoundNBT tag;

        SafeStackNBT(@Nonnull ItemStack stack) {
            super(stack);
            this.tag = stack.getTag();
        }

        @Override
        @Nonnull
        public ItemStack toStack() {
            ItemStack result = new ItemStack(ForgeRegistries.ITEMS.getValue(location));
            CompoundNBT copy = tag.copy();
            result.setTag(copy);
            return result;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = super.hashCode();
            result = prime * result + ((tag == null) ? 0 : tag.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!super.equals(obj)) return false;
            if (getClass() != obj.getClass()) return false;
            SafeStackNBT other = (SafeStackNBT) obj;
            if (tag == null) {
                if (other.tag != null) return false;
            } else if (!tag.equals(other.tag)) return false;
            return true;
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((location == null) ? 0 : location.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        SafeStack other = (SafeStack) obj;
        if (location == null) {
            if (other.location != null) return false;
        } else if (!location.equals(other.location)) return false;
        return true;
    }
}
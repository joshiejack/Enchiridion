package joshie.enchiridion.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;

public class SafeStack {
    public ResourceLocation location;

    protected SafeStack(@Nonnull ItemStack stack) {
        if (!stack.isEmpty()) {
            location = Item.REGISTRY.getNameForObject(stack.getItem());
        }
    }

    @Nonnull
    public ItemStack toStack() {
        return new ItemStack(Item.REGISTRY.getObject(location), 1, OreDictionary.WILDCARD_VALUE);
    }

    public static NonNullList<SafeStack> allInstances(@Nonnull ItemStack stack) {
        NonNullList<SafeStack> safe = NonNullList.create();
        safe.add(new SafeStackMod(stack));
        int[] ids = OreDictionary.getOreIDs(stack);
        for (int i : ids) {
            safe.add(new SafeStackOre(OreDictionary.getOreName(i)));
        }

        safe.add(new SafeStackMod(stack));
        safe.add(new SafeStackDamage(stack));
        safe.add(new SafeStackNBT(stack));
        safe.add(new SafeStackNBTDamage(stack));
        safe.add(new SafeStack(stack));
        return safe;
    }

    public static SafeStack newInstance(String modid, @Nonnull ItemStack stack, String oreName, boolean matchDamage, boolean matchNBT) {
        if (!modid.equals("IGNORE")) {
            return new SafeStackMod(stack);
        } else if (!oreName.equals("IGNORE")) {
            return new SafeStackOre(oreName);
        } else if (matchNBT && matchDamage) {
            return new SafeStackNBTDamage(stack);
        } else if (matchNBT) {
            return new SafeStackNBT(stack);
        } else if (matchDamage) {
            return new SafeStackDamage(stack);
        } else return new SafeStack(stack);
    }

    public static class SafeStackOre extends SafeStack {
        String oreName;

        SafeStackOre(String oreName) {
            super(ItemStack.EMPTY);
            this.oreName = oreName;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = super.hashCode();
            result = prime * result + ((oreName == null) ? 0 : oreName.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!super.equals(obj)) return false;
            if (getClass() != obj.getClass()) return false;
            SafeStackOre other = (SafeStackOre) obj;
            if (oreName == null) {
                if (other.oreName != null) return false;
            } else if (!oreName.equals(other.oreName)) return false;
            return true;
        }
    }

    public static class SafeStackMod extends SafeStack {
        public String modid;

        protected SafeStackMod(@Nonnull ItemStack stack) {
            super(stack);

            ResourceLocation key = Item.REGISTRY.getNameForObject(stack.getItem());
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

    public static class SafeStackDamage extends SafeStack {
        int damage;

        SafeStackDamage(@Nonnull ItemStack stack) {
            super(stack);
            this.damage = stack.getItemDamage();
        }

        @Override
        @Nonnull
        public ItemStack toStack() {
            return new ItemStack(Item.REGISTRY.getObject(location), 1, damage);
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = super.hashCode();
            result = prime * result + damage;
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!super.equals(obj)) return false;
            if (getClass() != obj.getClass()) return false;
            SafeStackDamage other = (SafeStackDamage) obj;
            return damage == other.damage;
        }
    }

    public static class SafeStackNBT extends SafeStack {
        NBTTagCompound tag;

        SafeStackNBT(@Nonnull ItemStack stack) {
            super(stack);
            this.tag = stack.getTagCompound();
        }

        @Override
        @Nonnull
        public ItemStack toStack() {
            ItemStack result = new ItemStack(Item.REGISTRY.getObject(location), 1, OreDictionary.WILDCARD_VALUE);
            NBTTagCompound copy = tag.copy();
            result.setTagCompound(copy);
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

    public static class SafeStackNBTDamage extends SafeStackDamage {
        NBTTagCompound tag;

        SafeStackNBTDamage(ItemStack stack) {
            super(stack);
            this.tag = stack.getTagCompound();
        }

        @Override
        @Nonnull
        public ItemStack toStack() {
            ItemStack result = new ItemStack(Item.REGISTRY.getObject(location), 1, damage);
            NBTTagCompound copy = tag.copy();
            result.setTagCompound(copy);
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
            SafeStackNBTDamage other = (SafeStackNBTDamage) obj;
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
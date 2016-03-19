package joshie.enchiridion.util;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;

public class SafeStack {
    public ResourceLocation location;

    protected SafeStack(ItemStack stack) {
        if (stack != null) {
            location = Item.itemRegistry.getNameForObject(stack.getItem());
        }
    }

    public static List<SafeStack> allInstances(ItemStack stack) {
        List<SafeStack> safe = new ArrayList();
        safe.add(new SafeStackMod(stack));
        int[] ids = OreDictionary.getOreIDs(stack);
        for (int i : ids) {
            safe.add(new SafeStackOre(stack, OreDictionary.getOreName(i)));
        }

        safe.add(new SafeStackMod(stack));
        safe.add(new SafeStackDamage(stack));
        safe.add(new SafeStackNBT(stack));
        safe.add(new SafeStackNBTDamage(stack));
        safe.add(new SafeStack(stack));
        return safe;
    }

    public static SafeStack newInstance(String modid, ItemStack stack, String orename, boolean matchDamage, boolean matchNBT) {
        if (!modid.equals("IGNORE")) {
            return new SafeStackMod(stack);
        } else if (!orename.equals("IGNORE")) {
            return new SafeStackOre(stack, orename);
        } else if (matchNBT && matchDamage) {
            return new SafeStackNBTDamage(stack);
        } else if (matchNBT) {
            return new SafeStackNBT(stack);
        } else if (matchDamage) {
            return new SafeStackDamage(stack);
        } else return new SafeStack(stack);
    }

    public static class SafeStackOre extends SafeStack {
        public String orename;

        protected SafeStackOre(ItemStack stack, String orename) {
            super(null);
            this.orename = orename;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = super.hashCode();
            result = prime * result + ((orename == null) ? 0 : orename.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!super.equals(obj)) return false;
            if (getClass() != obj.getClass()) return false;
            SafeStackOre other = (SafeStackOre) obj;
            if (orename == null) {
                if (other.orename != null) return false;
            } else if (!orename.equals(other.orename)) return false;
            return true;
        }
    }

    public static class SafeStackMod extends SafeStack {
        public String modid;

        protected SafeStackMod(ItemStack stack) {
            super(stack);

            ResourceLocation key = Item.itemRegistry.getNameForObject(stack.getItem());
            modid = key.getResourceDomain();
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = prime * ((modid == null) ? 0 : modid.hashCode());
            return result;
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
        public int damage;

        protected SafeStackDamage(ItemStack stack) {
            super(stack);
            this.damage = stack.getItemDamage();
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
            if (damage != other.damage) return false;
            return true;
        }
    }

    public static class SafeStackNBT extends SafeStack {
        public NBTTagCompound tag;

        protected SafeStackNBT(ItemStack stack) {
            super(stack);
            this.tag = stack.getTagCompound();
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
        public NBTTagCompound tag;

        protected SafeStackNBTDamage(ItemStack stack) {
            super(stack);
            this.tag = stack.getTagCompound();
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
package joshie.enchiridion.helpers;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.List;

public class StackHelper {
    @Nonnull
    public static ItemStack getStackFromString(String str) {
        if (str == null || str.equals("")) return ItemStack.EMPTY;
        return getStackFromArray(str.trim().split(" "));
    }

    public static String getStringFromObject(Object object) {
        if (object instanceof Item) {
            return getStringFromStack(new ItemStack((Item) object));
        } else if (object instanceof Block) {
            return getStringFromStack(new ItemStack((Block) object));
        } else if (object instanceof ItemStack) {
            return getStringFromStack((ItemStack) object);
        } else if (object instanceof String) {
            return (String) object;
        } else if (object instanceof List) {
            return getStringFromStack((ItemStack) ((List) object).get(0));
        } else return "";
    }

    public static String getStringFromStack(@Nonnull ItemStack stack) {
        String str = String.valueOf(ForgeRegistries.ITEMS.getKey(stack.getItem())).replace(" ", "%20");
        if (stack.isDamageable()) {
            str = str + " " + stack.getDamage();
        }

        if (stack.getCount() > 1) {
            str = str + " *" + stack.getCount();
        }

        if (stack.hasTag()) {
            str = str + " " + stack.getTag();
        }
        return str;
    }

    private static CompoundNBT getTag(String[] str, int pos) {
        String s = formatNBT(str, pos).getUnformattedComponentText();
        try {
            return JsonToNBT.getTagFromJson(s);
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean isNBT(String str) {
        return str.startsWith("{");
    }

    public static boolean isAmount(String str) {
        return str.startsWith("*");
    }

    @Nonnull
    private static ItemStack getStackFromArray(String[] str) {
        Item item = getItemByText(str[0]);
        if (item == null) return ItemStack.EMPTY;

        int amount = 1;
        ItemStack stack = new ItemStack(item, 1);
        CompoundNBT tag = null;

        for (int i = 1; i <= 3; i++) {
            if (str.length > i) {
                if (isAmount(str[i])) amount = parseAmount(str[i]);
                if (isNBT(str[i])) tag = getTag(str, i);
            }
        }

        stack.setTag(tag);
        stack.setCount(amount);
        return stack;
    }

    private static Item getItemByText(String str) {
        str = str.replace("%20", " ");
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(str));
        if (item == null) {
            try {
                item = Item.getItemById(Integer.parseInt(str));
            } catch (NumberFormatException ignored) {
            }
        }
        return item;
    }

    private static ITextComponent formatNBT(String[] str, int start) {
        StringTextComponent textComponentString = new StringTextComponent("");

        for (int j = start; j < str.length; ++j) {
            if (j > start) {
                textComponentString.appendText(" ");
            }
            Object object = new StringTextComponent(str[j]);
            textComponentString.appendSibling((ITextComponent) object);
        }

        return textComponentString;
    }

    private static int parseAmount(String str) {
        try {
            return Integer.parseInt(str.substring(1, str.length()));
        } catch (NumberFormatException numberformatexception) {
            return 0;
        }
    }
}
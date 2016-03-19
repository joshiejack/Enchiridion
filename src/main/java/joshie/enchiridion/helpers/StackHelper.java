package joshie.enchiridion.helpers;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ResourceLocation;

public class StackHelper {
    public static ItemStack getStackFromString(String str) {
        if (str == null || str.equals("")) return null;
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

    public static String getStringFromStack(ItemStack stack) {
        String str = Item.itemRegistry.getNameForObject(stack.getItem()).toString().replace(" ", "%20");
        if (stack.getHasSubtypes() || stack.isItemStackDamageable()) {
            str = str + " " + stack.getItemDamage();
        }

        if (stack.stackSize > 1) {
            str = str + " *" + stack.stackSize;
        }

        if (stack.hasTagCompound()) {
            str = str + " " + stack.getTagCompound().toString();
        }

        return str;
    }

    private static NBTTagCompound getTag(String[] str, int pos) {
        String s = formatNBT(str, pos).getUnformattedText();
        try {
            NBTBase nbtbase = JsonToNBT.getTagFromJson(s);
            if (!(nbtbase instanceof NBTTagCompound)) return null;
            return (NBTTagCompound) nbtbase;
        } catch (Exception nbtexception) {
            return null;
        }
    }

    public static boolean isMeta(String str) {
        return !isNBT(str) && !isAmount(str);
    }

    public static boolean isNBT(String str) {
        return str.startsWith("{");
    }

    public static boolean isAmount(String str) {
        return str.startsWith("*");
    }

    private static ItemStack getStackFromArray(String[] str) {
        Item item = getItemByText(str[0]);
        if (item == null) return null;

        int meta = 0;
        int amount = 1;
        ItemStack stack = new ItemStack(item, 1, meta);
        NBTTagCompound tag = null;

        for (int i = 1; i <= 3; i++) {
            if (str.length > i) {
                if (isMeta(str[i])) meta = parseMeta(str[i]);
                if (isAmount(str[i])) amount = parseAmount(str[i]);
                if (isNBT(str[i])) tag = getTag(str, i);
            }
        }

        stack.setItemDamage(meta);
        stack.setTagCompound(tag);
        stack.stackSize = amount;
        return stack;
    }

    private static Item getItemByText(String str) {
        str = str.replace("%20", " ");
        Item item = (Item) Item.itemRegistry.getObject(new ResourceLocation(str));
        if (item == null) {
            try {
                Item item1 = Item.getItemById(Integer.parseInt(str));
                item = item1;
            } catch (NumberFormatException numberformatexception) {
                ;
            }
        }

        return item;
    }

    private static IChatComponent formatNBT(String[] str, int start) {
        ChatComponentText chatcomponenttext = new ChatComponentText("");

        for (int j = start; j < str.length; ++j) {
            if (j > start) {
                chatcomponenttext.appendText(" ");
            }

            Object object = new ChatComponentText(str[j]);
            chatcomponenttext.appendSibling((IChatComponent) object);
        }

        return chatcomponenttext;
    }

    private static int parseMeta(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException numberformatexception) {
            return 0;
        }
    }

    private static int parseAmount(String str) {
        try {
            return Integer.parseInt(str.substring(1, str.length()));
        } catch (NumberFormatException numberformatexception) {
            return 0;
        }
    }
}

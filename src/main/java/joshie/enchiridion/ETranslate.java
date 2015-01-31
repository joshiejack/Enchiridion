package joshie.enchiridion;

import net.minecraft.util.StatCollector;

public class ETranslate {
    public static String translate(String text) {
        return StatCollector.translateToLocal("enchiridion." + text);
    }

    public static String translate(String string, Object... data) {
        return StatCollector.translateToLocalFormatted("enchiridion." + string, data);
    }
}

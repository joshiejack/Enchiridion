package joshie.enchiridion.lib;

import net.minecraft.util.StatCollector;

public class ETranslate {
    public static String translate(String text) {
        return StatCollector.translateToLocal("enchiridion." + text);
    }
}

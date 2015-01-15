package joshie.enchiridion;

import java.util.ArrayList;

import net.minecraftforge.common.config.Configuration;

import org.apache.logging.log4j.Level;

public class EConfig {
    public static boolean EDIT_ENABLED = true;
    private static ArrayList<String> colors = new ArrayList();
    private static final String[] default_colors = new String[] { "#000000", "#FFFFFF" };
    
    public static void init(Configuration config) {
        try {
            config.load();
            String[] color_list = config.get("Settings", "Edit Mode Colours", default_colors, 
                    "This is a list of the colours to show up in edit mode by default").getStringList();
            EDIT_ENABLED = config.get("Settings", "Enable Wiki Editing", true).getBoolean(true);
            for(String color: color_list) {
                addColor(color);
            }
       } catch (Exception e) {
            ELogger.log(Level.ERROR, "Enchiridion 2 failed to load it's config");
            e.printStackTrace();
        } finally {
            config.save();
        }
    }
    
    public static void addColor(String color) {
        color = color.replace("#", "");
        color = color.replace("0x", "");
        if(color.length() == 6) {
            color = "FF" + color;
        }
        
        boolean added = false;
        try {
            Long.parseLong(color, 16);
            added = true;
        } catch (Exception e) { added = false; }
        
        if(added) {
            colors.add(color);
        }
    }
    
    public static String getColor(int index) {        
        return (index >= colors.size() || index < 0)? null: colors.get(index);
    }
}

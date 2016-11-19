package joshie.enchiridion.gui.book;

import joshie.enchiridion.EConfig;
import joshie.enchiridion.api.gui.IBookEditorOverlay;
import joshie.enchiridion.util.IColorable;

public class GuiSimpleEditorColor extends GuiSimpleEditorAbstract {
    public static final GuiSimpleEditorColor INSTANCE = new GuiSimpleEditorColor();
    private static final String[][] COLORS = new String[][]{
            new String[]{"#ffbfbf", "#f99", "#ff7373", "#ff4d4d", "#ff2626", "#f00", "#d90000", "#b20000", "#8c0000", "#600", "#400000", "#000", "#fff", "#fff", "#fff"},
            new String[]{"#ffcfbf", "#ffb399", "#ff9673", "#ff7a4d", "#ff5c26", "#ff4000", "#d93600", "#b22d00", "#8c2300", "#661a00", "#401000", "#000", "#eee", "#f0f0e1", "#ececfb"},
            new String[]{"#ffdfbf", "#fc9", "#ffb973", "#ffa64d", "#ff9326", "#ff8000", "#d96d00", "#b25900", "#8c4600", "#630", "#402000", "#000", "#ddd", "#dfdfd0", "#dbdbea"},
            new String[]{"#ffefbf", "#ffe599", "#ffdc73", "#ffd24d", "#ffc926", "#ffbf00", "#d9a300", "#b28500", "#8c6900", "#664c00", "#403000", "#000", "#ccc", "#cecebf", "#cacad9"},
            new String[]{"#ffffbf", "#ff9", "#ffff73", "#ffff4d", "#ffff26", "#ff0", "#d9d900", "#b2b200", "#8c8c00", "#660", "#404000", "#000", "#bbb", "#bdbdae", "#b9b9c8"},

            new String[]{"#efffbf", "#e5ff99", "#dcff73", "#d2ff4d", "#c9ff26", "#bfff00", "#a3d900", "#85b200", "#698c00", "#4c6600", "#304000", "#000", "#aaa", "#acac9d", "#a8a8b7"},
            new String[]{"#dfffbf", "#cf9", "#b9ff73", "#a6ff4d", "#93ff26", "#80ff00", "#6dd900", "#59b200", "#468c00", "#360", "#204000", "#000", "#999", "#9b9b8c", "#9797a6"},
            new String[]{"#cfffbf", "#b3ff99", "#96ff73", "#7aff4d", "#5cff26", "#40ff00", "#36d900", "#2db200", "#238c00", "#1a6600", "#104000", "#000", "#888", "#8a8a7b", "#868695"},
            new String[]{"#bfffbf", "#9f9", "#73ff73", "#4dff4d", "#26ff26", "#0f0", "#00d900", "#00b200", "#008c00", "#060", "#004000", "#000", "#777", "#79796a", "#757584"},
            new String[]{"#bfffcf", "#99ffb3", "#73ff96", "#4dff7a", "#26ff5c", "#00ff40", "#00d936", "#00b22d", "#008c23", "#00661a", "#004010", "#000", "#666", "#686859", "#646473"},

            new String[]{"#bfffdf", "#9fc", "#73ffb9", "#4dffa6", "#26ff93", "#00ff80", "#00d96d", "#00b259", "#008c46", "#063", "#004020", "#000", "#555", "#575748", "#535362"},
            new String[]{"#bfffef", "#99ffe5", "#73ffdc", "#4dffd2", "#26ffc9", "#00ffbf", "#00d9a3", "#00b285", "#008c69", "#00664c", "#004030", "#000", "#444", "#464637", "#424251"},
            new String[]{"#bfffff", "#9ff", "#73ffff", "#4dffff", "#26ffff", "#0ff", "#00d9d9", "#00b2b2", "#008c8c", "#066", "#004040", "#000", "#333", "#353526", "#313140"},
            new String[]{"#bfefff", "#99e5ff", "#73dcff", "#4dd2ff", "#26c9ff", "#00bfff", "#00a3d9", "#0085b2", "#00698c", "#004c66", "#003040", "#000", "#222", "#242415", "#20202f"},
            new String[]{"#bfdfff", "#9cf", "#73b9ff", "#4da6ff", "#2693ff", "#0080ff", "#006dd9", "#0059b2", "#00468c", "#036", "#002040", "#000", "#111", "#131304", "#0f0f1e"},

            new String[]{"#bfcfff", "#99b3ff", "#7396ff", "#4d7aff", "#265cff", "#0040ff", "#0036d9", "#002db2", "#00238c", "#001a66", "#001040", "#000", "#000", "#000", "#000"},
            new String[]{"#bfbfff", "#99f", "#7373ff", "#4d4dff", "#2626ff", "#00f", "#0000d9", "#0000b2", "#00008c", "#006", "#000040", "#000", "#000", "#000", "#000"},
            new String[]{"#cfbfff", "#b399ff", "#9673ff", "#7a4dff", "#5c26ff", "#4000ff", "#3600d9", "#2d00b2", "#23008c", "#1a0066", "#100040", "#000", "#f00", "#007fff", "#00f"},
            new String[]{"#dfbfff", "#c9f", "#b973ff", "#a64dff", "#9326ff", "#8000ff", "#6d00d9", "#5900b2", "#46008c", "#306", "#200040", "#000", "#ff7f00", "#0ff", "#7f00ff"},
            new String[]{"#efbfff", "#e599ff", "#dc73ff", "#d24dff", "#c926ff", "#bf00ff", "#a300d9", "#8500b2", "#69008c", "#4c0066", "#300040", "#000", "#ff0", "#00ff7f", "#f0f"},
            new String[]{"#ffbfff", "#f9f", "#ff73ff", "#ff4dff", "#ff26ff", "#f0f", "#d900d9", "#b200b2", "#8c008c", "#606", "#400040", "#000", "#7fff00", "#0f0", "#ff007f"}
    };

    private static int[][] colorsI;

    static {
        colorsI = new int[COLORS.length][];
        for (int y = 0; y < COLORS.length; y++) {
            int[] color = new int[15];
            for (int x = 0; x < 15; x++) {
                String colorS = COLORS[y][x];
                try {
                    if (colorS.length() == 4) colorS = colorS.replaceAll(".", "$0$0");
                    color[x] = (int) Long.parseLong("ff" + colorS.replace("#", ""), 16);
                } catch (Exception ignored) {
                }
            }

            colorsI[y] = color;
        }
    }

    private static IColorable colorable = null;

    protected GuiSimpleEditorColor() {
    }

    public IBookEditorOverlay setColorable(IColorable colorable) {
        GuiSimpleEditorColor.colorable = colorable;
        return this;
    }

    private boolean isOverColor(int x, int y, int mouseX, int mouseY) {
        if (mouseX >= EConfig.editorXPos + (y * 5) + 5 && mouseX <= EConfig.editorXPos + (y * 5) + 10) {
            if (mouseY >= EConfig.toolbarYPos + (x * 5) + 11 && mouseY <= EConfig.toolbarYPos + (x * 5) + 16) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        drawBorderedRectangle(4, 11, 81, 118, 0xFF392F27, 0xFF392F27);
        for (int x = 0; x < 21; x++) {
            for (int y = 0; y < 15; y++) {
                drawRectangle((y * 5) + 5, (x * 5) + 12, (y * 5) + 10, (x * 5) + 17, colorsI[x][y]);
            }
        }
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY) {
        for (int x = 0; x < 21; x++) {
            for (int y = 0; y < 15; y++) {
                if (isOverColor(x, y, mouseX, mouseY)) {
                    colorable.setColorAsHex(Integer.toHexString(colorsI[x][y]));
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public void updateSearch(String search) {
        try {
            colorable.setColorAsHex(search);
        } catch (Exception ignored) {
        }
    }
}
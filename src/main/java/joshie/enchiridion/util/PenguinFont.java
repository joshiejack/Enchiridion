package joshie.enchiridion.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;

import java.util.Locale;

public class PenguinFont extends FontRenderer {
    public static PenguinFont INSTANCE = null;
    private boolean resetColor = false;

    public static void load() {
        Minecraft mc = Minecraft.getMinecraft();
        INSTANCE = new PenguinFont(mc.gameSettings, new ResourceLocation("textures/font/ascii.png"), mc.renderEngine, false);
        if (mc.getLanguageManager() != null) {
            INSTANCE.setUnicodeFlag(mc.fontRendererObj.getUnicodeFlag());
            INSTANCE.setBidiFlag(mc.getLanguageManager().isCurrentLanguageBidirectional());
        }

        ((IReloadableResourceManager) mc.getResourceManager()).registerReloadListener(INSTANCE);
    }

    private PenguinFont(GameSettings gameSettingsIn, ResourceLocation location, TextureManager textureManagerIn, boolean unicode) {
        super(gameSettingsIn, location, textureManagerIn, unicode);
    }

    public String stripFormatting(String text) {
        String ret = text;
        for (CharReplace c: CharReplace.values()) {
            ret = ret.replace(c.search, "");
        }

        return ret;
    }

    private final static String start = "\u00a7";

    public String replaceFormatting(String text) {
        String ret = text;
        for (CharReplace c: CharReplace.values()) {
            ret = ret.replace(c.search, "" + start + c.character);
        }

        return ret.replace("\r", " ").replace("\t", "    ");
    }

    @Override
    public int getStringWidth(String text) {
        return super.getStringWidth(replaceFormatting(text));
    }

    @Override
    public String wrapFormattedStringToWidth(String str, int wrapWidth) {
        if (str == null || wrapWidth <= 1) {
            return "";
        } else return super.wrapFormattedStringToWidth(str, wrapWidth);
    }

    @Override
    public void drawSplitString(String text, int x, int y, int wrapWidth, int textColor) {
        originalColor = textColor;
        super.drawSplitString(replaceFormatting(text), x, y, wrapWidth, textColor);
    }

    @Override
    public int drawStringWithShadow(String text, float x, float y, int color) {
        return super.drawStringWithShadow(replaceFormatting(text), x, y, color);
    }

    private static enum CharReplace {
        BOLD_S("[b]", 'l'), BOLD_F("[/b]", 'r'), 
        ITALIC_S("[i]", 'o'), ITALIC_F("[/i]", 'r'), 
        STRIKE_S("[s]", 'm'), STRIKE_F("[/s]", 'r'), 
        UNDER_S("[u]", 'n'), UNDER_F("[/u]", 'r'), 
        CURSOR("[*cursor*]", 's'), CURSOR_HIDE("[*/cursor*]", 't'),
        COLOR_F("[/color]", 'r'), COLOR_BLACK("[color=black]", '0'),
        COLOR_D_BLUE("[color=dark_blue]", '1'), COLOR_D_GREEN("[color=dark_green]", '2'),
        COLOR_D_AQUA("[color=dark_aqua]", '3'), COLOR_D_RED("[color=dark_red]", '4'),
        COLOR_D_PURP("[color=dark_purple]", '5'), COLOR_GOLD("[color=gold]", '6'),
        COLOR_GRAY("[color=gray]", '7'), COLOR_GREY("[color=grey]", '7'),
        COLOR_D_GRAY("[color=dark_gray]", '8'), COLOR_D_GREY("[color=dark_grey]", '8'),
        COLOR_BLUE("[color=blue]", '9'), COLOR_GREEN("[color=green]", 'a'),
        COLOR_AQUA("[color=aqua]", 'b'), COLOR_RED("[color=red]", 'c'),
        COLOR_L_PURP("[color=light_purple]", 'd'), COLOR_YELLOW("[color=yellow]", 'e'),
        COLOR_WHITE("[color=white]", 'f');

        protected final String search;
        protected final char character;

        private CharReplace(String search, int character) {
            this.search = search;
            this.character = (char) character;
        }

        public boolean is(char char2) {
            return character == char2;
        }
    }

    private boolean cursor = false;
    private boolean white = false;
    private int originalColor;

    @Override
    public void resetStyles() {
        super.resetStyles();
        this.cursor = false;
        this.resetColor = false;
    }

    @Override
    public void renderStringAtPos(String text, boolean shadow)
    {
        for (int i = 0; i < text.length(); ++i)
        {
            char c0 = text.charAt(i);

            if (c0 == 167 && i + 1 < text.length())
            {
                int i1 = "0123456789abcdefklmnorst".indexOf(text.toLowerCase(Locale.ENGLISH).charAt(i + 1));

                if (i1 < 16)
                {
                    this.randomStyle = false;
                    this.boldStyle = false;
                    this.strikethroughStyle = false;
                    this.underlineStyle = false;
                    this.italicStyle = false;
                    this.cursor = false;

                    if (i1 < 0 || i1 > 15)
                    {
                        i1 = 15;
                    }

                    if (shadow)
                    {
                        i1 += 16;
                    }

                    int j1 = this.colorCode[i1];
                    this.textColor = j1;
                    
                    setColor((float)(j1 >> 16) / 255.0F, (float)(j1 >> 8 & 255) / 255.0F, (float)(j1 & 255) / 255.0F, this.alpha);
                }
                else if (i1 == 16)
                {
                    this.randomStyle = true;
                }
                else if (i1 == 17)
                {
                    this.boldStyle = true;
                }
                else if (i1 == 18)
                {
                    this.strikethroughStyle = true;
                }
                else if (i1 == 19)
                {
                    this.underlineStyle = true;
                }
                else if (i1 == 20)
                {
                    this.italicStyle = true;
                }
                else if (i1 == 22 || i1 == 23) {
                    this.cursor = true;
                    this.white = i1 == 22;
                }
                else if (i1 == 21)
                {
                    this.cursor = false;
                    this.randomStyle = false;
                    this.boldStyle = false;
                    this.strikethroughStyle = false;
                    this.underlineStyle = false;
                    this.italicStyle = false;
                    this.resetColor = true;
                    this.textColor = originalColor;
                    setColor(this.red, this.blue, this.green, this.alpha);
                }

                ++i;
            }
            else
            {
                int j = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".indexOf(c0);

                if (this.randomStyle && j != -1)
                {
                    int k = this.getCharWidth(c0);
                    char c1;

                    while (true)
                    {
                        j = this.fontRandom.nextInt("\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".length());
                        c1 = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".charAt(j);

                        if (k == this.getCharWidth(c1))
                        {
                            break;
                        }
                    }

                    c0 = c1;
                }

                float f1 = j == -1 || this.unicodeFlag ? 0.5f : 1f;
                boolean flag = (c0 == 0 || j == -1 || this.unicodeFlag) && shadow;

                if (flag)
                {
                    this.posX -= f1;
                    this.posY -= f1;
                }

                float f = this.renderChar(c0, this.italicStyle);

                if (flag)
                {
                    this.posX += f1;
                    this.posY += f1;
                }

                if (this.boldStyle)
                {
                    this.posX += f1;

                    if (flag)
                    {
                        this.posX -= f1;
                        this.posY -= f1;
                    }

                    this.renderChar(c0, this.italicStyle);
                    this.posX -= f1;

                    if (flag)
                    {
                        this.posX += f1;
                        this.posY += f1;
                    }

                    ++f;
                }
                doDraw(f);
            }
        }
    }

    @Override
    public void doDraw(float f)
    {
        {
            {

                if (this.cursor && this.white)
                {
                    GlStateManager.pushMatrix();
                    Tessellator tessellator = Tessellator.getInstance();
                    VertexBuffer worldrenderer = tessellator.getBuffer();
                    GlStateManager.disableTexture2D();
                    worldrenderer.begin(7, DefaultVertexFormats.POSITION);
                    worldrenderer.pos((double)this.posX - 0.75F, (double)(this.posY + (float)(this.FONT_HEIGHT)), 0.0D).endVertex();
                    worldrenderer.pos((double)(this.posX - 0.25F), (double)(this.posY + (float)(this.FONT_HEIGHT)), 0.0D).endVertex();
                    worldrenderer.pos((double)(this.posX - 0.25F), (double)(this.posY + (float)(this.FONT_HEIGHT - 10F)), 0.0D).endVertex();
                    worldrenderer.pos((double)this.posX - 0.75F, (double)(this.posY + (float)(this.FONT_HEIGHT - 10F)), 0.0D).endVertex();
                    tessellator.draw();
                    GlStateManager.enableTexture2D();
                    GlStateManager.popMatrix();
                    this.cursor = false;
                }
                
                if (this.strikethroughStyle)
                {
                    Tessellator tessellator = Tessellator.getInstance();
                    VertexBuffer worldrenderer = tessellator.getBuffer();
                    GlStateManager.disableTexture2D();
                    worldrenderer.begin(7, DefaultVertexFormats.POSITION);
                    worldrenderer.pos((double)this.posX, (double)(this.posY + (float)(this.FONT_HEIGHT / 2)), 0.0D).endVertex();
                    worldrenderer.pos((double)(this.posX + f), (double)(this.posY + (float)(this.FONT_HEIGHT / 2)), 0.0D).endVertex();
                    worldrenderer.pos((double)(this.posX + f), (double)(this.posY + (float)(this.FONT_HEIGHT / 2) - 1.0F), 0.0D).endVertex();
                    worldrenderer.pos((double)this.posX, (double)(this.posY + (float)(this.FONT_HEIGHT / 2) - 1.0F), 0.0D).endVertex();
                    tessellator.draw();
                    GlStateManager.enableTexture2D();
                }

                if (this.underlineStyle)
                {
                    Tessellator tessellator1 = Tessellator.getInstance();
                    VertexBuffer worldrenderer1 = tessellator1.getBuffer();
                    GlStateManager.disableTexture2D();
                    worldrenderer1.begin(7, DefaultVertexFormats.POSITION);
                    int l = this.underlineStyle ? -1 : 0;
                    worldrenderer1.pos((double)(this.posX + (float)l), (double)(this.posY + (float)this.FONT_HEIGHT), 0.0D).endVertex();
                    worldrenderer1.pos((double)(this.posX + f), (double)(this.posY + (float)this.FONT_HEIGHT), 0.0D).endVertex();
                    worldrenderer1.pos((double)(this.posX + f), (double)(this.posY + (float)this.FONT_HEIGHT - 1.0F), 0.0D).endVertex();
                    worldrenderer1.pos((double)(this.posX + (float)l), (double)(this.posY + (float)this.FONT_HEIGHT - 1.0F), 0.0D).endVertex();
                    tessellator1.draw();
                    GlStateManager.enableTexture2D();
                }

                this.posX += (float)((int)f);
            }
        }
    }
}

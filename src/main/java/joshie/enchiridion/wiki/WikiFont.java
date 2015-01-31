package joshie.enchiridion.wiki;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class WikiFont extends FontRenderer {
    private boolean isHidden;
    private boolean isColored;
    private int renderColor = 0xFFFFFF;
    private boolean cursor;

    public WikiFont(GameSettings settings, ResourceLocation resource, TextureManager tm, boolean unicodeflag) {
        super(settings, resource, tm, unicodeflag);
    }

    private int isFormatCode(String string, int i) {
        char character = string.charAt(i);
        if (CharReplace.BOLD_S.is(character)) {
            this.boldStyle = true;
        } else if (CharReplace.BOLD_F.is(character)) {
            this.boldStyle = false;
        } else if (CharReplace.ITALIC_S.is(character)) {
            this.italicStyle = true;
        } else if (CharReplace.ITALIC_F.is(character)) {
            this.italicStyle = false;
        } else if (CharReplace.RANDOM_S.is(character)) {
            this.randomStyle = true;
        } else if (CharReplace.RANDOM_F.is(character)) {
            this.randomStyle = false;
        } else if (CharReplace.STRIKE_S.is(character)) {
            this.strikethroughStyle = true;
        } else if (CharReplace.STRIKE_F.is(character)) {
            this.strikethroughStyle = false;
        } else if (CharReplace.UNDER_S.is(character)) {
            this.underlineStyle = true;
        } else if (CharReplace.UNDER_F.is(character)) {
            this.underlineStyle = false;
        } else if (CharReplace.CURSOR.is(character)) {
            this.cursor = true;
        } else if (CharReplace.CURSOR_HIDE.is(character)) {

        } else {
            return 0;
        }

        return 1;
    }

    @Override
    public void renderStringAtPos(String string, boolean p_78255_2_) {
        for (int i = 0; i < string.length(); ++i) {
            char character = string.charAt(i);
            int j;
            int k;
            
            if (isFormatCode(string, i) <= 0) {                
                j = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".indexOf(character);
                if (this.randomStyle && j != -1) {
                    do {
                        k = this.fontRandom.nextInt(this.charWidth.length);
                    } while (this.charWidth[j] != this.charWidth[k]);

                    j = k;
                }

                float f1 = this.unicodeFlag ? 0.5F : 1.0F;
                boolean flag1 = (character == 0 || j == -1 || this.unicodeFlag) && p_78255_2_;

                if (flag1) {
                    this.posX -= f1;
                    this.posY -= f1;
                }

                float f;
                if(CharReplace.HIDE.is(character)) {
                    f = 1F;
                } else {
                    f = this.renderCharAtPos(j, character, this.italicStyle);
                }

                if (flag1) {
                    this.posX += f1;
                    this.posY += f1;
                }

                if (this.boldStyle) {
                    this.posX += f1;

                    if (flag1) {
                        this.posX -= f1;
                        this.posY -= f1;
                    }

                    this.renderCharAtPos(j, character, this.italicStyle);
                    this.posX -= f1;

                    if (flag1) {
                        this.posX += f1;
                        this.posY += f1;
                    }

                    ++f;
                }
                
                

                Tessellator tessellator;

                if (this.strikethroughStyle) {
                    tessellator = Tessellator.instance;
                    GL11.glDisable(GL11.GL_TEXTURE_2D);
                    tessellator.startDrawingQuads();
                    tessellator.addVertex((double) this.posX, (double) (this.posY + (float) (this.FONT_HEIGHT / 2)), 0.0D);
                    tessellator.addVertex((double) (this.posX + f), (double) (this.posY + (float) (this.FONT_HEIGHT / 2)), 0.0D);
                    tessellator.addVertex((double) (this.posX + f), (double) (this.posY + (float) (this.FONT_HEIGHT / 2) - 1.0F), 0.0D);
                    tessellator.addVertex((double) this.posX, (double) (this.posY + (float) (this.FONT_HEIGHT / 2) - 1.0F), 0.0D);
                    tessellator.draw();
                    GL11.glEnable(GL11.GL_TEXTURE_2D);
                }

                if (this.underlineStyle) {
                    tessellator = Tessellator.instance;
                    GL11.glDisable(GL11.GL_TEXTURE_2D);
                    tessellator.startDrawingQuads();
                    int l = this.underlineStyle ? -1 : 0;
                    tessellator.addVertex((double) (this.posX + (float) l), (double) (this.posY + (float) this.FONT_HEIGHT), 0.0D);
                    tessellator.addVertex((double) (this.posX + f), (double) (this.posY + (float) this.FONT_HEIGHT), 0.0D);
                    tessellator.addVertex((double) (this.posX + f), (double) (this.posY + (float) this.FONT_HEIGHT - 1.0F), 0.0D);
                    tessellator.addVertex((double) (this.posX + (float) l), (double) (this.posY + (float) this.FONT_HEIGHT - 1.0F), 0.0D);
                    tessellator.draw();
                    GL11.glEnable(GL11.GL_TEXTURE_2D);
                }

                if (this.cursor) {                            
                    float red = (0xCCCCCC >> 16 & 255) / 255.0F;
                    float green = (0xCCCCCC >> 8 & 255) / 255.0F;
                    float blue = (0xCCCCCC & 255) / 255.0F;
                    tessellator = Tessellator.instance;
                    GL11.glDisable(GL11.GL_TEXTURE_2D);
                    tessellator.startDrawingQuads();
                    tessellator.addVertex((double) (this.posX - 0.75F), (double) (this.posY + (float) this.FONT_HEIGHT), 0.0D);
                    tessellator.addVertex((double) (this.posX) - 0.25F, (double) (this.posY + (float) this.FONT_HEIGHT), 0.0D);
                    tessellator.addVertex((double) (this.posX) - 0.25F, (double) (this.posY + (float) this.FONT_HEIGHT - 10.0F), 0.0D);
                    tessellator.addVertex((double) (this.posX - 0.75F), (double) (this.posY + (float) this.FONT_HEIGHT - 10.0F), 0.0D);
                    tessellator.draw();
                    GL11.glEnable(GL11.GL_TEXTURE_2D);
                    this.cursor = false;
                }

                this.posX += (float) ((int) f);
            }
        }
    }

    private static enum CharReplace {
        BOLD_S("[b]", 1), BOLD_F("[/b]", 2), 
        ITALIC_S("[i]", 3), ITALIC_F("[/i]", 4), 
        STRIKE_S("[s]", 5), STRIKE_F("[/s]", 6), 
        UNDER_S("[u]", 7), UNDER_F("[/u]", 8), 
        RANDOM_S("[r]", 9), RANDOM_F("[/r]", 10), 
        CURSOR("[*cursor*]", 11), CURSOR_HIDE("[*/cursor*]", 12),
        HIDE("@99[]", 13);

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

    @Override
    public int drawString(String string, int x, int y, int color) {
        String clone = new StringBuilder(string + CharReplace.HIDE.character).toString();
        for (CharReplace character : CharReplace.values()) {
            clone = clone.replace(character.search, "" + character.character);
        }
        
        return super.drawString(clone, x, y, color);
    }

    @Override
    public int getStringWidth(String string) {
        String clone = new StringBuilder(string).toString();
        for (CharReplace character : CharReplace.values()) {
            clone = clone.replace(character.search, "");
        }
        
        if(clone.contains("" + CharReplace.CURSOR.character) || clone.contains("" + CharReplace.CURSOR_HIDE.character)) {
            return super.getStringWidth(clone) - 10;
        }

        return super.getStringWidth(clone);
    }

    @Override
    public void drawSplitString(String string, int x, int y, int length, int color) {
        String clone = new StringBuilder(string + CharReplace.HIDE.character).toString();
        for (CharReplace character : CharReplace.values()) {
            clone = clone.replace(character.search, "" + character.character);
        }

        super.drawSplitString(clone, x, y, length, color);
    }

    public void drawUnformattedSplitString(String string, int x, int y, int length, int color) {
        String clone = new StringBuilder(string + CharReplace.HIDE.character).toString();
        clone = clone.replace(CharReplace.CURSOR.search, "" + CharReplace.CURSOR.character);
        clone = clone.replace(CharReplace.CURSOR_HIDE.search, "" + CharReplace.CURSOR_HIDE.character);
        super.drawSplitString(clone, x, y, length, color);
    }
}

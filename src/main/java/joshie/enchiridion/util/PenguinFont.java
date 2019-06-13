package joshie.enchiridion.util;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.fonts.Font;
import net.minecraft.client.gui.fonts.IGlyph;
import net.minecraft.client.gui.fonts.TexturedGlyph;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.resource.IResourceType;
import net.minecraftforge.resource.ISelectiveResourceReloadListener;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Predicate;

@OnlyIn(Dist.CLIENT)
public class PenguinFont extends FontRenderer implements ISelectiveResourceReloadListener { //TODO Check
    public static PenguinFont INSTANCE = null;
    private boolean resetColor = false;

    public static void load() {
        Minecraft mc = Minecraft.getInstance();
        INSTANCE = new PenguinFont(mc.textureManager, new Font(mc.textureManager, new ResourceLocation("textures/font/default.json")));

        ((IReloadableResourceManager) mc.getResourceManager()).addReloadListener(INSTANCE);
    }

    private PenguinFont(TextureManager textureManager, Font font) {
        super(textureManager, font);
    }

    public String stripFormatting(String text) {
        String ret = text;
        for (CharReplace c : CharReplace.values()) {
            ret = ret.replace(c.search, "");
        }

        return ret;
    }

    private final static String start = "\u00a7";

    public String replaceFormatting(String text) {
        String ret = text;
        for (CharReplace c : CharReplace.values()) {
            ret = ret.replace(c.search, "" + start + c.character);
        }

        return ret.replace("\r", " ").replace("\t", "    ");
    }

    @Override
    public int getStringWidth(String text) {
        return super.getStringWidth(replaceFormatting(text));
    }

    @Override
    @Nonnull
    public String wrapFormattedStringToWidth(String str, int wrapWidth) {
        if (wrapWidth <= 1) {
            return "";
        } else return super.wrapFormattedStringToWidth(str, wrapWidth);
    }

    @Override
    public void drawSplitString(@Nonnull String text, int x, int y, int wrapWidth, int textColor) {
        originalColor = textColor;
        super.drawSplitString(replaceFormatting(text), x, y, wrapWidth, textColor);
    }

    @Override
    public int drawStringWithShadow(String text, float x, float y, int color) {
        return super.drawStringWithShadow(replaceFormatting(text), x, y, color);
    }

    @Override
    public void onResourceManagerReload(@Nonnull IResourceManager manager, @Nonnull Predicate<IResourceType> predicate) {
        //TODO
    }

    private int originalColor;

    @Override
    public float renderStringAtPos(String text, float x, float y, int textColor, boolean hasShadow) { //Copied from FontRenderer. Changes made is commented
        float shadow = hasShadow ? 0.25F : 1.0F;
        float red = (float) (textColor >> 16 & 255) / 255.0F * shadow;
        float green = (float) (textColor >> 8 & 255) / 255.0F * shadow;
        float blue = (float) (textColor & 255) / 255.0F * shadow;
        float redCached = red;
        float greenCached = green;
        float blueCached = blue;
        float color = (float) (textColor >> 24 & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuffer();
        ResourceLocation texture = null;
        builder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        boolean isObfuscated = false;
        boolean isBold = false;
        boolean isItalic = false;
        boolean isUnderline = false;
        boolean isStrikethrough = false;
        boolean cursor = false; //Enchiridion
        boolean white = false; //Enchiridion
        List<PenguinFont.Entry> fontEntry = Lists.newArrayList();

        for (int size = 0; size < text.length(); ++size) {
            char character = text.charAt(size);
            if (character == 167 && size + 1 < text.length()) {
                TextFormatting textFormatting = TextFormatting.fromFormattingCode(text.charAt(size + 1));
                if (textFormatting != null) {
                    if (textFormatting.isNormalStyle()) {
                        isObfuscated = false;
                        isBold = false;
                        isStrikethrough = false;
                        isUnderline = false;
                        isItalic = false;
                        cursor = false; //Enchiridion
                        this.resetColor = true; //Enchiridion
                        textColor = originalColor; //Enchiridion
                        redCached = red;
                        greenCached = green;
                        blueCached = blue;
                    }

                    if (textFormatting.getColor() != null) {
                        int formatedColor = textFormatting.getColor();
                        redCached = (float) (formatedColor >> 16 & 255) / 255.0F * shadow;
                        greenCached = (float) (formatedColor >> 8 & 255) / 255.0F * shadow;
                        blueCached = (float) (formatedColor & 255) / 255.0F * shadow;
                    } else if (textFormatting == TextFormatting.OBFUSCATED) {
                        isObfuscated = true;
                    } else if (textFormatting == TextFormatting.BOLD) {
                        isBold = true;
                    } else if (textFormatting == TextFormatting.STRIKETHROUGH) {
                        isStrikethrough = true;
                    } else if (textFormatting == TextFormatting.UNDERLINE) {
                        isUnderline = true;
                    } else if (textFormatting == TextFormatting.ITALIC) {
                        isItalic = true;
                    }
                }
                ++size;
            } else {
                IGlyph glyph = this.font.findGlyph(character);
                TexturedGlyph texturedGlyph = isObfuscated && character != ' ' ? this.font.obfuscate(glyph) : this.font.getGlyph(character);
                ResourceLocation glyphTexture = texturedGlyph.getTextureLocation();
                float boldOffset;
                float shadowOffset;
                if (glyphTexture != null) {
                    if (texture != glyphTexture) {
                        tessellator.draw();
                        this.textureManager.bindTexture(glyphTexture);
                        builder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                        texture = glyphTexture;
                    }

                    boldOffset = isBold ? glyph.getBoldOffset() : 0.0F;
                    shadowOffset = hasShadow ? glyph.getShadowOffset() : 0.0F;
                    this.func_212452_a(texturedGlyph, isBold, isItalic, boldOffset, x + shadowOffset, y + shadowOffset, builder, redCached, greenCached, blueCached, color);
                }

                boldOffset = glyph.getAdvance(isBold);
                shadowOffset = hasShadow ? 1.0F : 0.0F;
                if (isStrikethrough) {
                    fontEntry.add(new PenguinFont.Entry(x + shadowOffset - 1.0F, y + shadowOffset + 4.5F, x + shadowOffset + boldOffset, y + shadowOffset + 4.5F - 1.0F, redCached, greenCached, blueCached, color));
                }

                if (isUnderline) {
                    fontEntry.add(new PenguinFont.Entry(x + shadowOffset - 1.0F, y + shadowOffset + 9.0F, x + shadowOffset + boldOffset, y + shadowOffset + 9.0F - 1.0F, redCached, greenCached, blueCached, color));
                }

                x += boldOffset;
            }
        }

        tessellator.draw();
        if (!fontEntry.isEmpty()) {
            GlStateManager.disableTexture();
            builder.begin(7, DefaultVertexFormats.POSITION_COLOR);

            for (Entry font : fontEntry) { //Changed from while loop to for loop
                font.pipe(builder);
            }
            tessellator.draw();
            GlStateManager.enableTexture();
        }
        return x;
    }

    private enum CharReplace {
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

        CharReplace(String search, int character) {
            this.search = search;
            this.character = (char) character;
        }

        public boolean is(char char2) {
            return character == char2;
        }
    }

    @OnlyIn(Dist.CLIENT)
    static class Entry { //Whole static class copied from FontRenderer
        final float x1;
        final float y1;
        final float x2;
        final float y2;
        final float red;
        final float green;
        final float blue;
        final float alpha;

        private Entry(float x1, float y1, float x2, float y2, float red, float green, float blue, float alpha) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
            this.red = red;
            this.green = green;
            this.blue = blue;
            this.alpha = alpha;
        }

        void pipe(BufferBuilder builder) {
            builder.pos((double) this.x1, (double) this.y1, 0.0D).color(this.red, this.green, this.blue, this.alpha).endVertex();
            builder.pos((double) this.x2, (double) this.y1, 0.0D).color(this.red, this.green, this.blue, this.alpha).endVertex();
            builder.pos((double) this.x2, (double) this.y2, 0.0D).color(this.red, this.green, this.blue, this.alpha).endVertex();
            builder.pos((double) this.x1, (double) this.y2, 0.0D).color(this.red, this.green, this.blue, this.alpha).endVertex();
        }
    }
}
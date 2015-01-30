package joshie.enchiridion.designer;

import static joshie.enchiridion.helpers.OpenGLHelper.end;
import static joshie.enchiridion.helpers.OpenGLHelper.resetZ;
import static joshie.enchiridion.helpers.OpenGLHelper.start;

import java.util.ArrayList;

import joshie.enchiridion.designer.features.Feature;
import joshie.enchiridion.helpers.ClientHelper;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ChatAllowedCharacters;

import com.google.gson.annotations.Expose;

public class DesignerCanvas {
    @Expose
    public ArrayList<Feature> features = new ArrayList();
    @Expose
    public String pageName = "";
    private int position;
    private int tick;
    private boolean white;

    public Feature selected;

    //Draws all the features on the canvas
    public void draw(int x, int y) {
        boolean noneSelected = true;
        for (Feature feature : features) {
            start();
            resetZ();
            feature.draw(x, y);
            if (feature.isSelected) {
                noneSelected = false;
            }

            end();
        }

        //Editing the Name of this page
        if (noneSelected && DesignerHelper.getGui().canEdit) {
            DesignerHelper.drawRect(-102, -55, -100, -37, 0xFFFFFFFF);
            DesignerHelper.drawRect(0, -55, 2, -37, 0xFFFFFFFF);
            DesignerHelper.drawRect(-102, -57, 2, -55, 0xFFFFFFFF);
            DesignerHelper.drawRect(-100, -55, 0, -37, 0xFF000000);
            DesignerHelper.drawRect(-100, -37, 0, -39, 0xFFFFFFFF);
            DesignerHelper.drawSplitString(getText(), -95, -50, 250, 0xFFFFFFFF);
        }
    }

    public void clicked(int x, int y, boolean isEditMode) {
        for (Feature feature : features) {
            feature.click(x, y, isEditMode);
        }
    }

    public void release(int x, int y) {
        for (Feature feature : features) {
            feature.release(x, y);
        }
    }

    public void follow(int x, int y) {
        for (Feature feature : features) {
            feature.follow(x, y);
        }
    }

    public void keyTyped(char character, int key) {
        boolean noneSelected = true;
        Feature remove = null;
        for (Feature feature : features) {
            feature.keyTyped(character, key);
            if (ClientHelper.isShiftPressed() && key == 211) {
                if (feature.isSelected) {
                    remove = feature;
                }
            }
            
            if (feature.isSelected) {
                noneSelected = false;
            }
        }

        if (remove != null) {
            features.remove(remove);
            selected = null;
        }

        if (noneSelected) {
            if (pageName != null) {
                if (key == 203) {
                    cursorLeft(1);
                } else if (key == 205) {
                    cursorRight(1);
                } else if (character == 22) {
                    add(GuiScreen.getClipboardString());
                } else if (key == 14) {
                    delete(-1);
                } else if (key == 211) {
                    delete(0);
                } else if (key == 28 || key == 156) {
                    add("\n");
                } else if (ChatAllowedCharacters.isAllowedCharacter(character)) {
                    add(Character.toString(character));
                }
            }
        }
    }

    public void scroll(boolean scrolledDown) {
        for (Feature feature : features) {
            feature.scroll(scrolledDown);
        }
    }

    /** Text Editing **/
    public String getText() {
        tick++;
        if (tick % 60 == 0) {
            if (white) {
                white = false;
            } else {
                white = true;
            }
        }

        if (white) {
            return new StringBuilder(pageName).insert(Math.min(position, pageName.length()), "[*cursor*]").toString();
        } else {
            return new StringBuilder(pageName).insert(Math.min(position, pageName.length()), "[*/cursor*]").toString();
        }
    }

    private void cursorLeft(int count) {
        int left = position - count;
        if (left < 0) {
            position = 0;
        } else position = left;
    }

    private void cursorRight(int count) {
        String text = pageName;
        int right = position + count;
        if (right > text.length()) {
            position = text.length();
        } else position = right;
    }

    private void add(String string) {
        String text = pageName;
        StringBuilder builder = new StringBuilder(text);
        text = builder.insert(position, string).toString();
        pageName = text;
        cursorRight(string.length());
    }

    private void delete(int count) {
        String text = pageName;
        if ((count < 0 && position > 0) || (count >= 0 && position < text.length())) {
            StringBuilder builder = new StringBuilder(text);
            text = builder.deleteCharAt(position + count).toString();
            pageName = text;
            if (count < 0) cursorLeft(-count);
            else if (count >= 0) cursorRight(count);
        }
    }
}

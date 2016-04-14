package joshie.enchiridion.gui.book;

import joshie.enchiridion.EConfig;
import joshie.enchiridion.Enchiridion;
import joshie.enchiridion.api.gui.IBookEditorOverlay;
import joshie.enchiridion.api.gui.ISimpleEditorFieldProvider;
import joshie.enchiridion.util.ITextEditable;
import joshie.enchiridion.util.PenguinFont;
import joshie.enchiridion.util.TextEditor;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;

public class GuiSimpleEditorGeneric extends GuiSimpleEditorAbstract {
    public static final GuiSimpleEditorGeneric INSTANCE = new GuiSimpleEditorGeneric();
    private HashMap<String, WrappedEditable> fieldCache = new HashMap();
    private String[] fieldNameCache;
    private static ISimpleEditorFieldProvider provider = null;

    protected GuiSimpleEditorGeneric() {}

    public IBookEditorOverlay setFeature(ISimpleEditorFieldProvider points) {
        this.provider = points;
        this.fieldCache = new HashMap();
        this.fieldNameCache = null;
        return this;
    }

    private boolean isOverPosition(int x1, int y1, int x2, int y2, int mouseX, int mouseY) {
        if (mouseX >= EConfig.editorXPos + x1 && mouseX <= EConfig.editorXPos + x2) {
            if (mouseY >= EConfig.toolbarYPos + y1 && mouseY <= EConfig.toolbarYPos + y2) {
                return true;
            }
        }

        return false;
    }

    private static final int xPosStart = 4;

    private void drawBoxLabel(String name, int yPos) {
        drawBorderedRectangle(xPosStart - 2, yPos, 83, yPos + 10, 0xFFB0A483, 0xFF48453C);
        drawSplitScaledString("[b]" + name + "[/b]", xPosStart, yPos + 3, 0xFF48453C, 0.5F);
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        int xPos = xPosStart;
        int yPos = -11;

        //Draw the extra information for the actions
        drawBoxLabel(Enchiridion.translate("fields.extra"), yPos + 20);
        for (String f : getFieldNames()) {
            if(isTransient(f)) continue;
            drawBorderedRectangle(2, yPos + 30, 83, yPos + 37, 0xFF312921, 0xFF191511);
            String name = Enchiridion.translate("button.action.field." + f);
            drawSplitScaledString("[b]" + name + "[/b]", 4, yPos + 32, 0xFFFFFFFF, 0.5F);

            WrappedEditable editable = null;
            if (!fieldCache.containsKey(f)) {
                editable = new WrappedEditable(provider, f);
                fieldCache.put(f, editable);
            } else editable = fieldCache.get(f);

            String text = TextEditor.INSTANCE.getText(editable);
            if (text == null) {
                TextEditor.INSTANCE.setText("");
                text = TextEditor.INSTANCE.getText(editable);
            }

            int lines = getLineCount(text) - 1;
            drawSplitScaledString(text, 4, yPos + 39, 0xFF191511, 0.5F);
            yPos = yPos + 6 + lines;
        }

        drawBorderedRectangle(2, yPos + 30, 83, yPos + 31, 0xFF312921, 0xFF191511);
    }

    public int getLineCount(String text) {
        text = PenguinFont.INSTANCE.replaceFormatting(text);
        while (text != null && text.endsWith("\n")) {
            text = text.substring(0, text.length() - 1);
        }

        return PenguinFont.INSTANCE.splitStringWidth(text, 155);
    }
    
    public String[] getFieldNames() {
        if (fieldNameCache != null) return fieldNameCache;
        fieldNameCache = new String[provider.getClass().getFields().length];
        int i = 0;
        for (Field field : provider.getClass().getFields()) {
            fieldNameCache[i] = field.getName();
            i++;
        }
        
        return fieldNameCache;
    }

    public boolean isTransient(String fieldName) {
        try {
            Field f = provider.getClass().getField(fieldName);
            return (Modifier.isTransient(f.getModifiers()));
        } catch (Exception e) {}

        return false;
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY) {
        int xPos = xPosStart;
        int yPos = -11;
        //Draw the extra information for the actions
        drawBoxLabel("Extra Fields", yPos + 20);
        for (String f : getFieldNames()) {
            if(isTransient(f)) continue;
            WrappedEditable editable = null;
            if (!fieldCache.containsKey(f)) {
                editable = new WrappedEditable(provider, f);
                fieldCache.put(f, editable);
            } else editable = fieldCache.get(f);

            String text = TextEditor.INSTANCE.getText(editable);
            if (text == null) {
                TextEditor.INSTANCE.setText("");
                text = TextEditor.INSTANCE.getText(editable);
            }

            int lines = getLineCount(text) - 1;
            if (isOverPosition(2, yPos + 37, 83, yPos + 44 + lines, mouseX, mouseY)) {
                editable.click();
                return true;
            }

            yPos = yPos + 6 + lines;
        }

        return false;
    }

    //Helper Editable
    public static class WrappedEditable implements ITextEditable {
        private String temporaryField;
        private String fieldName;
        private Object object;

        public WrappedEditable(Object object, String fieldName) {
            this.object = object;
            this.fieldName = fieldName;
        }

        public void onFieldsSet() {
            //After all is done update the
            if (object instanceof ISimpleEditorFieldProvider) {
                ((ISimpleEditorFieldProvider)object).onFieldsSet(fieldName);
            }
        }

        public boolean click() {
            try {
                Field f = object.getClass().getField(fieldName);
                if (f.getType() == boolean.class) {
                    boolean bool = f.getBoolean(object);
                    f.setBoolean(object, !bool);
                    temporaryField = "" + !bool;

                    onFieldsSet();
                    return true;
                }
            } catch (Exception e){ e.printStackTrace(); }

            TextEditor.INSTANCE.setEditable(this);
            return true;
        }

        @Override
        public String getTextField() {
            if (temporaryField == null) {
                try {
                    Field f = object.getClass().getField(fieldName);
                    if (fieldName.equals("pageNumber")) {
                        temporaryField = "" + (f.getInt(object) + 1);
                    } else temporaryField = "" + f.get(object);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            //Fix it up
            if (temporaryField == null) temporaryField = "";
            return temporaryField;
        }

        @Override
        public void setTextField(String text) {
            temporaryField = text;

            try {
                Field f = object.getClass().getField(fieldName);
                if (f.getType() == int.class) {
                    try {
                        Integer number = Integer.parseInt(temporaryField);
                        if (fieldName.equals("pageNumber")) {
                            number -= 1;
                        }

                        f.set(object, number);
                    } catch (Exception e) {
                        f.set(object, 0);
                    }
                } else f.set(object, text);
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            //After all is done update the
            onFieldsSet();
        }
    }
}

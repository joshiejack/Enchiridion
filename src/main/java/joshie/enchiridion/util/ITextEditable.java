package joshie.enchiridion.util;

public interface ITextEditable {
    public void setText(String text);

    public String getText();

    //For verification purposes
    public boolean canEdit(Object... objects);
}

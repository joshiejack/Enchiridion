package joshie.enchiridion.api;

public interface ITextEditable {
    public void setText(String text);
    public String getText();
    //For verification purposes
    public boolean canEdit(Object... objects);
}

package joshie.enchiridion.gui.book.buttons.actions;

import com.google.gson.JsonObject;
import joshie.enchiridion.Enchiridion;
import joshie.enchiridion.api.book.IButtonAction;
import joshie.enchiridion.util.ELocation;
import net.minecraft.util.ResourceLocation;

import java.util.Comparator;

public abstract class AbstractAction implements IButtonAction {
    protected transient ResourceLocation resource;
    protected transient String name;

    public AbstractAction() {
    }

    public AbstractAction(String name) {
        this.name = name;
        this.resource = new ELocation(name);
    }

    public IButtonAction copyAbstract(AbstractAction action) {
        action.name = name;
        return this;
    }

    @Override
    public void onFieldsSet(String field) {
        if (field.equals("")) initAction();
    }

    public void initAction() {
    }

    @Override
    public String getName() {
        return Enchiridion.translate("action." + name);
    }

    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    public ResourceLocation getResource() {
        return resource;
    }

    @Override
    public void readFromJson(JsonObject object) {
    }

    @Override
    public void writeToJson(JsonObject object) {
    }

    protected static class SortNumerical implements Comparator {
        @Override
        public int compare(Object o1, Object o2) {
            return ((Integer) o1).compareTo((Integer) o2);
        }
    }
}
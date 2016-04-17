package joshie.enchiridion.gui.book.buttons.actions;

import com.google.gson.JsonObject;
import joshie.enchiridion.Enchiridion;
import joshie.enchiridion.api.book.IButtonAction;
import joshie.enchiridion.util.ELocation;
import net.minecraft.util.ResourceLocation;

public abstract class AbstractAction implements IButtonAction {
    protected transient ResourceLocation resource;
    protected transient String name;

    public AbstractAction() {}
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

    public void initAction() {}

    @Override
    public String getName() {
        return Enchiridion.translate("action." + name);
    }

    @Override
    public ResourceLocation getResource() {
        return resource;
    }

    @Override
    public void readFromJson(JsonObject object) {}

    @Override
    public void writeToJson(JsonObject object) {}
}

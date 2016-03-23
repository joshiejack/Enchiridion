package joshie.enchiridion.api.book;

import net.minecraft.util.ResourceLocation;

import java.util.List;

public interface ITemplate {
    /** Returns the unique name **/
    public String getUniqueName();

    /** Returns the name of the template **/
    public String getTemplateName();

    /** Returns a resourcelocation for this templates icon **/
    public ResourceLocation getIcon();

    /** Returns a list of all the features in this template **/
    public List<IFeatureProvider> getFeatures();
}

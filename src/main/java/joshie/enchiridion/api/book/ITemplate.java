package joshie.enchiridion.api.book;

import net.minecraft.util.ResourceLocation;

import java.util.List;

public interface ITemplate {
    /** Returns the unique name **/
    String getUniqueName();

    /** Returns the name of the template **/
    String getTemplateName();

    /** Returns a resource location for this templates icon **/
    ResourceLocation getIcon();

    /** Returns a list of all the features in this template **/
    List<IFeatureProvider> getFeatures();
}
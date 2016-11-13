package joshie.enchiridion.data.book;

import joshie.enchiridion.api.book.IFeatureProvider;
import joshie.enchiridion.api.book.IPage;
import joshie.enchiridion.api.book.ITemplate;
import joshie.enchiridion.lib.EInfo;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Template implements ITemplate {
    private String templatename;
    private List<IFeatureProvider> features;
    private String uniquename;

    private transient ResourceLocation location;

    public Template() {
    }

    public Template(String uniquename, String templatename, ResourceLocation location, IPage page) {
        this.uniquename = uniquename;
        this.templatename = templatename;
        this.location = location;
        this.features = new ArrayList<>();
        this.features.addAll(page.getFeatures().stream().map(IFeatureProvider::copy).collect(Collectors.toList()));
    }

    public Template(String uniquename, String templatename, IPage page) {
        this.uniquename = uniquename;
        this.templatename = templatename;
        this.features = new ArrayList<>();
        for (IFeatureProvider provider : page.getFeatures()) {
            this.features.add(provider.copy());
        }
    }

    @Override
    public String getUniqueName() {
        return uniquename;
    }

    @Override
    public String getTemplateName() {
        return templatename;
    }

    @Override
    public ResourceLocation getIcon() {
        if (location == null) {
            location = new ResourceLocation(EInfo.MODID, "templates/" + uniquename + ".png");
        }

        return location;
    }

    @Override
    public List<IFeatureProvider> getFeatures() {
        return features;
    }
}
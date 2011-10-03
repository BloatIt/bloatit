package com.bloatit.web.linkable.sitemap;

import com.bloatit.framework.utils.PageIterable;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.masters.SiteMap;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.Feature;
import com.bloatit.model.feature.FeatureManager;
import com.bloatit.web.linkable.features.FeatureTabPane.FeatureTabKey;
import com.bloatit.web.url.FeatureListPageUrl;
import com.bloatit.web.url.FeaturePageUrl;
import com.bloatit.web.url.IndexPageUrl;

@ParamContainer("sitemap.xml")
public class ElveosSiteMap extends SiteMap {
    private final Url privateUrl;

    public ElveosSiteMap(Url url) {
        super();
        this.privateUrl = url;
        generate();
    }

    private void generate() {
        //Index page
        addSiteMapEntry(new SiteMapEntry(new IndexPageUrl(), ChangeFrequency.DAILY, null, 1.0f));
        
        //Features
        
        addSiteMapEntry(new SiteMapEntry(new FeatureListPageUrl(), ChangeFrequency.DAILY, null, 0.9f));
        
        PageIterable<Feature> features = FeatureManager.getFeatures();
        for(Feature feature: features) {
            addSiteMapEntry(new SiteMapEntry(new FeaturePageUrl(feature, FeatureTabKey.description), ChangeFrequency.DAILY, null, 0.7f));
        }
        
    }

}

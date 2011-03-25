package com.bloatit.web.components;

import static com.bloatit.framework.webserver.Context.tr;

import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.framework.webserver.components.HtmlParagraph;
import com.bloatit.model.Feature;
import com.bloatit.web.linkable.features.FeaturesTools;
import com.bloatit.web.linkable.softwares.SoftwaresTools;
import com.bloatit.web.pages.master.DefineParagraph;
import com.bloatit.web.pages.master.SideBarElementLayout;
import com.bloatit.web.url.FeaturePageUrl;

public class SideBarFeatureBlock extends SideBarElementLayout {

    public SideBarFeatureBlock(Feature feature) {
        setTitle(tr("Feature abstract"));

        try {

            setFloatRight(SoftwaresTools.getSoftwareLogo(feature.getSoftware()));

            add(new DefineParagraph(tr("Title: "), FeaturesTools.getTitle(feature)));

            add(new DefineParagraph(tr("Software: "), SoftwaresTools.getSoftwareLink(feature.getSoftware())));
            add(new DefineParagraph(tr("Popularity: "), String.valueOf(feature.getPopularity())));

            add(new HtmlParagraph(FeaturesTools.generateProgress(feature, true)));

            add(new HtmlParagraph(new FeaturePageUrl(feature).getHtmlLink(tr("more details..."))));

        } catch (UnauthorizedOperationException e) {
        }
    }
}

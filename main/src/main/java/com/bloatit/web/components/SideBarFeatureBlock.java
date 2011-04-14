package com.bloatit.web.components;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import java.math.BigDecimal;

import com.bloatit.framework.exceptions.lowlevel.UnauthorizedOperationException;
import com.bloatit.framework.webprocessor.components.HtmlParagraph;
import com.bloatit.model.Feature;
import com.bloatit.web.linkable.features.FeaturesTools;
import com.bloatit.web.linkable.softwares.SoftwaresTools;
import com.bloatit.web.pages.master.HtmlDefineParagraph;
import com.bloatit.web.pages.master.sidebar.TitleSideBarElementLayout;
import com.bloatit.web.url.FeaturePageUrl;

public class SideBarFeatureBlock extends TitleSideBarElementLayout {

    public SideBarFeatureBlock(final Feature feature, final BigDecimal amount) {
        setTitle(tr("Feature abstract"));

        try {

            setFloatRight(SoftwaresTools.getSoftwareLogo(feature.getSoftware()));

            add(new HtmlDefineParagraph(tr("Title: "), FeaturesTools.getTitle(feature)));

            add(new HtmlDefineParagraph(tr("Software: "), SoftwaresTools.getSoftwareLink(feature.getSoftware())));
            add(new HtmlDefineParagraph(tr("Popularity: "), String.valueOf(feature.getPopularity())));

            add(new HtmlParagraph(FeaturesTools.generateProgress(feature, true, amount)));

            add(new HtmlParagraph(new FeaturePageUrl(feature).getHtmlLink(tr("more details..."))));

        } catch (final UnauthorizedOperationException e) {
        }
    }

    public SideBarFeatureBlock(final Feature feature) {
        this(feature, BigDecimal.ZERO);
    }
}

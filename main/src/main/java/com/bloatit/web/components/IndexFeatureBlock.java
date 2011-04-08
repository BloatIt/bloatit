package com.bloatit.web.components;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import com.bloatit.framework.exceptions.lowlevel.UnauthorizedOperationException;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlTitle;
import com.bloatit.framework.webprocessor.components.PlaceHolderElement;
import com.bloatit.framework.webprocessor.components.meta.HtmlBranch;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.model.HighlightFeature;
import com.bloatit.web.linkable.features.FeaturesTools;
import com.bloatit.web.linkable.softwares.SoftwaresTools;
import com.bloatit.web.pages.master.DefineParagraph;
import com.bloatit.web.url.FeaturePageUrl;

public class IndexFeatureBlock extends HtmlDiv {

    private final PlaceHolderElement floatRight;

    public IndexFeatureBlock(final HighlightFeature highlightFeature) {
        super("index_element");

        add(new HtmlTitle(highlightFeature.getReason(), 2));
        final HtmlDiv indexBodyElement = new HtmlDiv("index_body_element");
        add(indexBodyElement);
        floatRight = new PlaceHolderElement();
        indexBodyElement.add(floatRight);

        try {

            setFloatRight(SoftwaresTools.getSoftwareLogo(highlightFeature.getFeature().getSoftware()));

            indexBodyElement.add(new HtmlTitle(new FeaturePageUrl(highlightFeature.getFeature()).getHtmlLink(FeaturesTools.getTitle(highlightFeature.getFeature())),
                                               3));

            indexBodyElement.add(new DefineParagraph(tr("Software: "), SoftwaresTools.getSoftwareLink(highlightFeature.getFeature().getSoftware())));

            indexBodyElement.add(FeaturesTools.generateProgress(highlightFeature.getFeature(), true));

            indexBodyElement.add(new FeaturePageUrl(highlightFeature.getFeature()).getHtmlLink(tr("more details...")));

            indexBodyElement.add(FeaturesTools.generateDetails(highlightFeature.getFeature(), false));

        } catch (final UnauthorizedOperationException e) {
        }
    }

    public HtmlBranch setFloatRight(final HtmlElement element) {
        floatRight.add(new HtmlDiv("float_right").add(element));
        return this;
    }
}

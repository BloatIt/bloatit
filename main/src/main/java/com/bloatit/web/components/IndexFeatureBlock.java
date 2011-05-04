//
// Copyright (c) 2011 Linkeos.
//
// This file is part of Elveos.org.
// Elveos.org is free software: you can redistribute it and/or modify it
// under the terms of the GNU General Public License as published by the
// Free Software Foundation, either version 3 of the License, or (at your
// option) any later version.
//
// Elveos.org is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
// more details.
// You should have received a copy of the GNU General Public License along
// with Elveos.org. If not, see http://www.gnu.org/licenses/.
//
package com.bloatit.web.components;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import com.bloatit.data.DaoFeature.FeatureState;
import com.bloatit.framework.exceptions.highlevel.ShallNotPassException;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlImage;
import com.bloatit.framework.webprocessor.components.HtmlTitle;
import com.bloatit.framework.webprocessor.components.PlaceHolderElement;
import com.bloatit.framework.webprocessor.components.meta.HtmlBranch;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.HighlightFeature;
import com.bloatit.model.Image;
import com.bloatit.model.Member;
import com.bloatit.model.right.UnauthorizedOperationException;
import com.bloatit.web.WebConfiguration;
import com.bloatit.web.linkable.features.FeaturesTools;
import com.bloatit.web.linkable.softwares.SoftwaresTools;
import com.bloatit.web.pages.master.HtmlDefineParagraph;
import com.bloatit.web.url.FeaturePageUrl;

public class IndexFeatureBlock extends HtmlDiv {

    private final PlaceHolderElement floatRight;

    public IndexFeatureBlock(final HighlightFeature highlightFeature, Member me) {
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

            indexBodyElement.add(new HtmlDefineParagraph(tr("Software: "),
                                                         SoftwaresTools.getSoftwareLink(highlightFeature.getFeature().getSoftware())));

            // Generate progess bar and text
            indexBodyElement.add(FeaturesTools.generateProgress(highlightFeature.getFeature(), me));

            indexBodyElement.add(FeaturesTools.generateDetails(highlightFeature.getFeature(), false));

            if (highlightFeature.getFeature().getFeatureState() == FeatureState.FINISHED) {
                final HtmlImage sucessImage = new HtmlImage(new Image(WebConfiguration.getImgFeatureStateSuccess(Context.getLocalizator()
                                                                                                                        .getLanguageCode())),
                                                            tr("success"));
                final HtmlDiv sucessImageBlock = new HtmlDiv("successImageBlock");
                sucessImageBlock.add(sucessImage);
                indexBodyElement.add(sucessImageBlock);
            }

        } catch (final UnauthorizedOperationException e) {
            throw new ShallNotPassException(e);
        }
    }

    private final HtmlBranch setFloatRight(final HtmlElement element) {
        floatRight.add(new HtmlDiv("float_right").add(element));
        return this;
    }
}

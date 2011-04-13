/*
 * Copyright (C) 2010 BloatIt. This file is part of BloatIt. BloatIt is free
 * software: you can redistribute it and/or modify it under the terms of the GNU
 * Affero General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 * BloatIt is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details. You should have received a copy of the GNU Affero General Public
 * License along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */

package com.bloatit.web.pages;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.utils.Image;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlImage;
import com.bloatit.framework.webprocessor.components.HtmlLink;
import com.bloatit.framework.webprocessor.components.HtmlTitle;
import com.bloatit.framework.webprocessor.components.PlaceHolderElement;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.HighlightFeature;
import com.bloatit.model.managers.HighlightFeatureManager;
import com.bloatit.web.WebConfiguration;
import com.bloatit.web.components.IndexFeatureBlock;
import com.bloatit.web.linkable.documentation.SideBarDocumentationBlock;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.pages.master.MasterPage;
import com.bloatit.web.pages.master.sidebar.SideBarElementLayout;
import com.bloatit.web.pages.master.sidebar.TwoColumnLayout;
import com.bloatit.web.url.CreateFeaturePageUrl;
import com.bloatit.web.url.DocumentationPageUrl;
import com.bloatit.web.url.IndexPageUrl;

@ParamContainer("index")
public final class IndexPage extends MasterPage {

    private final IndexPageUrl url;

    public IndexPage(final IndexPageUrl url) {
        super(url);
        this.url = url;
    }

    @Override
    protected HtmlElement createBodyContent() throws RedirectException {
        final PlaceHolderElement element = new PlaceHolderElement();
        final HtmlDiv globalDescription = new HtmlDiv("global_description");
        {
            final HtmlTitle title = new HtmlTitle(Context.tr("Get paid to create free software"), 1);
            globalDescription.add(title);
            final HtmlImage image = new HtmlImage(new Image(WebConfiguration.getImgPresentation(Context.getLocalizator().getLanguageCode())),
                                                  tr("Elveos's presentation"));
            final DocumentationPageUrl documentationPageUrl = new DocumentationPageUrl();
            documentationPageUrl.setDocTarget("presentation");
            final HtmlLink presentationLink = documentationPageUrl.getHtmlLink();
            presentationLink.add(image);
            globalDescription.add(presentationLink);

        }
        element.add(globalDescription);

        final TwoColumnLayout twoColumnLayout = new TwoColumnLayout(true, url);
        twoColumnLayout.addLeft(new HtmlTitle(tr("Hightlighted features"), 1));

        final HtmlDiv featureList = new HtmlDiv("feature_list");
        {
            final int featureCount = 6;
            final PageIterable<HighlightFeature> hightlightFeatureList = HighlightFeatureManager.getAll();
            final HighlightFeature[] hightlightFeatureArray = new HighlightFeature[featureCount];

            for (final HighlightFeature highlightFeature : hightlightFeatureList) {
                final int position = highlightFeature.getPosition() - 1;
                if (position < featureCount) {
                    if (hightlightFeatureArray[position] == null) {
                        hightlightFeatureArray[position] = highlightFeature;
                    } else {
                        if (hightlightFeatureArray[position].getActivationDate().before(highlightFeature.getActivationDate())) {
                            hightlightFeatureArray[position] = highlightFeature;
                        }
                    }
                }
            }

            for (int i = 0; i < (featureCount + 1) / 2; i++) {
                final HtmlDiv featureListRow = new HtmlDiv("feature_list_row");
                {
                    final HtmlDiv featureListLeftCase = new HtmlDiv("feature_list_left_case");
                    {
                        final HighlightFeature highlightFeature = hightlightFeatureArray[i * 2];
                        if (highlightFeature != null) {
                            featureListLeftCase.add(new IndexFeatureBlock(highlightFeature));
                        }
                    }
                    featureListRow.add(featureListLeftCase);

                    final HtmlDiv featureListRightCase = new HtmlDiv("feature_list_right_case");
                    {
                        final HighlightFeature highlightFeature = hightlightFeatureArray[i * 2 + 1];
                        if (highlightFeature != null) {
                            featureListRightCase.add(new IndexFeatureBlock(highlightFeature));
                        }
                    }
                    featureListRow.add(featureListRightCase);
                }
                featureList.add(featureListRow);
            }
        }

        twoColumnLayout.addLeft(featureList);

        // Display of a button to create a feature
        SideBarElementLayout createBox = new SideBarElementLayout();
        HtmlDiv createDiv = new HtmlDiv("feature_create");
        HtmlLink link = new HtmlLink(new CreateFeaturePageUrl().urlString(), createDiv);
        { // Box to hold feature creating button content
            HtmlImage img = new HtmlImage(new Image(WebConfiguration.getImgIdea()), Context.tr("Request a feature"));
            img.setCssClass("feature_create_img");
            createDiv.add(img);
            HtmlDiv createTextDiv = new HtmlDiv("feature_create_text_box");
            { // Box to hold text of the button
                HtmlDiv createTextDiv2 = new HtmlDiv("feature_create_text");
                createTextDiv.add(createTextDiv2);
                createTextDiv2.addText(Context.tr("Request a feature"));
            }
            createDiv.add(createTextDiv);
        }

        createBox.add(link);
        twoColumnLayout.addRight(createBox);

        // Display of a summary of all website activity since creation
        SideBarElementLayout summaryBox = new SideBarElementLayout();
        twoColumnLayout.addRight(summaryBox);

        twoColumnLayout.addRight(new SideBarDocumentationBlock("home"));
        element.add(twoColumnLayout);
        return element;
    }

    @Override
    protected String createPageTitle() {
        return "Finance free software";
    }

    @Override
    public boolean isStable() {
        return true;
    }

    public static Breadcrumb generateBreadcrumb() {
        final Breadcrumb breadcrumb = new Breadcrumb();
        final IndexPageUrl pageUrl = new IndexPageUrl();
        breadcrumb.pushLink(pageUrl.getHtmlLink(tr("Home")));
        return breadcrumb;
    }

    @Override
    protected Breadcrumb createBreadcrumb() {
        return generateBreadcrumb();
    }
}

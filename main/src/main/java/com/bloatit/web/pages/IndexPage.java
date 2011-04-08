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

import static com.bloatit.framework.webprocessor.Context.tr;

import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.utils.Image;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.framework.webprocessor.Context;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlImage;
import com.bloatit.framework.webprocessor.components.HtmlLink;
import com.bloatit.framework.webprocessor.components.HtmlTitle;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.model.HighlightFeature;
import com.bloatit.model.managers.HighlightFeatureManager;
import com.bloatit.web.WebConfiguration;
import com.bloatit.web.components.IndexFeatureBlock;
import com.bloatit.web.linkable.documentation.SideBarDocumentationBlock;
import com.bloatit.web.linkable.meta.bugreport.SideBarBugReportBlock;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.pages.master.MasterPage;
import com.bloatit.web.pages.master.sidebar.TwoColumnLayout;
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
    protected void doCreate() throws RedirectException {
        final HtmlDiv globalDescription = new HtmlDiv("global_description");
        {
            HtmlTitle title = new HtmlTitle("Get paid to create free software", 1);
            globalDescription.add(title);
            HtmlImage image = new HtmlImage(new Image(WebConfiguration.getImgPresentation(Context.getLocalizator().getLanguageCode())), tr("Elveos's presentation"));
            DocumentationPageUrl documentationPageUrl = new DocumentationPageUrl();
            documentationPageUrl.setDocTarget("presentation");
            HtmlLink presentationLink = documentationPageUrl.getHtmlLink();
            presentationLink.add(image);
            globalDescription.add(presentationLink);

        }
        add(globalDescription);

        TwoColumnLayout twoColumnLayout = new TwoColumnLayout(true, url);
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
                        HighlightFeature highlightFeature = hightlightFeatureArray[i * 2];
                        if (highlightFeature != null) {
                            featureListLeftCase.add(new IndexFeatureBlock(highlightFeature));
                        }
                    }
                    featureListRow.add(featureListLeftCase);

                    final HtmlDiv featureListRightCase = new HtmlDiv("feature_list_right_case");
                    {
                        HighlightFeature highlightFeature = hightlightFeatureArray[i * 2 + 1];
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
        twoColumnLayout.addRight(new SideBarDocumentationBlock("home"));
        add(twoColumnLayout);
    }

    @Override
    protected String getPageTitle() {
        return "Finance free software";
    }

    @Override
    public boolean isStable() {
        return true;
    }

    public static Breadcrumb generateBreadcrumb() {
        Breadcrumb breadcrumb = new Breadcrumb();
        IndexPageUrl pageUrl = new IndexPageUrl();
        breadcrumb.pushLink(pageUrl.getHtmlLink(tr("Home")));
        return breadcrumb;
    }

    @Override
    protected Breadcrumb getBreadcrumb() {
        return generateBreadcrumb();
    }
}

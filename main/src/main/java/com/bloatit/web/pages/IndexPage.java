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

import static com.bloatit.framework.webserver.Context.tr;

import java.util.ArrayList;
import java.util.List;

import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.utils.Image;
import com.bloatit.framework.utils.Image.ImageType;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.components.HtmlDiv;
import com.bloatit.framework.webserver.components.HtmlImage;
import com.bloatit.framework.webserver.components.HtmlParagraph;
import com.bloatit.framework.webserver.components.HtmlTitle;
import com.bloatit.model.HighlightFeature;
import com.bloatit.model.managers.HighlightFeatureManager;
import com.bloatit.web.components.IndexFeatureBlock;
import com.bloatit.web.pages.documentation.SideBarDocumentationBlock;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.pages.master.MasterPage;
import com.bloatit.web.pages.master.SideBarElementLayout;
import com.bloatit.web.pages.master.TwoColumnLayout;
import com.bloatit.web.url.IndexPageUrl;

@ParamContainer("index")
public final class IndexPage extends MasterPage {

    public IndexPage(final IndexPageUrl indexPageUrl) {
        super(indexPageUrl);
    }

    @Override
    protected void doCreate() throws RedirectException {

        final HtmlDiv globalDescription = new HtmlDiv("global_description");
        {
            // final HtmlParagraph globalConcept = new
            // HtmlParagraph(Context.tr("Linkeos is a platform for free software funding."));
            // globalDescription.add(globalConcept);
            //
            // final HtmlParagraph needText = new HtmlParagraph();
            // needText.addText(Context.tr("If you have a need about a free software, you can "));
            // needText.add(new
            // CreateFeaturePageUrl().getHtmlLink(Context.tr("create a new feature")));
            // needText.addText(Context.tr(" and contribute to it."));
            // globalDescription.add(needText);
            //
            // final HtmlParagraph devText = new HtmlParagraph();
            // devText.addText(Context.tr("If you are a developer, you can make an offer on existing features to develop it again money."));
            // globalDescription.add(devText);
            //
            // final HtmlParagraph moreText = new HtmlParagraph();
            // moreText.addText(Context.tr("You can find more informations about Linkeos's in the documentation."));
            // globalDescription.add(moreText);

            HtmlTitle title = new HtmlTitle("Get paid to create free software", 1);

            globalDescription.add(title);

            HtmlImage image = new HtmlImage(new Image("presentation.png", ImageType.LOCAL), tr("Elveos's presentation"));
            globalDescription.add(image);

        }
        add(globalDescription);

        TwoColumnLayout twoColumnLayout = new TwoColumnLayout(true);
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
    protected List<String> getCustomCss() {
        ArrayList<String> custom = new ArrayList<String>();

        custom.add("index.css");
        return custom;
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

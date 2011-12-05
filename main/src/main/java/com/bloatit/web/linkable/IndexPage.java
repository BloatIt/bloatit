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

package com.bloatit.web.linkable;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlImage;
import com.bloatit.framework.webprocessor.components.HtmlLink;
import com.bloatit.framework.webprocessor.components.HtmlSpan;
import com.bloatit.framework.webprocessor.components.PlaceHolderElement;
import com.bloatit.framework.webprocessor.components.meta.HtmlBranch;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.components.meta.HtmlMixedText;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.masters.HtmlHeaderLink;
import com.bloatit.model.HighlightFeature;
import com.bloatit.model.Image;
import com.bloatit.model.feature.FeatureManager;
import com.bloatit.model.managers.ContributionManager;
import com.bloatit.model.managers.HighlightFeatureManager;
import com.bloatit.model.managers.OfferManager;
import com.bloatit.model.managers.ReleaseManager;
import com.bloatit.web.WebConfiguration;
import com.bloatit.web.components.IndexFeatureBlock;
import com.bloatit.web.components.MoneyDisplayComponent;
import com.bloatit.web.components.NewsFeedSideBlock;
import com.bloatit.web.components.SideBarButton;
import com.bloatit.web.linkable.atom.master.ElveosAtomFeed;
import com.bloatit.web.linkable.documentation.SideBarDocumentationBlock;
import com.bloatit.web.linkable.master.Breadcrumb;
import com.bloatit.web.linkable.master.ElveosPage;
import com.bloatit.web.linkable.master.sidebar.TwoColumnLayout;
import com.bloatit.web.url.ChooseFeatureTypePageUrl;
import com.bloatit.web.url.DocumentationPageUrl;
import com.bloatit.web.url.FeatureAtomFeedUrl;
import com.bloatit.web.url.FeatureListPageUrl;
import com.bloatit.web.url.IndexPageUrl;

/**
 * Index of elveos website
 */
@ParamContainer("")
public final class IndexPage extends ElveosPage {

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
            final HtmlImage image = new HtmlImage(new Image(WebConfiguration.getImgPresentation(Context.getLocalizator().getLanguageCode())),
                                                  tr("Elveos's presentation"));
            final DocumentationPageUrl documentationPageUrl = new DocumentationPageUrl("presentation");
            final HtmlLink presentationLink = documentationPageUrl.getHtmlLink();
            presentationLink.add(image);
            globalDescription.add(presentationLink);

            generateCounts(globalDescription);
        }
        element.add(globalDescription);

        final TwoColumnLayout twoColumnLayout = new TwoColumnLayout(true, url);
        element.add(twoColumnLayout);

        // List of features
        final HtmlDiv featureList = new HtmlDiv("feature_list");
        {
            final int featureCount = 6;

            final List<HighlightFeature> hightlightFeatureArray = HighlightFeatureManager.getPositionArray(featureCount);

            for (int i = 0; i < (featureCount + 1) / 2; i++) {
                final HtmlDiv featureListRow = new HtmlDiv("feature_list_row");
                {
                    final HtmlDiv featureListLeftCase = new HtmlDiv("feature_list_left_case");
                    {
                        final HighlightFeature highlightFeature = hightlightFeatureArray.get(i * 2);
                        if (highlightFeature != null) {
                            featureListLeftCase.add(new IndexFeatureBlock(highlightFeature));
                        }
                    }
                    featureListRow.add(featureListLeftCase);

                    final HtmlDiv featureListRightCase = new HtmlDiv("feature_list_right_case");
                    {
                        final HighlightFeature highlightFeature = hightlightFeatureArray.get(i * 2 + 1);
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

        // A link to all the features available on elveos website
        final HtmlLink allFeatures = new FeatureListPageUrl().getHtmlLink(Context.tr("View all feature requests"));
        allFeatures.setCssClass("button all_features_button");
        twoColumnLayout.addLeft(allFeatures);

        // Display of a button to create a feature
        twoColumnLayout.addRight(new SideBarButton(Context.tr("Request a feature"), new ChooseFeatureTypePageUrl(), WebConfiguration.getImgIdea()));
        twoColumnLayout.addRight(new SideBarButton(Context.tr("Elveos Atom feed"), new FeatureAtomFeedUrl(), WebConfiguration.getAtomImg(), false));

        // Adding doc
        twoColumnLayout.addRight(new SideBarDocumentationBlock("home"));
        twoColumnLayout.addRight(new NewsFeedSideBlock());

        return element;
    }

    @Override
    protected String createPageTitle() {
        return Context.tr("Finance free software");
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

    private void generateCounts(final HtmlDiv parent) {
        final HtmlDiv summaryBox = new HtmlDiv("elveos_summary");
        parent.add(summaryBox);

        int featureNb = FeatureManager.getFeatureCount();
        // Feature count
        final HtmlBranch featureCount = new HtmlSpan("count_line").addText(Context.trn("{0}&nbsp;Feature request, ",
                                                                                       "{0}&nbsp;Features requests, ",
                                                                                       featureNb,
                                                                                       featureNb));
        summaryBox.add(featureCount);

        // Contribution amount
        BigDecimal moneyRaised = ContributionManager.getMoneyRaised();
        if (moneyRaised == null) {
            moneyRaised = BigDecimal.ZERO;
        }

        final MoneyDisplayComponent mdc = new MoneyDisplayComponent(moneyRaised, Context.getLocalizator());
        final HtmlMixedText moneyMix = new HtmlMixedText(Context.tr("<0::>&nbsp;Funded, "), mdc);
        final HtmlBranch contributionRaised = new HtmlSpan("count_line").add(moneyMix);
        summaryBox.add(contributionRaised);

        // Count of offers
        int offerNb = OfferManager.getOfferCount();
        final HtmlBranch offerCount = new HtmlSpan("count_line").addText(Context.trn("{0}&nbsp;Development&nbsp;offer, ",
                                                                                     "{0}&nbsp;Development&nbsp;offers, ",
                                                                                     offerNb,
                                                                                     offerNb));
        summaryBox.add(offerCount);

        // Count of releases
        int releaseNb = ReleaseManager.getReleaseCount();
        final HtmlBranch releaseCount = new HtmlSpan("count_line").addText(Context.trn("{0}&nbsp;Release", "{0}&nbsp;Releases", releaseNb, releaseNb));
        summaryBox.add(releaseCount);
    }

    @Override
    protected ArrayList<HtmlHeaderLink> getLinks() {
        ArrayList<HtmlHeaderLink> list = new ArrayList<HtmlHeaderLink>();
        list.add(ElveosAtomFeed.generateHeaderLink(new FeatureAtomFeedUrl(), Context.tr("Feature feed")));
        list.add(new HtmlHeaderLink("https://plus.google.com/112969426055352289328", "publisher"));
        return list;
    }
}

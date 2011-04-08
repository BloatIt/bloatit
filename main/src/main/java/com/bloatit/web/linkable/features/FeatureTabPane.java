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
package com.bloatit.web.linkable.features;

import java.util.Locale;

import com.bloatit.data.DaoFeature.FeatureState;
import com.bloatit.framework.exceptions.highlevel.ShallNotPassException;
import com.bloatit.framework.exceptions.lowlevel.UnauthorizedOperationException;
import com.bloatit.framework.webprocessor.Context;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlParagraph;
import com.bloatit.framework.webprocessor.components.HtmlSpan;
import com.bloatit.framework.webprocessor.components.advanced.HtmlTabBlock;
import com.bloatit.framework.webprocessor.components.advanced.HtmlTabBlock.HtmlTab;
import com.bloatit.framework.webprocessor.components.meta.XmlNode;
import com.bloatit.framework.webprocessor.components.renderer.HtmlRawTextRenderer;
import com.bloatit.framework.webprocessor.url.PageNotFoundUrl;
import com.bloatit.framework.webserver.annotations.Optional;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.annotations.RequestParam;
import com.bloatit.model.Feature;
import com.bloatit.model.Translation;
import com.bloatit.web.HtmlTools;
import com.bloatit.web.pages.master.HtmlPageComponent;
import com.bloatit.web.url.FeaturePageUrl;
import com.bloatit.web.url.FeatureTabPaneUrl;
import com.bloatit.web.url.MemberPageUrl;

@ParamContainer(value = "featureTabPane", isComponent = true)
public final class FeatureTabPane extends HtmlPageComponent {

    public static final String BUGS_TAB = "bugs_tab";

    public static final String DETAILS_TAB = "details_tab";

    public static final String OFFERS_TAB = "offers_tab";

    public static final String CONTRIBUTIONS_TAB = "contributions_tab";

    public static final String DESCRIPTION_TAB = "description_tab";

    @RequestParam(name = "feature_tab_pane")
    @Optional(DESCRIPTION_TAB)
    private String activeTabKey;

    /**
     * Useful for Url generation Do not delete
     */
    @SuppressWarnings("unused")
    private FeatureContributorsComponent contribution;

    public FeatureTabPane(final FeatureTabPaneUrl url, final Feature feature) {
        super();
        activeTabKey = url.getActiveTabKey();

        final FeaturePageUrl featureUrl = new FeaturePageUrl(feature);
        featureUrl.setFeatureTabPaneUrl(new FeatureTabPaneUrl());

        // Create tab pane
        final HtmlTabBlock tabPane = new HtmlTabBlock("feature_tab_pane", activeTabKey, featureUrl);

        // Create description tab
        tabPane.addTab(new HtmlTab(Context.tr("Description"), DESCRIPTION_TAB) {
            @SuppressWarnings("synthetic-access")
            @Override
            public XmlNode generateBody() {
                return generateDescriptionTabContent(feature);
            }
        });

        // Create participations tab
        try {
            tabPane.addTab(new HtmlTab(Context.tr("Contributions ({0})", feature.getContributions().size()), CONTRIBUTIONS_TAB) {
                @Override
                public XmlNode generateBody() {
                    return new FeatureContributorsComponent(url.getContributionUrl(), feature);
                }
            });
        } catch (final UnauthorizedOperationException e1) {
            Context.getSession().notifyError(Context.tr("An error prevented us from displaying cotnribution informations. Please notify us."));
            throw new ShallNotPassException("User cannot access contributions informations", e1); 
        }

        // Create Comments tab
        try {
            tabPane.addTab(new HtmlTab(Context.tr("Offers ({0})", feature.getOffers().size()), OFFERS_TAB) {
                @Override
                public XmlNode generateBody() {
                    return new FeatureOfferListComponent(feature);
                }
            });
        } catch (final UnauthorizedOperationException e) {
            // No access to offer, the tab is not displayed
        }

        // Create Details tab
        tabPane.addTab(new HtmlTab(Context.tr("Details"), DETAILS_TAB) {
            @Override
            public XmlNode generateBody() {
                return new FeatureOfferListComponent(feature);
            }
        });

        // Create Bugtracker tab only after preparation
        if (feature.getFeatureState() != FeatureState.PENDING && feature.getFeatureState() != FeatureState.PREPARING) {

            tabPane.addTab(new HtmlTab(Context.tr("Bugs ({0})", feature.countOpenBugs()), BUGS_TAB) {
                @Override
                public XmlNode generateBody() {
                    return new FeatureBugListComponent(feature);
                }
            });
        }

        add(tabPane);

    }

    private XmlNode generateDescriptionTabContent(final Feature feature) {

        final HtmlDiv descriptionBlock = new HtmlDiv("description_block");
        {
            final HtmlDiv descriptionText = new HtmlDiv("description_text");
            {

                final Locale defaultLocale = Context.getLocalizator().getLocale();
                try {
                    final Translation translatedDescription = feature.getDescription().getTranslationOrDefault(defaultLocale);
                    final HtmlParagraph description = new HtmlParagraph(new HtmlRawTextRenderer(translatedDescription.getText()));
                    descriptionText.add(description);
                } catch (final UnauthorizedOperationException e1) {
                    // Nothing.
                }

            }
            descriptionBlock.add(descriptionText);

            final HtmlDiv descriptionSeparator = new HtmlDiv("description_separator");
            descriptionBlock.add(descriptionSeparator);

            final HtmlDiv descriptionFooter = new HtmlDiv("description_footer");
            {
                final HtmlDiv descriptionDetails = new HtmlDiv("description_details");
                {
                    descriptionDetails.addText(Context.tr("Created by "));

                    try {
                        final MemberPageUrl memberUrl = new MemberPageUrl(feature.getAuthor());
                        descriptionDetails.add(memberUrl.getHtmlLink(feature.getAuthor().getDisplayName()));
                    } catch (final UnauthorizedOperationException e1) {
                        // Nothing.
                    }

                    descriptionDetails.addText(Context.tr(" – "));

                    final HtmlSpan dateSpan = new HtmlSpan("description_date");
                    dateSpan.addText(HtmlTools.formatDate(Context.getLocalizator().getDate(feature.getCreationDate())));
                    descriptionDetails.add(dateSpan);
                }
                descriptionFooter.add(descriptionDetails);

                final HtmlDiv descriptionTranslate = new HtmlDiv("description_translate");
                {
                    descriptionTranslate.add(new PageNotFoundUrl().getHtmlLink(Context.tr("Translate")));
                }
                descriptionFooter.add(descriptionTranslate);

            }
            descriptionBlock.add(descriptionFooter);

        }
        return descriptionBlock;
    }
}

/*
 * Copyright (C) 2010 BloatIt. This file is part of BloatIt. BloatIt is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version. BloatIt is distributed in the hope that it will
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public
 * License for more details. You should have received a copy of the GNU Affero General
 * Public License along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
package com.bloatit.web.pages.demand;

import java.util.Locale;

import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.annotations.RequestParam;
import com.bloatit.framework.webserver.components.HtmlDiv;
import com.bloatit.framework.webserver.components.HtmlParagraph;
import com.bloatit.framework.webserver.components.HtmlSpan;
import com.bloatit.framework.webserver.components.meta.HtmlNode;
import com.bloatit.framework.webserver.components.renderer.HtmlRawTextRenderer;
import com.bloatit.framework.webserver.url.PageNotFoundUrl;
import com.bloatit.model.Translation;
import com.bloatit.model.demand.Demand;
import com.bloatit.web.HtmlTools;
import com.bloatit.web.components.HtmlTabBlock;
import com.bloatit.web.components.HtmlTabBlock.HtmlTab;
import com.bloatit.web.pages.master.HtmlPageComponent;
import com.bloatit.web.url.DemandPageUrl;
import com.bloatit.web.url.DemandTabPaneUrl;
import com.bloatit.web.url.MemberPageUrl;

@ParamContainer(value = "demandTabPane", isComponent = true)
public final class DemandTabPane extends HtmlPageComponent {

    @RequestParam(name = "demand_tab_pane", defaultValue = "description_tab")
    private String activeTabKey;

    /**
     * Useful for Url generation Do not delete
     */
    @SuppressWarnings("unused")
    private IdeaContributorsComponent contribution;

    public DemandTabPane(final DemandTabPaneUrl url, final Demand demand) {
        super();
        activeTabKey = url.getActiveTabKey();

        final DemandPageUrl demandUrl = new DemandPageUrl(demand);
        demandUrl.setDemandTabPaneUrl(url);

        // Create tab pane
        final HtmlTabBlock tabPane = new HtmlTabBlock("demand_tab_pane", activeTabKey, demandUrl);

        // Create description tab
        tabPane.addTab(new HtmlTab(Context.tr("Description"), "description_tab") {
            @Override
            public HtmlNode generateBody() {
                return generateDescriptionTabContent(demand);
            }
        });

        // Create participations tab
        try {
            tabPane.addTab(new HtmlTab(Context.tr("Participations ({0})", demand.getContributions().size()), "participations_tab") {
                @Override
                public HtmlNode generateBody() {
                    return new IdeaContributorsComponent(url.getContributionUrl(), demand);
                }
            });
        } catch (final UnauthorizedOperationException e1) {
            // No access to contributions, the tab is not displayed
        }

        // Create Comments tab
        try {
            tabPane.addTab(new HtmlTab(Context.tr("Offers ({0})", demand.getOffers().size()), "offers_tab") {
                @Override
                public HtmlNode generateBody() {
                    return new IdeaOfferListComponent(demand);
                }
            });
        } catch (final UnauthorizedOperationException e) {
            // No access to offer, the tab is not displayed
        }

        // Create Details tab
        tabPane.addTab(new HtmlTab(Context.tr("Details"), "details_tab") {
            @Override
            public HtmlNode generateBody() {
                return new IdeaOfferListComponent(demand);
            }
        });

        add(tabPane);

    }

    private HtmlNode generateDescriptionTabContent(final Demand demand) {

        final HtmlDiv descriptionBlock = new HtmlDiv("description_block");
        {
            final HtmlDiv descriptionText = new HtmlDiv("description_text");
            {

                final Locale defaultLocale = Context.getLocalizator().getLocale();
                try {
                    final Translation translatedDescription = demand.getDescription().getTranslationOrDefault(defaultLocale);
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
                        final MemberPageUrl memberUrl = new MemberPageUrl(demand.getAuthor());
                        descriptionDetails.add(memberUrl.getHtmlLink(demand.getAuthor().getDisplayName()));
                    } catch (final UnauthorizedOperationException e1) {
                        // Nothing.
                    }

                    descriptionDetails.addText(Context.tr(" – "));

                    final HtmlSpan dateSpan = new HtmlSpan("description_date");
                    dateSpan.addText(HtmlTools.formatDate(Context.getLocalizator().getDate(demand.getCreationDate())));
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

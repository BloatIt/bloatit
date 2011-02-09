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

package com.bloatit.web.components;

import java.text.NumberFormat;
import java.util.Locale;

import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.framework.utils.Image;
import com.bloatit.framework.utils.i18n.CurrencyLocale;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.components.HtmlDiv;
import com.bloatit.framework.webserver.components.HtmlImage;
import com.bloatit.framework.webserver.components.HtmlParagraph;
import com.bloatit.framework.webserver.components.HtmlSpan;
import com.bloatit.framework.webserver.components.HtmlTitle;
import com.bloatit.framework.webserver.components.PlaceHolderElement;
import com.bloatit.framework.webserver.components.meta.HtmlElement;
import com.bloatit.model.Demand;
import com.bloatit.model.Offer;
import com.bloatit.model.Translation;
import com.bloatit.model.demand.DemandImplementation;
import com.bloatit.web.HtmlTools;
import com.bloatit.web.url.DemandPageUrl;

public final class HtmlDemandSumary extends HtmlDiv {

    private static final String IMPORTANT_CSS_CLASS = "important";

    public HtmlDemandSumary(final Demand demand, boolean compact) {
        super("demand_summary");

        if (demand == null) {
            addText("");
            return;
        }

        // Extract locales stuffs
        final Locale defaultLocale = Context.getLocalizator().getLocale();

        // ////////////////////
        // Div demand_summary_top
        final HtmlDiv demandSummaryTop = new HtmlDiv("demand_summary_top");
        {
            // ////////////////////
            // Div demand_summary_left
            final HtmlDiv demandSummaryLeft = new HtmlDiv("demand_summary_left");
            {
                // Add project image
                final HtmlImage projectImage = new HtmlImage(new Image("vlc.png", Image.ImageType.LOCAL), "project_image");
                demandSummaryLeft.add(projectImage);
            }
            demandSummaryTop.add(demandSummaryLeft);

            // ////////////////////
            // Div demand_summary_center
            final HtmlDiv demandSummaryCenter = new HtmlDiv("demand_summary_center");
            {
                // Try to display the title
                try {
                    final Translation translatedDescription = demand.getDescription().getTranslationOrDefault(defaultLocale);
                    final HtmlSpan projectSpan = new HtmlSpan("demand_project_title");
                    projectSpan.addText("VLC");
                    final HtmlTitle title = new HtmlTitle(1);
                    title.setCssClass("demand_title");
                    title.add(projectSpan);
                    title.addText(" – ");
                    title.add(new DemandPageUrl(demand).getHtmlLink(translatedDescription.getTitle()));

                    demandSummaryCenter.add(title);

                } catch (final UnauthorizedOperationException e) {
                    // no right no description and no title
                }

            }
            demandSummaryTop.add(demandSummaryCenter);
        }
        add(demandSummaryTop);

        // ////////////////////
        // Div demand_summary_bottom
        final HtmlDiv demandSummaryBottom = new HtmlDiv("demand_sumary_bottom");
        {

            // ////////////////////
            // Div demand_summary_popularity
            final HtmlDiv demandSummaryPopularity = new HtmlDiv("demand_summary_popularity");
            {
                final HtmlParagraph popularityText = new HtmlParagraph(Context.tr("Popularity"), "demand_popularity_text");
                final HtmlParagraph popularityScore = new HtmlParagraph(HtmlTools.compressKarma(demand.getPopularity()), "demand_popularity_score");

                demandSummaryPopularity.add(popularityText);
                demandSummaryPopularity.add(popularityScore);
            }
            demandSummaryBottom.add(demandSummaryPopularity);

            // ////////////////////
            // Div demand_summary_progress
            final HtmlDiv demandSummaryProgress = new HtmlDiv("demand_summary_progress");
            {
                float progressValue = 0;
                // Progress bar
                try {

                    progressValue = (float) Math.floor(demand.getProgression());
                    float cappedProgressValue = progressValue;
                    if (cappedProgressValue > DemandImplementation.PROGRESSION_PERCENT) {
                        cappedProgressValue = DemandImplementation.PROGRESSION_PERCENT;
                    }

                    final HtmlProgressBar progressBar = new HtmlProgressBar(cappedProgressValue);
                    demandSummaryProgress.add(progressBar);

                    // Progress text
                    demandSummaryProgress.add(generateProgressText(demand, progressValue));

                } catch (final UnauthorizedOperationException e) {
                    // No right, no progress bar
                }

                // ////////////////////
                // Div details
                final HtmlDiv demandSummaryDetails = new HtmlDiv("demand_sumary_details");
                {
                    try {
                        final int commentsCount = demand.getComments().size();
                        final int offersCount = demand.getOffers().size();

                        final int contributionsCount = demand.getContributions().size();

                        final DemandPageUrl commentsDemandUrl = new DemandPageUrl(demand);
                        commentsDemandUrl.setAnchor("comments_block");

                        final DemandPageUrl offersDemandUrl = new DemandPageUrl(demand);
                        offersDemandUrl.getDemandTabPaneUrl().setActiveTabKey("offers_tab");
                        offersDemandUrl.setAnchor("demand_tab_pane");

                        final DemandPageUrl contributionsDemandUrl = new DemandPageUrl(demand);
                        contributionsDemandUrl.getDemandTabPaneUrl().setActiveTabKey("participations_tab");
                        contributionsDemandUrl.setAnchor("demand_tab_pane");

                        demandSummaryDetails.add(commentsDemandUrl.getHtmlLink(Context.trn("{0} comment", "{0} comments", commentsCount, new Integer(
                                commentsCount))));
                        demandSummaryDetails.addText(" – ");
                        demandSummaryDetails.add(offersDemandUrl.getHtmlLink(Context.trn("{0} offer", "{0} offers", offersCount, new Integer(
                                offersCount))));
                        demandSummaryDetails.addText(" – ");
                        demandSummaryDetails.add(contributionsDemandUrl.getHtmlLink(Context.trn("{0} contribution",
                                                                                                "{0} contributions",
                                                                                                contributionsCount,
                                                                                                new Integer(contributionsCount))));

                    } catch (final UnauthorizedOperationException e) {
                        // No right to see demandImplementation details
                    }

                }
                demandSummaryProgress.add(demandSummaryDetails);
            }
            demandSummaryBottom.add(demandSummaryProgress);

        }
        add(demandSummaryBottom);

    }

    private HtmlElement generateProgressText(final Demand demand, final float progressValue) {

        Offer currentOffer = null;
        try {
            currentOffer = demand.getSelectedOffer();
        } catch (final UnauthorizedOperationException e1) {
            // Nothing.
        }
        if (currentOffer == null) {

            final HtmlSpan amount = new HtmlSpan();
            amount.setCssClass(IMPORTANT_CSS_CLASS);

            CurrencyLocale currency;
            try {
                currency = Context.getLocalizator().getCurrency(demand.getContribution());

                amount.addText(currency.getDefaultString());

            } catch (final UnauthorizedOperationException e) {
                // No right, no display
            }
            final HtmlParagraph progressText = new HtmlParagraph();
            progressText.setCssClass("demand_progress_text");

            progressText.add(amount);
            progressText.addText(Context.tr(" no offer"));

            return progressText;
        }

        // Amount
        CurrencyLocale amountCurrency;
        try {
            amountCurrency = Context.getLocalizator().getCurrency(demand.getContribution());
            final HtmlSpan amount = new HtmlSpan();
            amount.setCssClass(IMPORTANT_CSS_CLASS);
            amount.addText(amountCurrency.getDefaultString());

            // Target
            final CurrencyLocale targetCurrency = Context.getLocalizator().getCurrency(currentOffer.getAmount());
            final HtmlSpan target = new HtmlSpan();
            target.setCssClass(IMPORTANT_CSS_CLASS);
            target.addText(targetCurrency.getDefaultString());

            // Progress
            final HtmlSpan progress = new HtmlSpan();
            progress.setCssClass(IMPORTANT_CSS_CLASS);
            final NumberFormat format = NumberFormat.getNumberInstance();
            format.setMinimumFractionDigits(0);
            progress.addText("" + format.format(progressValue) + " %");

            final HtmlParagraph progressText = new HtmlParagraph();
            progressText.setCssClass("demand_progress_text");

            progressText.add(amount);
            progressText.addText(Context.tr(" i.e. "));
            progressText.add(progress);
            progressText.addText(Context.tr(" of "));
            progressText.add(target);
            progressText.addText(Context.tr(" requested "));

            return progressText;
        } catch (final UnauthorizedOperationException e) {
            // No right, no display
            return new PlaceHolderElement();
        }
    }
}

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

package com.bloatit.web.html.components.custom;

import java.text.NumberFormat;
import java.util.Locale;

import com.bloatit.common.Image;
import com.bloatit.common.UnauthorizedOperationException;
import com.bloatit.framework.Offer;
import com.bloatit.framework.Translation;
import com.bloatit.framework.demand.Demand;
import com.bloatit.web.html.HtmlElement;
import com.bloatit.web.html.HtmlTools;
import com.bloatit.web.html.components.PlaceHolderElement;
import com.bloatit.web.html.components.standard.HtmlDiv;
import com.bloatit.web.html.components.standard.HtmlImage;
import com.bloatit.web.html.components.standard.HtmlParagraph;
import com.bloatit.web.html.components.standard.HtmlSpan;
import com.bloatit.web.html.components.standard.HtmlTitle;
import com.bloatit.web.server.Context;
import com.bloatit.web.utils.i18n.CurrencyLocale;
import com.bloatit.web.utils.url.DemandPageUrl;

public final class HtmlDemandSumary extends HtmlDiv {

    private static final int SHORT_TEXT_CONSTANT = 50;
    private static final int SHORT_DESCRIPTION_LENGTH = 220;
    private static final String IMPORTANT_CSS_CLASS = "important";

    public HtmlDemandSumary(final Demand demand) {
        super("demand_compact_summary");

      //Extract locales stuffs
        final Locale defaultLocale = Context.getLocalizator().getLocale();


        //////////////////////
        // Div demand_summary
        final HtmlDiv demandSummary = new HtmlDiv("demand_summary");
        {
            //////////////////////
            // Div demand_summary_top
            final HtmlDiv demandSummaryTop = new HtmlDiv("demand_summary_top");
            {
                //////////////////////
                // Div demand_summary_left
                final HtmlDiv demandSummaryLeft = new HtmlDiv("demand_summary_left");
                {
                    //Add project image
                    HtmlImage projectImage = new HtmlImage(new Image("vlc.png", Image.ImageType.LOCAL), "project_image");
                    demandSummaryLeft.add(projectImage);
                }
                demandSummaryTop.add(demandSummaryLeft);

                //////////////////////
                // Div demand_summary_center
                final HtmlDiv demandSummaryCenter = new HtmlDiv("demand_summary_center");
                {
                    //Try to display the title
                    try {
                        final Translation translatedDescription = demand.getDescription().getTranslationOrDefault(defaultLocale);
                        HtmlSpan projectSpan = new HtmlSpan("demand_project_title");
                        projectSpan.addText("VLC");
                        HtmlTitle title = new HtmlTitle(1);
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
            demandSummary.add(demandSummaryTop);

            //////////////////////
            // Div demand_summary_bottom
            final HtmlDiv demandSummaryBottom = new HtmlDiv("demand_sumary_bottom");
            {

                //////////////////////
                // Div demand_summary_popularity
                final HtmlDiv demandSummaryPopularity = new HtmlDiv("demand_summary_popularity");
                {
                    HtmlParagraph popularityText = new HtmlParagraph(Context.tr("Popularity"), "demand_popularity_text");
                    HtmlParagraph popularityScore = new HtmlParagraph(HtmlTools.compressKarma(demand.getPopularity()), "demand_popularity_score");

                    demandSummaryPopularity.add(popularityText);
                    demandSummaryPopularity.add(popularityScore);
                }
                demandSummaryBottom.add(demandSummaryPopularity);

                //////////////////////
                // Div demand_summary_progress
                final HtmlDiv demandSummaryProgress = new HtmlDiv("demand_summary_progress");
                {
                    float progressValue = 0;
                    //Progress bar
                    try {


                        progressValue = (float) Math.floor(demand.getProgression());
                        float cappedProgressValue = progressValue;
                        if (cappedProgressValue > Demand.PROGRESSION_PERCENT) {
                            cappedProgressValue = Demand.PROGRESSION_PERCENT;
                        }

                        final HtmlProgressBar progressBar = new HtmlProgressBar(cappedProgressValue);
                        demandSummaryProgress.add(progressBar);

                        //Progress text
                        demandSummaryProgress.add(generateProgressText(demand, progressValue));

                    } catch (final UnauthorizedOperationException e) {
                        // No right, no progress bar
                    }

                    //////////////////////
                    // Div details
                    final HtmlDiv demandSummaryDetails = new HtmlDiv("demand_sumary_details");
                    {
                        try {
                            int commentsCount = demand.getComments().size();
                            int offersCount = demand.getOffers().size();

                            int contributionsCount = demand.getContributions().size();


                            DemandPageUrl commentsDemandUrl = new DemandPageUrl(demand);
                            commentsDemandUrl.setAnchor("comments_block");

                            DemandPageUrl offersDemandUrl = new DemandPageUrl(demand);
                            offersDemandUrl.getDemandTabPaneUrl().setActiveTabKey("offers_tab");
                            offersDemandUrl.setAnchor("demand_tab_pane");

                            DemandPageUrl contributionsDemandUrl = new DemandPageUrl(demand);
                            contributionsDemandUrl.getDemandTabPaneUrl().setActiveTabKey("participations_tab");
                            contributionsDemandUrl.setAnchor("demand_tab_pane");




                            demandSummaryDetails.add(commentsDemandUrl.getHtmlLink(Context.trn("{0} comment", "{0} comments", commentsCount, new Integer(commentsCount))));
                            demandSummaryDetails.addText(" – ");
                            demandSummaryDetails.add(offersDemandUrl.getHtmlLink(Context.trn("{0} offer", "{0} offers", offersCount, new Integer(offersCount))));
                            demandSummaryDetails.addText(" – ");
                            demandSummaryDetails.add(contributionsDemandUrl.getHtmlLink(Context.trn("{0} contribution", "{0} contributions", contributionsCount, new Integer(contributionsCount))));

                        } catch (UnauthorizedOperationException e) {
                            // No right to see demand details
                        }

                    }
                    demandSummaryProgress.add(demandSummaryDetails);
                }
                demandSummaryBottom.add(demandSummaryProgress);





            }
            demandSummary.add(demandSummaryBottom);


        }

        add(demandSummary);
    }

    private HtmlElement generateProgressText(Demand demand, float progressValue) {

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
        } else {

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

}

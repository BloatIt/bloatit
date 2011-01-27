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
package com.bloatit.web.html.pages.demand;

import java.util.Locale;

import com.bloatit.common.Image;
import com.bloatit.common.UnauthorizedOperationException;
import com.bloatit.framework.Translation;
import com.bloatit.framework.demand.Demand;
import com.bloatit.web.html.components.standard.HtmlDiv;
import com.bloatit.web.html.components.standard.HtmlImage;
import com.bloatit.web.html.components.standard.HtmlSpan;
import com.bloatit.web.html.components.standard.HtmlTitle;
import com.bloatit.web.html.pages.master.HtmlPageComponent;
import com.bloatit.web.server.Context;

public final class DemandSummaryComponent extends HtmlPageComponent {

    private static final int SHORT_TEXT_VARIANCE = 50;
    private static final int SHORT_DESCRIPTION_LENGTH = 200;
    private static final String IMPORTANT_CSS_CLASS = "important";

    public DemandSummaryComponent(final Demand demand) {
        super();

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
                        title.addText(translatedDescription.getTitle());

                        demandSummaryCenter.add(title);

                    } catch (final UnauthorizedOperationException e) {
                        // no right no description and no title
                    }

                }
                demandSummaryTop.add(demandSummaryCenter);

                /*final HtmlDiv karmaBlock = new HtmlDiv("idea_karma");
                karmaBlock.add(new HtmlKudoBlock(demand));

                leftBlock.add(karmaBlock);*/

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

                }
                demandSummaryBottom.add(demandSummaryPopularity);

                //////////////////////
                // Div demand_summary_progress
                final HtmlDiv demandSummaryProgress = new HtmlDiv("demand_summary_popularity");
                {

                }
                demandSummaryBottom.add(demandSummaryProgress);

                //////////////////////
                // Div demand_summary_share
                final HtmlDiv demandSummaryShare = new HtmlDiv("demand_summary_share");
                {

                }
                demandSummaryBottom.add(demandSummaryShare);

                /*final Locale defaultLocale = Context.getLocalizator().getLocale();
                String shortDescription = tr("Error: you do not have the right to see the description.");
                try {
                    final Translation translatedDescription = demand.getDescription().getTranslationOrDefault(defaultLocale);
                    shortDescription = translatedDescription.getShortText(SHORT_DESCRIPTION_LENGTH, SHORT_TEXT_VARIANCE);
                } catch (final UnauthorizedOperationException e1) {
                    // Do nothing.
                }

                final HtmlParagraph text = new HtmlParagraph(shortDescription);
                text.setCssClass("idea_link_text");
                demandSumaryBottom.add(text);

                float progressValue = 0;
                try {
                    progressValue = (float) Math.floor(demand.getProgression());
                    float cappedProgressValue = progressValue;
                    if (cappedProgressValue > Demand.PROGRESSION_PERCENT) {
                        cappedProgressValue = Demand.PROGRESSION_PERCENT;
                    }

                    final HtmlProgressBar progressBar = new HtmlProgressBar(cappedProgressValue);
                    demandSumaryBottom.add(progressBar);
                } catch (final UnauthorizedOperationException e) {
                    // No right, no progress bar
                }

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
                    progressText.setCssClass("idea_progress_text");

                    progressText.add(amount);
                    progressText.addText(Context.tr(" no offer ("));
                    progressText.add(new OfferPageUrl(demand).getHtmlLink(Context.tr("make an offer")));
                    progressText.addText(Context.tr(")"));

                    demandSumaryBottom.add(progressText);
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
                        progressText.setCssClass("idea_progress_text");

                        progressText.add(amount);
                        progressText.addText(Context.tr(" i.e. "));
                        progressText.add(progress);
                        progressText.addText(Context.tr(" of "));
                        progressText.add(target);
                        progressText.addText(Context.tr(" requested "));

                        demandSumaryBottom.add(progressText);
                    } catch (final UnauthorizedOperationException e) {
                        // No right, no display
                    }
                }*/

            }
            demandSummary.add(demandSummaryBottom);

            //////////////////////
            // Div demand_summary_share
            final HtmlDiv demand_sumary_share = new HtmlDiv("demand_sumary_share", "demand_sumary_share");
            {

                /*demand_sumary_share.add(new IdeaContributeButtonComponent(demand));
                demand_sumary_share.add(new IdeaMakeOfferButtonComponent(demand));*/

            }
            demandSummary.add(demand_sumary_share);

            /*
            final HtmlDiv rightBlock = new HtmlDiv("idea_summary_right");
            {
                rightBlock.add(new HtmlImage(new Image("/resources/img/idea.png", Image.ImageType.DISTANT)));
            }
            demandSumary.add(rightBlock);*/
        }

        add(demandSummary);
    }
}

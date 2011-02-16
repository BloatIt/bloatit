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

import org.apache.commons.lang.NotImplementedException;

import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.framework.utils.i18n.CurrencyLocale;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.components.HtmlDiv;
import com.bloatit.framework.webserver.components.HtmlImage;
import com.bloatit.framework.webserver.components.HtmlParagraph;
import com.bloatit.framework.webserver.components.HtmlSpan;
import com.bloatit.framework.webserver.components.HtmlTitle;
import com.bloatit.framework.webserver.components.PlaceHolderElement;
import com.bloatit.framework.webserver.components.meta.HtmlElement;
import com.bloatit.framework.webserver.components.meta.HtmlNode;
import com.bloatit.model.Demand;
import com.bloatit.model.Offer;
import com.bloatit.model.Translation;
import com.bloatit.model.demand.DemandImplementation;
import com.bloatit.web.HtmlTools;
import com.bloatit.web.pages.demand.DemandTabPane;
import com.bloatit.web.url.DemandPageUrl;
import com.bloatit.web.url.FileResourceUrl;

public final class HtmlDemandSumary extends HtmlDiv {

    private static final String IMPORTANT_CSS_CLASS = "important";
    private final Demand demand;
    private Locale defaultLocale;

    public enum Compacity {
        NORMAL("demand_summary"), COMPACT("compact_demand_summary"), LINE("line_demand_summary");

        private final String cssClass;


        private Compacity(String cssClass) {
            this.cssClass = cssClass;

        }

        public String getCssClass() {
            return cssClass;
        }

    }

    //"demand_summary"
    public HtmlDemandSumary(final Demand demand, Compacity compacity) {
        super(compacity.getCssClass());
        this.demand = demand;
        if (demand == null) {
            addText("");
            return;
        }
        // Extract locales stuffs
        defaultLocale = Context.getLocalizator().getLocale();


        try {
            switch (compacity) {
            case NORMAL:
                generateNormalStructure();
                break;
            case COMPACT:
                generateCompactStructure();
                break;
            case LINE:
                throw new NotImplementedException();
            default:
                break;
            }


        } catch (UnauthorizedOperationException e) {
            addText("");
            return;
        }
    }

    /**
     * @throws UnauthorizedOperationException
     *
     */
    private void generateCompactStructure() throws UnauthorizedOperationException {
        final HtmlDiv demandSummaryTop = new HtmlDiv("demand_summary_top");
        {
            demandSummaryTop.add(generateTitle());
        }
        add(demandSummaryTop);




        final HtmlDiv demandSummaryBottom = new HtmlDiv("demand_sumary_bottom");
        {



            final HtmlDiv demandSummaryLeft = new HtmlDiv("demand_summary_left");
            {
                // Add project image
                demandSummaryLeft.add(generateProjectImage());
            }
            demandSummaryBottom.add(demandSummaryLeft);

            final HtmlDiv demandSummaryCenter = new HtmlDiv("demand_summary_center");
            {

                final HtmlDiv demandSummaryProgress = generateProgress();
                demandSummaryProgress.add(generateDetails());
                demandSummaryCenter.add(demandSummaryProgress);

            }
            demandSummaryBottom.add(demandSummaryCenter);




        }
        add(demandSummaryBottom);

    }

    /**
     *
     * @throws UnauthorizedOperationException
     */
    private void generateNormalStructure() throws UnauthorizedOperationException {
        final HtmlDiv demandSummaryTop = new HtmlDiv("demand_summary_top");
        {
            final HtmlDiv demandSummaryLeft = new HtmlDiv("demand_summary_left");
            {
                // Add project image
                demandSummaryLeft.add(generateProjectImage());
            }
            demandSummaryTop.add(demandSummaryLeft);

            final HtmlDiv demandSummaryCenter = new HtmlDiv("demand_summary_center");
            {
                // Try to display the title
                demandSummaryCenter.add(generateTitle());
            }
            demandSummaryTop.add(demandSummaryCenter);
        }
        add(demandSummaryTop);

        final HtmlDiv demandSummaryBottom = new HtmlDiv("demand_sumary_bottom");
        {
            demandSummaryBottom.add(generatePopularityBlock());

            final HtmlDiv demandSummaryProgress = generateProgress();
            demandSummaryProgress.add(generateDetails());
            demandSummaryBottom.add(demandSummaryProgress);
        }
        add(demandSummaryBottom);
    }

    /**
     *
     * @return
     * @throws UnauthorizedOperationException
     */
    private HtmlDiv generateProgress() throws UnauthorizedOperationException {
        final HtmlDiv demandSummaryProgress = new HtmlDiv("demand_summary_progress");
        {
            float progressValue = 0;
            // Progress bar

            progressValue = (float) Math.floor(demand.getProgression());
            float cappedProgressValue = progressValue;
            if (cappedProgressValue > DemandImplementation.PROGRESSION_PERCENT) {
                cappedProgressValue = DemandImplementation.PROGRESSION_PERCENT;
            }

            final HtmlProgressBar progressBar = new HtmlProgressBar(cappedProgressValue);
            demandSummaryProgress.add(progressBar);

            // Progress text
            demandSummaryProgress.add(generateProgressText(demand, progressValue));

        }
        return demandSummaryProgress;
    }

    /**
     *
     * @return
     * @throws UnauthorizedOperationException
     */
    private HtmlDiv generateDetails() throws UnauthorizedOperationException {
        final HtmlDiv demandSummaryDetails = new HtmlDiv("demand_sumary_details");
        {

            final int commentsCount = demand.getComments().size();
            final int offersCount = demand.getOffers().size();

            final int contributionsCount = demand.getContributions().size();

            final DemandPageUrl commentsDemandUrl = new DemandPageUrl(demand);
            commentsDemandUrl.setAnchor("comments_block");

            final DemandPageUrl offersDemandUrl = new DemandPageUrl(demand);
            offersDemandUrl.getDemandTabPaneUrl().setActiveTabKey(DemandTabPane.OFFERS_TAB);
            offersDemandUrl.setAnchor("demand_tab_pane");

            final DemandPageUrl contributionsDemandUrl = new DemandPageUrl(demand);
            contributionsDemandUrl.getDemandTabPaneUrl().setActiveTabKey(DemandTabPane.PARTICIPATIONS_TAB);
            contributionsDemandUrl.setAnchor("demand_tab_pane");

            demandSummaryDetails.add(commentsDemandUrl.getHtmlLink(Context.trn("{0} comment", "{0} comments", commentsCount, new Integer(
                    commentsCount))));
            demandSummaryDetails.addText(" – ");
            demandSummaryDetails.add(offersDemandUrl.getHtmlLink(Context.trn("{0} offer", "{0} offers", offersCount, new Integer(offersCount))));
            demandSummaryDetails.addText(" – ");
            demandSummaryDetails.add(contributionsDemandUrl.getHtmlLink(Context.trn("{0} contribution", "{0} contributions", contributionsCount,
                    new Integer(contributionsCount))));

        }
        return demandSummaryDetails;
    }

    /**
     *
     * @return
     */
    private HtmlDiv generatePopularityBlock() {
        final HtmlDiv demandSummaryPopularity = new HtmlDiv("demand_summary_popularity");
        {
            final HtmlParagraph popularityText = new HtmlParagraph(Context.tr("Popularity"), "demand_popularity_text");
            final HtmlParagraph popularityScore = new HtmlParagraph(HtmlTools.compressKarma(demand.getPopularity()), "demand_popularity_score");

            demandSummaryPopularity.add(popularityText);
            demandSummaryPopularity.add(popularityScore);
        }
        return demandSummaryPopularity;
    }

    /**
     *
     * @return
     * @throws UnauthorizedOperationException
     */
    private HtmlNode generateProjectImage() throws UnauthorizedOperationException {
        FileResourceUrl imageUrl = new FileResourceUrl(demand.getProject().getImage());
        return new HtmlImage(imageUrl, "project_image");
    }

    /**
     *
     * @return
     * @throws UnauthorizedOperationException
     */
    private HtmlNode generateTitle() throws UnauthorizedOperationException {
        final Translation translatedDescription = demand.getDescription().getTranslationOrDefault(defaultLocale);
        final HtmlSpan projectSpan = new HtmlSpan("demand_project_title");
        projectSpan.addText(demand.getProject().getName());
        final HtmlTitle title = new HtmlTitle(1);
        title.setCssClass("demand_title");
        title.add(projectSpan);
        title.addText(" – ");
        title.add(new DemandPageUrl(demand).getHtmlLink(translatedDescription.getTitle()));

        return title;
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

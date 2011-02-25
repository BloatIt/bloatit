package com.bloatit.web.linkable.demands;

import java.text.NumberFormat;
import java.util.Locale;

import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.framework.utils.i18n.CurrencyLocale;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.components.HtmlDiv;
import com.bloatit.framework.webserver.components.HtmlParagraph;
import com.bloatit.framework.webserver.components.HtmlSpan;
import com.bloatit.framework.webserver.components.PlaceHolderElement;
import com.bloatit.framework.webserver.components.meta.HtmlElement;
import com.bloatit.model.Demand;
import com.bloatit.model.Offer;
import com.bloatit.model.Translation;
import com.bloatit.model.demand.DemandImplementation;
import com.bloatit.web.components.HtmlProgressBar;
import com.bloatit.web.url.DemandPageUrl;

public class DemandsTools {

    private static final String IMPORTANT_CSS_CLASS = "important";


    public static String getTitle(Demand demand) throws UnauthorizedOperationException {
        final Locale defaultLocale = Context.getLocalizator().getLocale();
        final Translation translatedDescription = demand.getDescription().getTranslationOrDefault(defaultLocale);
        return translatedDescription.getTitle();
    }

    /**
     * @return
     * @throws UnauthorizedOperationException
     */
    public static HtmlDiv generateProgress(Demand demand) throws UnauthorizedOperationException {
        final HtmlDiv demandSummaryProgress = new HtmlDiv("summary_progress");
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

    private static HtmlElement generateProgressText(final Demand demand, final float progressValue) {

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
            progressText.setCssClass("progress_text");

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
            progressText.setCssClass("progress_text");

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

    public static HtmlDiv generateDetails(Demand demand) throws UnauthorizedOperationException {
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

            demandSummaryDetails.add(commentsDemandUrl.getHtmlLink(Context.trn("{0} comment",
                                                                               "{0} comments",
                                                                               commentsCount,
                                                                               new Integer(commentsCount))));
            demandSummaryDetails.addText(" – ");
            demandSummaryDetails.add(offersDemandUrl.getHtmlLink(Context.trn("{0} offer", "{0} offers", offersCount, new Integer(offersCount))));
            demandSummaryDetails.addText(" – ");
            demandSummaryDetails.add(contributionsDemandUrl.getHtmlLink(Context.trn("{0} contribution",
                                                                                    "{0} contributions",
                                                                                    contributionsCount,
                                                                                    new Integer(contributionsCount))));

        }
        return demandSummaryDetails;
    }

}

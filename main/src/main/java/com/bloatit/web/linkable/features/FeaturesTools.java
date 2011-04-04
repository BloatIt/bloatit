package com.bloatit.web.linkable.features;

import static com.bloatit.framework.webserver.Context.trn;

import java.text.NumberFormat;
import java.util.Locale;

import com.bloatit.framework.exceptions.specific.UnauthorizedOperationException;
import com.bloatit.framework.utils.i18n.CurrencyLocale;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.components.HtmlDiv;
import com.bloatit.framework.webserver.components.HtmlParagraph;
import com.bloatit.framework.webserver.components.HtmlSpan;
import com.bloatit.framework.webserver.components.PlaceHolderElement;
import com.bloatit.framework.webserver.components.meta.HtmlElement;
import com.bloatit.framework.webserver.components.meta.HtmlMixedText;
import com.bloatit.model.Feature;
import com.bloatit.model.Offer;
import com.bloatit.model.Translation;
import com.bloatit.model.feature.FeatureImplementation;
import com.bloatit.web.components.HtmlProgressBar;
import com.bloatit.web.url.FeaturePageUrl;

public class FeaturesTools {

    private static final String IMPORTANT_CSS_CLASS = "important";

    public static String getTitle(Feature feature) throws UnauthorizedOperationException {
        final Locale defaultLocale = Context.getLocalizator().getLocale();
        final Translation translatedDescription = feature.getDescription().getTranslationOrDefault(defaultLocale);
        return translatedDescription.getTitle();
    }

    public static HtmlDiv generateProgress(Feature feature) throws UnauthorizedOperationException {
        return generateProgress(feature, false);
    }

    /**
     * @return
     * @throws UnauthorizedOperationException
     */
    public static HtmlDiv generateProgress(Feature feature, boolean slim) throws UnauthorizedOperationException {
        final HtmlDiv featureSummaryProgress = new HtmlDiv("summary_progress");
        {
            float progressValue = 0;
            // Progress bar

            progressValue = (float) Math.floor(feature.getProgression());
            float cappedProgressValue = progressValue;
            if (cappedProgressValue > FeatureImplementation.PROGRESSION_PERCENT) {
                cappedProgressValue = FeatureImplementation.PROGRESSION_PERCENT;
            }

            final HtmlProgressBar progressBar = new HtmlProgressBar(cappedProgressValue, slim);
            featureSummaryProgress.add(progressBar);

            // Progress text
            featureSummaryProgress.add(generateProgressText(feature, progressValue));

        }
        return featureSummaryProgress;
    }

    private static HtmlElement generateProgressText(final Feature feature, final float progressValue) {

        Offer currentOffer = null;
        try {
            currentOffer = feature.getSelectedOffer();
        } catch (final UnauthorizedOperationException e1) {
            // Nothing.
        }
        if (currentOffer == null) {

            final HtmlSpan amount = new HtmlSpan();
            amount.setCssClass(IMPORTANT_CSS_CLASS);

            CurrencyLocale currency;
            try {
                currency = Context.getLocalizator().getCurrency(feature.getContribution());

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
            amountCurrency = Context.getLocalizator().getCurrency(feature.getContribution());
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

            progressText.add(new HtmlMixedText(trn("<0:1600€:> i.e <1:69%:> of <2:2300€:> requested",
                                                   "<0:1600€:> i.e <1:69%:> of <2:2300€:> requested",
                                                   currentOffer.getAmount().intValue()), amount, progress, target));

            return progressText;
        } catch (final UnauthorizedOperationException e) {
            // No right, no display
            return new PlaceHolderElement();
        }
    }

    public static HtmlDiv generateDetails(Feature feature) throws UnauthorizedOperationException {
        final HtmlDiv featureSummaryDetails = new HtmlDiv("feature_sumary_details");
        {

            final int commentsCount = feature.getComments().size();
            final int offersCount = feature.getOffers().size();

            final int contributionsCount = feature.getContributions().size();

            final FeaturePageUrl commentsFeatureUrl = new FeaturePageUrl(feature);
            commentsFeatureUrl.setAnchor("comments_block");

            final FeaturePageUrl offersFeatureUrl = new FeaturePageUrl(feature);
            offersFeatureUrl.getFeatureTabPaneUrl().setActiveTabKey(FeatureTabPane.OFFERS_TAB);
            offersFeatureUrl.setAnchor("feature_tab_pane");

            final FeaturePageUrl contributionsFeatureUrl = new FeaturePageUrl(feature);
            contributionsFeatureUrl.getFeatureTabPaneUrl().setActiveTabKey(FeatureTabPane.CONTRIBUTIONS_TAB);
            contributionsFeatureUrl.setAnchor("feature_tab_pane");

            featureSummaryDetails.add(commentsFeatureUrl.getHtmlLink(Context.trn("{0} comment",
                                                                               "{0} comments",
                                                                               commentsCount,
                                                                               new Integer(commentsCount))));
            featureSummaryDetails.addText(" – ");
            featureSummaryDetails.add(offersFeatureUrl.getHtmlLink(Context.trn("{0} offer", "{0} offers", offersCount, new Integer(offersCount))));
            featureSummaryDetails.addText(" – ");
            featureSummaryDetails.add(contributionsFeatureUrl.getHtmlLink(Context.trn("{0} contribution",
                                                                                    "{0} contributions",
                                                                                    contributionsCount,
                                                                                    new Integer(contributionsCount))));

        }
        return featureSummaryDetails;
    }

}

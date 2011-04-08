package com.bloatit.web.linkable.features;

import static com.bloatit.framework.webserver.Context.trn;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

import com.bloatit.framework.exceptions.highlevel.ShallNotPassException;
import com.bloatit.framework.exceptions.lowlevel.UnauthorizedOperationException;
import com.bloatit.framework.utils.i18n.CurrencyLocale;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.components.HtmlDiv;
import com.bloatit.framework.webserver.components.HtmlParagraph;
import com.bloatit.framework.webserver.components.HtmlSpan;
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

    public static String getTitle(final Feature feature) throws UnauthorizedOperationException {
        final Locale defaultLocale = Context.getLocalizator().getLocale();
        final Translation translatedDescription = feature.getDescription().getTranslationOrDefault(defaultLocale);
        return translatedDescription.getTitle();
    }

    public static HtmlDiv generateProgress(final Feature feature) throws UnauthorizedOperationException {
        return generateProgress(feature, false, BigDecimal.ZERO);
    }

    public static HtmlDiv generateProgress(final Feature feature, final boolean slim) throws UnauthorizedOperationException {
        return generateProgress(feature, slim, BigDecimal.ZERO);
    }

    /**
     * @throws UnauthorizedOperationException
     */
    public static HtmlDiv generateProgress(final Feature feature, final boolean slim, final BigDecimal futureAmount) throws UnauthorizedOperationException {
        final HtmlDiv featureSummaryProgress = new HtmlDiv("feature_summary_progress");
        {
            // Progress bar

            final float progressValue = (float) Math.floor(feature.getProgression());
            float myProgressValue = 0;
            float futureProgressValue = 0;

            if (Context.getSession().isLogged()) {
                myProgressValue = feature.getMemberProgression(Context.getSession().getAuthToken().getMember());
                if (myProgressValue > 0.0f && myProgressValue < 5f) {
                    myProgressValue = 5f;

                }
                myProgressValue = (float) Math.floor(myProgressValue);
            }

            if (!futureAmount.equals(BigDecimal.ZERO)) {
                futureProgressValue = feature.getRelativeProgression(futureAmount);
                if (futureProgressValue > 0.0f && futureProgressValue < 5f) {
                    futureProgressValue = 5f;

                }
                futureProgressValue = (float) Math.floor(futureProgressValue);

            }

            float cappedProgressValue = progressValue;
            if (cappedProgressValue + futureProgressValue > FeatureImplementation.PROGRESSION_PERCENT) {
                cappedProgressValue = FeatureImplementation.PROGRESSION_PERCENT - futureProgressValue;
            }

            final HtmlProgressBar progressBar = new HtmlProgressBar(slim,
                                                                    cappedProgressValue - myProgressValue,
                                                                    cappedProgressValue,
                                                                    cappedProgressValue + futureProgressValue);
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
            Context.getSession().notifyError(Context.tr("An error prevented us from displaying selected offer. Please notify us."));
            throw new ShallNotPassException("User cannot access selected offer", e1);
        }
        if (currentOffer == null) {

            final HtmlSpan amount = new HtmlSpan();
            amount.setCssClass(IMPORTANT_CSS_CLASS);

            CurrencyLocale currency;
            try {
                currency = Context.getLocalizator().getCurrency(feature.getContribution());
                amount.addText(currency.getDefaultString());
            } catch (final UnauthorizedOperationException e) {
                Context.getSession().notifyError(Context.tr("An error prevented us from displaying contribution amount. Please notify us."));
                throw new ShallNotPassException("User cannot access contribution amount", e);
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
            Context.getSession().notifyError(Context.tr("An error prevented us from displaying contribution amount. Please notify us."));
            throw new ShallNotPassException("User cannot access contibution amount", e);
        }
    }

    public static HtmlDiv generateDetails(final Feature feature, final boolean showBugs) throws UnauthorizedOperationException {
        final HtmlDiv featureSummaryDetails = new HtmlDiv("feature_summary_details");
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
            if (showBugs) {
                final int bugCount = feature.getOpenBugs().size();
                if(bugCount > 0) {

                    final FeaturePageUrl bugsFeatureUrl = new FeaturePageUrl(feature);
                    bugsFeatureUrl.getFeatureTabPaneUrl().setActiveTabKey(FeatureTabPane.BUGS_TAB);
                    bugsFeatureUrl.setAnchor("feature_tab_pane");

                    featureSummaryDetails.addText(" – ");
                    featureSummaryDetails.add(bugsFeatureUrl.getHtmlLink(Context.trn("{0} bug",
                                                                                              "{0} bugs",
                                                                                              bugCount,
                                                                                              new Integer(bugCount))));
                }
            }

        }
        return featureSummaryDetails;
    }
}

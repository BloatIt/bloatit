//
// Copyright (c) 2011 Linkeos.
//
// This file is part of Elveos.org.
// Elveos.org is free software: you can redistribute it and/or modify it
// under the terms of the GNU General Public License as published by the
// Free Software Foundation, either version 3 of the License, or (at your
// option) any later version.
//
// Elveos.org is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
// more details.
// You should have received a copy of the GNU General Public License along
// with Elveos.org. If not, see http://www.gnu.org/licenses/.
//
package com.bloatit.web.linkable.features;

import static com.bloatit.framework.webprocessor.context.Context.trn;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

import com.bloatit.data.DaoFeature.FeatureState;
import com.bloatit.framework.utils.i18n.CurrencyLocale;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlImage;
import com.bloatit.framework.webprocessor.components.HtmlParagraph;
import com.bloatit.framework.webprocessor.components.HtmlSpan;
import com.bloatit.framework.webprocessor.components.HtmlTitle;
import com.bloatit.framework.webprocessor.components.PlaceHolderElement;
import com.bloatit.framework.webprocessor.components.meta.HtmlBranch;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.components.meta.HtmlMixedText;
import com.bloatit.framework.webprocessor.components.meta.HtmlText;
import com.bloatit.framework.webprocessor.components.meta.XmlNode;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.Feature;
import com.bloatit.model.FeatureImplementation;
import com.bloatit.model.Image;
import com.bloatit.model.Milestone;
import com.bloatit.model.Offer;
import com.bloatit.model.Translation;
import com.bloatit.model.right.AuthToken;
import com.bloatit.model.right.UnauthorizedOperationException;
import com.bloatit.web.WebConfiguration;
import com.bloatit.web.components.HtmlProgressBar;
import com.bloatit.web.linkable.features.FeatureTabPane.FeatureTabKey;
import com.bloatit.web.linkable.softwares.SoftwaresTools;
import com.bloatit.web.url.FeaturePageUrl;

public class FeaturesTools {

    private static final String IMPORTANT_CSS_CLASS = "important";

    public enum FeatureContext { 
        FEATURE_PAGE, FEATURE_LIST_PAGE, INDEX_PAGE, OTHER, DASHBOARD,
    }

    public static String getTitle(final Feature feature) {
        final Locale defaultLocale = Context.getLocalizator().getLocale();
        final Translation translatedDescription = feature.getDescription().getTranslationOrDefault(defaultLocale);
        return translatedDescription.getTitle();
    }

    /**
     * Convenience method for {@link #generateFeatureTitle(Feature, boolean)}
     * with <i>isTitle</i> set to false
     * 
     * @param feature the feature for which a block will be generated
     * @return the generated block
     * @throws UnauthorizedOperationException when some operation cannot be
     *             accessed
     */
    public static HtmlBranch generateFeatureTitle(final Feature feature) throws UnauthorizedOperationException {
        return generateFeatureTitle(feature, false);
    }

    /**
     * Generates a block indicating the title of a given <code>feature</code>
     * <p>
     * The block contains:
     * <li>The software name (and a link to the software page) of the
     * <code>feature</code></li>
     * <li>The feature title</li>
     * </p>
     * <p>
     * Note:
     * <li>If <code>isTitle</code> is <i>true</i>, the whole block will be
     * rendered as a title</li>
     * <li>If <code>isTitle</code> is <i>true</i>, the whole block will be
     * rendered as a normal text, and a link to the feature page will be added</li>
     * </p>
     * 
     * @param feature the feature for which we want to generate a description
     *            block
     * @param isTitle <i>true</i> if the block has to be rendered as a title,
     *            <i>false</i> otherwise
     * @return the title block
     * @throws UnauthorizedOperationException when some operation cannot be
     *             accessed
     */
    private static HtmlBranch generateFeatureTitle(final Feature feature, final boolean isTitle) throws UnauthorizedOperationException {
        HtmlBranch master;
        if (isTitle) {
            master = new HtmlTitle(1);
        } else {
            master = new HtmlSpan("feature_complete_title");
        }
        master.setCssClass("feature_title");

        if (isTitle) {
            master.addText(getTitle(feature));
        } else {
            master.add(new FeaturePageUrl(feature, FeatureTabKey.description).getHtmlLink(getTitle(feature)));
        }
        final HtmlSpan softwareLink = new SoftwaresTools.Link(feature.getSoftware());
        final HtmlMixedText mixed = new HtmlMixedText(Context.tr(" (<0:software:>)"), softwareLink);
        master.add(mixed);
        return master;
    }

    public static HtmlDiv generateProgress(final Feature feature, FeatureContext context) throws UnauthorizedOperationException {
        return generateProgress(feature, BigDecimal.ZERO, context);
    }

    public static HtmlDiv generateProgress(final Feature feature, final BigDecimal futureAmount, FeatureContext context)
            throws UnauthorizedOperationException {
        final HtmlDiv featureSummaryProgress = new HtmlDiv("feature_summary_progress");
        {
            // Progress bar
            final float progressValue = (float) Math.floor(feature.getProgression());
            float myProgressValue = 0;
            float futureProgressValue = 0;

            if (AuthToken.isAuthenticated()) {
                myProgressValue = feature.getMemberProgression(AuthToken.getMember());
                if (myProgressValue > 0.0f && myProgressValue < Math.min(progressValue / 2, 5f)) {
                    myProgressValue = Math.min(progressValue / 2, 5f);
                }
                myProgressValue = (float) Math.floor(myProgressValue);
            }

            if (!futureAmount.equals(BigDecimal.ZERO)) {
                futureProgressValue = feature.getRelativeProgression(futureAmount);
                if (futureProgressValue > 0.0f && futureProgressValue < Math.min(progressValue / 2, 5f)) {
                    futureProgressValue = Math.min(progressValue / 2, 5f);
                }
                futureProgressValue = (float) Math.floor(futureProgressValue);
            }

            float cappedProgressValue = progressValue;
            if (cappedProgressValue + futureProgressValue > FeatureImplementation.PROGRESSION_PERCENT) {
                cappedProgressValue = FeatureImplementation.PROGRESSION_PERCENT - futureProgressValue;
            }

            XmlNode barLabel = new PlaceHolderElement();

            if (feature.getFeatureState() == FeatureState.DEVELOPPING) {
                barLabel = new HtmlText(Context.tr("In developement"));
            } else if (feature.getFeatureState() == FeatureState.PENDING && context != FeatureContext.FEATURE_PAGE) {
                final FeaturePageUrl offersFeatureUrl = new FeaturePageUrl(feature, FeatureTabKey.offers);
                offersFeatureUrl.setAnchor("feature_tab_pane");
                barLabel = offersFeatureUrl.getHtmlLink(Context.tr("make an offer"));
            }

            String styleSuffix = null;
            if (feature.getFeatureState() == FeatureState.FINISHED) {
                styleSuffix = "success";
            }

            final HtmlProgressBar progressBar = new HtmlProgressBar(barLabel,
                                                                    styleSuffix,
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
        currentOffer = feature.getSelectedOffer();
        if (currentOffer == null) {

            final HtmlSpan amount = new HtmlSpan();
            amount.setCssClass(IMPORTANT_CSS_CLASS);

            CurrencyLocale currency;
            currency = Context.getLocalizator().getCurrency(feature.getContribution());
            amount.addText(currency.getSimpleEuroString());

            final HtmlParagraph progressText = new HtmlParagraph();
            progressText.setCssClass("progress_text");

            progressText.add(new HtmlMixedText(Context.trn("<0::> funded", "<0::> funded", feature.getContribution().intValue()), amount));

            return progressText;
        }

        // Amount
        CurrencyLocale amountCurrency;
        amountCurrency = Context.getLocalizator().getCurrency(feature.getContribution());
        final HtmlSpan amount = new HtmlSpan();
        amount.setCssClass(IMPORTANT_CSS_CLASS);
        amount.addText(amountCurrency.getSimpleEuroString());

        // Target
        final CurrencyLocale targetCurrency = Context.getLocalizator().getCurrency(currentOffer.getAmount());
        final HtmlSpan target = new HtmlSpan();
        target.setCssClass(IMPORTANT_CSS_CLASS);
        target.addText(targetCurrency.getSimpleEuroString());

        // Progress
        final HtmlSpan progress = new HtmlSpan();
        progress.setCssClass(IMPORTANT_CSS_CLASS);
        final NumberFormat format = Context.getLocalizator().getNumberFormat();
        format.setMinimumFractionDigits(0);
        progress.addText("" + format.format(progressValue) + " %");

        final HtmlParagraph progressText = new HtmlParagraph();
        progressText.setCssClass("progress_text");

        progressText.add(new HtmlMixedText(trn("<0:1600€:> i.e <1:69%:> of <2:2300€:> requested",
                                               "<0:1600€:> i.e <1:69%:> of <2:2300€:> requested",
                                               currentOffer.getAmount().intValue()), amount, progress, target));

        return progressText;
    }

    public static HtmlDiv generateDetails(final Feature feature, final boolean showBugs) {
        final HtmlDiv featureSummaryDetails = new HtmlDiv("feature_summary_details");
        {

            feature.getComments();
            final Long commentsCount = feature.getCommentsCount();

            final int contributionsCount = feature.getContributions().size();

            final FeaturePageUrl commentsFeatureUrl = new FeaturePageUrl(feature, FeatureTabKey.description);
            commentsFeatureUrl.setAnchor("comments_block");

            final FeaturePageUrl contributionsFeatureUrl = new FeaturePageUrl(feature, FeatureTabKey.contributions);
            contributionsFeatureUrl.setAnchor("feature_tab_pane");

            final String trn = Context.trn("{0} comment", "{0} comments", commentsCount, Long.valueOf((commentsCount)));
            featureSummaryDetails.add(commentsFeatureUrl.getHtmlLink(trn));
            if (feature.getFeatureState() == FeatureState.PENDING || feature.getFeatureState() == FeatureState.PREPARING) {

                // PENDING or PREPARING we add the number of offers
                final FeaturePageUrl offersFeatureUrl = new FeaturePageUrl(feature, FeatureTabKey.offers);
                offersFeatureUrl.setAnchor("feature_tab_pane");

                final int offersCount = feature.getOffers().size();
                if (feature.getSelectedOffer() != null) {
                    featureSummaryDetails.addText(" – ");
                    featureSummaryDetails.add(offersFeatureUrl.getHtmlLink(Context.trn("{0} offer", "{0} offers", offersCount, offersCount)));
                }

            } else if (feature.getFeatureState() == FeatureState.DEVELOPPING) {
                // DEVELOPPING we add bug count and release count
                final FeaturePageUrl bugsFeatureUrl = new FeaturePageUrl(feature, FeatureTabKey.bugs);
                bugsFeatureUrl.setAnchor("feature_tab_pane");

                final int bugCount = feature.getOpenBugs().size();
                featureSummaryDetails.addText(" – ");
                featureSummaryDetails.add(bugsFeatureUrl.getHtmlLink(Context.trn("{0} open bug", "{0} open bugs", bugCount, bugCount)));

                final FeaturePageUrl releasesFeatureUrl = new FeaturePageUrl(feature, FeatureTabKey.offers);
                releasesFeatureUrl.setAnchor("feature_tab_pane");

                int releaseCount = 0;
                for (final Milestone m : feature.getValidatedOffer().getMilestones()) {
                    releaseCount += m.getReleases().size();
                }
                featureSummaryDetails.addText(" – ");
                featureSummaryDetails.add(releasesFeatureUrl.getHtmlLink(Context.trn("{0} release", "{0} releases", releaseCount, releaseCount)));
            }
            featureSummaryDetails.addText(" – ");
            final String trn2 = Context.trn("{0} contribution", "{0} contributions", contributionsCount, contributionsCount);
            featureSummaryDetails.add(contributionsFeatureUrl.getHtmlLink(trn2));

            if (showBugs) {
                final int bugCount = feature.getOpenBugs().size();
                if (bugCount > 0) {

                    final FeaturePageUrl bugsFeatureUrl = new FeaturePageUrl(feature, FeatureTabKey.bugs);
                    bugsFeatureUrl.setAnchor("feature_tab_pane");

                    featureSummaryDetails.addText(" – ");
                    featureSummaryDetails.add(bugsFeatureUrl.getHtmlLink(Context.trn("{0} bug", "{0} bugs", bugCount, Integer.valueOf(bugCount))));
                }
            }

        }
        return featureSummaryDetails;
    }

    public static HtmlElement generateState(final Feature feature) {
        // Progress state

        final HtmlDiv progressState = new HtmlDiv("feature_summary_state");

        final String languageCode = Context.getLocalizator().getLanguageCode();
        switch (feature.getFeatureState()) {
            case DEVELOPPING:
            case PENDING:
            case PREPARING:
                break;
            case FINISHED:
                progressState.add(new HtmlImage(new Image(WebConfiguration.getImgFeatureStateSuccess(languageCode)), Context.tr("success")));
                return progressState;
            case DISCARDED:
                progressState.add(new HtmlImage(new Image(WebConfiguration.getImgFeatureStateFailed(languageCode)), Context.tr("failed")));
                return progressState;
        }
        return new PlaceHolderElement();
    }
}

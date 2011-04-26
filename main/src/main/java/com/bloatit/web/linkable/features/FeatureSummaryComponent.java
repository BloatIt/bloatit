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

import static com.bloatit.framework.webprocessor.context.Context.tr;
import static com.bloatit.framework.webprocessor.context.Context.trn;

import java.math.BigDecimal;
import java.util.Date;

import com.bloatit.framework.exceptions.highlevel.ShallNotPassException;
import com.bloatit.framework.exceptions.lowlevel.UnauthorizedOperationException;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.framework.utils.datetime.DateUtils;
import com.bloatit.framework.utils.datetime.TimeRenderer;
import com.bloatit.framework.utils.i18n.CurrencyLocale;
import com.bloatit.framework.utils.i18n.DateLocale.FormatStyle;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlLink;
import com.bloatit.framework.webprocessor.components.HtmlParagraph;
import com.bloatit.framework.webprocessor.components.HtmlTitle;
import com.bloatit.framework.webprocessor.components.PlaceHolderElement;
import com.bloatit.framework.webprocessor.components.meta.HtmlMixedText;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.Actor;
import com.bloatit.model.Bug;
import com.bloatit.model.Feature;
import com.bloatit.model.Offer;
import com.bloatit.model.Release;
import com.bloatit.web.HtmlTools;
import com.bloatit.web.components.HtmlAuthorLink;
import com.bloatit.web.linkable.members.MembersTools;
import com.bloatit.web.linkable.softwares.SoftwaresTools;
import com.bloatit.web.pages.master.HtmlPageComponent;
import com.bloatit.web.url.ContributionProcessUrl;
import com.bloatit.web.url.MakeOfferPageUrl;
import com.bloatit.web.url.PopularityVoteActionUrl;
import com.bloatit.web.url.ReleasePageUrl;
import com.bloatit.web.url.ReportBugPageUrl;

public final class FeatureSummaryComponent extends HtmlPageComponent {

    private final Feature feature;

    public FeatureSummaryComponent(final Feature feature) {
        super();
        this.feature = feature;

        try {
            // ////////////////////
            // Div feature_summary
            final HtmlDiv featureSummary = new HtmlDiv("feature_summary");
            {
                // ////////////////////
                // Div feature_summary_top
                final HtmlDiv featureSummaryTop = new HtmlDiv("feature_summary_top");
                {
                    // ////////////////////
                    // Div feature_summary_left
                    final HtmlDiv featureSummaryLeft = new HtmlDiv("feature_summary_left");
                    {
                        featureSummaryLeft.add(SoftwaresTools.getSoftwareLogo(feature.getSoftware()));
                    }
                    featureSummaryTop.add(featureSummaryLeft);

                    // ////////////////////
                    // Div feature_summary_center
                    final HtmlDiv featureSummaryCenter = new HtmlDiv("feature_summary_center");
                    {
                        // Try to display the title

                        final HtmlTitle title = new HtmlTitle(1);
                        title.setCssClass("feature_title");
                        title.add(SoftwaresTools.getSoftwareLink(feature.getSoftware()));
                        title.addText(" – ");
                        title.addText(FeaturesTools.getTitle(feature));

                        featureSummaryCenter.add(title);
                    }
                    featureSummaryTop.add(featureSummaryCenter);
                }
                featureSummary.add(featureSummaryTop);

                // ////////////////////
                // Div feature_summary_bottom
                final HtmlDiv featureSummaryBottom = new HtmlDiv("feature_summary_bottom");
                {

                    // ////////////////////
                    // Div feature_summary_popularity
                    final HtmlDiv featureSummaryPopularity = new HtmlDiv("feature_summary_popularity");
                    {
                        final HtmlParagraph popularityText = new HtmlParagraph(Context.tr("Popularity"), "feature_popularity_text");
                        final HtmlParagraph popularityScore = new HtmlParagraph(HtmlTools.compressKarma(feature.getPopularity()),
                                                                                "feature_popularity_score");

                        featureSummaryPopularity.add(popularityText);
                        featureSummaryPopularity.add(popularityScore);

                        if (!feature.getRights().isOwner()) {
                            final int vote = feature.getUserVoteValue();
                            if (vote == 0) {
                                final HtmlDiv featurePopularityJudge = new HtmlDiv("feature_popularity_judge");
                                {

                                    // Usefull
                                    final PopularityVoteActionUrl usefulUrl = new PopularityVoteActionUrl(feature, true);
                                    final HtmlLink usefulLink = usefulUrl.getHtmlLink("+");
                                    usefulLink.setCssClass("useful");

                                    // Useless
                                    final PopularityVoteActionUrl uselessUrl = new PopularityVoteActionUrl(feature, false);
                                    final HtmlLink uselessLink = uselessUrl.getHtmlLink("−");
                                    uselessLink.setCssClass("useless");

                                    featurePopularityJudge.add(usefulLink);
                                    featurePopularityJudge.add(uselessLink);
                                }
                                featureSummaryPopularity.add(featurePopularityJudge);
                            } else {
                                // Already voted
                                final HtmlDiv featurePopularityJudged = new HtmlDiv("feature_popularity_judged");
                                {
                                    if (vote > 0) {
                                        featurePopularityJudged.add(new HtmlParagraph("+" + vote, "useful"));
                                    } else {
                                        featurePopularityJudged.add(new HtmlParagraph("−" + Math.abs(vote), "useless"));
                                    }
                                }
                                featureSummaryPopularity.add(featurePopularityJudged);
                            }
                        } else {
                            final HtmlDiv featurePopularityNone = new HtmlDiv("feature_popularity_none");

                            featureSummaryPopularity.add(featurePopularityNone);
                        }

                    }
                    featureSummaryBottom.add(featureSummaryPopularity);

                    HtmlDiv featureSummaryProgress;
                    featureSummaryProgress = generateProgressBlock(feature);
                    featureSummaryBottom.add(featureSummaryProgress);

                    // ////////////////////
                    // Div feature_summary_share
                    final HtmlDiv featureSummaryShare = new HtmlDiv("feature_summary_share_button");
                    {
                        @SuppressWarnings("unused") final HtmlLink showHideShareBlock = new HtmlLink("javascript:showHide('feature_summary_share')",
                                                                                                     Context.tr("+ Share"));
                        // TODO: enable share button
                        // featureSummaryShare.add(showHideShareBlock);
                    }
                    featureSummaryBottom.add(featureSummaryShare);
                }
                featureSummary.add(featureSummaryBottom);

                // ////////////////////
                // Div feature_summary_share
                final HtmlDiv feature_summary_share = new HtmlDiv("feature_summary_share", "feature_summary_share");
                featureSummary.add(feature_summary_share);
            }
            add(featureSummary);

        } catch (final UnauthorizedOperationException e) {
            Context.getSession().notifyError(Context.tr("An error prevented us from displaying feature information. Please notify us."));
            throw new ShallNotPassException("User cannot access feature information", e);
        }
    }

    public HtmlDiv generateProgressBlock(final Feature feature) throws UnauthorizedOperationException {
        // ////////////////////
        // Div feature_summary_progress
        final HtmlDiv featureSummaryProgress = new HtmlDiv("feature_summary_progress");
        {

            final HtmlDiv featureSummaryProgressAndState = new HtmlDiv("feature_summary_progress_and_state");
            {
                featureSummaryProgressAndState.add(FeaturesTools.generateProgress(feature));
                featureSummaryProgressAndState.add(FeaturesTools.generateState(feature));
            }

            featureSummaryProgress.add(featureSummaryProgressAndState);

            // ////////////////////
            // Div feature_summary_actions
            final HtmlDiv actions = new HtmlDiv("feature_summary_actions");
            {
                final HtmlDiv actionsButtons = new HtmlDiv("feature_summary_actions_buttons");
                actions.add(actionsButtons);
                switch (feature.getFeatureState()) {
                    case PENDING:
                        actionsButtons.add(new HtmlDiv("contribute_block").add(generateContributeAction()));
                        actionsButtons.add(new HtmlDiv("make_offer_block").add(generateMakeAnOfferAction()));
                        break;
                    case PREPARING:
                        actionsButtons.add(new HtmlDiv("contribute_block").add(generateContributeAction()));
                        actionsButtons.add(new HtmlDiv("alternative_offer_block").add(generateAlternativeOfferAction()));
                        break;
                    case DEVELOPPING:
                        actionsButtons.add(new HtmlDiv("report_bug_block").add(generateDevelopingLeftActions()));
                        actionsButtons.add(new HtmlDiv("developer_description_block").add(generateReportBugAction()));
                        break;
                    case FINISHED:
                        actionsButtons.add(new HtmlDiv("report_bug_block").add(generateFinishedAction()));
                        actionsButtons.add(new HtmlDiv("developer_description_block").add(generateReportBugAction()));
                        break;
                    case DISCARDED:
                        // TODO
                        // actionsButtons.add(new
                        // HtmlDiv("contribute_block").add(generatePendingRightActions()));
                        // actionsButtons.add(new
                        // HtmlDiv("make_offer_block").add(generatePendingLeftActions()));
                        break;
                    default:
                        break;
                }
            }
            featureSummaryProgress.add(actions);

        }
        return featureSummaryProgress;
    }

    public PlaceHolderElement generateContributeAction() {
        final PlaceHolderElement element = new PlaceHolderElement();
        final HtmlParagraph contributeText = new HtmlParagraph(Context.tr("You share this need and you want participate financially?"));
        element.add(contributeText);

        final HtmlLink link = new ContributionProcessUrl(feature).getHtmlLink(Context.tr("Contribute"));
        link.setCssClass("button");
        element.add(link);
        return element;
    }

    private PlaceHolderElement generateMakeAnOfferAction() {
        final PlaceHolderElement element = new PlaceHolderElement();
        final HtmlParagraph makeOfferText = new HtmlParagraph(Context.tr("You are a developer and want to be paid to achieve this request?"));
        element.add(makeOfferText);

        final HtmlLink link = new MakeOfferPageUrl(feature).getHtmlLink(Context.tr("Make an offer"));
        link.setCssClass("button");
        element.add(link);
        return element;
    }

    private PlaceHolderElement generateAlternativeOfferAction() throws UnauthorizedOperationException {
        final PlaceHolderElement element = new PlaceHolderElement();

        final Offer selectedOffer = feature.getSelectedOffer();
        if (selectedOffer != null) {
            final BigDecimal amountLeft = selectedOffer.getAmount().subtract(feature.getContribution());

            if (amountLeft.compareTo(BigDecimal.ZERO) > 0) {

                final CurrencyLocale currency = Context.getLocalizator().getCurrency(amountLeft);

                element.add(new HtmlParagraph(tr(" {0} are missing before the development start.", currency.toString())));
            } else {
                final TimeRenderer renderer = new TimeRenderer(DateUtils.elapsed(DateUtils.now(), feature.getValidationDate()));

                element.add(new HtmlParagraph(tr("The development will begin in about ") + renderer.getTimeString() + "."));
            }
        }
        final HtmlLink link = new MakeOfferPageUrl(feature).getHtmlLink();
        final HtmlParagraph makeOfferText = new HtmlParagraph(new HtmlMixedText(Context.tr("An offer has already been made on this feature. However, you can <0::make an alternative offer>."),
                                                                                link));
        element.add(makeOfferText);

        return element;
    }

    public PlaceHolderElement generateReportBugAction() throws UnauthorizedOperationException {
        final PlaceHolderElement element = new PlaceHolderElement();

        if (!feature.getSelectedOffer().hasRelease()) {
            final Date releaseDate = feature.getSelectedOffer().getCurrentMilestone().getExpirationDate();

            final String date = Context.getLocalizator().getDate(releaseDate).toString(FormatStyle.LONG);

            element.add(new HtmlParagraph(tr("There is no release yet.")));
            if (DateUtils.isInTheFuture(releaseDate)) {
                element.add(new HtmlParagraph(tr("Next release is scheduled for {0}.", date)));
            } else {
                element.add(new HtmlParagraph(tr("Next release was scheduled for {0}.", date)));
            }

        } else {
            final int releaseCount = feature.getSelectedOffer().getCurrentMilestone().getReleases().size();

            final Release lastRelease = feature.getSelectedOffer().getLastRelease();

            final HtmlLink lastReleaseLink = new ReleasePageUrl(lastRelease).getHtmlLink();
            final String releaseDate = Context.getLocalizator().getDate(lastRelease.getCreationDate()).toString(FormatStyle.SHORT);

            element.add(new HtmlParagraph(trn("There is {0} release.", "There is {0} releases.", releaseCount, releaseCount)));

            element.add(new HtmlParagraph(new HtmlMixedText(tr("The <0::last version> was released the {0}.", releaseDate), lastReleaseLink)));

            element.add(new HtmlParagraph(tr(" Test it and report bugs.")));
            final HtmlLink link = new ReportBugPageUrl(feature.getSelectedOffer()).getHtmlLink(Context.tr("Report a bug"));
            link.setCssClass("button");
            element.add(link);
        }

        return element;

    }

    public PlaceHolderElement generateDevelopingLeftActions() throws UnauthorizedOperationException {
        final PlaceHolderElement element = new PlaceHolderElement();

        final Actor<?> author = feature.getSelectedOffer().getAuthor();
        final HtmlLink authorLink = new HtmlAuthorLink(feature.getSelectedOffer());
        element.add(new HtmlDiv("float_left").add(MembersTools.getMemberAvatar(author)));

        element.add(new HtmlParagraph(new HtmlMixedText(tr("This feature is developed by <0>."), authorLink)));

        element.add(new HtmlParagraph(tr("Read the comments to have an more recent informations.")));

        return element;
    }

    public PlaceHolderElement generateFinishedAction() throws UnauthorizedOperationException {
        final PlaceHolderElement element = new PlaceHolderElement();

        final HtmlLink authorLink = new HtmlAuthorLink(feature.getSelectedOffer());
        element.add(new HtmlDiv("float_left").add(MembersTools.getMemberAvatar(feature.getSelectedOffer().getAuthor())));

        element.add(new HtmlParagraph(tr("This feature is finished.")));

        element.add(new HtmlParagraph(new HtmlMixedText(tr("The development was done by <0>."), authorLink)));

        final PageIterable<Bug> openBugs = feature.getOpenBugs();

        if (openBugs.size() > 0) {
            element.add(new HtmlParagraph(trn("There is {0} open bug.", "There is {0} open bug.", openBugs.size(), openBugs.size())));
        } else {
            element.add(new HtmlParagraph(tr("There is no open bug.")));
        }

        return element;
    }
}

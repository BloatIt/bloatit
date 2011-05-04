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
package com.bloatit.web.linkable.contribution;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import java.math.BigDecimal;

import javax.mail.IllegalWriteException;

import com.bloatit.framework.exceptions.highlevel.ShallNotPassException;
import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.exceptions.lowlevel.UnauthorizedOperationException;
import com.bloatit.framework.webprocessor.annotations.Optional;
import com.bloatit.framework.webprocessor.annotations.ParamConstraint;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlLink;
import com.bloatit.framework.webprocessor.components.HtmlParagraph;
import com.bloatit.framework.webprocessor.components.HtmlTitle;
import com.bloatit.framework.webprocessor.components.HtmlTitleBlock;
import com.bloatit.framework.webprocessor.components.advanced.HtmlClearer;
import com.bloatit.framework.webprocessor.components.advanced.HtmlTable;
import com.bloatit.framework.webprocessor.components.advanced.HtmlTable.HtmlLineTableModel;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.Actor;
import com.bloatit.model.Feature;
import com.bloatit.model.Member;
import com.bloatit.web.components.SideBarFeatureBlock;
import com.bloatit.web.linkable.features.FeaturePage;
import com.bloatit.web.linkable.features.FeaturesTools;
import com.bloatit.web.linkable.members.MembersTools;
import com.bloatit.web.linkable.softwares.SoftwaresTools;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.pages.master.HtmlDefineParagraph;
import com.bloatit.web.pages.master.sidebar.TwoColumnLayout;
import com.bloatit.web.url.CheckContributionPageUrl;
import com.bloatit.web.url.ContributePageUrl;
import com.bloatit.web.url.ContributionActionUrl;
import com.bloatit.web.url.StaticCheckContributionPageUrl;

/**
 * A page that hosts the form used to check the contribution on a Feature
 */
@ParamContainer("contribute/check")
public final class CheckContributionPage extends QuotationPage {

    @RequestParam(conversionErrorMsg = @tr("The process is closed, expired, missing or invalid."))
    @ParamConstraint(optionalErrorMsg = @tr("The process is closed, expired, missing or invalid."))
    private final ContributionProcess process;

    @Optional
    @RequestParam(conversionErrorMsg = @tr("The amount to load on your account must be a positive integer."))
    @ParamConstraint(min = "0", minErrorMsg = @tr("You must specify a positive value."), //
    max = "100000", maxErrorMsg = @tr("We cannot accept such a generous offer."),//
    precision = 0, precisionErrorMsg = @tr("Please do not use cents."))
    private BigDecimal preload;

    private final CheckContributionPageUrl url;

    public CheckContributionPage(final CheckContributionPageUrl url) {
        super(url);
        this.url = url;
        preload = url.getPreload();
        process = url.getProcess();
    }

    @Override
    public HtmlElement createBodyContentOnParameterError() throws RedirectException {
        if (url.getMessages().hasMessage()) {
            if (url.getProcessParameter().getMessages().isEmpty()) {
                if (!url.getPreloadParameter().getMessages().isEmpty()) {
                    preload = process.getAmountToCharge() != null ? process.getAmountToCharge() : BigDecimal.ZERO;
                }
            } else {
                throw new RedirectException(Context.getSession().pickPreferredPage());
            }
        }
        return createBodyContent();
    }

    @Override
    public HtmlElement createRestrictedContent(final Member loggedUser) throws RedirectException {
        final TwoColumnLayout layout = new TwoColumnLayout(true, url);
        layout.addLeft(generateCheckContributeForm(loggedUser));
        layout.addRight(new SideBarFeatureBlock(process.getFeature(), process.getAmount(), loggedUser));
        return layout;
    }

    private HtmlElement generateCheckContributeForm(final Member member) {
        final HtmlTitleBlock group = new HtmlTitleBlock(tr("Check contribution"), 1);

        final Feature feature = process.getFeature();

        BigDecimal account;
        try {
            account = getActor(member).getInternalAccount().getAmount();
            if (process.getAmount().compareTo(account) <= 0) {
                generateWithMoneyContent(group, feature, getActor(member));
            } else {
                generateNoMoneyContent(group, getActor(member), account);
            }
        } catch (final UnauthorizedOperationException e) {
            getSession().notifyError(Context.tr("An error prevented us from displaying getting your account balance. Please notify us."));
            throw new ShallNotPassException("User cannot access user's account balance", e);
        }

        return group;
    }

    private void generateWithMoneyContent(final HtmlTitleBlock group, final Feature feature, final Actor<?> actor) {
        final HtmlDiv contributionSummaryDiv = new HtmlDiv("contribution_summary");
        {
            contributionSummaryDiv.add(generateFeatureSummary(feature));

            final HtmlDiv authorContributionSummary = new HtmlDiv("author_contribution_summary");
            {

                if (process.getTeam() != null) {
                    authorContributionSummary.add(new HtmlTitle(tr("Account of {0}", actor.getDisplayName()), 2));
                } else {
                    authorContributionSummary.add(new HtmlTitle(tr("Your account"), 2));
                }

                try {
                    final HtmlDiv changeLine = new HtmlDiv("change_line");
                    {

                        changeLine.add(new MoneyVariationBlock(actor.getInternalAccount().getAmount(), actor.getInternalAccount()
                                                                                                            .getAmount()
                                                                                                            .subtract(process.getAmount())));
                        changeLine.add(MembersTools.getMemberAvatar(actor));
                        authorContributionSummary.add(changeLine);
                        authorContributionSummary.add(new HtmlDefineParagraph(tr("Author: "), actor.getDisplayName()));
                    }
                } catch (final UnauthorizedOperationException e) {
                    getSession().notifyError(Context.tr("An error prevented us from accessing user's info. Please notify us."));
                    throw new ShallNotPassException("User cannot access user information", e);
                }

                if (process.getComment() != null) {
                    authorContributionSummary.add(new HtmlDefineParagraph(tr("Comment: "), process.getComment()));
                } else {
                    authorContributionSummary.add(new HtmlDefineParagraph(tr("Comment: "), tr("No comment")));
                }

            }
            contributionSummaryDiv.add(authorContributionSummary);

        }
        group.add(contributionSummaryDiv);

        final HtmlDiv buttonDiv = new HtmlDiv("contribution_actions");
        {
            final ContributionActionUrl contributionActionUrl = new ContributionActionUrl(process);
            final HtmlLink confirmContributionLink = contributionActionUrl.getHtmlLink(tr("Contribute {0}",
                                                                                          Context.getLocalizator()
                                                                                                 .getCurrency(process.getAmount())
                                                                                                 .getSimpleEuroString()));
            confirmContributionLink.setCssClass("button");

            if (process.getTeam() != null) {
                buttonDiv.add(new HtmlParagraph(Context.tr("You are using the account of ''{0}'' team.", process.getTeam().getLogin()), "use_account"));
            }

            buttonDiv.add(confirmContributionLink);

            // Modify contribution button
            final ContributePageUrl contributePageUrl = new ContributePageUrl(process);
            final HtmlLink modifyContributionLink = contributePageUrl.getHtmlLink(tr("or modify contribution"));

            buttonDiv.add(modifyContributionLink);

        }
        group.add(buttonDiv);
    }

    private Actor<?> getActor(final Member member) {
        if (process.getTeam() != null) {
            return process.getTeam();
        }
        return member;
    }

    private void generateNoMoneyContent(final HtmlTitleBlock group, final Actor<?> actor, final BigDecimal account) {
        if (process.isLocked()) {
            getSession().notifyBad(tr("You have a payment in progress. The contribution is locked."));
        }
        try {
            if (!process.getAmountToCharge().equals(preload) && preload != null) {
                process.setAmountToCharge(preload);
            }
        } catch (final IllegalWriteException e) {
            getSession().notifyBad(tr("The preload amount is locked during the payment process."));
        }

        // Total
        final BigDecimal missingAmount = process.getAmount().subtract(account).add(process.getAmountToCharge());
        final StandardQuotation quotation = new StandardQuotation(missingAmount);

        try {
            if (!process.getAmountToPay().equals(quotation.subTotalTTCEntry.getValue())) {
                process.setAmountToPay(quotation.subTotalTTCEntry.getValue());
            }
        } catch (final IllegalWriteException e) {
            getSession().notifyBad(tr("The contribution's total amount is locked during the payment process."));
        }

        HtmlLineTableModel model = new HtmlLineTableModel();

        try {
            final ContributePageUrl contributePageUrl = new ContributePageUrl(process);
            model.addLine(new HtmlContributionLine(process.getFeature(), process.getAmount(), contributePageUrl));

            if (actor.getInternalAccount().getAmount().compareTo(BigDecimal.ZERO) > 0) {
                model.addLine(new HtmlPrepaidLine(actor));
            }

            final CheckContributionPageUrl recalculateUrl = url.clone();
            recalculateUrl.setPreload(null);
            model.addLine(new HtmlChargeAccountLine(true, process.getAmountToCharge(), actor, recalculateUrl));
        } catch (final UnauthorizedOperationException e) {
            getSession().notifyError(Context.tr("An error prevented us from accessing user's info. Please notify us."));
            throw new ShallNotPassException("User cannot access user information", e);
        }

        // Pay block
        final HtmlDiv payBlock = new HtmlDiv("pay_actions");
        {
            final HtmlLink payContributionLink = new StaticCheckContributionPageUrl(process).getHtmlLink(tr("Validate"));
            payContributionLink.setCssClass("button");
            if (process.getTeam() != null) {
                payBlock.add(new HtmlParagraph(Context.tr("You are using the account of ''{0}'' team.", process.getTeam().getLogin()), "use_account"));
            }
            payBlock.add(payContributionLink);
        }


        final HtmlTable lines = new HtmlTable(model);
        lines.setCssClass("quotation_details_lines");
        group.add(lines);

        final HtmlDiv summary = new HtmlDiv("quotation_totals_lines_block");
        summary.add(new HtmlTotalSummary(quotation, hasToShowFeeDetails(), url));
        summary.add(new HtmlClearer());
        summary.add(payBlock);
        group.add(summary);

    }

    private HtmlDiv generateFeatureSummary(final Feature feature) {
        final HtmlDiv featureContributionSummary = new HtmlDiv("feature_contribution_summary");
        {
            featureContributionSummary.add(new HtmlTitle(tr("The feature"), 2));

            final HtmlDiv changeLine = new HtmlDiv("change_line");
            {

                changeLine.add(SoftwaresTools.getSoftwareLogo(feature.getSoftware()));
                changeLine.add(new MoneyVariationBlock(feature.getContribution(), feature.getContribution().add(process.getAmount())));
            }
            featureContributionSummary.add(changeLine);
            featureContributionSummary.add(new HtmlDefineParagraph(tr("Target feature: "), FeaturesTools.getTitle(feature)));
        }
        return featureContributionSummary;
    }

    @Override
    protected String createPageTitle() {
        return tr("Contribute to a feature - check");
    }

    @Override
    public boolean isStable() {
        return false;
    }

    @Override
    public String getRefusalReason() {
        return tr("You must be logged to contribute");
    }

    @Override
    protected Breadcrumb createBreadcrumb() {
        return CheckContributionPage.generateBreadcrumb(process.getFeature(), process);
    }

    private static Breadcrumb generateBreadcrumb(final Feature feature, final ContributionProcess process) {
        final Breadcrumb breadcrumb = FeaturePage.generateBreadcrumbContributions(feature);
        final CheckContributionPageUrl checkContributionPageUrl = new CheckContributionPageUrl(process);
        breadcrumb.pushLink(checkContributionPageUrl.getHtmlLink(tr("Contribute - Check")));
        return breadcrumb;
    }
}

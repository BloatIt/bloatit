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

import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;
import com.bloatit.framework.exceptions.highlevel.ShallNotPassException;
import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.webprocessor.annotations.MaxConstraint;
import com.bloatit.framework.webprocessor.annotations.MinConstraint;
import com.bloatit.framework.webprocessor.annotations.NonOptional;
import com.bloatit.framework.webprocessor.annotations.Optional;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.PrecisionConstraint;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
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
import com.bloatit.model.ElveosUserToken;
import com.bloatit.model.Feature;
import com.bloatit.model.Member;
import com.bloatit.model.right.AuthenticatedUserToken;
import com.bloatit.model.right.UnauthorizedOperationException;
import com.bloatit.model.right.UnauthorizedPrivateAccessException;
import com.bloatit.web.components.SideBarFeatureBlock;
import com.bloatit.web.linkable.features.FeaturePage;
import com.bloatit.web.linkable.features.FeaturesTools;
import com.bloatit.web.linkable.invoice.ContactBox;
import com.bloatit.web.linkable.members.MembersTools;
import com.bloatit.web.linkable.softwares.SoftwaresTools;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.pages.master.HtmlDefineParagraph;
import com.bloatit.web.pages.master.sidebar.TwoColumnLayout;
import com.bloatit.web.url.CheckContributePageUrl;
import com.bloatit.web.url.ContributePageUrl;
import com.bloatit.web.url.ContributeActionUrl;
import com.bloatit.web.url.ModifyInvoicingContactProcessUrl;
import com.bloatit.web.url.StaticCheckContributionPageUrl;

/**
 * A page that hosts the form used to check the contribution on a Feature
 */
@ParamContainer("contribute/%process%/check")
public final class CheckContributePage extends QuotationPage {

    @RequestParam(message = @tr("The process is closed, expired, missing or invalid."), role = Role.PAGENAME)
    @NonOptional(@tr("The process is closed, expired, missing or invalid."))
    private final ContributionProcess process;

    @Optional
    @RequestParam(message = @tr("The amount to load on your account must be a positive integer."))
    @MinConstraint(min = 0, message = @tr("You must specify a positive value."))
    @MaxConstraint(max = 100000, message = @tr("We cannot accept such a generous offer."))
    @PrecisionConstraint(precision = 0, message = @tr("Please do not use cents."))
    private BigDecimal preload;

    private final CheckContributePageUrl url;

    public CheckContributePage(final CheckContributePageUrl url) {
        super(url);
        this.url = url;
        preload = url.getPreload();
        process = url.getProcess();
    }

    @Override
    public HtmlElement createBodyContentOnParameterError(final ElveosUserToken userToken) throws RedirectException {
        if (url.getMessages().hasMessage()) {
            if (url.getProcessParameter().getMessages().isEmpty()) {
                if (!url.getPreloadParameter().getMessages().isEmpty()) {
                    preload = process.getAmountToCharge() != null ? process.getAmountToCharge() : BigDecimal.ZERO;
                }
            } else {
                throw new RedirectException(Context.getSession().pickPreferredPage());
            }
        }
        return createBodyContent(userToken);
    }

    @Override
    public HtmlElement createRestrictedContent(final Member loggedUser) throws RedirectException {
        final TwoColumnLayout layout = new TwoColumnLayout(true, url);
        layout.addLeft(generateCheckContributeForm(loggedUser));
        layout.addRight(new SideBarFeatureBlock(process.getFeature(), process.getAmount(), new AuthenticatedUserToken(loggedUser)));
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

                try {
                    authorContributionSummary.add(new HtmlDefineParagraph(tr("Invoice at {0}: ", actor.getContact().getCity()),
                                                                          new ModifyInvoicingContactProcessUrl(actor, process).getHtmlLink(Context.tr("modify invoicing contact"))));
                } catch (final UnauthorizedPrivateAccessException e) {
                    throw new ShallNotPassException("User cannot access user contact information", e);
                }

            }
            contributionSummaryDiv.add(authorContributionSummary);

        }
        group.add(contributionSummaryDiv);

        final HtmlDiv buttonDiv = new HtmlDiv("contribution_actions");
        {
            final ContributeActionUrl contributionActionUrl = new ContributeActionUrl(process);
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

        group.add(ContactBox.generate(actor, process));

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

        final HtmlLineTableModel model = new HtmlLineTableModel();

        HtmlChargeAccountLine line;
        try {
            final ContributePageUrl contributePageUrl = new ContributePageUrl(process);
            model.addLine(new HtmlContributionLine(process.getFeature(), process.getAmount(), contributePageUrl));

            if (actor.getInternalAccount().getAmount().compareTo(BigDecimal.ZERO) > 0) {
                model.addLine(new HtmlPrepaidLine(actor));
            }

            final CheckContributePageUrl recalculateUrl = url.clone();
            recalculateUrl.setPreload(null);
            line = new HtmlChargeAccountLine(true, process.getAmountToCharge(), actor, recalculateUrl);
            model.addLine(line);
        } catch (final UnauthorizedOperationException e) {
            getSession().notifyError(Context.tr("An error prevented us from accessing user's info. Please notify us."));
            throw new ShallNotPassException("User cannot access user information", e);
        }

        // Pay block
        final HtmlDiv payBlock = new HtmlDiv("pay_actions");
        {
            final HtmlLink invoicingContactLink;

            try {
                if (!actor.hasInvoicingContact()) {
                    invoicingContactLink = new ModifyInvoicingContactProcessUrl(actor, process).getHtmlLink(tr("Fill invoicing contact"));
                } else {
                    invoicingContactLink = new StaticCheckContributionPageUrl(process).getHtmlLink(tr("Validate"));
                }
            } catch (final UnauthorizedPrivateAccessException e) {
                throw new BadProgrammerException("fail ton check the existence of invoicing contact", e);
            }

            invoicingContactLink.setCssClass("button");
            if (process.getTeam() != null) {
                payBlock.add(new HtmlParagraph(Context.tr("You are using the account of ''{0}'' team.", process.getTeam().getLogin()), "use_account"));
            }
            payBlock.add(invoicingContactLink);
        }

        final HtmlTable lines = new HtmlTable(model);
        lines.setCssClass("quotation_details_lines");
        group.add(lines);

        final HtmlDiv summary = new HtmlDiv("quotation_totals_lines_block");
        summary.add(new HtmlTotalSummary(quotation, hasToShowFeeDetails(), url, process.getAmount().subtract(account), line.getMoneyField(), summary));
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

                changeLine.add(new SoftwaresTools.Logo(feature.getSoftware()));
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
    protected Breadcrumb createBreadcrumb(final Member member) {
        return CheckContributePage.generateBreadcrumb(process.getFeature(), process);
    }

    public static Breadcrumb generateBreadcrumb(final Feature feature, final ContributionProcess process) {
        final Breadcrumb breadcrumb = FeaturePage.generateBreadcrumbContributions(feature);
        final CheckContributePageUrl checkContributionPageUrl = new CheckContributePageUrl(process);
        breadcrumb.pushLink(checkContributionPageUrl.getHtmlLink(tr("Contribute - Check")));
        return breadcrumb;
    }
}
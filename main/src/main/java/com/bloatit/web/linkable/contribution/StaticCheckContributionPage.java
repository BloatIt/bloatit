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
import com.bloatit.framework.webprocessor.annotations.NonOptional;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlTitleBlock;
import com.bloatit.framework.webprocessor.components.advanced.HtmlClearer;
import com.bloatit.framework.webprocessor.components.advanced.HtmlTable;
import com.bloatit.framework.webprocessor.components.advanced.HtmlTable.HtmlLineTableModel;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.Actor;
import com.bloatit.model.InternalAccount;
import com.bloatit.model.Member;
import com.bloatit.model.right.UnauthorizedOperationException;
import com.bloatit.web.components.SideBarFeatureBlock;
import com.bloatit.web.linkable.features.FeaturePage;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.pages.master.sidebar.TwoColumnLayout;
import com.bloatit.web.url.CheckContributeActionUrl;
import com.bloatit.web.url.CheckContributePageUrl;
import com.bloatit.web.url.PaylineProcessUrl;
import com.bloatit.web.url.StaticCheckContributionPageUrl;
import com.bloatit.web.url.UnlockContributionProcessActionUrl;

/**
 * A page that hosts the form used to check the contribution on a Feature
 */
@ParamContainer("contribute/staticcheck")
public final class StaticCheckContributionPage extends QuotationPage {

    @RequestParam(message = @tr("The process is closed, expired, missing or invalid."))
    @NonOptional(@tr("The process is closed, expired, missing or invalid."))
    private final ContributionProcess process;

    private final StaticCheckContributionPageUrl url;

    public StaticCheckContributionPage(final StaticCheckContributionPageUrl url) {
        super(url);
        this.url = url;
        process = url.getProcess();
    }

    @Override
    public HtmlElement createRestrictedContent(final Member loggedUser) throws RedirectException {
        if (process != null) {
            process.setLock(true);
        }
        final TwoColumnLayout layout = new TwoColumnLayout(true, url);
        layout.addLeft(generateCheckContributeForm(loggedUser));
        layout.addRight(new SideBarFeatureBlock(process.getFeature(), process.getAmount()));
        return layout;
    }

    private HtmlElement generateCheckContributeForm(final Member member) throws RedirectException {
        final HtmlTitleBlock group = new HtmlTitleBlock(tr("Final check"), 1);
        BigDecimal account;
        try {
            account = getAccount(member).getAmount();
            if (process.getAmount().compareTo(account) <= 0) {
                throw new RedirectException(new CheckContributePageUrl(process));
            }
            generateNoMoneyContent(group, getActor(member), account);
        } catch (final UnauthorizedOperationException e) {
            getSession().notifyError(Context.tr("An error prevented us from displaying getting your account balance. Please notify us."));
            throw new ShallNotPassException("User cannot access user's account balance", e);
        }
        return group;
    }

    private InternalAccount getAccount(final Member member) throws UnauthorizedOperationException {
        return getActor(member).getInternalAccount();
    }

    private Actor<?> getActor(final Member member) {
        if (process.getTeam() != null) {
            return process.getTeam();
        }
        return member;
    }

    private void generateNoMoneyContent(final HtmlTitleBlock group, final Actor<?> actor, final BigDecimal account) {
        // Total
        final BigDecimal missingAmount = process.getAmount().subtract(account).add(process.getAmountToCharge());
        final StandardQuotation quotation = new StandardQuotation(missingAmount);
        try {
            if (!process.getAmountToPay().equals(quotation.subTotalTTCEntry.getValue())) {
                process.setAmountToPay(quotation.subTotalTTCEntry.getValue());
            }
        } catch (final IllegalWriteException e) {
            getSession().notifyWarning(tr("The contribution's total amount is locked during the payment process."));
        }
        final HtmlLineTableModel model = new HtmlLineTableModel();
        try {
            model.addLine(new HtmlContributionLine(process.getFeature(), process.getAmount(), null));
            if (actor.getInternalAccount().getAmount().compareTo(BigDecimal.ZERO) > 0) {
                model.addLine(new HtmlPrepaidLine(actor));
            }
            if (process.getAmountToCharge().compareTo(BigDecimal.ZERO) > 0) {
                model.addLine(new HtmlChargeAccountLine(true, process.getAmountToCharge(), actor, null));
            }
            
        } catch (final UnauthorizedOperationException e) {
            getSession().notifyError(Context.tr("An error prevented us from accessing user's info. Please notify us."));
            throw new ShallNotPassException("User cannot access user information", e);
        }

        final HtmlTable lines = new HtmlTable(model);
        lines.setCssClass("quotation_details_lines");
        group.add(lines);

        final HtmlDiv summary = new HtmlDiv("quotation_totals_lines_block");
        summary.add(new HtmlTotalSummary(quotation, hasToShowFeeDetails(), url));
        summary.add(new HtmlClearer());
        summary.add(new HtmlPayBlock(quotation,
                                     process.getTeam(),
                                     new PaylineProcessUrl(actor, process),
                                     new UnlockContributionProcessActionUrl(getSession().getShortKey(), process)));
        group.add(summary);
    }

    @Override
    protected String createPageTitle() {
        return tr("Contribute to a feature - final check");
    }

    @Override
    public boolean isStable() {
        return false;
    }

    @Override
    public String getRefusalReason() {
        return tr("You must be logged to contribute");
    }

    private Url createUnlockedReturnUrl() {
        return new UnlockContributionProcessActionUrl(getSession().getShortKey(), process);
    }

    @Override
    protected Breadcrumb createBreadcrumb(final Member member) {
        final Breadcrumb breadcrumb = FeaturePage.generateBreadcrumbContributions(process.getFeature());
        final CheckContributeActionUrl returnUrl = new CheckContributeActionUrl(getSession().getShortKey(), process);
        returnUrl.setAmount(process.getAmount());
        breadcrumb.pushLink(createUnlockedReturnUrl().getHtmlLink(tr("Contribute - Check")));
        breadcrumb.pushLink(new CheckContributePageUrl(process).getHtmlLink(tr("Final check")));
        return breadcrumb;
    }

}

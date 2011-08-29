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
package com.bloatit.web.linkable.money;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import java.math.BigDecimal;

import javax.mail.IllegalWriteException;

import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;
import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.webprocessor.annotations.MaxConstraint;
import com.bloatit.framework.webprocessor.annotations.MinConstraint;
import com.bloatit.framework.webprocessor.annotations.NonOptional;
import com.bloatit.framework.webprocessor.annotations.Optional;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.ParamContainer.Protocol;
import com.bloatit.framework.webprocessor.annotations.PrecisionConstraint;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlLink;
import com.bloatit.framework.webprocessor.components.HtmlParagraph;
import com.bloatit.framework.webprocessor.components.HtmlTitleBlock;
import com.bloatit.framework.webprocessor.components.advanced.HtmlClearer;
import com.bloatit.framework.webprocessor.components.advanced.HtmlTable;
import com.bloatit.framework.webprocessor.components.advanced.HtmlTable.HtmlLineTableModel;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.Actor;
import com.bloatit.model.Member;
import com.bloatit.model.Team;
import com.bloatit.model.right.UnauthorizedPrivateAccessException;
import com.bloatit.web.WebConfiguration;
import com.bloatit.web.linkable.contribution.HtmlChargeAccountLine;
import com.bloatit.web.linkable.contribution.HtmlTotalSummary;
import com.bloatit.web.linkable.contribution.QuotationPage;
import com.bloatit.web.linkable.contribution.StandardQuotation;
import com.bloatit.web.linkable.documentation.SideBarDocumentationBlock;
import com.bloatit.web.linkable.invoice.ContactBox;
import com.bloatit.web.linkable.members.MemberPage;
import com.bloatit.web.linkable.team.TeamPage;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.pages.master.sidebar.TwoColumnLayout;
import com.bloatit.web.url.AccountChargingPageUrl;
import com.bloatit.web.url.ModifyInvoicingContactProcessUrl;
import com.bloatit.web.url.StaticAccountChargingPageUrl;

/**
 * A page used to put money onto the internal bloatit account
 */
@ParamContainer(value = "account/charging/process/%process%", protocol = Protocol.HTTPS)
public final class AccountChargingPage extends QuotationPage {

    @RequestParam(role=Role.PAGENAME,  message = @tr("The process is closed, expired, missing or invalid."))
    @NonOptional(@tr("The process is closed, expired, missing or invalid."))
    private final AccountChargingProcess process;

    @Optional
    @RequestParam(message = @tr("The amount to load on your account must be a positive integer."))
    @MaxConstraint(max = 100000, message = @tr("We cannot accept such a generous offer."))
    @MinConstraint(min = 1, message = @tr("You must specify a positive value."))
    @PrecisionConstraint(precision = 0, message = @tr("Please do not use cents."))
    private BigDecimal preload;

    private final AccountChargingPageUrl url;

    public AccountChargingPage(final AccountChargingPageUrl url) {
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
                    preload = process.getAccountChargingAmount() != null ? process.getAccountChargingAmount() : BigDecimal.ZERO;
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
        layout.addRight(new SideBarDocumentationBlock("account_charging"));
        return layout;
    }

    private HtmlElement generateCheckContributeForm(final Member member) {
        final HtmlTitleBlock group;
        if (process.getTeam() != null) {
            group = new HtmlTitleBlock(tr("Charge the {0} account", process.getTeam().getDisplayName()), 1);
        } else {
            group = new HtmlTitleBlock(tr("Charge your account"), 1);
        }
        generateNoMoneyContent(group, getActor(member));
        return group;
    }

    private Actor<?> getActor(final Member member) {
        if (process.getTeam() != null) {
            return process.getTeam();
        }
        return member;
    }

    private void generateNoMoneyContent(final HtmlTitleBlock group, final Actor<?> actor) {
        if (process.isLocked()) {
            getSession().notifyWarning(tr("You have a payment in progress, you cannot change the amount."));
        }
        try {
            if (!process.getAccountChargingAmount().equals(preload) && preload != null) {
                process.setAmountToCharge(preload);
                process.setAmountToPayBeforeComission(preload);
            }
            if (process.getAccountChargingAmount().equals(BigDecimal.ZERO)) {

                process.setAmountToCharge(WebConfiguration.getDefaultChargingAmount());
                process.setAmountToPayBeforeComission(WebConfiguration.getDefaultChargingAmount());
            }
        } catch (final IllegalWriteException e) {
            getSession().notifyWarning(tr("You have a payment in progress, you cannot change the amount."));
        }

        group.add(ContactBox.generate(actor, process));

        // Total
        final StandardQuotation quotation = new StandardQuotation(process.getAmountToPayBeforeComission());

        final HtmlLineTableModel model = new HtmlLineTableModel();

        final AccountChargingPageUrl recalculateUrl = url.clone();
        recalculateUrl.setPreload(null);
        final HtmlChargeAccountLine line = new HtmlChargeAccountLine(false, process.getAccountChargingAmount(), actor, recalculateUrl);

        model.addLine(line);

        // Pay block
        final HtmlDiv payBlock = new HtmlDiv("pay_actions");
        {
            final HtmlLink invoicingContactLink;

            try {
                if (!actor.hasInvoicingContact()) {
                    invoicingContactLink = new ModifyInvoicingContactProcessUrl(actor, process).getHtmlLink(tr("Fill invoicing contact"));
                } else {
                    invoicingContactLink = new StaticAccountChargingPageUrl(process).getHtmlLink(tr("Validate"));
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
        summary.add(new HtmlTotalSummary(quotation, hasToShowFeeDetails(), url, BigDecimal.ZERO, line.getMoneyField(), summary));
        summary.add(new HtmlClearer());
        summary.add(payBlock);
        group.add(summary);

    }

    @Override
    protected String createPageTitle() {
        return tr("Charge your account");
    }

    @Override
    public boolean isStable() {
        return false;
    }

    @Override
    public String getRefusalReason() {
        return tr("You must be logged to charge your account");
    }

    @Override
    protected Breadcrumb createBreadcrumb(final Member member) {
        return generateBreadcrumb(member, process.getTeam(), process);
    }

    public static Breadcrumb generateBreadcrumb(final Member member, final Team asTeam, final AccountChargingProcess process) {
        final Breadcrumb breadcrumb;
        if (asTeam != null) {
            breadcrumb = TeamPage.generateAccountBreadcrumb(asTeam);
        } else {
            breadcrumb = MemberPage.generateAccountBreadcrumb(member);
        }
        final AccountChargingPageUrl url = new AccountChargingPageUrl(process);
        breadcrumb.pushLink(url.getHtmlLink(tr("Charging")));
        return breadcrumb;
    }
}

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

import static com.bloatit.framework.webprocessor.Context.tr;

import java.math.BigDecimal;

import javax.mail.IllegalWriteException;

import com.bloatit.framework.exceptions.highlevel.ShallNotPassException;
import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.exceptions.lowlevel.UnauthorizedOperationException;
import com.bloatit.framework.utils.Image;
import com.bloatit.framework.webprocessor.Context;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlImage;
import com.bloatit.framework.webprocessor.components.HtmlLink;
import com.bloatit.framework.webprocessor.components.HtmlSpan;
import com.bloatit.framework.webprocessor.components.HtmlTitle;
import com.bloatit.framework.webprocessor.components.HtmlTitleBlock;
import com.bloatit.framework.webprocessor.components.form.HtmlForm;
import com.bloatit.framework.webprocessor.components.form.HtmlMoneyField;
import com.bloatit.framework.webprocessor.components.form.HtmlSubmit;
import com.bloatit.framework.webprocessor.components.form.HtmlForm.Method;
import com.bloatit.framework.webprocessor.components.javascript.JsShowHide;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.components.meta.HtmlMixedText;
import com.bloatit.framework.webserver.annotations.Optional;
import com.bloatit.framework.webserver.annotations.ParamConstraint;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.annotations.RequestParam;
import com.bloatit.framework.webserver.annotations.tr;
import com.bloatit.model.Feature;
import com.bloatit.model.Member;
import com.bloatit.model.Payline;
import com.bloatit.web.WebConfiguration;
import com.bloatit.web.components.SideBarFeatureBlock;
import com.bloatit.web.linkable.features.FeaturePage;
import com.bloatit.web.linkable.features.FeaturesTools;
import com.bloatit.web.linkable.members.MembersTools;
import com.bloatit.web.linkable.meta.bugreport.SideBarBugReportBlock;
import com.bloatit.web.linkable.money.Quotation;
import com.bloatit.web.linkable.money.Quotation.QuotationAmountEntry;
import com.bloatit.web.linkable.money.Quotation.QuotationDifferenceEntry;
import com.bloatit.web.linkable.money.Quotation.QuotationPercentEntry;
import com.bloatit.web.linkable.money.Quotation.QuotationTotalEntry;
import com.bloatit.web.linkable.money.QuotationEntry;
import com.bloatit.web.linkable.softwares.SoftwaresTools;
import com.bloatit.web.pages.LoggedPage;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.pages.master.DefineParagraph;
import com.bloatit.web.pages.master.sidebar.TwoColumnLayout;
import com.bloatit.web.url.CheckContributionPageUrl;
import com.bloatit.web.url.ContributePageUrl;
import com.bloatit.web.url.ContributionActionUrl;
import com.bloatit.web.url.PaylineProcessUrl;

/**
 * A page that hosts the form used to check the contribution on a Feature
 */
@ParamContainer("contribute/check")
public final class CheckContributionPage extends LoggedPage {

    @RequestParam
    @ParamConstraint(optionalErrorMsg = @tr("The process is closed, expired, missing or invalid."))
    private final ContributionProcess process;

    private final CheckContributionPageUrl url;

    @Optional("false")
    @RequestParam(name = "show_fees_detail")
    private final Boolean showFeesDetails;

    @Optional("0")
    @RequestParam
    private final BigDecimal preload;

    public CheckContributionPage(final CheckContributionPageUrl url) {
        super(url);
        this.url = url;
        process = url.getProcess();
        showFeesDetails = url.getShowFeesDetails();
        preload = url.getPreload();
    }

    @Override
    public void processErrors() throws RedirectException {
        addNotifications(url.getMessages());
        if (url.getMessages().hasMessage()) {
            session.notifyList(url.getMessages());
            throw new RedirectException(Context.getSession().pickPreferredPage());
        }

    }

    @Override
    public HtmlElement createRestrictedContent() throws RedirectException {

        final TwoColumnLayout layout = new TwoColumnLayout(true, url);
        layout.addLeft(generateCheckContributeForm());

        layout.addRight(new SideBarFeatureBlock(process.getFeature(),process.getAmount()));
        

        return layout;
    }

    public HtmlElement generateCheckContributeForm() throws RedirectException {
        final HtmlTitleBlock group = new HtmlTitleBlock("Check contribution", 1);

        final Feature feature = process.getFeature();
        final Member member = session.getAuthToken().getMember();

        BigDecimal account;
        try {
            account = member.getInternalAccount().getAmount();
        } catch (final UnauthorizedOperationException e) {
            session.notifyError(Context.tr("An error prevented us from displaying getting your account balance. Please notify us."));
            throw new ShallNotPassException("User cannot access user's account balance", e);
        }

        if (process.getAmount().compareTo(account) <= 0) {
            generateWithMoneyContent(group, feature, member);
        } else {
            generateNoMoneyContent(group, member, account);
        }

        return group;
    }

    public void generateWithMoneyContent(final HtmlTitleBlock group, final Feature feature, final Member member) {
        final HtmlDiv contributionSummaryDiv = new HtmlDiv("contribution_summary");
        {
            contributionSummaryDiv.add(generateFeatureSummary(feature));

            final HtmlDiv authorContributionSummary = new HtmlDiv("author_contribution_summary");
            {

                authorContributionSummary.add(new HtmlTitle(tr("Your account"), 2));

                try {
                    final HtmlDiv changeLine = new HtmlDiv("change_line");
                    {

                        changeLine.add(new MoneyVariationBlock(member.getInternalAccount().getAmount(), member.getInternalAccount()
                                                                                                              .getAmount()
                                                                                                              .subtract(process.getAmount())));
                        changeLine.add(MembersTools.getMemberAvatar(member));
                        authorContributionSummary.add(changeLine);
                        authorContributionSummary.add(new DefineParagraph(tr("Author: "), member.getDisplayName()));
                    }
                } catch (final UnauthorizedOperationException e) {
                    session.notifyError(Context.tr("An error prevented us from accessing user's info. Please notify us."));
                    throw new ShallNotPassException("User cannot access user information", e);
                }

                if (process.getComment() != null) {
                    authorContributionSummary.add(new DefineParagraph(tr("Comment: "), process.getComment()));
                } else {
                    authorContributionSummary.add(new DefineParagraph(tr("Comment: "), tr("No comment")));
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
                                                                                           .getDefaultString()));
            confirmContributionLink.setCssClass("button");

            buttonDiv.add(confirmContributionLink);

            // Modify contribution button
            final ContributePageUrl contributePageUrl = new ContributePageUrl(process);
            final HtmlLink modifyContributionLink = contributePageUrl.getHtmlLink(tr("or modify contribution"));

            buttonDiv.add(modifyContributionLink);

        }
        group.add(buttonDiv);
    }

    private void generateNoMoneyContent(final HtmlTitleBlock group, final Member member, final BigDecimal account) {

        if (process.isLocked()) {
            session.notifyBad(tr("You have a payment in progress. The contribution is locked."));
        }

        try {
            if (!process.getAmountToCharge().equals(preload) && preload != null) {
                process.setAmountToCharge(preload);
            }
        } catch (final IllegalWriteException e) {
            session.notifyBad(tr("The preload amount is locked during the payment process."));
        }

        // Total
        final BigDecimal missingAmount = process.getAmount().subtract(account).add(process.getAmountToCharge());
        final StandardQuotation quotation = new StandardQuotation(missingAmount);

        try {
            if (!process.getAmountToPay().equals(quotation.subTotalTTCEntry.getValue())) {
                process.setAmountToPay(quotation.subTotalTTCEntry.getValue());
            }
        } catch (final IllegalWriteException e) {
            session.notifyBad(tr("The contribution's total amount is locked during the payment process."));
        }
        final CheckContributionPageUrl recalculateUrl = url.clone();
        recalculateUrl.setPreload(null);

        final HtmlForm detailsLines = new HtmlForm(recalculateUrl.urlString(), Method.GET);

        detailsLines.setCssClass("quotation_details_lines");

        // HtmlDiv detailsLines = new HtmlDiv("quotation_details_lines");

        // Contribution

        try {
            detailsLines.add(new HtmlContributionLine(process));

            if (member.getInternalAccount().getAmount().compareTo(BigDecimal.ZERO) > 0) {
                detailsLines.add(new HtmlPrepaidLine(member));
            }

            detailsLines.add(new HtmlChargeAccountLine(member));
        } catch (final UnauthorizedOperationException e) {
            session.notifyError(Context.tr("An error prevented us from accessing user's info. Please notify us."));
            throw new ShallNotPassException("User cannot access user information", e);
        }

        final HtmlDiv totalsLines = new HtmlDiv("quotation_totals_lines");
        {

            final HtmlDiv subtotal = new HtmlDiv("quotation_total_line");
            {
                subtotal.add(new HtmlDiv("label").addText(tr("Subtotal TTC")));
                subtotal.add(new HtmlDiv("money").addText(Context.getLocalizator()
                                                                 .getCurrency(quotation.subTotalTTCEntry.getValue())
                                                                 .getDecimalDefaultString()));
            }
            totalsLines.add(subtotal);

            final CheckContributionPageUrl showDetailUrl = url.clone();
            showDetailUrl.setShowFeesDetails(!showFeesDetails);
            final HtmlLink showDetailLink = showDetailUrl.getHtmlLink(tr("fees details"));

            final HtmlDiv feesHT = new HtmlDiv("quotation_total_line_ht");
            {

                final HtmlSpan detailSpan = new HtmlSpan("details");
                detailSpan.add(showDetailLink);

                feesHT.add(new HtmlDiv("label").add(new HtmlMixedText(tr("Fees HT <0::>"), detailSpan)));
                feesHT.add(new HtmlDiv("money").addText(Context.getLocalizator().getCurrency(quotation.feesHT.getValue()).getDecimalDefaultString()));

            }
            totalsLines.add(feesHT);
            // Fee details
            final HtmlDiv feesDetail = new HtmlDiv("quotation_total_line_details_block");
            final HtmlDiv feesBank = new HtmlDiv("quotation_total_line_details");
            {
                feesBank.add(new HtmlDiv("label").addText(tr("Bank fees")));
                feesBank.add(new HtmlDiv("money").addText(Context.getLocalizator().getCurrency(quotation.bank.getValue()).getDecimalDefaultString()));
            }
            feesDetail.add(feesBank);

            final HtmlDiv elveosCommission = new HtmlDiv("quotation_total_line_details");
            {
                elveosCommission.add(new HtmlDiv("label").addText(tr("Elveos's commission")));
                elveosCommission.add(new HtmlDiv("money").addText(Context.getLocalizator()
                                                                         .getCurrency(quotation.commission.getValue())
                                                                         .getDecimalDefaultString()));

            }
            feesDetail.add(elveosCommission);
            totalsLines.add(feesDetail);

            // Add show/hide template
            final JsShowHide showHideFees = new JsShowHide(showFeesDetails);
            showHideFees.addActuator(showDetailLink);
            showHideFees.addListener(feesDetail);
            showHideFees.apply();

            final HtmlDiv feesTTC = new HtmlDiv("quotation_total_line");
            {

                final HtmlSpan detailSpan = new HtmlSpan("details");
                detailSpan.addText(tr("({0}% + {1})",
                                      Payline.COMMISSION_VARIABLE_RATE.multiply(new BigDecimal("100")),
                                      Context.getLocalizator().getCurrency(Payline.COMMISSION_FIX_RATE).getDecimalDefaultString()));

                feesTTC.add(new HtmlDiv("label").add(new HtmlMixedText(tr("Fees TTC <0::>"), detailSpan)));
                feesTTC.add(new HtmlDiv("money").addText(Context.getLocalizator().getCurrency(quotation.feesTTC.getValue()).getDecimalDefaultString()));
            }
            totalsLines.add(feesTTC);

            final HtmlDiv totalHT = new HtmlDiv("quotation_total_line_ht");
            {
                totalHT.add(new HtmlDiv("label").addText(tr("Total HT")));
                totalHT.add(new HtmlDiv("money").addText(Context.getLocalizator().getCurrency(quotation.totalHT.getValue()).getDecimalDefaultString()));
            }
            totalsLines.add(totalHT);

            final HtmlDiv totalTTC = new HtmlDiv("quotation_total_line_total");
            {
                totalTTC.add(new HtmlDiv("label").addText(tr("Total TTC")));
                totalTTC.add(new HtmlDiv("money").addText(Context.getLocalizator()
                                                                 .getCurrency(quotation.totalTTC.getValue())
                                                                 .getDecimalDefaultString()));
            }
            totalsLines.add(totalTTC);

        }

        // Pay block

        final HtmlDiv payBlock = new HtmlDiv("pay_actions");
        {

            // Pay later button
            // HtmlLink continueNavigation = new HtmlLink("", tr("Pay later"));
            // payBlock.add(continueNavigation);
            // TODO: real pay later button

            final PaylineProcessUrl paylineProcessUrl = new PaylineProcessUrl(process);

            final HtmlLink payContributionLink = paylineProcessUrl.getHtmlLink(tr("Pay {0}",
                                                                            Context.getLocalizator()
                                                                                   .getCurrency(quotation.totalTTC.getValue())
                                                                                   .getDecimalDefaultString()));
            payContributionLink.setCssClass("button");
            payBlock.add(payContributionLink);

        }

        group.add(detailsLines);
        group.add(new HtmlDiv("quotation_totals_lines_block").add(totalsLines).add(payBlock));

    }

    public static class HtmlContributionLine extends HtmlDiv {

        public HtmlContributionLine(final ContributionProcess contribution) throws UnauthorizedOperationException {
            super("quotation_detail_line");

            add(SoftwaresTools.getSoftwareLogoSmall(contribution.getFeature().getSoftware()));

            add(new HtmlDiv("quotation_detail_line_money").addText(Context.getLocalizator()
                                                                          .getCurrency(contribution.getFeature().getContribution())
                                                                          .getDefaultString()));
            add(new HtmlDiv().setCssClass("quotation_detail_line_money_image").add(new HtmlImage(new Image(WebConfiguration.getImgMoneyUpSmall()),
                                                                                                 "money up")));
            add(new HtmlDiv("quotation_detail_line_money").addText(Context.getLocalizator()
                                                                          .getCurrency(contribution.getFeature()
                                                                                                   .getContribution()
                                                                                                   .add(contribution.getAmount()))
                                                                          .getDefaultString()));

            add(new HtmlDiv("quotation_detail_line_categorie").addText(tr("Contribution")));
            add(new HtmlDiv("quotation_detail_line_description").addText(FeaturesTools.getTitle(contribution.getFeature())));

            final HtmlDiv amountBlock = new HtmlDiv("quotation_detail_line_amount");

            amountBlock.add(new HtmlDiv("quotation_detail_line_amount_money").addText(Context.getLocalizator()
                                                                                             .getCurrency(contribution.getAmount())
                                                                                             .getDecimalDefaultString()));

            // Modify contribution button
            final ContributePageUrl contributePageUrl = new ContributePageUrl(contribution);
            final HtmlLink modifyContributionLink = contributePageUrl.getHtmlLink(tr("modify"));
            final HtmlLink deleteContributionLink = contributePageUrl.getHtmlLink(tr("delete"));
            // TODO: real delete button
            amountBlock.add(new HtmlDiv("quotation_detail_line_amount_modify").add(modifyContributionLink).addText(" - ").add(deleteContributionLink));

            add(amountBlock);

        }
    }

    public static class HtmlPrepaidLine extends HtmlDiv {

        public HtmlPrepaidLine(final Member member) throws UnauthorizedOperationException {
            super("quotation_detail_line");

            add(MembersTools.getMemberAvatarSmall(member));

            add(new HtmlDiv("quotation_detail_line_money").addText(Context.getLocalizator()
                                                                          .getCurrency(member.getInternalAccount().getAmount())
                                                                          .getDefaultString()));
            add(new HtmlDiv().setCssClass("quotation_detail_line_money_image").add(new HtmlImage(new Image(WebConfiguration.getImgMoneyDownSmall()),
                                                                                                 "money up")));
            add(new HtmlDiv("quotation_detail_line_money").addText(Context.getLocalizator().getCurrency(BigDecimal.ZERO).getDefaultString()));

            add(new HtmlDiv("quotation_detail_line_categorie").addText(tr("Prepaid from internal account")));

            final HtmlDiv amountBlock = new HtmlDiv("quotation_detail_line_amount");

            amountBlock.add(new HtmlDiv("quotation_detail_line_amount_money").addText(Context.getLocalizator()
                                                                                             .getCurrency(member.getInternalAccount()
                                                                                                                .getAmount()
                                                                                                                .negate())
                                                                                             .getDecimalDefaultString()));

            add(amountBlock);

        }
    }

    public class HtmlChargeAccountLine extends HtmlDiv {

        @SuppressWarnings("synthetic-access")
        public HtmlChargeAccountLine(final Member member) throws UnauthorizedOperationException {
            super("quotation_detail_line");

            add(MembersTools.getMemberAvatarSmall(member));

            add(new HtmlDiv("quotation_detail_line_money").addText(Context.getLocalizator().getCurrency(BigDecimal.ZERO).getDefaultString()));
            add(new HtmlDiv().setCssClass("quotation_detail_line_money_image").add(new HtmlImage(new Image(WebConfiguration.getImgMoneyUpSmall()),
                                                                                                 "money up")));
            add(new HtmlDiv("quotation_detail_line_money").addText(Context.getLocalizator()
                                                                          .getCurrency(process.getAmountToCharge())
                                                                          .getDefaultString()));

            add(new HtmlDiv("quotation_detail_line_categorie").addText(tr("Internal account")));
            add(new HtmlDiv("quotation_detail_line_description").addText(tr("Load money in your internal account for future contributions.")));

            final HtmlDiv amountBlock = new HtmlDiv("quotation_detail_line_field");

            final HtmlMoneyField moneyField = new HtmlMoneyField("preload");
            moneyField.setDefaultValue(process.getAmountToCharge().toPlainString());

            final HtmlSubmit recalculate = new HtmlSubmit(tr("recalculate"));

            amountBlock.add(moneyField);
            amountBlock.add(recalculate);

            add(amountBlock);

        }
    }

    public HtmlDiv generateFeatureSummary(final Feature feature) {
        final HtmlDiv featureContributionSummary = new HtmlDiv("feature_contribution_summary");
        {
            featureContributionSummary.add(new HtmlTitle(tr("The feature"), 2));

            try {
                final HtmlDiv changeLine = new HtmlDiv("change_line");
                {

                    changeLine.add(SoftwaresTools.getSoftwareLogo(feature.getSoftware()));
                    changeLine.add(new MoneyVariationBlock(feature.getContribution(), feature.getContribution().add(process.getAmount())));
                }
                featureContributionSummary.add(changeLine);
                featureContributionSummary.add(new DefineParagraph(tr("Target feature: "), FeaturesTools.getTitle(feature)));
            } catch (final UnauthorizedOperationException e) {
                session.notifyError(Context.tr("An error prevented us from accessing user's info. Please notify us."));
                throw new ShallNotPassException("User cannot access user information", e);
            }
        }
        return featureContributionSummary;
    }

    private class StandardQuotation {

        final public QuotationEntry subTotalTTCEntry;
        final public QuotationEntry feesHT;
        final public QuotationEntry feesTTC;
        final public QuotationEntry totalHT;
        final public QuotationEntry totalTTC;
        final public QuotationEntry bank;
        final public QuotationEntry commission;

        public StandardQuotation(final BigDecimal amount) {

            final String fixBank = "0.30";
            final String variableBank = "0.03";
            final String TVAInvertedRate = "0.836120401";

            final Quotation quotation = new Quotation(Payline.computateAmountToPay(amount));

            subTotalTTCEntry = new QuotationAmountEntry("Subtotal TTC", null, amount);

            // Fees TTC
            final QuotationTotalEntry feesTotal = new QuotationTotalEntry(null, null, null);
            final QuotationPercentEntry feesVariable = new QuotationPercentEntry("Fees", null, subTotalTTCEntry, Payline.COMMISSION_VARIABLE_RATE);
            final QuotationAmountEntry feesFix = new QuotationAmountEntry("Fees", null, Payline.COMMISSION_FIX_RATE);
            feesTotal.addEntry(feesVariable);
            feesTotal.addEntry(feesFix);

            feesTTC = feesTotal;

            // Fees HT

            feesHT = new QuotationPercentEntry("Fees HT", null, feesTotal, new BigDecimal(TVAInvertedRate));

            // Total TTC
            totalTTC = quotation;

            // Total HT
            totalHT = new QuotationTotalEntry("Fees HT", null, null).addEntry(feesHT).addEntry(subTotalTTCEntry);

            // Fees details
            // Bank fees
            bank = new QuotationTotalEntry("Bank fees", null, "Total bank fees");

            final QuotationAmountEntry fixBankFee = new QuotationAmountEntry("Fix fee", null, new BigDecimal(fixBank));

            final QuotationPercentEntry variableBankFee = new QuotationPercentEntry("Variable fee",
                                                                              "" + Float.valueOf(variableBank) * 100 + "%",
                                                                              quotation,
                                                                              new BigDecimal(variableBank));
            bank.addEntry(variableBankFee);
            bank.addEntry(fixBankFee);

            // Our fees
            commission = new QuotationDifferenceEntry("Elveos's commission TTC", null, feesHT, bank);

            quotation.addEntry(subTotalTTCEntry);
            quotation.addEntry(feesTTC);

        }
    }

    @Override
    protected String getPageTitle() {
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
    protected Breadcrumb getBreadcrumb() {
        return CheckContributionPage.generateBreadcrumb(process.getFeature(), process);
    }

    public static Breadcrumb generateBreadcrumb(final Feature feature, final ContributionProcess process) {
        final Breadcrumb breadcrumb = FeaturePage.generateBreadcrumbContributions(feature);

        breadcrumb.pushLink(new CheckContributionPageUrl(process).getHtmlLink(tr("Contribute - Check")));

        return breadcrumb;
    }
}

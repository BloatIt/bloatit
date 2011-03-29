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

import static com.bloatit.framework.webserver.Context.tr;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.bloatit.common.Log;
import com.bloatit.framework.exceptions.RedirectException;
import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.annotations.ParamConstraint;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.annotations.RequestParam;
import com.bloatit.framework.webserver.annotations.tr;
import com.bloatit.framework.webserver.components.HtmlDiv;
import com.bloatit.framework.webserver.components.HtmlLink;
import com.bloatit.framework.webserver.components.HtmlParagraph;
import com.bloatit.framework.webserver.components.HtmlTitle;
import com.bloatit.framework.webserver.components.HtmlTitleBlock;
import com.bloatit.framework.webserver.components.advanced.HtmlClearer;
import com.bloatit.framework.webserver.components.meta.HtmlElement;
import com.bloatit.model.Feature;
import com.bloatit.model.Member;
import com.bloatit.model.Payline;
import com.bloatit.web.components.SideBarFeatureBlock;
import com.bloatit.web.linkable.features.FeaturePage;
import com.bloatit.web.linkable.features.FeaturesTools;
import com.bloatit.web.linkable.members.MembersTools;
import com.bloatit.web.linkable.money.HtmlQuotation;
import com.bloatit.web.linkable.money.Quotation;
import com.bloatit.web.linkable.money.Quotation.QuotationAmountEntry;
import com.bloatit.web.linkable.money.Quotation.QuotationDifferenceEntry;
import com.bloatit.web.linkable.money.Quotation.QuotationPercentEntry;
import com.bloatit.web.linkable.money.Quotation.QuotationProxyEntry;
import com.bloatit.web.linkable.money.Quotation.QuotationTotalEntry;
import com.bloatit.web.linkable.softwares.SoftwaresTools;
import com.bloatit.web.pages.LoggedPage;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.pages.master.DefineParagraph;
import com.bloatit.web.pages.master.TwoColumnLayout;
import com.bloatit.web.url.AccountChargingProcessUrl;
import com.bloatit.web.url.CheckContributionPageUrl;
import com.bloatit.web.url.ContributePageUrl;
import com.bloatit.web.url.ContributionActionUrl;
import com.bloatit.web.url.FeaturePageUrl;
import com.bloatit.web.url.PaylineActionUrl;

/**
 * A page that hosts the form used to check the contribution on a Feature
 */
@ParamContainer("contribute/check")
public final class CheckContributionPage extends LoggedPage {

    @RequestParam
    @ParamConstraint(optionalErrorMsg = @tr("The process is closed, expired, missing or invalid."))
    private final ContributionProcess process;

    private final CheckContributionPageUrl url;

    public CheckContributionPage(final CheckContributionPageUrl url) {
        super(url);
        this.url = url;
        process = url.getProcess();

    }

    @Override
    public void processErrors() throws RedirectException {
        addNotifications(url.getMessages());
        if (url.getMessages().hasMessage()) {
            session.notifyList(url.getMessages());
            throw new RedirectException(Context.getSession().getLastStablePage());
        }

    }

    @Override
    public HtmlElement createRestrictedContent() throws RedirectException {

        final TwoColumnLayout layout = new TwoColumnLayout(true);
        layout.addLeft(generateCheckContributeForm());

        layout.addRight(new SideBarFeatureBlock(process.getFeature()));

        return layout;
    }

    public HtmlElement generateCheckContributeForm() throws RedirectException {
        final HtmlTitleBlock group = new HtmlTitleBlock("Check contribution", 1);

        try {

            Feature feature = process.getFeature();
            Member member = session.getAuthToken().getMember();
            BigDecimal account = member.getInternalAccount().getAmount();

            if (process.getAmount().compareTo(account) <= 0) {

                generateWithMoneyContent(group, feature, member);

            } else {
                generateNoMoneyContent(group, feature, member, account);
            }

        } catch (UnauthorizedOperationException e) {
            Log.web().error("Fail to check contribution", e);
            throw new RedirectException(new FeaturePageUrl(process.getFeature()));
        }

        return group;
    }

    public void generateWithMoneyContent(final HtmlTitleBlock group, Feature feature, Member member) throws UnauthorizedOperationException {
        HtmlDiv contributionSummaryDiv = new HtmlDiv("contribution_summary");
        {
            contributionSummaryDiv.add(generateFeatureSummary(feature));

            HtmlDiv authorContributionSummary = new HtmlDiv("author_contribution_summary");
            {

                authorContributionSummary.add(new HtmlTitle(tr("Your account"), 2));

                HtmlDiv changeLine = new HtmlDiv("change_line");
                {

                    changeLine.add(new MoneyVariationBlock(member.getInternalAccount().getAmount(), member.getInternalAccount()
                                                                                                          .getAmount()
                                                                                                          .subtract(process.getAmount())));
                    changeLine.add(MembersTools.getMemberAvatar(member));
                }
                authorContributionSummary.add(changeLine);
                authorContributionSummary.add(new DefineParagraph(tr("Author:"), member.getDisplayName()));
                if (process.getComment() != null) {
                    authorContributionSummary.add(new DefineParagraph(tr("Comment:"), process.getComment()));
                } else {
                    authorContributionSummary.add(new DefineParagraph(tr("Comment:"), tr("No comment")));
                }

            }
            contributionSummaryDiv.add(authorContributionSummary);

        }
        group.add(contributionSummaryDiv);

        HtmlDiv buttonDiv = new HtmlDiv("contribution_actions");
        {
            ContributionActionUrl contributionActionUrl = new ContributionActionUrl(process);
            HtmlLink confirmContributionLink = contributionActionUrl.getHtmlLink(tr("Contribute {0}",
                                                                                    Context.getLocalizator()
                                                                                           .getCurrency(process.getAmount())
                                                                                           .getDefaultString()));
            confirmContributionLink.setCssClass("button");

            buttonDiv.add(confirmContributionLink);

            // Modify contribution button
            ContributePageUrl contributePageUrl = new ContributePageUrl(process);
            HtmlLink modifyContributionLink = contributePageUrl.getHtmlLink(tr("or modify contribution"));

            buttonDiv.add(modifyContributionLink);

        }
        group.add(buttonDiv);
    }

    private void generateNoMoneyContent(final HtmlTitleBlock group, Feature feature, Member member, BigDecimal account)
            throws UnauthorizedOperationException {

        BigDecimal missingAmount = process.getAmount().subtract(account);

        session.setTargetPage(url);

        Quotation quotation = generateQuotationModel(missingAmount);

        HtmlDiv contributionSummaryDiv = new HtmlDiv("contribution_summary");
        {
            contributionSummaryDiv.add(generateFeatureSummary(feature));

            HtmlDiv authorContributionSummary = new HtmlDiv("author_contribution_summary");
            {

                authorContributionSummary.add(new HtmlTitle(tr("Quotation"), 2));


                authorContributionSummary.add( new HtmlDiv("float_right").add(MembersTools.getMemberAvatar(member)));
                authorContributionSummary.add(new DefineParagraph(tr("Author:"), member.getDisplayName()));
                if (process.getComment() != null) {
                    authorContributionSummary.add(new DefineParagraph(tr("Comment:"), process.getComment()));
                } else {
                    authorContributionSummary.add(new DefineParagraph(tr("Comment:"), tr("No comment")));
                }

                authorContributionSummary.add(new HtmlClearer());

                HtmlQuotation quotationBlock = new HtmlQuotation(quotation);
                authorContributionSummary.add(quotationBlock);

            }
            contributionSummaryDiv.add(authorContributionSummary);

        }
        group.add(contributionSummaryDiv);

        HtmlDiv buttonDiv = new HtmlDiv("contribution_actions");
        {

            final PaylineActionUrl payActionUrl = new PaylineActionUrl();
            payActionUrl.setAmount(missingAmount);

            HtmlLink payContributionLink = payActionUrl.getHtmlLink(tr("Pay {0}",
                                                                       Context.getLocalizator()
                                                                              .getCurrency(quotation.getValue())
                                                                              .getDecimalDefaultString()));
            payContributionLink.setCssClass("button");
            buttonDiv.add(payContributionLink);

            // Modify contribution button
            ContributePageUrl contributePageUrl = new ContributePageUrl(process);
            HtmlLink modifyContributionLink = contributePageUrl.getHtmlLink(tr("or modify contribution"));

            buttonDiv.add(modifyContributionLink);

        }
        group.add(buttonDiv);

        HtmlParagraph chargeAccountPara = new HtmlParagraph(tr("You can also pay now more money for future contributions."));
        group.add(chargeAccountPara);

        final AccountChargingProcessUrl accountChargingProcess = new AccountChargingProcessUrl();
        accountChargingProcess.setAmount(missingAmount);

        HtmlLink accountChargingProcessLink = accountChargingProcess.getHtmlLink(tr("Charge account"));
        accountChargingProcessLink.setCssClass("button");
        group.add(accountChargingProcessLink);

    }

    public HtmlDiv generateFeatureSummary(Feature feature) throws UnauthorizedOperationException {
        HtmlDiv featureContributionSummary = new HtmlDiv("feature_contribution_summary");
        {
            featureContributionSummary.add(new HtmlTitle(tr("The feature"), 2));

            HtmlDiv changeLine = new HtmlDiv("change_line");
            {

                changeLine.add(SoftwaresTools.getSoftwareLogo(feature.getSoftware()));
                changeLine.add(new MoneyVariationBlock(feature.getContribution(), feature.getContribution().add(process.getAmount())));
            }
            featureContributionSummary.add(changeLine);

            featureContributionSummary.add(new DefineParagraph(tr("Target feature:"), FeaturesTools.getTitle(feature)));

        }
        return featureContributionSummary;
    }

    private Quotation generateQuotationModel(BigDecimal amount) {

        String fixBank = "0.30";
        String variableBank = "0.03";

        Quotation quotation = new Quotation(Payline.computateAmountToPay(amount));

        QuotationTotalEntry contributionTotal = new QuotationTotalEntry("Contributions", null, "Total before fees");
        contributionTotal.setClosed(false);
        QuotationAmountEntry missingAmount = new QuotationAmountEntry("Missing amount", null, amount);

        QuotationAmountEntry prepaid = new QuotationAmountEntry("Prepaid", null, new BigDecimal(0));
        contributionTotal.addEntry(missingAmount);
        contributionTotal.addEntry(prepaid);
        quotation.addEntry(contributionTotal);

        QuotationTotalEntry feesTotal = new QuotationTotalEntry(null, null, null);


        QuotationPercentEntry percentFeesTotal = new QuotationPercentEntry("Fees", null, contributionTotal, Payline.COMMISSION_VARIABLE_RATE);

        QuotationAmountEntry fixfeesTotal = new QuotationAmountEntry("Fees", null, Payline.COMMISSION_FIX_RATE);
        feesTotal.addEntry(percentFeesTotal);
        feesTotal.addEntry(fixfeesTotal);

        QuotationProxyEntry feesProxy = new QuotationProxyEntry("Fees", "" + Payline.COMMISSION_VARIABLE_RATE.multiply(new BigDecimal(100)) + "% + "
                + Payline.COMMISSION_FIX_RATE + "€", feesTotal);

        feesProxy.setClosed(false);

        // Fees details
        // Bank fees
        QuotationTotalEntry bankFeesTotal = new QuotationTotalEntry("Bank fees", null, "Total bank fees");

        QuotationAmountEntry fixBankFee = new QuotationAmountEntry("Fix fee", null, new BigDecimal(fixBank));

        QuotationPercentEntry variableBankFee = new QuotationPercentEntry("Variable fee",
                                                                          "" + Float.valueOf(variableBank) * 100 + "%",
                                                                          quotation,
                                                                          new BigDecimal(variableBank));
        bankFeesTotal.addEntry(variableBankFee);
        bankFeesTotal.addEntry(fixBankFee);
        feesProxy.addEntry(bankFeesTotal);

        // Our fees
        QuotationDifferenceEntry commissionTTC = new QuotationDifferenceEntry("Elveos's commission TTC", null, feesTotal, bankFeesTotal);

        QuotationPercentEntry commissionHT = new QuotationPercentEntry("Commission HT", null, commissionTTC, new BigDecimal(1 / 1.196));
        QuotationDifferenceEntry ourFeesTVA = new QuotationDifferenceEntry("TVA for commission", "19.6%", commissionTTC, commissionHT);
        commissionTTC.addEntry(commissionHT);
        commissionTTC.addEntry(ourFeesTVA);
        feesProxy.addEntry(commissionTTC);

        quotation.addEntry(feesProxy);

        System.out.println("Rendement: "
                + commissionHT.getValue()
                              .divide(quotation.getValue(), BigDecimal.ROUND_HALF_EVEN)
                              .multiply(new BigDecimal(100))
                              .setScale(2, BigDecimal.ROUND_HALF_EVEN));


        return quotation;
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

    public static Breadcrumb generateBreadcrumb(Feature feature, ContributionProcess process) {
        Breadcrumb breadcrumb = FeaturePage.generateBreadcrumbContributions(feature);

        breadcrumb.pushLink(new CheckContributionPageUrl(process).getHtmlLink(tr("Contribute - Check")));

        return breadcrumb;
    }

    @Override
    protected List<String> getCustomCss() {
        List<String> cssList = new ArrayList<String>();
        cssList.add("check_contribution.css");
        return cssList;
    }

}

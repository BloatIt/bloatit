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
import com.bloatit.framework.utils.Image;
import com.bloatit.framework.webprocessor.annotations.Optional;
import com.bloatit.framework.webprocessor.annotations.ParamConstraint;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlImage;
import com.bloatit.framework.webprocessor.components.HtmlLink;
import com.bloatit.framework.webprocessor.components.HtmlParagraph;
import com.bloatit.framework.webprocessor.components.HtmlSpan;
import com.bloatit.framework.webprocessor.components.HtmlTitle;
import com.bloatit.framework.webprocessor.components.HtmlTitleBlock;
import com.bloatit.framework.webprocessor.components.javascript.JsShowHide;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.components.meta.HtmlMixedText;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.Actor;
import com.bloatit.model.Feature;
import com.bloatit.model.InternalAccount;
import com.bloatit.model.Member;
import com.bloatit.model.Payline;
import com.bloatit.web.WebConfiguration;
import com.bloatit.web.components.SideBarFeatureBlock;
import com.bloatit.web.linkable.features.FeaturePage;
import com.bloatit.web.linkable.features.FeaturesTools;
import com.bloatit.web.linkable.members.MembersTools;
import com.bloatit.web.linkable.softwares.SoftwaresTools;
import com.bloatit.web.linkable.usercontent.CreateUserContentPage;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.pages.master.HtmlDefineParagraph;
import com.bloatit.web.pages.master.sidebar.TwoColumnLayout;
import com.bloatit.web.url.CheckContributionActionUrl;
import com.bloatit.web.url.CheckContributionPageUrl;
import com.bloatit.web.url.PaylineProcessUrl;
import com.bloatit.web.url.StaticCheckContributionPageUrl;
import com.bloatit.web.url.UnlockActionUrl;

/**
 * A page that hosts the form used to check the contribution on a Feature
 */
@ParamContainer("contribute/staticcheck")
public final class StaticCheckContributionPage extends CreateUserContentPage {

    @RequestParam(conversionErrorMsg = @tr("The process is closed, expired, missing or invalid."))
    @ParamConstraint(optionalErrorMsg = @tr("The process is closed, expired, missing or invalid."))
    private final ContributionProcess process;

    private final StaticCheckContributionPageUrl url;

    @Optional("false")
    @RequestParam(name = "show_fees_detail")
    private final Boolean showFeesDetails;

    public StaticCheckContributionPage(final StaticCheckContributionPageUrl url) {
        super(url, new CheckContributionActionUrl(url.getProcess()));
        this.url = url;
        process = url.getProcess();
        showFeesDetails = url.getShowFeesDetails();
        if (process != null) {
            process.setLock(true);
        }
    }

    @Override
    public HtmlElement createRestrictedContent(final Member loggedUser) throws RedirectException {
        final TwoColumnLayout layout = new TwoColumnLayout(true, url);
        layout.addLeft(generateCheckContributeForm(loggedUser));
        layout.addRight(new SideBarFeatureBlock(process.getFeature(), process.getAmount()));
        return layout;
    }

    public HtmlElement generateCheckContributeForm(final Member member) throws RedirectException {
        final HtmlTitleBlock group = new HtmlTitleBlock(tr("Final check"), 1);
        BigDecimal account;
        try {
            account = getAccount(member).getAmount();
            if (process.getAmount().compareTo(account) <= 0) {
                throw new RedirectException(new CheckContributionPageUrl(process));
            }
            generateNoMoneyContent(group, getActor(member), account);
        } catch (final UnauthorizedOperationException e) {
            session.notifyError(Context.tr("An error prevented us from displaying getting your account balance. Please notify us."));
            throw new ShallNotPassException("User cannot access user's account balance", e);
        }
        return group;
    }

    private InternalAccount getAccount(final Member member) throws UnauthorizedOperationException {
        return getActor(member).getInternalAccount();
    }

    private Actor<?> getActor(final Member member) throws UnauthorizedOperationException {
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
            session.notifyBad(tr("The contribution's total amount is locked during the payment process."));
        }

        // Contribution
        final HtmlDiv detailsLines = new HtmlDiv("quotation_details_lines");
        try {
            detailsLines.add(new HtmlContributionLine(process));
            if (actor.getInternalAccount().getAmount().compareTo(BigDecimal.ZERO) > 0) {
                detailsLines.add(new HtmlPrepaidLine(actor));
            }
            detailsLines.add(new HtmlChargeAccountLine(actor));
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

            final StaticCheckContributionPageUrl showDetailUrl = url.clone();
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

        final HtmlDiv payBlock = new HtmlDiv("pay_actions");
        {
            final PaylineProcessUrl paylineProcessUrl = new PaylineProcessUrl(actor, process);

            final HtmlLink payContributionLink = paylineProcessUrl.getHtmlLink(tr("Pay {0}",
                                                                                  Context.getLocalizator()
                                                                                         .getCurrency(quotation.totalTTC.getValue())
                                                                                         .getDecimalDefaultString()));
            payContributionLink.setCssClass("button");

            payBlock.add(new HtmlParagraph(Context.tr("You are using a beta version. Payment with real money is not activated."), "debug"))
                    .add(new HtmlParagraph(Context.tr("You can simulate it using this card number: 4970100000325734, and the security number: 123."),
                                           "debug"));
            if (process.getTeam() != null) {
                try {
                    payBlock.add(new HtmlParagraph(Context.tr("You are using the account of ''{0}'' team.", process.getTeam().getLogin()),
                                                   "use_account"));
                } catch (final UnauthorizedOperationException e) {
                    throw new ShallNotPassException(e);
                }
            }
            payBlock.add(createUnlockedReturnUrl().getHtmlLink(Context.tr("edit")));
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
            add(amountBlock);
        }
    }

    public static class HtmlPrepaidLine extends HtmlDiv {

        public HtmlPrepaidLine(final Actor<?> actor) throws UnauthorizedOperationException {
            super("quotation_detail_line");

            add(MembersTools.getMemberAvatarSmall(actor));

            add(new HtmlDiv("quotation_detail_line_money").addText(Context.getLocalizator()
                                                                          .getCurrency(actor.getInternalAccount().getAmount())
                                                                          .getDefaultString()));
            add(new HtmlDiv().setCssClass("quotation_detail_line_money_image").add(new HtmlImage(new Image(WebConfiguration.getImgMoneyDownSmall()),
                                                                                                 "money up")));
            add(new HtmlDiv("quotation_detail_line_money").addText(Context.getLocalizator().getCurrency(BigDecimal.ZERO).getDefaultString()));

            add(new HtmlDiv("quotation_detail_line_categorie").addText(tr("Prepaid from internal account")));

            final HtmlDiv amountBlock = new HtmlDiv("quotation_detail_line_amount");

            amountBlock.add(new HtmlDiv("quotation_detail_line_amount_money").addText(Context.getLocalizator()
                                                                                             .getCurrency(actor.getInternalAccount()
                                                                                                               .getAmount()
                                                                                                               .negate())
                                                                                             .getDecimalDefaultString()));

            add(amountBlock);
        }
    }

    public class HtmlChargeAccountLine extends HtmlDiv {

        @SuppressWarnings("synthetic-access")
        public HtmlChargeAccountLine(final Actor<?> actor) throws UnauthorizedOperationException {
            super("quotation_detail_line");

            add(MembersTools.getMemberAvatarSmall(actor));

            add(new HtmlDiv("quotation_detail_line_money").addText(Context.getLocalizator().getCurrency(BigDecimal.ZERO).getDefaultString()));
            add(new HtmlDiv().setCssClass("quotation_detail_line_money_image").add(new HtmlImage(new Image(WebConfiguration.getImgMoneyUpSmall()),
                                                                                                 "money up")));
            add(new HtmlDiv("quotation_detail_line_money").addText(Context.getLocalizator()
                                                                          .getCurrency(process.getAmountToCharge())
                                                                          .getDefaultString()));

            add(new HtmlDiv("quotation_detail_line_categorie").addText(tr("Internal account")));
            add(new HtmlDiv("quotation_detail_line_description").addText(tr("Load money in your internal account for future contributions.")));

            final HtmlDiv amountBlock = new HtmlDiv("quotation_detail_line_amount");

            amountBlock.add(new HtmlDiv("quotation_detail_line_amount_money").addText(Context.getLocalizator()
                                                                                             .getCurrency(process.getAmountToCharge())
                                                                                             .getDecimalDefaultString()));

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
                featureContributionSummary.add(new HtmlDefineParagraph(tr("Target feature: "), FeaturesTools.getTitle(feature)));
            } catch (final UnauthorizedOperationException e) {
                session.notifyError(Context.tr("An error prevented us from accessing user's info. Please notify us."));
                throw new ShallNotPassException("User cannot access user information", e);
            }
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

    private Url createUnlockedReturnUrl() {
        return new UnlockActionUrl(process);
    }

    @Override
    protected Breadcrumb createBreadcrumb() {
        final Breadcrumb breadcrumb = FeaturePage.generateBreadcrumbContributions(process.getFeature());
        final CheckContributionActionUrl returnUrl = new CheckContributionActionUrl(process);
        returnUrl.setAmount(process.getAmount());
        breadcrumb.pushLink(createUnlockedReturnUrl().getHtmlLink(tr("Contribute - Check")));
        breadcrumb.pushLink(new CheckContributionPageUrl(process).getHtmlLink(tr("Final check")));
        return breadcrumb;
    }

}

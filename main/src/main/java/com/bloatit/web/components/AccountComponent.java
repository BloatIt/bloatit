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
package com.bloatit.web.components;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.bloatit.data.DaoBankTransaction.State;
import com.bloatit.data.DaoFeature.FeatureState;
import com.bloatit.data.DaoMoneyWithdrawal;
import com.bloatit.framework.exceptions.highlevel.ShallNotPassException;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.framework.utils.Sorter;
import com.bloatit.framework.utils.Sorter.Order;
import com.bloatit.framework.utils.datetime.DateUtils;
import com.bloatit.framework.utils.i18n.DateLocale.FormatStyle;
import com.bloatit.framework.utils.i18n.Language;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlImage;
import com.bloatit.framework.webprocessor.components.HtmlLink;
import com.bloatit.framework.webprocessor.components.HtmlParagraph;
import com.bloatit.framework.webprocessor.components.HtmlSpan;
import com.bloatit.framework.webprocessor.components.HtmlTitle;
import com.bloatit.framework.webprocessor.components.PlaceHolderElement;
import com.bloatit.framework.webprocessor.components.advanced.HtmlTable;
import com.bloatit.framework.webprocessor.components.advanced.HtmlTable.HtmlLineTableModel;
import com.bloatit.framework.webprocessor.components.advanced.HtmlTable.HtmlLineTableModel.EmptyCell;
import com.bloatit.framework.webprocessor.components.advanced.HtmlTable.HtmlLineTableModel.HtmlTableCell;
import com.bloatit.framework.webprocessor.components.advanced.HtmlTable.HtmlLineTableModel.HtmlTableLine;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.components.meta.HtmlMixedText;
import com.bloatit.framework.webprocessor.components.meta.HtmlNode;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.Actor;
import com.bloatit.model.BankTransaction;
import com.bloatit.model.Contribution;
import com.bloatit.model.ContributionInvoice;
import com.bloatit.model.FeatureImplementation;
import com.bloatit.model.Image;
import com.bloatit.model.Invoice;
import com.bloatit.model.Member;
import com.bloatit.model.Milestone;
import com.bloatit.model.MoneyWithdrawal;
import com.bloatit.model.Team;
import com.bloatit.model.right.UnauthorizedOperationException;
import com.bloatit.model.right.UnauthorizedPrivateAccessException;
import com.bloatit.model.right.UnauthorizedReadOnlyBankDataAccessException;
import com.bloatit.web.WebConfiguration;
import com.bloatit.web.linkable.features.FeatureTabPane;
import com.bloatit.web.linkable.features.FeatureTabPane.FeatureTabKey;
import com.bloatit.web.linkable.master.HtmlDefineParagraph;
import com.bloatit.web.linkable.master.HtmlPageComponent;
import com.bloatit.web.linkable.softwares.SoftwaresTools;
import com.bloatit.web.url.CancelContributionPageUrl;
import com.bloatit.web.url.CancelWithdrawMoneyActionUrl;
import com.bloatit.web.url.ContributionInvoiceResourceUrl;
import com.bloatit.web.url.FeaturePageUrl;
import com.bloatit.web.url.InvoiceResourceUrl;

public class AccountComponent extends HtmlPageComponent {
    public AccountComponent(final Team team) throws UnauthorizedOperationException {
        final HtmlDiv accountPage = new HtmlDiv("account_page");
        add(accountPage);
        accountPage.add(generateAccountSolde(team));
        accountPage.add(new HtmlTitle(tr("Account informations – {0}", team.getDisplayName()), 1));
        accountPage.add(generateAccountMovementList(team.getContributions(),
                                                    team.getBankTransactions(),
                                                    team.getMoneyWithdrawals(),
                                                    team.getMilestones()));
    }

    public AccountComponent(final Member me) throws UnauthorizedOperationException {
        final HtmlDiv accountPage = new HtmlDiv("account_page");
        add(accountPage);
        accountPage.add(generateAccountSolde(me));
        accountPage.add(new HtmlTitle(tr("Account informations – {0}", me.getDisplayName()), 1));
        accountPage.add(generateAccountMovementList(me.getContributions(), me.getBankTransactions(), me.getMoneyWithdrawals(), me.getMilestones()));
    }

    private HtmlElement generateAccountSolde(final Actor<?> loggedUser) {
        final HtmlDiv floatRight = new HtmlDiv("float_right");
        final HtmlDiv soldeBlock = new HtmlDiv("solde_block");
        final HtmlDiv soldeText = new HtmlDiv("solde_text");
        if (loggedUser instanceof Team) {
            soldeText.addText(tr("The team currently has "));
        } else {
            soldeText.addText(tr("You currently have "));
        }

        final HtmlDiv soldeAmount = new HtmlDiv("solde_amount");

        try {
            soldeAmount.addText(Context.getLocalizator().getCurrency(loggedUser.getInternalAccount().getAmount()).getSimpleEuroString());
        } catch (final UnauthorizedOperationException e) {
            throw new ShallNotPassException("Right error.", e);
        }

        soldeBlock.add(soldeText);
        soldeBlock.add(soldeAmount);
        floatRight.add(soldeBlock);
        return floatRight;
    }

    private HtmlElement generateAccountMovementList(final PageIterable<Contribution> contributions,
                                                    final PageIterable<BankTransaction> bankTransactions,
                                                    final PageIterable<MoneyWithdrawal> moneyWithdrawals,
                                                    final PageIterable<Milestone> milestones) {
        final List<HtmlTableLine> lineList = new ArrayList<HtmlTableLine>();
        final Sorter<HtmlTableLine, Date> sorter = new Sorter<HtmlTableLine, Date>(lineList);

        try {
            for (final MoneyWithdrawal moneyWithdrawal : moneyWithdrawals) {
                sorter.add(new MoneyWithdrawalLine(moneyWithdrawal), moneyWithdrawal.getCreationDate());
            }

            for (final Contribution contribution : contributions) {
                if (contribution.getState() == com.bloatit.data.DaoContribution.ContributionState.CANCELED) {
                    sorter.add(new ContributionCanceledLine(contribution), contribution.getCreationDate());
                } else {
                    if (contribution.getFeature().getFeatureState() != FeatureState.DISCARDED) {
                        sorter.add(new ContributionLine(contribution), contribution.getCreationDate());
                    } else {
                        sorter.add(new ContributionFailedLine(contribution), contribution.getCreationDate());
                    }
                }
            }

            for (final BankTransaction bankTransaction : bankTransactions) {
                if (bankTransaction.getValue().compareTo(BigDecimal.ZERO) >= 0) {
                    if (bankTransaction.getState() == State.VALIDATED) {
                        sorter.add(new ChargeAccountLine(bankTransaction), bankTransaction.getModificationDate());
                    } else if (bankTransaction.getState() == State.REFUSED) {
                        sorter.add(new ChargeAccountFailedLine(bankTransaction), bankTransaction.getModificationDate());
                    } else {
                        if (DateUtils.elapsed(bankTransaction.getModificationDate(), DateUtils.now()) > DateUtils.SECOND_PER_DAY * 1000) {
                            // Aborted
                            sorter.add(new ChargeAccountAbordedLine(bankTransaction), bankTransaction.getModificationDate());
                        }
                    }
                }
            }

            for (final Milestone milestone : milestones) {
                if (milestone.getAmountPaid().compareTo(BigDecimal.ZERO) > 0) {
                    sorter.add(new MilestoneLine(milestone), milestone.getLastPaymentDate());
                }
            }
        } catch (final UnauthorizedOperationException e) {
            throw new ShallNotPassException("Right fail on account page", e);
        }

        sorter.performSort(Order.DESC);

        if (sorter.size() == 0) {
            return new HtmlParagraph(Context.tr("No operations occured on your account yet."));
        }

        final HtmlLineTableModel model = new HtmlLineTableModel();
        for (final HtmlTableLine line : lineList) {
            model.addLine(line);
        }

        final HtmlTable table = new HtmlTable(model);
        return table;
    }

    private static class ContributionLine extends HtmlTableLine {

        private final Contribution contribution;

        public ContributionLine(final Contribution contribution) throws UnauthorizedOperationException {
            this.contribution = contribution;
            addCell(new MoneyVariationCell(false));
            addCell(new TitleCell(contribution.getCreationDate(), generateContributionTitle()));
            addCell(new DescriptionCell(tr("Feature summary"), generateContributionDescription()));
            addCell(new MoneyCell(contribution.getAmount().negate()));
        }

        private HtmlDiv generateContributionDescription() throws UnauthorizedPrivateAccessException {
            final HtmlDiv description = new HtmlDiv("description");
            final HtmlSpan softwareLink = new SoftwaresTools.Link(contribution.getFeature().getSoftware());
            final HtmlMixedText descriptionString = new HtmlMixedText(Context.tr("{0} (<0::>)",
                                                                                 contribution.getFeature()
                                                                                             .getDescription()
                                                                                             .getTranslationOrDefault(Language.fromLocale(Context.getLocalizator()
                                                                                                                                                 .getLocale()))
                                                                                             .getTitle()),
                                                                      softwareLink);

            final HtmlSpan status = new HtmlSpan();

            switch (contribution.getFeature().getFeatureState()) {
                case DEVELOPPING:
                    status.addText(tr("In development"));
                    break;
                case FINISHED:
                    status.addText(tr("Success"));
                    break;
                case DISCARDED:
                    status.addText(tr("Failed"));
                    break;
                case PENDING:
                case PREPARING:
                    final HtmlLink cancelLink = new CancelContributionPageUrl(contribution).getHtmlLink(Context.tr("(Cancel contribution)"));
                    status.addText(tr("Funding "));
                    status.add(cancelLink);
                    break;
            }

            description.add(new HtmlDefineParagraph(tr("Description: "), descriptionString));
            description.add(new HtmlDefineParagraph(tr("Status: "), status));

            final PageIterable<ContributionInvoice> invoices = contribution.getInvoices();
            if (invoices.size() > 1) {
                final HtmlSpan invoiceList = new HtmlSpan();
                for (final ContributionInvoice invoice : invoices) {
                    invoiceList.addText(" ");
                    invoiceList.add(new ContributionInvoiceResourceUrl(invoice).getHtmlLink(Context.tr("milestone {0}", invoice.getMilestone()
                                                                                                                               .getPosition())));
                }
                description.add(new HtmlDefineParagraph(tr("Invoices: "), invoiceList));
            } else if (invoices.size() == 1) {
                final HtmlSpan invoiceList = new HtmlSpan();
                for (final ContributionInvoice invoice : invoices) {
                    invoiceList.addText(" ");
                    invoiceList.add(new ContributionInvoiceResourceUrl(invoice).getHtmlLink(invoice.getInvoiceNumber()));
                }
                description.add(new HtmlDefineParagraph(tr("Invoice: "), invoiceList));
            }
            return description;
        }

        private HtmlDiv generateContributionTitle() {
            final HtmlDiv title = new HtmlDiv("title");
            final FeaturePageUrl featurePageUrl = new FeaturePageUrl(contribution.getFeature(), FeatureTabKey.contributions);
            featurePageUrl.setAnchor(FeatureTabPane.FEATURE_TAB_PANE);
            title.add(new HtmlMixedText(tr("Contributed to a <0::feature>"), featurePageUrl.getHtmlLink()));
            return title;
        }
    }

    private static class ContributionFailedLine extends HtmlTableLine {
        private final Contribution contribution;

        public ContributionFailedLine(final Contribution contribution) throws UnauthorizedOperationException {
            setCssClass("failed_line");
            this.contribution = contribution;
            addCell(new EmptyCell());
            addCell(new TitleCell(contribution.getCreationDate(), generateContributionTitle()));
            addCell(new EmptyCell());
            addCell(new MoneyCell(contribution.getAmount().negate()));
        }

        private HtmlDiv generateContributionTitle() {
            final HtmlDiv title = new HtmlDiv("title");
            final FeaturePageUrl featurePageUrl = new FeaturePageUrl(contribution.getFeature(), FeatureTabKey.contributions);
            featurePageUrl.setAnchor(FeatureTabPane.FEATURE_TAB_PANE);
            title.add(new HtmlMixedText(tr("Contributed to a failed <0::feature>"), featurePageUrl.getHtmlLink()));
            return title;
        }
    }

    /**
     * 
     */
    private static class ContributionCanceledLine extends HtmlTableLine {
        private final Contribution contribution;

        public ContributionCanceledLine(final Contribution contribution) throws UnauthorizedOperationException {
            setCssClass("failed_line");
            this.contribution = contribution;
            addCell(new EmptyCell());
            addCell(new TitleCell(contribution.getCreationDate(), generateContributionTitle()));
            addCell(new EmptyCell());
            addCell(new MoneyCell(contribution.getAmount().negate()));
        }

        private HtmlDiv generateContributionTitle() {
            final HtmlDiv title = new HtmlDiv("title");
            final FeaturePageUrl featurePageUrl = new FeaturePageUrl(contribution.getFeature(), FeatureTabKey.contributions);
            title.add(new HtmlMixedText(tr("Contribution canceled (<0::feature>)"), featurePageUrl.getHtmlLink()));
            return title;
        }
    }

    private static class MilestoneLine extends HtmlTableLine {

        private final Milestone milestone;

        public MilestoneLine(final Milestone milestone) throws UnauthorizedOperationException {
            this.milestone = milestone;
            addCell(new MoneyVariationCell(true));
            addCell(new TitleCell(milestone.getLastPaymentDate(), generateMilestoneTitle()));
            addCell(new DescriptionCell(tr("Feature summary"), generateMilestoneDescription()));
            addCell(new MoneyCell(milestone.getAmountPaid()));
        }

        private HtmlDiv generateMilestoneDescription() {
            final HtmlDiv description = new HtmlDiv("description");
            final FeatureImplementation feature = milestone.getOffer().getFeature();
            final HtmlSpan softwareLink = new SoftwaresTools.Link(feature.getSoftware());
            final HtmlMixedText descriptionString = new HtmlMixedText(Context.tr("{0} (<0::>)",
                                                                                 feature.getDescription()
                                                                                        .getTranslationOrDefault(Language.fromLocale(Context.getLocalizator()
                                                                                                                                            .getLocale()))
                                                                                        .getTitle()),
                                                                      softwareLink);

            String statusString = "";
            switch (feature.getFeatureState()) {
                case DEVELOPPING:
                    statusString = tr("In development");
                    break;
                case FINISHED:
                    statusString = tr("Success");
                    break;
                case DISCARDED:
                    statusString = tr("Failed");
                    break;
                case PENDING:
                case PREPARING:
                    statusString = tr("Funding");
                    break;
            }

            description.add(new HtmlDefineParagraph(tr("Description: "), descriptionString));
            description.add(new HtmlDefineParagraph(tr("Status: "), statusString));
            return description;
        }

        private HtmlDiv generateMilestoneTitle() {
            final HtmlDiv title = new HtmlDiv("title");
            final FeaturePageUrl featurePageUrl = new FeaturePageUrl(milestone.getOffer().getFeature(), FeatureTabKey.offers);
            title.add(new HtmlMixedText(tr("Payed for <0::milestone> development"), featurePageUrl.getHtmlLink()));
            return title;
        }
    }

    private static class MoneyWithdrawalLine extends HtmlTableLine {
        private final MoneyWithdrawal moneyWithdrawal;
        private final boolean failed;

        public MoneyWithdrawalLine(final MoneyWithdrawal moneyWithdrawal) throws UnauthorizedOperationException {
            this.moneyWithdrawal = moneyWithdrawal;
            failed = (moneyWithdrawal.getState() == DaoMoneyWithdrawal.State.CANCELED || moneyWithdrawal.getState() == DaoMoneyWithdrawal.State.REFUSED);
            if (failed) {
                setCssClass("failed_line");
                addCell(new EmptyCell());
            } else {
                addCell(new MoneyVariationCell(false));
            }
            addCell(new TitleCell(moneyWithdrawal.getCreationDate(), generateTitle()));
            if (!failed) {
                addCell(new DescriptionCell(tr("Withdrawal summary"), generateContributionDescription()));
            } else {
                addCell(new DescriptionCell("", generateContributionDescription()));
            }
            addCell(new MoneyCell(moneyWithdrawal.getAmountWithdrawn().negate()));
        }

        private HtmlDiv generateContributionDescription() throws UnauthorizedOperationException {
            final HtmlDiv description = new HtmlDiv("description");

            String statusString = "";
            switch (moneyWithdrawal.getState()) {
                case CANCELED:
                    statusString = tr("Canceled");
                    break;
                case REFUSED:
                    statusString = tr("Rejected");
                    break;
                case COMPLETE:
                    statusString = tr("Complete");
                    break;
                case REQUESTED:
                    statusString = tr("Requested");
                    break;
                case TREATED:
                    statusString = tr("In progress");
                    break;
            }

            description.add(new HtmlDefineParagraph(tr("IBAN: "), moneyWithdrawal.getIBAN()));
            description.add(new HtmlDefineParagraph(tr("Reference: "), moneyWithdrawal.getReference()));
            if (moneyWithdrawal.getState() == DaoMoneyWithdrawal.State.REQUESTED) {
                final CancelWithdrawMoneyActionUrl cancelUrl = new CancelWithdrawMoneyActionUrl(Context.getSession().getShortKey(), moneyWithdrawal);
                final HtmlMixedText statusWithCancel = new HtmlMixedText(tr("{0} (<0::cancel withdrawal>)", statusString), cancelUrl.getHtmlLink());
                description.add(new HtmlDefineParagraph(tr("Status: "), statusWithCancel));
            } else {
                description.add(new HtmlDefineParagraph(tr("Status: "), statusString));
            }
            return description;
        }

        private HtmlDiv generateTitle() throws UnauthorizedReadOnlyBankDataAccessException {
            final HtmlDiv title = new HtmlDiv("title");
            if (failed) {
                if (moneyWithdrawal.getState() == DaoMoneyWithdrawal.State.CANCELED) {
                    title.addText(tr("Withdrew money canceled"));
                } else {
                    title.addText(tr("Withdrew money failure"));
                }
            } else {
                title.addText(tr("Withdrew money"));
            }
            return title;
        }
    }

    private static class ChargeAccountLine extends HtmlTableLine {
        private final BankTransaction bankTransaction;

        public ChargeAccountLine(final BankTransaction bankTransaction) {
            this.bankTransaction = bankTransaction;
            try {
                addCell(new MoneyVariationCell(true));
                addCell(new TitleCell(bankTransaction.getCreationDate(), generateChargeAccountTitle()));
                addCell(new DescriptionCell(tr("Account loading summary"), generateChargeAccountDescription()));
                addCell(new MoneyCell(bankTransaction.getValue()));
            } catch (final UnauthorizedOperationException e) {
                addCell(new EmptyCell());
                addCell(new EmptyCell());
                addCell(new EmptyCell());
                addCell(new EmptyCell());
                Context.getSession().notifyWarning(Context.tr("You are not allowed to see this account informations."));
            }
        }

        private HtmlDiv generateChargeAccountDescription() throws UnauthorizedOperationException {
            final HtmlDiv description = new HtmlDiv("description");
            description.add(new HtmlDefineParagraph(tr("Total cost: "), Context.getLocalizator()
                                                                               .getCurrency(bankTransaction.getValuePaid())
                                                                               .getTwoDecimalEuroString()));
            final Invoice invoice = bankTransaction.getInvoice();
            if (invoice != null) {
                description.add(new HtmlDefineParagraph(tr("Invoice: "), new InvoiceResourceUrl(invoice).getHtmlLink("invoice-"
                        + invoice.getInvoiceNumber() + ".pdf")));
            }
            return description;
        }

        private HtmlDiv generateChargeAccountTitle() {
            final HtmlDiv title = new HtmlDiv("title");
            title.addText(tr("Charged account"));
            return title;
        }
    }

    private static class ChargeAccountFailedLine extends HtmlTableLine {

        public ChargeAccountFailedLine(final BankTransaction bankTransaction) {
            setCssClass("failed_line");
            try {
                addCell(new EmptyCell());
                addCell(new TitleCell(bankTransaction.getCreationDate(), generateChargeAccountFailedTitle()));
                addCell(new EmptyCell());
                addCell(new MoneyCell(bankTransaction.getValue()));
            } catch (final UnauthorizedOperationException e) {
                addCell(new EmptyCell());
                addCell(new EmptyCell());
                addCell(new EmptyCell());
                addCell(new EmptyCell());
                Context.getSession().notifyWarning(Context.tr("You are not allowed to see this account informations."));
            }
        }

        private HtmlDiv generateChargeAccountFailedTitle() {
            final HtmlDiv title = new HtmlDiv("title");
            title.addText(tr("Charging account failure"));
            return title;
        }
    }

    private static class ChargeAccountAbordedLine extends HtmlTableLine {

        public ChargeAccountAbordedLine(final BankTransaction bankTransaction) {
            setCssClass("failed_line");
            try {
                addCell(new EmptyCell());
                addCell(new TitleCell(bankTransaction.getCreationDate(), generateChargeAccountFailedTitle()));
                addCell(new EmptyCell());
                addCell(new MoneyCell(bankTransaction.getValue()));
            } catch (final UnauthorizedOperationException e) {
                addCell(new EmptyCell());
                addCell(new EmptyCell());
                addCell(new EmptyCell());
                addCell(new EmptyCell());
                Context.getSession().notifyWarning(Context.tr("You are not allowed to see this account informations."));
            }
        }

        private HtmlDiv generateChargeAccountFailedTitle() {
            final HtmlDiv title = new HtmlDiv("title");
            title.addText(tr("Charging account aborted"));
            return title;
        }
    }

    private static class MoneyVariationCell extends HtmlTableCell {
        private final boolean up;

        public MoneyVariationCell(final boolean up) {
            super("money_variation_cell");
            this.up = up;
        }

        @Override
        public HtmlNode getBody() {
            if (up) {
                return new HtmlImage(new Image(WebConfiguration.getImgMoneyUpSmall()), "money up");
            }
            return new HtmlImage(new Image(WebConfiguration.getImgMoneyDownSmall()), "money down");
        }
    }

    private static class TitleCell extends HtmlTableCell {
        private final Date date;
        private final HtmlDiv title;

        public TitleCell(final Date date, final HtmlDiv title) {
            super("title_cell");
            this.date = date;
            this.title = title;
        }

        @Override
        public HtmlNode getBody() {
            final PlaceHolderElement titleCell = new PlaceHolderElement();
            final HtmlDiv dateDiv = new HtmlDiv("date");
            dateDiv.addText(Context.getLocalizator().getDate(date).toString(FormatStyle.LONG));
            titleCell.add(dateDiv);
            titleCell.add(title);
            return titleCell;
        }
    }

    private static class DescriptionCell extends HtmlTableCell {

        private final String title;
        private final HtmlDiv description;

        public DescriptionCell(final String title, final HtmlDiv description) {
            super("description_cell");
            this.title = title;
            this.description = description;
        }

        @Override
        public HtmlNode getBody() {
            final PlaceHolderElement descriptionCell = new PlaceHolderElement();
            descriptionCell.addText(title);
            descriptionCell.add(description);
            return descriptionCell;
        }
    }

    private static class MoneyCell extends HtmlTableCell {

        private final BigDecimal amount;

        public MoneyCell(final BigDecimal amount) {
            super("money");
            this.amount = amount;
        }

        @Override
        public HtmlNode getBody() {
            final HtmlDiv moneyCell = new HtmlDiv();
            String amountString = Context.getLocalizator().getCurrency(amount).getSimpleEuroString();
            if (amount.compareTo(BigDecimal.ZERO) > 0) {
                amountString = "+" + amountString;
                moneyCell.setCssClass("money_up");
            } else {
                moneyCell.setCssClass("money_down");
            }
            moneyCell.addText(amountString);
            return moneyCell;
        }
    }
}

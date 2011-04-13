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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.NotImplementedException;

import com.bloatit.framework.exceptions.highlevel.ShallNotPassException;
import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.exceptions.lowlevel.UnauthorizedOperationException;
import com.bloatit.framework.utils.Image;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.framework.utils.Sorter;
import com.bloatit.framework.utils.Sorter.Order;
import com.bloatit.framework.utils.i18n.DateLocale.FormatStyle;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlImage;
import com.bloatit.framework.webprocessor.components.HtmlSpan;
import com.bloatit.framework.webprocessor.components.HtmlTitle;
import com.bloatit.framework.webprocessor.components.PlaceHolderElement;
import com.bloatit.framework.webprocessor.components.advanced.HtmlTable;
import com.bloatit.framework.webprocessor.components.advanced.HtmlTable.HtmlLineTableModel;
import com.bloatit.framework.webprocessor.components.advanced.HtmlTable.HtmlLineTableModel.HtmlTableCell;
import com.bloatit.framework.webprocessor.components.advanced.HtmlTable.HtmlLineTableModel.HtmlTableLine;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.components.meta.HtmlMixedText;
import com.bloatit.framework.webprocessor.components.meta.XmlNode;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.BankTransaction;
import com.bloatit.model.Contribution;
import com.bloatit.model.Member;
import com.bloatit.web.WebConfiguration;
import com.bloatit.web.linkable.documentation.SideBarDocumentationBlock;
import com.bloatit.web.linkable.features.FeatureTabPane;
import com.bloatit.web.linkable.members.MemberPage;
import com.bloatit.web.linkable.softwares.SoftwaresTools;
import com.bloatit.web.pages.LoggedPage;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.pages.master.DefineParagraph;
import com.bloatit.web.pages.master.sidebar.TwoColumnLayout;
import com.bloatit.web.url.AccountPageUrl;
import com.bloatit.web.url.FeaturePageUrl;

/**
 * <p>
 * A page used to display logged member informations.
 * </p>
 */
@ParamContainer("account")
public final class AccountPage extends LoggedPage {

    private final AccountPageUrl url;

    public AccountPage(final AccountPageUrl url) {
        super(url);
        this.url = url;

    }

    @Override
    protected String createPageTitle() {
        return Context.tr("Account informations");
    }

    @Override
    public boolean isStable() {
        return true;
    }

    @Override
    public String getRefusalReason() {
        return Context.tr("You must be logged to show your account informations.");
    }

    @Override
    public void processErrors() throws RedirectException {
        // TODO we should process the errors.
    }

    @Override
    public HtmlElement createRestrictedContent(final Member loggedUser) {

        final TwoColumnLayout layout = new TwoColumnLayout(true, url);
        layout.addLeft(generateAccountSolde(loggedUser));
        layout.addLeft(new HtmlTitle(tr("Account informations"), 1));
        layout.addLeft(generateAccountMovementList(loggedUser));

        layout.addRight(new SideBarDocumentationBlock("internal_account"));
        layout.addRight(new SideBarLoadAccountBlock());
        layout.addRight(new SideBarWithdrawMoneyBlock());

        return layout;
    }

    private HtmlElement generateAccountSolde(Member loggedUser) {
        HtmlDiv floatRight = new HtmlDiv("float_right");

        HtmlDiv soldeBlock = new HtmlDiv("solde_block");
        HtmlDiv soldeText = new HtmlDiv("solde_text");
        soldeText.addText(tr("You currently have "));
        HtmlDiv soldeAmount = new HtmlDiv("solde_amount");

        try {
            soldeAmount.addText(Context.getLocalizator().getCurrency(loggedUser.getInternalAccount().getAmount()).getDefaultString());
        } catch (UnauthorizedOperationException e) {
            throw new ShallNotPassException("Right fail ton account page", e);
        }

        soldeBlock.add(soldeText);
        soldeBlock.add(soldeAmount);
        floatRight.add(soldeBlock);
        return floatRight;
    }

    private HtmlElement generateAccountMovementList(Member loggedUser) {

        List<HtmlTableLine> lineList = new ArrayList<HtmlTableLine>();
        Sorter<HtmlTableLine, Date> sorter = new Sorter<HtmlTableLine, Date>(lineList);

        try {

            PageIterable<Contribution> contributions = loggedUser.getContributions(true);

            PageIterable<BankTransaction> bankTransactions = loggedUser.getBankTransactions();

            for (Contribution contribution : contributions) {
                sorter.add(new ContributionLine(contribution), contribution.getCreationDate());

            }

            for (BankTransaction bankTransaction : bankTransactions) {
                if (bankTransaction.getValue().compareTo(BigDecimal.ZERO) > 0) {
                    sorter.add(new ChargeAccountLine(bankTransaction), bankTransaction.getCreationDate());
                } else {
                    // TODO withdraw
                    throw new NotImplementedException();
                }

            }

        } catch (UnauthorizedOperationException e) {
            throw new ShallNotPassException("Right fail ton account page", e);
        }

        sorter.performSort(Order.DESC);

        HtmlLineTableModel model = new HtmlLineTableModel();

        for (HtmlTableLine line : lineList) {
            model.addLine(line);
        }

        HtmlTable table = new HtmlTable(model);

        return table;

    }

    private static class ContributionLine extends HtmlTableLine {

        private final Contribution contribution;

        public ContributionLine(Contribution contribution) throws UnauthorizedOperationException {
            this.contribution = contribution;
            addCell(new MoneyVariationCell(false));
            addCell(new TitleCell(contribution.getCreationDate(), generateContributionTitle()));
            addCell(new DescriptionCell(tr("Feature summary"), generateContributionDescription()));
            addCell(new MoneyCell(contribution.getAmount().negate()));
        }

        private HtmlDiv generateContributionDescription() throws UnauthorizedOperationException {
            HtmlDiv description = new HtmlDiv("description");

            HtmlSpan softwareLink = SoftwaresTools.getSoftwareLink(contribution.getFeature().getSoftware());

            HtmlMixedText descriptionString = new HtmlMixedText(contribution.getFeature().getTitle() + " (<0::>)", softwareLink);

            String statusString = "";

            switch (contribution.getFeature().getFeatureState()) {
                case DEVELOPPING:
                    statusString = tr("In developement");
                    break;
                case FINISHED:
                    statusString = tr("Success");
                case DISCARDED:
                    statusString = tr("Failed");
                case PENDING:
                case PREPARING:
                    statusString = tr("Funding");
                    break;
            }

            description.add(new DefineParagraph(tr("Description: "), descriptionString));
            description.add(new DefineParagraph(tr("Status: "), statusString));

            return description;
        }

        private HtmlDiv generateContributionTitle() {
            HtmlDiv title = new HtmlDiv("title");
            FeaturePageUrl featurePageUrl = new FeaturePageUrl(contribution.getFeature());
            featurePageUrl.getFeatureTabPaneUrl().setActiveTabKey(FeatureTabPane.CONTRIBUTIONS_TAB);
            featurePageUrl.setAnchor(FeatureTabPane.FEATURE_TAB_PANE);
            title.add(new HtmlMixedText(tr("Contributed to a <0::feature>"), featurePageUrl.getHtmlLink()));
            return title;
        }

    }

    private static class ChargeAccountLine extends HtmlTableLine {

        private final BankTransaction bankTransaction;

        public ChargeAccountLine(BankTransaction bankTransaction) {
            this.bankTransaction = bankTransaction;
            addCell(new MoneyVariationCell(true));
            addCell(new TitleCell(bankTransaction.getCreationDate(), generateChargeAccountTitle()));
            addCell(new DescriptionCell(tr("Account loading summary"), generateChargeAccountDescription()));
            addCell(new MoneyCell(bankTransaction.getValue()));
        }

        private HtmlDiv generateChargeAccountDescription() {
            HtmlDiv description = new HtmlDiv("description");
            description.add(new DefineParagraph(tr("Total cost: "), Context.getLocalizator()
                                                                           .getCurrency(bankTransaction.getValuePaid())
                                                                           .getDecimalDefaultString()));
            return description;
        }

        private HtmlDiv generateChargeAccountTitle() {
            HtmlDiv title = new HtmlDiv("title");
            title.addText(tr("Charged account"));
            return title;
        }
    }

    private static class MoneyVariationCell extends HtmlTableCell {

        private final boolean up;

        public MoneyVariationCell(boolean up) {
            super("money_variation_cell");
            this.up = up;
        }

        @Override
        public XmlNode getBody() {
            if (up) {
                return new HtmlImage(new Image(WebConfiguration.getImgMoneyUpSmall()), "money up");
            }

            return new HtmlImage(new Image(WebConfiguration.getImgMoneyDownSmall()), "money down");
        }

    }

    private static class TitleCell extends HtmlTableCell {

        private final Date date;
        private final HtmlDiv title;

        public TitleCell(Date date, HtmlDiv title) {
            super("title_cell");
            this.date = date;
            this.title = title;

        }

        @Override
        public XmlNode getBody() {
            PlaceHolderElement titleCell = new PlaceHolderElement();

            HtmlDiv dateDiv = new HtmlDiv("date");
            dateDiv.addText(Context.getLocalizator().getDate(date).toString(FormatStyle.LONG));
            titleCell.add(dateDiv);
            titleCell.add(title);
            return titleCell;
        }

    }

    private static class DescriptionCell extends HtmlTableCell {

        private final String title;
        private final HtmlDiv description;

        public DescriptionCell(String title, HtmlDiv description) {
            super("description_cell");
            this.title = title;
            this.description = description;
        }

        @Override
        public XmlNode getBody() {
            PlaceHolderElement descriptionCell = new PlaceHolderElement();
            descriptionCell.addText(title);
            descriptionCell.add(description);
            return descriptionCell;
        }

    }

    private static class MoneyCell extends HtmlTableCell {

        private final BigDecimal amount;

        public MoneyCell(BigDecimal amount) {
            super("money");
            this.amount = amount;
        }

        @Override
        public XmlNode getBody() {
            HtmlDiv moneyCell = new HtmlDiv();

            String amountString = Context.getLocalizator().getCurrency(amount).getDefaultString();

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

    @Override
    protected Breadcrumb createBreadcrumb() {
        return AccountPage.generateBreadcrumb(session.getAuthToken().getMember());
    }

    public static Breadcrumb generateBreadcrumb(Member loggerUser) {
        final Breadcrumb breadcrumb = MemberPage.generateBreadcrumb(loggerUser);

        breadcrumb.pushLink(new AccountPageUrl().getHtmlLink(tr("Account informations")));

        return breadcrumb;
    }

}

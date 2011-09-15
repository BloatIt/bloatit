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
package com.bloatit.web.linkable.contribution;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import java.math.BigDecimal;

import com.bloatit.framework.exceptions.highlevel.ShallNotPassException;
import com.bloatit.framework.utils.i18n.Localizator;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlImage;
import com.bloatit.framework.webprocessor.components.advanced.HtmlTable.HtmlLineTableModel.HtmlTableCell;
import com.bloatit.framework.webprocessor.components.advanced.HtmlTable.HtmlLineTableModel.HtmlTableLine;
import com.bloatit.framework.webprocessor.components.form.HtmlForm;
import com.bloatit.framework.webprocessor.components.form.HtmlForm.Method;
import com.bloatit.framework.webprocessor.components.form.HtmlMoneyField;
import com.bloatit.framework.webprocessor.components.form.HtmlSubmit;
import com.bloatit.framework.webprocessor.components.meta.HtmlText;
import com.bloatit.framework.webprocessor.components.meta.HtmlNode;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.Actor;
import com.bloatit.model.Image;
import com.bloatit.model.right.UnauthorizedOperationException;
import com.bloatit.web.WebConfiguration;
import com.bloatit.web.linkable.members.MembersTools;

public class HtmlChargeAccountLine extends HtmlTableLine {

    private final Actor<?> actor;
    private final Localizator localizator;
    private final BigDecimal amountToCharge;
    private final Url recalculateTargetForm;
    private final boolean isContributing;
    private HtmlMoneyField moneyField;

    public HtmlChargeAccountLine(final boolean isContributing, final BigDecimal amountToCharge, final Actor<?> actor, final Url recalculateTargetForm) {
        this.isContributing = isContributing;
        this.amountToCharge = amountToCharge;
        this.actor = actor;
        this.recalculateTargetForm = recalculateTargetForm;
        setCssClass("quotation_detail_line");
        localizator = Context.getLocalizator();

        addCell(new MemberAvatarCell());
        addCell(new BeforeMoneyCell());
        addCell(new MoneyImageCell());
        addCell(new AfterMoneyCell());

        addCell(new CategorieCell());
        addCell(new DescriptionCell());
        addCell(new AmountCell());
    }

    private class MemberAvatarCell extends HtmlTableCell {

        public MemberAvatarCell() {
            super("");
        }

        @Override
        public HtmlNode getBody() {
            return MembersTools.getMemberAvatarSmall(actor);
        }
    }

    private class BeforeMoneyCell extends HtmlTableCell {

        public BeforeMoneyCell() {
            super("quotation_detail_line_money");
            setId("input_money");
        }

        @Override
        public HtmlNode getBody() {
            try {
                BigDecimal initialAmount;
                if (isContributing) {
                    initialAmount = BigDecimal.ZERO;
                } else {
                    initialAmount = actor.getInternalAccount().getAmount();
                }

                return new HtmlText(localizator.getCurrency(initialAmount).getSimpleEuroString());
            } catch (final UnauthorizedOperationException e) {
                throw new ShallNotPassException("Error getting internal account", e);
            }
        }
    }

    private class AfterMoneyCell extends HtmlTableCell {

        public AfterMoneyCell() {
            super("quotation_detail_line_money");
            setId("output_money");
        }

        @Override
        public HtmlNode getBody() {

            try {
                BigDecimal initialAmount;
                if (isContributing) {
                    initialAmount = BigDecimal.ZERO;
                } else {
                    initialAmount = actor.getInternalAccount().getAmount();
                }
                return new HtmlText(localizator.getCurrency(initialAmount.add(amountToCharge)).getSimpleEuroString());
            } catch (final UnauthorizedOperationException e) {
                throw new ShallNotPassException("Error getting internal account", e);
            }
        }
    }

    private class MoneyImageCell extends HtmlTableCell {

        public MoneyImageCell() {
            super("quotation_detail_line_money_image");
        }

        @Override
        public HtmlNode getBody() {
            return new HtmlImage(new Image(WebConfiguration.getImgMoneyUpSmall()), "money up");
        }
    }

    private class CategorieCell extends HtmlTableCell {

        public CategorieCell() {
            super("quotation_detail_line_categorie");
        }

        @Override
        public HtmlNode getBody() {
            final HtmlDiv internalAccount = new HtmlDiv("title");
            internalAccount.addText(tr("Internal account"));
            final HtmlDiv internalAccountDetails = new HtmlDiv("details");
            internalAccountDetails.addText(actor.getDisplayName());

            return new HtmlDiv("").add(internalAccount).add(internalAccountDetails);
        }
    }

    private class DescriptionCell extends HtmlTableCell {

        public DescriptionCell() {
            super("quotation_detail_line_description");
        }

        @Override
        public HtmlNode getBody() {
            return new HtmlDiv("").addText(tr("Load money in your internal account for future contributions."));
        }
    }

    private class AmountCell extends HtmlTableCell {

        public AmountCell() {
            super("quotation_detail_line_amount");
        }

        @Override
        public HtmlNode getBody() {

            final HtmlDiv amountBlock;
            if (recalculateTargetForm == null) {
                amountBlock = new HtmlDiv("quotation_detail_line_amount");
                amountBlock.add(new HtmlDiv("quotation_detail_line_amount_money").addText(localizator.getCurrency(amountToCharge)
                                                                                                     .getTwoDecimalEuroString()));
            } else {
                amountBlock = new HtmlDiv("quotation_detail_line_field");
                final HtmlForm form = new HtmlForm(recalculateTargetForm.urlString(), Method.POST);

                moneyField = new HtmlMoneyField("preload");
                if (amountToCharge == null) {
                    moneyField.setDefaultValue("0");
                } else {
                    moneyField.setDefaultValue(amountToCharge.toPlainString());
                }

                final HtmlSubmit recalculate = new HtmlSubmit(tr("recalculate"));

                form.add(moneyField);
                form.add(recalculate);
                amountBlock.add(form);
            }
            return amountBlock;
        }
    }

    public HtmlMoneyField getMoneyField() {
        return moneyField;
    }

}

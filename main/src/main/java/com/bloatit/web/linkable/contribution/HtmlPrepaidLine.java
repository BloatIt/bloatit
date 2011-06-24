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
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlImage;
import com.bloatit.framework.webprocessor.components.advanced.HtmlTable.HtmlLineTableModel.HtmlTableCell;
import com.bloatit.framework.webprocessor.components.advanced.HtmlTable.HtmlLineTableModel.HtmlTableLine;
import com.bloatit.framework.webprocessor.components.meta.HtmlText;
import com.bloatit.framework.webprocessor.components.meta.XmlNode;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.Actor;
import com.bloatit.model.Image;
import com.bloatit.model.right.UnauthorizedOperationException;
import com.bloatit.web.WebConfiguration;
import com.bloatit.web.linkable.members.MembersTools;

public class HtmlPrepaidLine extends HtmlTableLine {

    private final Actor<?> actor;

    protected HtmlPrepaidLine(final Actor<?> actor) {
        this.actor = actor;
        setCssClass("quotation_detail_line");

        addCell(new MemberAvatarCell());
        addCell(new BeforeMoneyCell());
        addCell(new MoneyImageCell());
        addCell(new AfterMoneyCell());
        addCell(new CategorieCell());
        addCell(new AmountCell());
    }

    private class MemberAvatarCell extends HtmlTableCell {

        public MemberAvatarCell() {
            super("");
        }

        @Override
        public XmlNode getBody() {
            return MembersTools.getMemberAvatarSmall(actor);
        }
    }

    private class BeforeMoneyCell extends HtmlTableCell {

        public BeforeMoneyCell() {
            super("quotation_detail_line_money");
        }

        @Override
        public XmlNode getBody() {
            try {
                return new HtmlText(Context.getLocalizator().getCurrency(actor.getInternalAccount().getAmount()).getSimpleEuroString());
            } catch (final UnauthorizedOperationException e) {
                throw new ShallNotPassException("Fail to get a account amount", e);
            }
        }
    }

    private class AfterMoneyCell extends HtmlTableCell {

        public AfterMoneyCell() {
            super("quotation_detail_line_money");
        }

        @Override
        public XmlNode getBody() {
            return new HtmlText(Context.getLocalizator().getCurrency(BigDecimal.ZERO).getSimpleEuroString());
        }
    }

    private class MoneyImageCell extends HtmlTableCell {

        public MoneyImageCell() {
            super("quotation_detail_line_money_image");
        }

        @Override
        public XmlNode getBody() {
            return new HtmlImage(new Image(WebConfiguration.getImgMoneyDownSmall()), "money up");
        }
    }

    private class CategorieCell extends HtmlTableCell {

        public CategorieCell() {
            super("quotation_detail_line_categorie");
        }

        @Override
        public XmlNode getBody() {

            final HtmlDiv prepaidAccount = new HtmlDiv("title");
            prepaidAccount.addText(tr("Prepaid from internal account"));
            final HtmlDiv prepaidAccountDetails = new HtmlDiv("details");
            prepaidAccountDetails.addText(actor.getDisplayName());

            return new HtmlDiv("").add(prepaidAccount).add(prepaidAccountDetails);
        }

        @Override
        public int getColspan() {
            return 2;
        }

    }

    private class AmountCell extends HtmlTableCell {

        public AmountCell() {
            super("quotation_detail_line_amount");
        }

        @Override
        public XmlNode getBody() {

            try {
                return new HtmlDiv("quotation_detail_line_amount_money").addText(Context.getLocalizator()
                                                                                        .getCurrency(actor.getInternalAccount().getAmount().negate())
                                                                                        .getTwoDecimalEuroString());
            } catch (final UnauthorizedOperationException e) {
                throw new ShallNotPassException("Fail to get a account amount", e);
            }
        }
    }

}

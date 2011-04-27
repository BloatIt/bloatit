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
import com.bloatit.framework.exceptions.lowlevel.UnauthorizedOperationException;
import com.bloatit.framework.utils.Image;
import com.bloatit.framework.utils.i18n.Localizator;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlImage;
import com.bloatit.framework.webprocessor.components.form.HtmlForm;
import com.bloatit.framework.webprocessor.components.form.HtmlForm.Method;
import com.bloatit.framework.webprocessor.components.form.HtmlMoneyField;
import com.bloatit.framework.webprocessor.components.form.HtmlSubmit;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.Actor;
import com.bloatit.web.WebConfiguration;
import com.bloatit.web.linkable.members.MembersTools;

public class HtmlChargeAccountLine extends HtmlDiv {

    public HtmlChargeAccountLine(final BigDecimal amountToCharge, final Actor<?> actor, final Url recalculateTargetForm) {
        super("quotation_detail_line");
        final Localizator localizator = Context.getLocalizator();

        BigDecimal initialAmount;
        try {
            initialAmount = actor.getInternalAccount().getAmount();
        } catch (UnauthorizedOperationException e) {
            throw new ShallNotPassException("Error getting internal account", e);
        }

        add(MembersTools.getMemberAvatarSmall(actor));
        add(new HtmlDiv("quotation_detail_line_money").addText(localizator.getCurrency(initialAmount).getSimpleEuroString()));
        add(new HtmlDiv("quotation_detail_line_money_image").add(new HtmlImage(new Image(WebConfiguration.getImgMoneyUpSmall()), "money up")));
        add(new HtmlDiv("quotation_detail_line_money").addText(localizator.getCurrency(initialAmount.add(amountToCharge)).getSimpleEuroString()));

        add(new HtmlDiv("quotation_detail_line_categorie").addText(tr("Internal account")));
        add(new HtmlDiv("quotation_detail_line_description").addText(tr("Load money in your internal account for future contributions.")));

        final HtmlDiv amountBlock;
        if (recalculateTargetForm == null) {
            amountBlock = new HtmlDiv("quotation_detail_line_amount");
            amountBlock.add(new HtmlDiv("quotation_detail_line_amount_money").addText(localizator.getCurrency(amountToCharge)
                                                                                                 .getTwoDecimalEuroString()));
        } else {
            amountBlock = new HtmlDiv("quotation_detail_line_field");
            final HtmlForm form = new HtmlForm(recalculateTargetForm.urlString(), Method.GET);

            final HtmlMoneyField moneyField = new HtmlMoneyField("preload");
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
        add(amountBlock);
    }
}

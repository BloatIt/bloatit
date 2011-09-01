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

import com.bloatit.framework.bank.MercanetAPI.PaymentMethod;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlImage;
import com.bloatit.framework.webprocessor.components.HtmlParagraph;
import com.bloatit.framework.webprocessor.components.form.HtmlForm;
import com.bloatit.framework.webprocessor.components.form.HtmlRadioButtonGroup;
import com.bloatit.framework.webprocessor.components.form.HtmlSubmit;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.framework.webprocessor.url.UrlParameter;
import com.bloatit.model.Image;
import com.bloatit.model.Team;
import com.bloatit.web.WebConfiguration;
import com.bloatit.web.url.PaymentProcessUrl;

public class HtmlPayBlock extends HtmlDiv {
    public HtmlPayBlock(final StandardQuotation quotation, final Team team, final PaymentProcessUrl paymentUrl, final Url returnUrl) {
        super("pay_actions");
        
        
        
        HtmlForm form = new HtmlForm(paymentUrl.urlString());
        
        UrlParameter<PaymentMethod,PaymentMethod> paymentMethodParameter = paymentUrl.getPaymentMethodParameter();
        HtmlRadioButtonGroup paymentMethodRadioButtonGroup = new HtmlRadioButtonGroup(paymentMethodParameter.getName());
        paymentMethodRadioButtonGroup.setDefaultValue(paymentMethodParameter.getDefaultValue());
        
        
        HtmlImage logoVISA = new HtmlImage(new Image(WebConfiguration.getImgMercanetVISA()), "VISA");
        HtmlImage logoMastercard = new HtmlImage(new Image(WebConfiguration.getImgMercanetMastercard()), "Mastercard");
        HtmlImage logoCB = new HtmlImage(new Image(WebConfiguration.getImgMercanetCB()), "CB");
        
        
        HtmlDiv paymentMethodBlock = new HtmlDiv("payment_methods");
        paymentMethodBlock.add(new HtmlParagraph(Context.tr("Choose your payment method:")));
        paymentMethodBlock.add(paymentMethodRadioButtonGroup);
        
        paymentMethodRadioButtonGroup.addRadioButton("VISA", logoVISA);
        paymentMethodRadioButtonGroup.addRadioButton("MASTERCARD", logoMastercard);
        paymentMethodRadioButtonGroup.addRadioButton("CB", logoCB);
        form.add(paymentMethodBlock);
        
        if (team != null) {
            add(new HtmlParagraph(Context.tr("You are using the account of ''{0}'' team.", team.getLogin()), "use_account"));
        }
        final HtmlSubmit payContributionButton = new HtmlSubmit(tr("Pay {0}", Context.getLocalizator()
                                                                       .getCurrency(quotation.totalTTC.getValue())
                                                                       .getTwoDecimalEuroString()));
        
        form.add(payContributionButton);
        form.add(returnUrl.getHtmlLink(Context.tr("edit")));
        
        
        
        add(form);
    }
}

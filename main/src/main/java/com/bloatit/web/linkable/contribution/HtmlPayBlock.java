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

import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlLink;
import com.bloatit.framework.webprocessor.components.HtmlParagraph;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.Team;

public class HtmlPayBlock extends HtmlDiv {
    public HtmlPayBlock(final StandardQuotation quotation, final Team team, final Url paymentUrl, final Url returnUrl) {
        super("pay_actions");
        final HtmlLink payContributionLink = paymentUrl.getHtmlLink(tr("Pay {0}", Context.getLocalizator()
                                                                                         .getCurrency(quotation.totalTTC.getValue())
                                                                                         .getTwoDecimalEuroString()));
        payContributionLink.setCssClass("button");

        add(new HtmlParagraph(Context.tr("You are using a beta version. Payment with real money is not activated."), "debug")).add(new HtmlParagraph(Context.tr("You can simulate it using this card number: 4970100000325734, and the security number: 123."),
                                                                                                                                                     "debug"));
        if (team != null) {
            add(new HtmlParagraph(Context.tr("You are using the account of ''{0}'' team.", team.getLogin()), "use_account"));
        }
        add(returnUrl.getHtmlLink(Context.tr("edit")));
        add(payContributionLink);
    }
}

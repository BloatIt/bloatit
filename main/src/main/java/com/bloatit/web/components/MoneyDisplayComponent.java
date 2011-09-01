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

import java.math.BigDecimal;

import com.bloatit.framework.utils.i18n.CurrencyLocale;
import com.bloatit.framework.webprocessor.components.HtmlSpan;
import com.bloatit.framework.webprocessor.components.meta.HtmlBranch;
import com.bloatit.framework.webprocessor.components.meta.HtmlText;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.Member;
import com.bloatit.model.Team;
import com.bloatit.web.linkable.members.MemberPage;
import com.bloatit.web.linkable.team.TeamPage;

/**
 * Small component that displays an amount of money in euro with and in locale
 * currency
 */
public class MoneyDisplayComponent extends HtmlSpan {
    
    
    /**
     * Creates a money display component
     * 
     * @param amount the amount of money to display (in euro)
     */
    public MoneyDisplayComponent(final BigDecimal amount) {
        this(amount, false, null);
    }
    
    /**
     * Creates a money display component with a link to user account page
     * 
     * @param amount the amount of money to display (in euro)
     * @param me
     */
    public MoneyDisplayComponent(final BigDecimal amount, final Member me) {
        this(amount, true, me);
    }

    /**
     * Creates a money display component with or without a link to user account
     * page
     * 
     * @param amount the amount of money to display (in euro)
     * @param link <i>true</i> if the component should link to the user account
     * @param me
     */
    public MoneyDisplayComponent(final BigDecimal amount, final boolean link, final Member me) {
        this(amount, link, null, me);
    }

    /**
     * Creates a money display component with or without a link to user or team
     * account page
     * 
     * @param amount the amount of money to display (in euro)
     * @param link <i>true</i> if the component should link to the user account
     * @param teamAccount if not null, the link will point to the account page
     *            of the team page, <i>false</i> otherwise.
     * @param me
     */
    public MoneyDisplayComponent(final BigDecimal amount, final boolean link, final Team teamAccount, final Member me) {
        super();

        // Display user money in euro
        final HtmlBranch euroMoney = new HtmlSpan();
        euroMoney.setCssClass("euro_money");

        final CurrencyLocale cl = Context.getLocalizator().getCurrency(amount);
        euroMoney.add(new HtmlText(cl.getSimpleEuroString()));

        HtmlBranch money;
        if (link) {
            if (teamAccount != null) {
                money = TeamPage.AccountUrl(teamAccount).getHtmlLink(euroMoney);
            } else {
                money = MemberPage.myAccountUrl(me).getHtmlLink(euroMoney);
            }

            money.setCssClass("money");
        } else {
            money = new HtmlSpan("money").add(euroMoney);
        }

        // Display user money in locale money (when needed)
        if (!cl.isDefaultCurrency()) {
            final HtmlBranch localeMoney = new HtmlSpan();
            localeMoney.setCssClass("locale_money");

            localeMoney.addText(cl.getLocaleString());
            money.add(localeMoney);
        }
        add(money);
    }
}

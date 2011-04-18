package com.bloatit.web.components;

import java.math.BigDecimal;

import com.bloatit.framework.utils.i18n.CurrencyLocale;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlSpan;
import com.bloatit.framework.webprocessor.components.meta.HtmlBranch;
import com.bloatit.framework.webprocessor.components.meta.HtmlText;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.web.url.AccountPageUrl;

/**
 * Small component that displays an amount of money in euro with and in locale
 * currency
 */
public class MoneyDisplayComponent extends HtmlSpan {
    /**
     * Creates a money display component with a link to user account page
     * 
     * @param amount the amount of money to display (in euro)
     */
    public MoneyDisplayComponent(final BigDecimal amount) {
        this(amount, true);
    }

    /**
     * Creates a money display component with or without a link to user account
     * page
     * 
     * @param amount the amount of money to display (in euro)
     * @param link <i>true</i> if the component should link to the user account
     *            page, <i>false</i> otherwise.
     */
    public MoneyDisplayComponent(final BigDecimal amount, boolean link) {
        super();

        // Display user money in euro
        final HtmlBranch euroMoney = new HtmlSpan();
        euroMoney.setCssClass("euro_money");

        final CurrencyLocale cl = Context.getLocalizator().getCurrency(amount);
        euroMoney.add(new HtmlText(cl.getDefaultString()));

        HtmlBranch money;
        if (link) {
            money = new AccountPageUrl().getHtmlLink(euroMoney);
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

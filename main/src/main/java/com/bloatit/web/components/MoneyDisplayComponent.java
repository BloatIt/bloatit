package com.bloatit.web.components;

import java.math.BigDecimal;

import com.bloatit.framework.utils.i18n.CurrencyLocale;
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
    public MoneyDisplayComponent(final BigDecimal amount) {
        super();

        // Display user money in euro
        final HtmlBranch euroMoney = new HtmlSpan();
        euroMoney.setCssClass("euro_money");

        final CurrencyLocale cl = Context.getLocalizator().getCurrency(amount);
        euroMoney.add(new HtmlText(cl.getDefaultString()));

        final HtmlBranch money = new AccountPageUrl().getHtmlLink(euroMoney);
        money.setCssClass("money");

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

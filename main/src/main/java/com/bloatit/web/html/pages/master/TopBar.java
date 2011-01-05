package com.bloatit.web.html.pages.master;

import com.bloatit.framework.InternalAccount;
import com.bloatit.framework.Member;
import com.bloatit.web.html.HtmlBranch;
import com.bloatit.web.html.HtmlText;
import com.bloatit.web.html.HtmlTools;
import com.bloatit.web.html.components.standard.HtmlDiv;
import com.bloatit.web.html.components.standard.HtmlGenericElement;
import com.bloatit.web.html.components.standard.HtmlLink;
import com.bloatit.web.server.Context;
import com.bloatit.web.server.Session;
import com.bloatit.web.utils.i18n.CurrencyLocale;
import com.bloatit.web.utils.url.AccountChargingPageUrl;
import com.bloatit.web.utils.url.LoginPageUrl;
import com.bloatit.web.utils.url.LogoutActionUrl;
import com.bloatit.web.utils.url.MyAccountPageUrl;

public class TopBar extends HtmlDiv {

    protected TopBar() {
        super();

        setId("top_bar");

        final Session session = Context.getSession();
        if (session.isLogged()) {
            // Display user name
            final String full_name = session.getAuthToken().getMember().getFullname();
            final HtmlLink memberLink = new MyAccountPageUrl().getHtmlLink(full_name);

            // Display user karma
            final HtmlBranch karma = new HtmlGenericElement("span");
            karma.setCssClass("karma");
            karma.addText(HtmlTools.compressKarma(session.getAuthToken().getMember().getKarma()));


            
            

            // Display user money in euro
            final HtmlBranch euroMoney = new HtmlGenericElement("span");
            euroMoney.setCssClass("euro_money");

            final Member member = session.getAuthToken().getMember();
            member.authenticate(session.getAuthToken());
            final InternalAccount internalAccount = member.getInternalAccount();
            internalAccount.authenticate(session.getAuthToken());
            
            if(!CurrencyLocale.availableCurrency(Context.getLocalizator().getLocale())){
            	
            }
            CurrencyLocale cl = new CurrencyLocale(internalAccount.getAmount(), Context.getLocalizator().getLocale());
            euroMoney.add(new HtmlText(cl.getDefaultString()));

            final HtmlBranch money = new AccountChargingPageUrl().getHtmlLink(euroMoney);
            money.setCssClass("money");

            // Display user money in locale money (when needed)
            if (cl.availableTargetCurrency() && !cl.getDefaultString().equals(cl.getLocaleString())) {
                final HtmlBranch localeMoney = new HtmlGenericElement("span");
                localeMoney.setCssClass("locale_money");

                localeMoney.addText(cl.getLocaleString());
                money.add(localeMoney);
            }


            // Display logout link
            final HtmlLink logoutLink = new LogoutActionUrl().getHtmlLink(Context.tr("Logout"));

            // Add all previously created components
            add(new HtmlGenericElement("span").setCssClass("top_bar_component").add(memberLink).add(karma));
            add(new HtmlGenericElement("span").setCssClass("top_bar_component").add(money));
            add(new HtmlGenericElement("span").setCssClass("top_bar_component").add(logoutLink));
        } else {
            final HtmlLink loginLink = new LoginPageUrl().getHtmlLink(Context.tr("Login / Signup"));
            add(new HtmlGenericElement("span").setCssClass("top_bar_component").add(loginLink));
        }
    }
}

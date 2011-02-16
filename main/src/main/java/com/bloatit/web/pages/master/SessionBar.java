package com.bloatit.web.pages.master;

import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.framework.utils.i18n.CurrencyLocale;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.Session;
import com.bloatit.framework.webserver.components.HtmlDiv;
import com.bloatit.framework.webserver.components.HtmlLink;
import com.bloatit.framework.webserver.components.HtmlSpan;
import com.bloatit.framework.webserver.components.meta.HtmlBranch;
import com.bloatit.framework.webserver.components.meta.HtmlText;
import com.bloatit.model.InternalAccount;
import com.bloatit.model.Member;
import com.bloatit.web.HtmlTools;
import com.bloatit.web.url.AccountChargingPageUrl;
import com.bloatit.web.url.LoginPageUrl;
import com.bloatit.web.url.LogoutActionUrl;
import com.bloatit.web.url.MemberPageUrl;
import com.bloatit.web.url.MessageListPageUrl;
import com.bloatit.web.url.RegisterPageUrl;

public class SessionBar extends HtmlDiv {

    private static final String SESSION_BAR_COMPONENT_CSS_CLASS = "session_bar_component";

    protected SessionBar() {
        super();

        setId("session_bar");

        final Session session = Context.getSession();
        if (session.isLogged()) {
            // Display user name
            String displayName = "John Doe";
            try {
                displayName = session.getAuthToken().getMember().getDisplayName();
            } catch (final UnauthorizedOperationException e) {
                // no right, leave invalid name
            }
            final HtmlLink memberLink = new MemberPageUrl(Context.getSession().getAuthToken().getMember()).getHtmlLink(displayName);

            // Display user karma
            final HtmlBranch karma = new HtmlSpan();
            karma.setCssClass("karma");
            try {
                karma.addText(HtmlTools.compressKarma(session.getAuthToken().getMember().getKarma()));
            } catch (final UnauthorizedOperationException e) {
                // No right, no display the karma
            }
            add(new HtmlSpan().setCssClass(SESSION_BAR_COMPONENT_CSS_CLASS).add(memberLink).add(karma));

            // Display user money in euro
            final HtmlBranch euroMoney = new HtmlSpan();
            euroMoney.setCssClass("euro_money");

            final Member member = session.getAuthToken().getMember();
            member.authenticate(session.getAuthToken());
            InternalAccount internalAccount;
            try {
                internalAccount = member.getInternalAccount();
                internalAccount.authenticate(session.getAuthToken());
                final CurrencyLocale cl = Context.getLocalizator().getCurrency(internalAccount.getAmount());
                euroMoney.add(new HtmlText(cl.getDefaultString()));

                final HtmlBranch money = new AccountChargingPageUrl().getHtmlLink(euroMoney);
                money.setCssClass("money");

                // Display user money in locale money (when needed)
                if (cl.availableTargetCurrency() && !cl.getDefaultString().equals(cl.getLocaleString())) {
                    final HtmlBranch localeMoney = new HtmlSpan();
                    localeMoney.setCssClass("locale_money");

                    localeMoney.addText(cl.getLocaleString());
                    money.add(localeMoney);
                }
                add(new HtmlSpan().setCssClass(SESSION_BAR_COMPONENT_CSS_CLASS).add(money));
            } catch (final UnauthorizedOperationException e) {
                // no right, no money displayed
            }

            // Display link to private messages
            final HtmlLink messagesLink = new MessageListPageUrl().getHtmlLink("Messages");
            add(new HtmlSpan().setCssClass(SESSION_BAR_COMPONENT_CSS_CLASS).add(messagesLink));

            // Display logout link
            final HtmlLink logoutLink = new LogoutActionUrl().getHtmlLink(Context.tr("Logout"));
            add(new HtmlSpan().setCssClass(SESSION_BAR_COMPONENT_CSS_CLASS).add(logoutLink));

        } else {
            final HtmlLink loginLink = new LoginPageUrl().getHtmlLink(Context.trc("Login (verb)", "Login"));
            final HtmlLink signupLink = new RegisterPageUrl().getHtmlLink(Context.trc("Sign in (verb)", "Sign in"));
            add(new HtmlSpan().setCssClass(SESSION_BAR_COMPONENT_CSS_CLASS).add(loginLink));
            add(new HtmlSpan().setCssClass(SESSION_BAR_COMPONENT_CSS_CLASS).add(signupLink));
        }
    }
}

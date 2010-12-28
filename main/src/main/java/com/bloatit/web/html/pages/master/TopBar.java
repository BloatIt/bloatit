package com.bloatit.web.html.pages.master;

import com.bloatit.framework.InternalAccount;
import com.bloatit.framework.Member;
import com.bloatit.web.html.HtmlBranch;
import com.bloatit.web.html.HtmlElement;
import com.bloatit.web.html.HtmlText;
import com.bloatit.web.html.HtmlTools;
import com.bloatit.web.html.components.standard.HtmlDiv;
import com.bloatit.web.html.components.standard.HtmlGenericElement;
import com.bloatit.web.html.components.standard.HtmlLink;
import com.bloatit.web.server.Context;
import com.bloatit.web.server.Session;
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

            // Display user money
            final HtmlBranch money = new HtmlGenericElement("span");
            final Member member = session.getAuthToken().getMember();
            member.authenticate(session.getAuthToken());
            final InternalAccount internalAccount = member.getInternalAccount();
            internalAccount.authenticate(session.getAuthToken());
            money.add(new HtmlText(internalAccount.getAmount().toString()));

            // Display logout link
            final HtmlLink logoutLink = new LogoutActionUrl().getHtmlLink(session.tr("Logout"));

            // Add all previously created components
            add(new HtmlGenericElement("span").setCssClass("top_bar_component").add(memberLink).add(karma));
            add(new HtmlGenericElement("span").setCssClass("top_bar_component").add(money));
            add(new HtmlGenericElement("span").setCssClass("top_bar_component").add(logoutLink));
        } else {
            final HtmlLink loginLink = new LoginPageUrl().getHtmlLink(session.tr("Login / Signup"));
            add(new HtmlGenericElement("span").setCssClass("top_bar_component").add(loginLink));
        }
    }
}

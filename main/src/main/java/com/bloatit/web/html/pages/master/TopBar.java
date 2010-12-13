package com.bloatit.web.html.pages.master;


import com.bloatit.web.actions.LogoutAction;
import com.bloatit.web.html.HtmlTools;
import com.bloatit.web.html.components.standard.HtmlDiv;
import com.bloatit.web.html.components.standard.HtmlGenericElement;
import com.bloatit.web.html.components.standard.HtmlLink;
import com.bloatit.web.html.pages.LoginPage;
import com.bloatit.web.html.pages.MyAccountPage;
import com.bloatit.web.server.Context;
import com.bloatit.web.server.Session;
import com.bloatit.web.utils.url.UrlBuilder;

public class TopBar extends HtmlDiv {

    protected TopBar() {
        super();

        setId("top_bar");

        final Session session = Context.getSession();
        if (session.isLogged()) {
            final String full_name = session.getAuthToken().getMember().getFullname();
            final String karma = "<span class=\"karma\">" + HtmlTools.compressKarma(session.getAuthToken().getMember().getKarma()) + "</span>";
            final HtmlLink memberLink = new UrlBuilder(MyAccountPage.class).getHtmlLink(full_name);
            final HtmlLink logoutLink = new UrlBuilder(LogoutAction.class).getHtmlLink(session.tr("Logout"));

            add(new HtmlGenericElement("span").setCssClass("top_bar_component").add(memberLink).addText(karma));
            add(new HtmlGenericElement("span").setCssClass("top_bar_component").add(logoutLink));

        } else {
            final HtmlLink loginLink = new UrlBuilder(LoginPage.class).getHtmlLink(session.tr("Login / Signup"));

            add(new HtmlGenericElement("span").setCssClass("top_bar_component").add(loginLink));
        }
    }

}

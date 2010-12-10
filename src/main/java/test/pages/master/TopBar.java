package test.pages.master;

import test.Context;

import com.bloatit.web.server.Session;
import test.UrlBuilder;
import test.actions.LogoutAction;
import test.html.HtmlTools;
import test.html.components.standard.HtmlDiv;
import test.html.components.standard.HtmlGenericElement;
import test.html.components.standard.HtmlLink;
import test.pages.LoginPage;
import test.pages.MyAccountPage;

public class TopBar extends HtmlDiv {

    protected TopBar() {
        super();

        setId("top_bar");

        Session session = Context.getSession();
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

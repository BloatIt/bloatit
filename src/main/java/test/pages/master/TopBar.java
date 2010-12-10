package test.pages.master;

import test.Context;

import com.bloatit.web.actions.LogoutAction;
import com.bloatit.web.htmlrenderer.HtmlTools;
import com.bloatit.web.pages.LoginPage;
import com.bloatit.web.pages.MyAccountPage;
import com.bloatit.web.server.Session;
import test.html.components.standard.HtmlBlock;
import test.html.components.standard.HtmlGenericElement;

public class TopBar extends HtmlBlock {

    protected TopBar() {
        super();

        setId("top_bar");

        Session session = Context.getSession();
        if (session.isLogged()) {
            final String full_name = session.getAuthToken().getMember().getFullname();
            final String karma = HtmlTools.compressKarma(session.getAuthToken().getMember().getKarma());
            final String memberLink = HtmlTools.generateLink(session, full_name, new MyAccountPage(session))
                    + "<span class=\"karma\">" + karma + "</span>";
            final String logoutLink = HtmlTools.generateLink(session, session.tr("Logout"), new LogoutAction(session));

            add(new HtmlGenericElement("span").setClass("top_bar_component").addText(memberLink));
            add(new HtmlGenericElement("span").setClass("top_bar_component").addText(logoutLink));

        } else {
            add(new HtmlGenericElement("span").setClass("top_bar_component").addText(HtmlTools.generateLink(session,
                    session.tr("Login / Signup"),
                    new LoginPage(session))));
        }
    }

}

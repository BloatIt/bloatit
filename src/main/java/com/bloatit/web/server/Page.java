/*
 * Copyright (C) 2010 BloatIt.
 *
 * This file is part of BloatIt.
 *
 * BloatIt is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BloatIt is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
package com.bloatit.web.server;

import com.bloatit.web.actions.LogoutAction;
import com.bloatit.web.htmlrenderer.HtmlTools;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlBlock;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlComponent;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlList;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlListItem;
import com.bloatit.web.pages.DemandsPage;
import com.bloatit.web.pages.IndexPage;
import com.bloatit.web.pages.LoginPage;
import com.bloatit.web.pages.MembersListPage;
import com.bloatit.web.pages.MyAccountPage;
import com.bloatit.web.pages.PageNotFound;
import com.bloatit.web.pages.SpecialsPage;
import java.util.HashMap;
import java.util.Map;

public abstract class Page extends Request {

    private final String design;

    public Page(Session session, Map<String, String> parameters) {
        super(session, parameters);
        this.design = "/resources/css/design.css";
    }

    public Page(Session session){
        this(session, new HashMap<String, String>());
    }

    @Override
    protected void process() {
        this.htmlResult.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        this.htmlResult.write("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
        this.htmlResult.write("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
        this.htmlResult.indent();
        this.generate_head();
        this.generate_body();
        this.htmlResult.unindent();
        this.htmlResult.write("</html>");
        this.session.flushNotifications();
    }

    private void generate_head() {
        this.htmlResult.write("<head>");
        this.htmlResult.indent();
        this.htmlResult.write("<metahttp-equiv=\"content-type\" content=\"text/html;charset=utf-8\"/>");
        this.htmlResult.write("<link rel=\"stylesheet\" href=" + this.design + " type=\"text/css\" media=\"handheld, all\" />");

        this.htmlResult.write("<title>BloatIt - " + this.getTitle() + "</title>");
        this.htmlResult.unindent();
        this.htmlResult.write("</head>");
    }

    private void generate_body() {
        this.htmlResult.write("<body>");
        this.htmlResult.indent();
        this.htmlResult.write("<div id=\"page\">");
        this.htmlResult.indent();
        this.generateTopBar();
        this.generateTitle();
        this.htmlResult.write("<div id=\"center\">");
        this.htmlResult.indent();
        this.generateMainMenu();

        this.htmlResult.write("<div id=\"body_content\">");
        this.htmlResult.indent();

        this.generateNotifications();
        this.generateContent().generate(htmlResult);
        this.htmlResult.unindent();
        this.htmlResult.write("</div>");
        this.htmlResult.unindent();
        this.htmlResult.write("</div>");

        this.generateFooter();
        this.htmlResult.unindent();
        this.htmlResult.write("</div>");
        this.htmlResult.unindent();
        this.htmlResult.write("</body>");
    }

    private void generateTopBar() {
        this.htmlResult.write("<div id=\"top_bar\">");
        this.htmlResult.indent();
        if (this.session.isLogged()) {
            String full_name = this.session.getAuthToken().getMember().getFullname();
            String karma = HtmlTools.compressKarma(this.session.getAuthToken().getMember().getKarma());
            String memberLink = HtmlTools.generateLink(this.session, full_name, new MyAccountPage(this.session)) + "<span class=\"karma\">" + karma + "</span>";
            String logoutLink = HtmlTools.generateActionLink(this.session, this.session.tr("Logout"), new LogoutAction(this.session));
            this.htmlResult.write("<span class=\"top_bar_component\">" + memberLink + "</span><span class=\"top_bar_component\">" + logoutLink + "</span>");

        } else {
            this.htmlResult.write("<span class=\"top_bar_component\">" + HtmlTools.generateLink(this.session, this.session.tr("Login / Signup"), new LoginPage(this.session)) + "</span>");
            
        }
        this.htmlResult.unindent();
        this.htmlResult.write("</div>");
    }

    private void generateMainMenu() {

        Session s = this.session;
        HtmlBlock mainMenu = new HtmlBlock("main_menu");

        HtmlList primaryList = new HtmlList();

        primaryList.addItem(new HtmlListItem(HtmlTools.generateLink(s, s.tr("Demands"), new DemandsPage(s))));
        primaryList.addItem(new HtmlListItem(HtmlTools.generateLink(s, s.tr("Projects"), new IndexPage(s))));
        primaryList.addItem(new HtmlListItem(HtmlTools.generateLink(s, s.tr("Groups"), new IndexPage(s))));
        primaryList.addItem(new HtmlListItem(HtmlTools.generateLink(s, s.tr("Members"), new MembersListPage(s))));

        HtmlList secondaryList = new HtmlList();

        secondaryList.addItem(new HtmlListItem(HtmlTools.generateLink(s, s.tr("Specials page"), new SpecialsPage(s))));
        secondaryList.addItem(new HtmlListItem(HtmlTools.generateLink(s, s.tr("Contact"), new PageNotFound(s))));
        secondaryList.addItem(new HtmlListItem(HtmlTools.generateLink(s, s.tr("Documentation"), new PageNotFound(s))));
        secondaryList.addItem(new HtmlListItem(HtmlTools.generateLink(s, s.tr("About BloatIt"), new PageNotFound(s))));
        secondaryList.addItem(new HtmlListItem(HtmlTools.generateLink(s, s.tr("Press"), new PageNotFound(s))));

        mainMenu.add(primaryList);
        mainMenu.add(secondaryList);

        mainMenu.generate(htmlResult);
    }

    private void generateTitle() {
        this.htmlResult.pushTitle();
        this.htmlResult.write("<h1>" + HtmlTools.generateLink(this.session, this.generateLogo(), new IndexPage(this.session)) + "</h1>");
    }

    private void generateFooter() {
        this.htmlResult.write("<div id='footer'>");
        this.htmlResult.indent();
        this.htmlResult.write(this.session.tr("This website is under GNU Affero Public Licence."));
        this.htmlResult.unindent();
        this.htmlResult.write("</div>");
    }

    protected abstract HtmlComponent generateContent();
    
    @Override
    public abstract String getCode();

    protected abstract String getTitle();

    protected String generateLogo() {
        return "<span class=\"logo_bloatit\"><span class=\"logo_bloatit_bloat\">Bloat</span><span class=\"logo_bloatit_it\">It</span></span>";
    }

    private void generateNotifications() {
        this.htmlResult.write("<div id='notifications'>");
        this.htmlResult.indent();
        for(Notification notification: session.getNotifications()) {
            generateNotification(notification);
        }
        this.htmlResult.unindent();
        this.htmlResult.write("</div>");
    }

    private void generateNotification(Notification notification) {

        String notificationClass = "";

        if(notification.getType() == Notification.Type.BAD) {
            notificationClass = "notification_bad";
        } else if (notification.getType() == Notification.Type.GOOD) {
            notificationClass = "notification_good";
        } else if (notification.getType() == Notification.Type.ERROR) {
            notificationClass = "notification_error";
        }

        this.htmlResult.write("<div class=\""+notificationClass+"\">");
        this.htmlResult.indent();
        this.htmlResult.write(notification.getMessage());
        this.htmlResult.unindent();
        this.htmlResult.write("</div>");
    }
}

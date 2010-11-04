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

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

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

public abstract class Page extends Request {

    private final String design;

    public Page(Session session, Map<String, String> parameters) {
        super(session, parameters);
        design = "/resources/css/design.css";
    }

    public Page(Session session) {
        this(session, new HashMap<String, String>());
    }

    @Override
    protected void process() {
        htmlResult.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        htmlResult
                .write("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
        htmlResult.write("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
        htmlResult.indent();
        generate_head();
        generate_body();
        htmlResult.unindent();
        htmlResult.write("</html>");
        session.flushNotifications();
    }

    private void generate_head() {
        htmlResult.write("<head>");
        htmlResult.indent();
        htmlResult.write("<metahttp-equiv=\"content-type\" content=\"text/html;charset=utf-8\"/>");
        htmlResult.write("<link rel=\"stylesheet\" href=" + design + " type=\"text/css\" media=\"handheld, all\" />");

        htmlResult.write("<title>BloatIt - " + getTitle() + "</title>");
        htmlResult.unindent();
        htmlResult.write("</head>");
    }

    private void generate_body() {
        htmlResult.write("<body>");
        htmlResult.indent();
        htmlResult.write("<div id=\"page\">");
        htmlResult.indent();
        generateTopBar();
        generateTitle();
        htmlResult.write("<div id=\"center\">");
        htmlResult.indent();
        generateMainMenu();

        htmlResult.write("<div id=\"body_content\">");
        htmlResult.indent();

        generateNotifications();
        generateContent().generate(htmlResult);
        htmlResult.unindent();
        htmlResult.write("</div>");
        htmlResult.unindent();
        htmlResult.write("</div>");

        generateFooter();
        htmlResult.unindent();
        htmlResult.write("</div>");
        htmlResult.unindent();
        htmlResult.write("</body>");
    }

    private void generateTopBar() {
        htmlResult.write("<div id=\"top_bar\">");
        htmlResult.indent();
        if (session.isLogged()) {
            final String full_name = session.getAuthToken().getMember().getFullname();
            final String karma = HtmlTools.compressKarma(session.getAuthToken().getMember().getKarma());
            final String memberLink = HtmlTools.generateLink(session, full_name, new MyAccountPage(session))
                    + "<span class=\"karma\">" + karma + "</span>";
            final String logoutLink = HtmlTools.generateLink(session, session.tr("Logout"), new LogoutAction(session));
            htmlResult.write("<span class=\"top_bar_component\">" + memberLink + "</span><span class=\"top_bar_component\">"
                    + logoutLink + "</span>");

        } else {
            htmlResult.write("<span class=\"top_bar_component\">"
                    + HtmlTools.generateLink(session, session.tr("Login / Signup"), new LoginPage(session)) + "</span>");

        }
        htmlResult.unindent();
        htmlResult.write("</div>");
    }

    private void generateMainMenu() {

        final Session s = session;
        final HtmlBlock mainMenu = new HtmlBlock();
        mainMenu.setId("main_menu");

        final HtmlList primaryList = new HtmlList();

        primaryList.addItem(new HtmlListItem(HtmlTools.generateLink(s, s.tr("Demands"), new DemandsPage(s))));
        primaryList.addItem(new HtmlListItem(HtmlTools.generateLink(s, s.tr("Projects"), new IndexPage(s))));
        primaryList.addItem(new HtmlListItem(HtmlTools.generateLink(s, s.tr("Groups"), new IndexPage(s))));
        primaryList.addItem(new HtmlListItem(HtmlTools.generateLink(s, s.tr("Members"), new MembersListPage(s))));

        final HtmlList secondaryList = new HtmlList();

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
        htmlResult.pushTitle();
        htmlResult.write("<h1>" + HtmlTools.generateLink(session, generateLogo(), new IndexPage(session)) + "</h1>");
    }

    private void generateFooter() {
        htmlResult.write("<div id='footer'>");
        htmlResult.indent();
        htmlResult.write(session.tr("This website is under GNU Affero Public Licence."));
        htmlResult.unindent();
        htmlResult.write("</div>");
    }

    protected abstract HtmlComponent generateContent();

    @Override
    public abstract String getCode();

    protected abstract String getTitle();

    protected String generateLogo() {
        return "<span class=\"logo_bloatit\"><span class=\"logo_bloatit_bloat\">Bloat</span><span class=\"logo_bloatit_it\">It</span></span>";
    }

    private void generateNotifications() {
        htmlResult.write("<div id='notifications'>");
        htmlResult.indent();
        for (final Notification notification : session.getNotifications()) {
            generateNotification(notification);
        }
        htmlResult.unindent();
        htmlResult.write("</div>");
    }

    private void generateNotification(Notification notification) {

        String notificationClass = "";

        if (notification.getType() == Notification.Type.BAD) {
            notificationClass = "notification_bad";
        } else if (notification.getType() == Notification.Type.GOOD) {
            notificationClass = "notification_good";
        } else if (notification.getType() == Notification.Type.ERROR) {
            notificationClass = "notification_error";
        }

        htmlResult.write("<div class=\"" + notificationClass + "\">");
        htmlResult.indent();
        htmlResult.write(notification.getMessage());
        htmlResult.unindent();
        htmlResult.write("</div>");
    }

    @Override
    public String getUrl() {
        String link = "/" + session.getLanguage().getCode() + "/" + getCode();

        for (Entry<String, String> entry : getOutputParameters().entrySet()) {
            link += "/" + entry.getKey() + "-" + entry.getValue();
        }

        return link;
    }

}

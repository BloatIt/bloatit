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
import com.bloatit.web.pages.DemandsPage;
import com.bloatit.web.pages.IndexPage;
import com.bloatit.web.pages.LoginPage;
import com.bloatit.web.pages.MyAccountPage;
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

        this.generateContent();
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
            String full_name = this.session.getAuthToken().getMember().getFullName();
            int karma = HtmlTools.compressKarma(this.session.getAuthToken().getMember().getKarma());
            String memberLink = HtmlTools.generateLink(this.session, full_name, new MyAccountPage(this.session)) + "<span class=\"karma\">" + karma + "</span>";
            String logoutLink = HtmlTools.generateActionLink(this.session, this.session.tr("Logout"), new LogoutAction(this.session));
            this.htmlResult.write("<span class=\"top_bar_component\">" + memberLink + "</span><span class=\"top_bar_component\">" + logoutLink + "</span>");

        } else {
            this.htmlResult.write("<span class=\"top_bar_component\">" + HtmlTools.generateLink(this.session, this.session.tr("Login / Signup"), new LoginPage(this.session)) + "</span>");
            this.htmlResult.unindent();
            this.htmlResult.write("</div>");
        }
    }

    private void generateMainMenu() {
        this.htmlResult.write("<div id='main_menu'>");
        this.htmlResult.indent();
        this.htmlResult.write("<ul>");
        this.htmlResult.indent();
        this.htmlResult.write("<li>" + HtmlTools.generateLink(this.session, this.session.tr("Demands"), new DemandsPage(this.session)) + "</li>");
        this.htmlResult.write("<li>" + HtmlTools.generateLink(this.session, this.session.tr("Projects"), new IndexPage(this.session)) + "</li>");
        this.htmlResult.write("<li>" + HtmlTools.generateLink(this.session, this.session.tr("Groups"), new IndexPage(this.session)) + "</li>");
        this.htmlResult.write("<li>" + HtmlTools.generateLink(this.session, this.session.tr("Members"), new IndexPage(this.session)) + "</li>");
        this.htmlResult.unindent();
        this.htmlResult.write("</ul>");
        this.htmlResult.write("<ul>");
        this.htmlResult.indent();
        this.htmlResult.write("<li>" + HtmlTools.generateLink(this.session, this.session.tr("Contact"), new IndexPage(this.session)) + "</li>");
        this.htmlResult.write("<li>" + HtmlTools.generateLink(this.session, this.session.tr("Documentation"), new IndexPage(this.session)) + "</li>");
        this.htmlResult.write("<li>" + HtmlTools.generateLink(this.session, this.session.tr("About BloatIt"), new IndexPage(this.session)) + "</li>");
        this.htmlResult.write("<li>" + HtmlTools.generateLink(this.session, this.session.tr("Press"), new IndexPage(this.session)) + "</li>");
        this.htmlResult.unindent();
        this.htmlResult.write("</ul>");
        this.htmlResult.unindent();
        this.htmlResult.write("</div>");
    }

    private void generateTitle() {
        this.htmlResult.write("<h1>" + HtmlTools.generateLink(this.session, this.generateLogo(), new IndexPage(this.session)) + "</h1>");
    }

    private void generateFooter() {
        this.htmlResult.write("<div id='footer'>");
        this.htmlResult.indent();
        this.htmlResult.write(this.session.tr("This website is under GNU Affero Public Licence."));
        this.htmlResult.unindent();
        this.htmlResult.write("</div>");
    }

    protected abstract void generateContent();
    
    @Override
    public abstract String getCode();

    protected abstract String getTitle();

    protected String generateLogo() {
        return "<span class=\"logo_bloatit\"><span class=\"logo_bloatit_bloat\">Bloat</span><span class=\"logo_bloatit_it\">It</span></span>";
    }
}

/*
 * Copyright (C) 2010 BloatIt. This file is part of BloatIt. BloatIt is free
 * software: you can redistribute it and/or modify it under the terms of the GNU
 * Affero General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 * BloatIt is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details. You should have received a copy of the GNU Affero General Public
 * License along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
package com.bloatit.web.pages;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.components.HtmlList;
import com.bloatit.framework.webprocessor.components.HtmlParagraph;
import com.bloatit.framework.webprocessor.components.HtmlTitle;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.components.meta.HtmlText;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.ElveosUserToken;
import com.bloatit.web.linkable.members.MemberPage;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.pages.master.ElveosPage;
import com.bloatit.web.pages.master.sidebar.TwoColumnLayout;
import com.bloatit.web.url.AccountChargingProcessUrl;
import com.bloatit.web.url.CreateSoftwarePageUrl;
import com.bloatit.web.url.ChangeLanguagePageUrl;
import com.bloatit.web.url.CreateFeaturePageUrl;
import com.bloatit.web.url.CreateTeamPageUrl;
import com.bloatit.web.url.DocumentationPageUrl;
import com.bloatit.web.url.FeatureListPageUrl;
import com.bloatit.web.url.LoginPageUrl;
import com.bloatit.web.url.LogoutActionUrl;
import com.bloatit.web.url.LostPasswordPageUrl;
import com.bloatit.web.url.MemberPageUrl;
import com.bloatit.web.url.MembersListPageUrl;
import com.bloatit.web.url.SignUpPageUrl;
import com.bloatit.web.url.SiteMapPageUrl;
import com.bloatit.web.url.SoftwareListPageUrl;
import com.bloatit.web.url.TeamsPageUrl;

@ParamContainer("sitemap")
public final class SiteMapPage extends ElveosPage {

    private final SiteMapPageUrl url;

    public SiteMapPage(final SiteMapPageUrl url) {
        super(url);
        this.url = url;

    }

    @Override
    protected HtmlElement createBodyContent(final ElveosUserToken userToken) throws RedirectException {

        final TwoColumnLayout layout = new TwoColumnLayout(true, url);

        final HtmlTitle title = new HtmlTitle(Context.tr("Site map"), 1);
        layout.addLeft(title);

        final HtmlParagraph goal = new HtmlParagraph(Context.tr("This page should list all page in this website. However, some dynamic pages are numerous and cannot be all listed here."));
        layout.addLeft(goal);

        final HtmlTitle titleContent = new HtmlTitle(Context.tr("Content pages"), 2);
        layout.addLeft(titleContent);
        final HtmlList contentLinkList = new HtmlList();
        layout.addLeft(contentLinkList);

        contentLinkList.add(new FeatureListPageUrl().getHtmlLink(Context.tr("Feature request list")));
        contentLinkList.add(new TeamsPageUrl().getHtmlLink(Context.tr("Team list")));
        contentLinkList.add(new MembersListPageUrl().getHtmlLink(Context.tr("Member list")));
        contentLinkList.add(new SoftwareListPageUrl().getHtmlLink(Context.tr("Softwares list")));
        contentLinkList.add(new CreateFeaturePageUrl().getHtmlLink(Context.tr("Request a new feature")));
        contentLinkList.add(new CreateTeamPageUrl().getHtmlLink(Context.tr("Create a team")));
        contentLinkList.add(new CreateSoftwarePageUrl().getHtmlLink(Context.tr("Add a software")));

        final HtmlTitle titleUseful = new HtmlTitle(Context.tr("Useful pages"), 2);
        layout.addLeft(titleUseful);
        final HtmlList usefulLinkList = new HtmlList();
        layout.addLeft(usefulLinkList);

        usefulLinkList.add(new LoginPageUrl().getHtmlLink(Context.tr("Login")));
        usefulLinkList.add(new SignUpPageUrl().getHtmlLink(Context.tr("Sign up ")));
        usefulLinkList.add(new LostPasswordPageUrl().getHtmlLink(Context.tr("Password recovery")));
        usefulLinkList.add(new SiteMapPageUrl().getHtmlLink(Context.tr("Site map")));
        usefulLinkList.add(new ChangeLanguagePageUrl().getHtmlLink(Context.tr("Change language")));
        usefulLinkList.add(new ChangeLanguagePageUrl().getHtmlLink(Context.tr("Signal a bug")));

        final HtmlTitle titlePersonal = new HtmlTitle(Context.tr("Personal informations pages"), 2);
        layout.addLeft(titlePersonal);
        layout.addLeft(new HtmlParagraph(Context.tr("You must be logged to use these pages.")));
        final HtmlList personalLinkList = new HtmlList();
        layout.addLeft(personalLinkList);

        if (userToken.isAuthenticated()) {
            personalLinkList.add(new MemberPageUrl(userToken.getMember()).getHtmlLink(Context.tr("My page")));
            personalLinkList.add(MemberPage.MyAccountUrl(userToken.getMember()).getHtmlLink(Context.tr("My account")));
            personalLinkList.add(MemberPage.MyMessagesUrl(userToken.getMember()).getHtmlLink(Context.tr("My messages")));
        } else {
            personalLinkList.add(new HtmlText(Context.tr("My page")));
            personalLinkList.add(new HtmlText(Context.tr("My account")));
            personalLinkList.add(new HtmlText(Context.tr("My messages")));
        }

        personalLinkList.add(new LogoutActionUrl().getHtmlLink(Context.tr("Logout")));
        personalLinkList.add(new AccountChargingProcessUrl().getHtmlLink(Context.tr("Charge account")));

        final HtmlTitle titleDocumenation = new HtmlTitle(Context.tr("Main documentation pages"), 2);
        layout.addLeft(titleDocumenation);
        final HtmlList documentationLinkList = new HtmlList();
        layout.addLeft(documentationLinkList);

        documentationLinkList.add(new DocumentationPageUrl().getHtmlLink(Context.tr("Documentation home")));

        return layout;
    }

    @Override
    public String createPageTitle() {
        return Context.tr("Site map");
    }

    @Override
    public boolean isStable() {
        return true;
    }

    @Override
    protected Breadcrumb createBreadcrumb(final ElveosUserToken userToken) {
        return SiteMapPage.generateBreadcrumb();
    }

    private static Breadcrumb generateBreadcrumb() {
        final Breadcrumb breadcrumb = IndexPage.generateBreadcrumb();

        breadcrumb.pushLink(new SiteMapPageUrl().getHtmlLink(tr("Site map")));

        return breadcrumb;
    }
}

//
// Copyright (c) 2011 Linkeos.
//
// This file is part of Elveos.org.
// Elveos.org is free software: you can redistribute it and/or modify it
// under the terms of the GNU General Public License as published by the
// Free Software Foundation, either version 3 of the License, or (at your
// option) any later version.
//
// Elveos.org is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
// more details.
// You should have received a copy of the GNU General Public License along
// with Elveos.org. If not, see http://www.gnu.org/licenses/.
//
package com.bloatit.web.linkable.team;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import com.bloatit.data.DaoTeam;
import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.components.HtmlTitleBlock;
import com.bloatit.framework.webprocessor.components.form.FormBuilder;
import com.bloatit.framework.webprocessor.components.form.HtmlDropDown;
import com.bloatit.framework.webprocessor.components.form.HtmlFileInput;
import com.bloatit.framework.webprocessor.components.form.HtmlSubmit;
import com.bloatit.framework.webprocessor.components.form.HtmlTextArea;
import com.bloatit.framework.webprocessor.components.form.HtmlTextField;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.Member;
import com.bloatit.web.components.HtmlElveosForm;
import com.bloatit.web.components.SidebarMarkdownHelp;
import com.bloatit.web.linkable.documentation.SideBarDocumentationBlock;
import com.bloatit.web.linkable.master.Breadcrumb;
import com.bloatit.web.linkable.master.LoggedElveosPage;
import com.bloatit.web.linkable.master.sidebar.TwoColumnLayout;
import com.bloatit.web.url.CreateTeamActionUrl;
import com.bloatit.web.url.CreateTeamPageUrl;

/**
 * <p>
 * A page used to create a new team
 * </p>
 */
@ParamContainer("team/create")
public final class CreateTeamPage extends LoggedElveosPage {
    private final CreateTeamPageUrl url;

    public CreateTeamPage(final CreateTeamPageUrl url) {
        super(url);
        this.url = url;
    }

    @Override
    public HtmlElement createRestrictedContent(final Member loggedUser) throws RedirectException {
        final TwoColumnLayout layout = new TwoColumnLayout(true, url);

        final HtmlTitleBlock master = new HtmlTitleBlock(Context.tr("Create a new team"), 1);
        final CreateTeamActionUrl target = new CreateTeamActionUrl(getSession().getShortKey());
        final HtmlElveosForm form = new HtmlElveosForm(target.urlString());
        master.add(form);
        FormBuilder ftool = new FormBuilder(CreateTeamAction.class, target);

        // name
        ftool.add(form, new HtmlTextField(target.getLoginParameter().getName()));

        // Contact
        final String suggested = Context.tr("You can contact us using: \n\n * [Website](http://www.example.com) \n * Email: contact@example.com \n * IRC: irc://irc.example.com:6667 \n * ... ");
        HtmlTextArea contact = new HtmlTextArea(target.getContactParameter().getName(), 5, 80);
        ftool.add(form, contact);
        ftool.setDefaultValueIfNeeded(contact, suggested);

        // Description
        ftool.add(form, new HtmlTextArea(target.getDescriptionParameter().getName(), 5, 80));

        // PUBLIC / PRIVATE
        final HtmlDropDown rightInput = new HtmlDropDown(target.getRightParameter().getName());
        rightInput.addDropDownElement(DaoTeam.Right.PUBLIC.toString(), Context.tr("Open to all"));
        rightInput.addDropDownElement(DaoTeam.Right.PROTECTED.toString(), Context.tr("By invitation"));
        ftool.add(form, rightInput);

        // Avatar
        ftool.add(form, new HtmlFileInput(target.getAvatarParameter().getName()));

        form.addSubmit(new HtmlSubmit(Context.tr("Submit")));

        layout.addLeft(master);
        layout.addRight(new SideBarDocumentationBlock("create_team"));
        layout.addRight(new SideBarDocumentationBlock("cc_by"));
        layout.addRight(new SideBarDocumentationBlock("describe_team"));
        layout.addRight(new SidebarMarkdownHelp());

        return layout;
    }

    @Override
    public String getRefusalReason() {
        return Context.tr("You cannot create a team without being logged in.");
    }

    @Override
    protected String createPageTitle() {
        return Context.tr("Create a new team");
    }

    @Override
    public boolean isStable() {
        return false;
    }

    @Override
    protected Breadcrumb createBreadcrumb(final Member member) {
        return CreateTeamPage.generateBreadcrumb();
    }

    private static Breadcrumb generateBreadcrumb() {
        final Breadcrumb breadcrumb = TeamsPage.generateBreadcrumb();

        breadcrumb.pushLink(new CreateTeamPageUrl().getHtmlLink(tr("Create a team")));

        return breadcrumb;
    }
}

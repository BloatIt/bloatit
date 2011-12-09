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

import com.bloatit.data.DaoTeam;
import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.components.HtmlTitle;
import com.bloatit.framework.webprocessor.components.form.FormBuilder;
import com.bloatit.framework.webprocessor.components.form.HtmlCheckbox;
import com.bloatit.framework.webprocessor.components.form.HtmlDropDown;
import com.bloatit.framework.webprocessor.components.form.HtmlFileInput;
import com.bloatit.framework.webprocessor.components.form.HtmlFormField.LabelPosition;
import com.bloatit.framework.webprocessor.components.form.HtmlSubmit;
import com.bloatit.framework.webprocessor.components.form.HtmlTextArea;
import com.bloatit.framework.webprocessor.components.form.HtmlTextField;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.Member;
import com.bloatit.model.Team;
import com.bloatit.web.components.HtmlElveosForm;
import com.bloatit.web.components.SidebarMarkdownHelp;
import com.bloatit.web.linkable.documentation.SideBarDocumentationBlock;
import com.bloatit.web.linkable.master.Breadcrumb;
import com.bloatit.web.linkable.master.LoggedElveosPage;
import com.bloatit.web.linkable.master.sidebar.TwoColumnLayout;
import com.bloatit.web.url.ModifyTeamActionUrl;
import com.bloatit.web.url.ModifyTeamPageUrl;

@ParamContainer("teams/%team%/modify")
public class ModifyTeamPage extends LoggedElveosPage {
    private final ModifyTeamPageUrl url;

    @RequestParam(role = Role.PAGENAME)
    private final Team team;

    public ModifyTeamPage(final ModifyTeamPageUrl url) {
        super(url);
        this.url = url;
        this.team = url.getTeam();
    }

    @Override
    public HtmlElement createRestrictedContent(final Member loggedUser) throws RedirectException {
        final TwoColumnLayout layout = new TwoColumnLayout(true, url);

        final HtmlTitle title = new HtmlTitle(1);
        title.addText(Context.tr("Change {0} settings", team.getDisplayName()));
        layout.addLeft(title);

        final ModifyTeamActionUrl target = new ModifyTeamActionUrl(getSession().getShortKey(), team);
        final HtmlElveosForm form = new HtmlElveosForm(target.urlString());
        FormBuilder ftool = new FormBuilder(ModifyTeamAction.class, target);

        // name
        HtmlTextField name = new HtmlTextField(target.getDisplayNameParameter().getName());
        ftool.add(form, name);
        ftool.setDefaultValueIfNeeded(name, team.getLogin());

        // PUBLIC / PRIVATE
        final HtmlDropDown rightInput = new HtmlDropDown(target.getRightParameter().getName());
        rightInput.addDropDownElement(DaoTeam.Right.PUBLIC.toString(), Context.tr("Open to all"));
        rightInput.addDropDownElement(DaoTeam.Right.PROTECTED.toString(), Context.tr("By invitation"));
        ftool.setDefaultValueIfNeeded(rightInput, team.getJoinRight().toString());
        ftool.add(form, rightInput);

        // Contact
        HtmlTextArea contact = new HtmlTextArea(target.getContactParameter().getName(), 5, 80);
        ftool.add(form, contact);
        ftool.setDefaultValueIfNeeded(contact, team.getPublicContact());

        // Description
        HtmlTextArea description = new HtmlTextArea(target.getDescriptionParameter().getName(), 5, 80);
        ftool.add(form, description);
        ftool.setDefaultValueIfNeeded(description, team.getDescription());

        // Avatar
        ftool.add(form, new HtmlFileInput(target.getAvatarParameter().getName()));
        HtmlCheckbox deleteAvatar = new HtmlCheckbox(target.getDeleteAvatarParameter().getName(), LabelPosition.BEFORE);
        ftool.add(form, deleteAvatar);
        if (team.getAvatar() == null || team.getAvatar().isNull()) {
            deleteAvatar.addAttribute("disabled", "disabled");
        }

        form.addSubmit(new HtmlSubmit(Context.tr("Submit")));

        layout.addLeft(form);
        layout.addRight(new SideBarDocumentationBlock("create_team"));
        layout.addRight(new SideBarDocumentationBlock("cc_by"));
        layout.addRight(new SideBarDocumentationBlock("describe_team"));
        layout.addRight(new SidebarMarkdownHelp());

        return layout;
    }

    @Override
    public String getRefusalReason() {
        return Context.tr("You must be logged to change team's information");
    }

    @Override
    protected String createPageTitle() {
        return Context.tr("Modify team information");
    }

    @Override
    public boolean isStable() {
        return false;
    }

    @Override
    protected Breadcrumb createBreadcrumb(final Member member) {
        return generateBreadcrumb(team);
    }

    private static Breadcrumb generateBreadcrumb(final Team team) {
        final Breadcrumb breadcrumb = TeamPage.generateBreadcrumb(team);
        breadcrumb.pushLink(new ModifyTeamPageUrl(team).getHtmlLink(Context.tr("modify")));
        return breadcrumb;
    }
}

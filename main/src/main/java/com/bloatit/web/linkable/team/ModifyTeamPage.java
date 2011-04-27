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

import com.bloatit.framework.exceptions.highlevel.ShallNotPassException;
import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.exceptions.lowlevel.UnauthorizedOperationException;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.components.HtmlTitle;
import com.bloatit.framework.webprocessor.components.advanced.showdown.MarkdownEditor;
import com.bloatit.framework.webprocessor.components.advanced.showdown.MarkdownPreviewer;
import com.bloatit.framework.webprocessor.components.form.FieldData;
import com.bloatit.framework.webprocessor.components.form.HtmlCheckbox;
import com.bloatit.framework.webprocessor.components.form.HtmlFileInput;
import com.bloatit.framework.webprocessor.components.form.HtmlForm;
import com.bloatit.framework.webprocessor.components.form.HtmlFormField.LabelPosition;
import com.bloatit.framework.webprocessor.components.form.HtmlSubmit;
import com.bloatit.framework.webprocessor.components.form.HtmlTextField;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.Member;
import com.bloatit.model.Team;
import com.bloatit.web.pages.LoggedPage;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.pages.master.sidebar.TwoColumnLayout;
import com.bloatit.web.url.ModifyTeamActionUrl;
import com.bloatit.web.url.ModifyTeamPageUrl;

@ParamContainer("team/modify")
public class ModifyTeamPage extends LoggedPage {
    private ModifyTeamPageUrl url;

    @RequestParam(role = Role.GET)
    private final Team team;

    public ModifyTeamPage(ModifyTeamPageUrl url) {
        super(url);
        this.url = url;
        this.team = url.getTeam();
    }

    @Override
    public HtmlElement createRestrictedContent(Member loggedUser) throws RedirectException {
        TwoColumnLayout layout = new TwoColumnLayout(true, url);

        ModifyTeamActionUrl targetUrl = new ModifyTeamActionUrl(team);

        try {
            HtmlTitle title = new HtmlTitle(1);
            title.addText(Context.tr("Change {0} settings", team.getDisplayName()));
            layout.addLeft(title);

            HtmlForm form = new HtmlForm(targetUrl.urlString());
            layout.addLeft(form);
            form.enableFileUpload();

            // ///////
            // Display name
            final FieldData displayNameFieldData = targetUrl.getDisplayNameParameter().pickFieldData();
            final HtmlTextField displayNameInput = new HtmlTextField(displayNameFieldData.getName(), tr("Display name"));
            displayNameInput.addErrorMessages(displayNameFieldData.getErrorMessages());
            if (displayNameFieldData.getSuggestedValue() != null && !displayNameFieldData.getSuggestedValue().isEmpty()) {
                displayNameInput.setDefaultValue(displayNameFieldData.getSuggestedValue());
            } else if (loggedUser.getFullname() != null) {
                displayNameInput.setDefaultValue(team.getDisplayName());
            }
            form.add(displayNameInput);

            // ///////
            // Contact
            final FieldData contactFieldData = targetUrl.getContactParameter().pickFieldData();
            final MarkdownEditor contactInput = new MarkdownEditor(contactFieldData.getName(), tr("Contact information"), 10, 80);
            if (contactFieldData.getSuggestedValue() != null && !contactFieldData.getSuggestedValue().isEmpty()) {
                contactInput.setDefaultValue(contactFieldData.getSuggestedValue());
            } else {
                contactInput.setDefaultValue(team.getContact());
            }
            contactInput.addErrorMessages(contactFieldData.getErrorMessages());
            form.add(contactInput);
            form.add(new MarkdownPreviewer(contactInput));

            // ///////
            // Description
            final FieldData descriptionFieldData = targetUrl.getDescriptionParameter().pickFieldData();
            final MarkdownEditor descriptionInput = new MarkdownEditor(descriptionFieldData.getName(), tr("Team description"), 5, 80);
            if (descriptionFieldData.getSuggestedValue() != null && !descriptionFieldData.getSuggestedValue().isEmpty()) {
                descriptionInput.setDefaultValue(descriptionFieldData.getSuggestedValue());
            } else {
                descriptionInput.setDefaultValue(team.getDescription());
            }
            descriptionInput.addErrorMessages(descriptionFieldData.getErrorMessages());
            form.add(descriptionInput);
            form.add(new MarkdownPreviewer(descriptionInput));

            // ///////
            // Avatar
            final FieldData avatarField = targetUrl.getAvatarParameter().pickFieldData();
            final HtmlFileInput avatarInput = new HtmlFileInput(avatarField.getName(), Context.tr("Avatar image file"));
            avatarInput.setComment(tr("64px x 64px. 50Kb max. Accepted formats: png, jpg"));
            form.add(avatarInput);

            // ///////
            // Delete avatar
            final FieldData deleteAvatarFieldData = targetUrl.getDeleteAvatarParameter().pickFieldData();
            final HtmlCheckbox deleteAvatar = new HtmlCheckbox(deleteAvatarFieldData.getName(), Context.tr("Delete avatar"), LabelPosition.BEFORE);
            if (loggedUser.getAvatar() == null && loggedUser.getAvatar().isNull()) {
                deleteAvatar.addAttribute("disabled", "disabled");
            }
            deleteAvatar.setComment(Context.tr("Checking this box will delete team's avatar."));
            form.add(deleteAvatar);

            final HtmlSubmit submit = new HtmlSubmit(Context.tr("Submit"));
            form.add(submit);
        } catch (UnauthorizedOperationException e) {
            throw new ShallNotPassException("Couldn't access logged member information", e);
        }

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
    protected Breadcrumb createBreadcrumb() {
        return generateBreadcrumb(team);
    }

    private static Breadcrumb generateBreadcrumb(final Team team) {
        final Breadcrumb breadcrumb = TeamPage.generateBreadcrumb(team);
        breadcrumb.pushLink(new ModifyTeamPageUrl(team).getHtmlLink(Context.tr("modify")));
        return breadcrumb;
    }
}

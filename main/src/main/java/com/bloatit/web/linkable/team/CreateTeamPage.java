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
import com.bloatit.framework.webprocessor.components.advanced.showdown.MarkdownEditor;
import com.bloatit.framework.webprocessor.components.advanced.showdown.MarkdownPreviewer;
import com.bloatit.framework.webprocessor.components.form.FieldData;
import com.bloatit.framework.webprocessor.components.form.HtmlDropDown;
import com.bloatit.framework.webprocessor.components.form.HtmlForm;
import com.bloatit.framework.webprocessor.components.form.HtmlSubmit;
import com.bloatit.framework.webprocessor.components.form.HtmlTextField;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.Member;
import com.bloatit.web.linkable.documentation.SideBarDocumentationBlock;
import com.bloatit.web.pages.LoggedPage;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.pages.master.sidebar.TwoColumnLayout;
import com.bloatit.web.url.CreateTeamActionUrl;
import com.bloatit.web.url.CreateTeamPageUrl;

/**
 * <p>
 * A page used to create a new team
 * </p>
 */
@ParamContainer("team/create")
public final class CreateTeamPage extends LoggedPage {
    private final CreateTeamPageUrl url;

    public CreateTeamPage(final CreateTeamPageUrl url) {
        super(url);
        this.url = url;
    }

    @Override
    public HtmlElement createRestrictedContent(final Member loggedUser) throws RedirectException {
        final TwoColumnLayout layout = new TwoColumnLayout(true, url);
        layout.addLeft(generateMain());
        layout.addRight(new SideBarDocumentationBlock("create_team"));
        layout.addRight(new SideBarDocumentationBlock("cc_by"));
        layout.addRight(new SideBarDocumentationBlock("describe_team"));
        layout.addRight(new SideBarDocumentationBlock("markdown"));

        return layout;
    }

    @Override
    public String getRefusalReason() {
        return Context.tr("You cannot create a team without being logged in.");
    }

    private HtmlElement generateMain() {
        final HtmlTitleBlock master = new HtmlTitleBlock(Context.tr("Create a new team"), 1);

        final CreateTeamActionUrl target = new CreateTeamActionUrl();

        final HtmlForm form = new HtmlForm(target.urlString());
        master.add(form);

        // name
        final FieldData nameData = target.getLoginParameter().pickFieldData();
        final HtmlTextField nameInput = new HtmlTextField(nameData.getName(), Context.tr("Team unique name "));
        nameInput.setDefaultValue(nameData.getSuggestedValue());
        nameInput.addErrorMessages(nameData.getErrorMessages());
        nameInput.setComment(Context.tr("The name of the team. It must be unique. Between 3 and 50 characters."));
        form.add(nameInput);

        // Contact
        final String suggested = Context.tr("You can contact us using: \n\n * [Website](http://www.example.com) \n * Email: contact@example.com \n * IRC: irc://irc.example.com:6667 \n * ... ");
        final FieldData contactData = target.getContactParameter().pickFieldData();
        final MarkdownEditor contactInput = new MarkdownEditor(contactData.getName(), Context.tr("Contact of the team: "), 5, 80);
        if (contactData.getSuggestedValue() == null || contactData.getSuggestedValue().isEmpty()) {
            contactInput.setDefaultValue(suggested);
        } else {
            contactInput.setDefaultValue(contactData.getSuggestedValue());
        }
        contactInput.addErrorMessages(contactData.getErrorMessages());
        contactInput.setComment(Context.tr("The ways to contact the team. Email, IRC channel, mailing list ... Maximum 300 characters. These informations will be publicly available. Markdown syntax available"));
        form.add(contactInput);

        // Contact preview
        final MarkdownPreviewer contactPreview = new MarkdownPreviewer(contactInput);
        form.add(contactPreview);

        // Description
        final FieldData descriptionData = target.getDescriptionParameter().pickFieldData();
        final MarkdownEditor descriptionInput = new MarkdownEditor(descriptionData.getName(), Context.tr("Description of the team"), 10, 80);
        descriptionInput.setDefaultValue(descriptionData.getSuggestedValue());
        descriptionInput.addErrorMessages(descriptionData.getErrorMessages());
        descriptionInput.setComment(Context.tr("Between 5 and 5000 characters."));
        form.add(descriptionInput);

        // Description preview
        final MarkdownPreviewer descriptionPreview = new MarkdownPreviewer(descriptionInput);
        form.add(descriptionPreview);

        // PUBLIC / PRIVATE
        final FieldData rightData = target.getRightParameter().pickFieldData();
        final HtmlDropDown rightInput = new HtmlDropDown(rightData.getName(), Context.tr("Type of the team : "));
        rightInput.addErrorMessages(rightData.getErrorMessages());
        rightInput.addDropDownElement(DaoTeam.Right.PUBLIC.toString(), Context.tr("Public"));
        rightInput.addDropDownElement(DaoTeam.Right.PROTECTED.toString(), Context.tr("Protected"));
        rightInput.setDefaultValue(rightData.getSuggestedValue());
        rightInput.setComment(Context.tr("Public teams can be joined by anybody without an invitation."));
        form.add(rightInput);

        form.add(new HtmlSubmit(Context.tr("Submit")));

        return master;
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

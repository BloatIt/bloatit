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
package com.bloatit.web.linkable.members;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import com.bloatit.framework.exceptions.highlevel.ShallNotPassException;
import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.utils.i18n.Country;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.ParamContainer.Protocol;
import com.bloatit.framework.webprocessor.components.HtmlTitle;
import com.bloatit.framework.webprocessor.components.form.FieldData;
import com.bloatit.framework.webprocessor.components.form.HtmlCheckbox;
import com.bloatit.framework.webprocessor.components.form.HtmlDropDown;
import com.bloatit.framework.webprocessor.components.form.HtmlFileInput;
import com.bloatit.framework.webprocessor.components.form.HtmlForm;
import com.bloatit.framework.webprocessor.components.form.HtmlFormBlock;
import com.bloatit.framework.webprocessor.components.form.HtmlFormField.LabelPosition;
import com.bloatit.framework.webprocessor.components.form.HtmlPasswordField;
import com.bloatit.framework.webprocessor.components.form.HtmlSubmit;
import com.bloatit.framework.webprocessor.components.form.HtmlTextArea;
import com.bloatit.framework.webprocessor.components.form.HtmlTextField;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.Member;
import com.bloatit.model.right.UnauthorizedOperationException;
import com.bloatit.web.components.LanguageSelector;
import com.bloatit.web.linkable.documentation.SideBarDocumentationBlock;
import com.bloatit.web.pages.LoggedElveosPage;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.pages.master.sidebar.TwoColumnLayout;
import com.bloatit.web.url.ModifyDetailActionUrl;
import com.bloatit.web.url.ModifyMemberActionUrl;
import com.bloatit.web.url.ModifyMemberPageUrl;
import com.bloatit.web.url.ModifyPasswordActionUrl;

@ParamContainer(value = "member/modify", protocol = Protocol.HTTPS)
public class ModifyMemberPage extends LoggedElveosPage {
    private final ModifyMemberPageUrl url;

    public ModifyMemberPage(final ModifyMemberPageUrl url) {
        super(url);
        this.url = url;
    }

    @Override
    public HtmlElement createRestrictedContent(final Member loggedUser) throws RedirectException {
        final TwoColumnLayout layout = new TwoColumnLayout(true, url);

        final HtmlTitle title = new HtmlTitle(1);
        title.addText(Context.tr("Change member settings"));
        layout.addLeft(title);

        try {
            // ///////
            // name etc.
            final ModifyMemberActionUrl targetUrl = new ModifyMemberActionUrl(getSession().getShortKey());
            final HtmlForm nameForm = new HtmlForm(targetUrl.urlString());
            nameForm.enableFileUpload();
            layout.addLeft(nameForm);
            final HtmlFormBlock nameBlock = new HtmlFormBlock(Context.tr("Name and description"));
            nameForm.add(nameBlock);
            // Full name
            final FieldData fullnameFieldData = targetUrl.getFullnameParameter().pickFieldData();
            final HtmlTextField fullnameInput = new HtmlTextField(fullnameFieldData.getName(), tr("Full name"));
            fullnameInput.addErrorMessages(fullnameFieldData.getErrorMessages());
            if (fullnameFieldData.getSuggestedValue() != null && !fullnameFieldData.getSuggestedValue().isEmpty()) {
                fullnameInput.setDefaultValue(fullnameFieldData.getSuggestedValue());
            } else if (loggedUser.getFullname() != null && !loggedUser.getFullname().isEmpty()) {
                fullnameInput.setDefaultValue(loggedUser.getFullname());
            }
            fullnameInput.setComment(Context.tr("If you set a value to your fullname, it will be used instead of your login to designate you."));
            nameBlock.add(fullnameInput);

            // User description
            final FieldData descriptionFD = targetUrl.getDescriptionParameter().pickFieldData();
            HtmlTextArea description = new HtmlTextArea(descriptionFD.getName(), Context.tr("Description"), 20, 100);
            if (descriptionFD.getSuggestedValue() != null && !descriptionFD.getSuggestedValue().isEmpty()) {
                description.setDefaultValue(descriptionFD.getSuggestedValue());
            } else if (loggedUser.getDescription() != null) {
                description.setDefaultValue(loggedUser.getDescription());
            }
            description.setComment(Context.tr("Introduce yourself in less than 200 characters."));
            nameBlock.add(description);

            // Avatar
            final FieldData avatarField = targetUrl.getAvatarParameter().pickFieldData();
            final HtmlFileInput avatarInput = new HtmlFileInput(avatarField.getName(), Context.tr("Avatar image file"));
            avatarInput.setComment(tr("64px x 64px. 50Kb max. Accepted formats: png, jpg"));
            nameBlock.add(avatarInput);

            // Delete avatar
            final FieldData deleteAvatarFieldData = targetUrl.getDeleteAvatarParameter().pickFieldData();
            final HtmlCheckbox deleteAvatar = new HtmlCheckbox(deleteAvatarFieldData.getName(), Context.tr("Delete avatar"), LabelPosition.BEFORE);
            if (loggedUser.getAvatar() == null && loggedUser.getAvatar().isNull()) {
                deleteAvatar.addAttribute("disabled", "disabled");
            }
            deleteAvatar.setComment(Context.tr("Checking this box will delete your avatar. If you have a libravatar it will be used instead."));
            nameBlock.add(deleteAvatar);
            nameBlock.add(new HtmlSubmit(Context.tr("Submit")));

            // ///////
            // password
            final ModifyPasswordActionUrl passwordUrl = new ModifyPasswordActionUrl(getSession().getShortKey());
            final HtmlForm passwordForm = new HtmlForm(passwordUrl.urlString());
            layout.addLeft(passwordForm);
            final HtmlFormBlock passwordBlock = new HtmlFormBlock(Context.tr("Password"));
            passwordForm.add(passwordBlock);
            // Current password
            final FieldData currentPasswordFieldData = passwordUrl.getCurrentPasswordParameter().pickFieldData();
            final HtmlPasswordField currentPasswordInput = new HtmlPasswordField(currentPasswordFieldData.getName(), tr("Current password"));
            currentPasswordInput.addErrorMessages(currentPasswordFieldData.getErrorMessages());
            currentPasswordInput.addAttribute("autocomplete", "off");
            passwordBlock.add(currentPasswordInput);

            // Password
            final FieldData passwordFieldData = passwordUrl.getPasswordParameter().pickFieldData();
            final HtmlPasswordField passwordInput = new HtmlPasswordField(passwordFieldData.getName(), tr("New password"));
            passwordInput.addErrorMessages(passwordFieldData.getErrorMessages());
            passwordInput.addAttribute("autocomplete", "off");
            passwordInput.setComment(Context.tr("7 characters minimum."));
            passwordBlock.add(passwordInput);

            // Password check
            final FieldData passwordCheckFieldData = passwordUrl.getPasswordCheckParameter().pickFieldData();
            final HtmlPasswordField passwordCheckInput = new HtmlPasswordField(passwordCheckFieldData.getName(), tr("Reenter new password"));
            passwordCheckInput.addErrorMessages(passwordCheckFieldData.getErrorMessages());
            passwordCheckInput.addAttribute("autocomplete", "off");
            passwordBlock.add(passwordCheckInput);
            passwordBlock.add(new HtmlSubmit(Context.tr("Submit")));

            // ///////
            // Details
            final ModifyDetailActionUrl detailUrl = new ModifyDetailActionUrl(getSession().getShortKey());
            final HtmlForm detailForm = new HtmlForm(detailUrl.urlString());
            layout.addLeft(detailForm);
            final HtmlFormBlock detailBlock = new HtmlFormBlock(Context.tr("Details"));
            detailForm.add(detailBlock);
            // Email
            final FieldData emailFieldData = detailUrl.getEmailParameter().pickFieldData();
            final HtmlTextField emailInput = new HtmlTextField(emailFieldData.getName(), tr("Email"));
            if (loggedUser.hasEmailToActivate()) {
                emailInput.setComment(Context.tr("Waiting for activation: {0}", loggedUser.getEmailToActivate()));
            }
            if (emailFieldData.getSuggestedValue() != null && !emailFieldData.getSuggestedValue().isEmpty()) {
                emailInput.setDefaultValue(emailFieldData.getSuggestedValue());
            } else {
                emailInput.setDefaultValue(loggedUser.getEmail());
            }
            emailInput.addErrorMessages(emailFieldData.getErrorMessages());
            detailBlock.add(emailInput);

            // Country
            final HtmlDropDown countryInput = new HtmlDropDown(detailUrl.getCountryParameter().getName(), tr("Country"));
            for (final Country entry : Country.getAvailableCountries()) {
                countryInput.addDropDownElement(entry.getCode(), entry.getName());
            }
            if (detailUrl.getCountryParameter().getStringValue() != null && !detailUrl.getCountryParameter().getStringValue().isEmpty()) {
                countryInput.setDefaultValue(detailUrl.getCountryParameter().getStringValue());
            } else {
                countryInput.setDefaultValue(loggedUser.getLocale().getCountry());
            }
            detailBlock.add(countryInput);

            // Language
            final LanguageSelector langInput = new LanguageSelector(detailUrl.getLangParameter().getName(), tr("Language"));
            langInput.setDefaultValue(detailUrl.getLangParameter().getStringValue(), loggedUser.getLocale().getLanguage());
            detailBlock.add(langInput);
            detailBlock.add(new HtmlSubmit(Context.tr("Submit")));

        } catch (final UnauthorizedOperationException e) {
            throw new ShallNotPassException("Couldn't access logged member information", e);
        }

        layout.addRight(new SideBarDocumentationBlock("modify_member"));

        return layout;
    }

    @Override
    public String getRefusalReason() {
        return Context.tr("You must be logged to modify your personal informations.");
    }

    @Override
    protected String createPageTitle() {
        return Context.tr("Modify member informations");
    }

    @Override
    public boolean isStable() {
        return false;
    }

    @Override
    protected Breadcrumb createBreadcrumb(final Member member) {
        return generateBreadcrumb(member);
    }

    private static Breadcrumb generateBreadcrumb(final Member member) {
        final Breadcrumb breadcrumb = MemberPage.generateBreadcrumb(member);
        breadcrumb.pushLink(new ModifyMemberPageUrl().getHtmlLink(Context.tr("modify")));
        return breadcrumb;
    }
}

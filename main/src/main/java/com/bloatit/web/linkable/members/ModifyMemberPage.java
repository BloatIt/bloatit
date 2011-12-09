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

import com.bloatit.framework.exceptions.highlevel.ShallNotPassException;
import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.utils.i18n.Country;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.ParamContainer.Protocol;
import com.bloatit.framework.webprocessor.components.HtmlTitle;
import com.bloatit.framework.webprocessor.components.form.FormBuilder;
import com.bloatit.framework.webprocessor.components.form.HtmlCheckbox;
import com.bloatit.framework.webprocessor.components.form.HtmlDropDown;
import com.bloatit.framework.webprocessor.components.form.HtmlFileInput;
import com.bloatit.framework.webprocessor.components.form.HtmlFormField.LabelPosition;
import com.bloatit.framework.webprocessor.components.form.HtmlPasswordField;
import com.bloatit.framework.webprocessor.components.form.HtmlSubmit;
import com.bloatit.framework.webprocessor.components.form.HtmlTextArea;
import com.bloatit.framework.webprocessor.components.form.HtmlTextField;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.Member;
import com.bloatit.model.right.UnauthorizedOperationException;
import com.bloatit.web.components.HtmlElveosForm;
import com.bloatit.web.components.LanguageSelector;
import com.bloatit.web.linkable.documentation.SideBarDocumentationBlock;
import com.bloatit.web.linkable.master.Breadcrumb;
import com.bloatit.web.linkable.master.LoggedElveosPage;
import com.bloatit.web.linkable.master.sidebar.TwoColumnLayout;
import com.bloatit.web.url.ModifyDetailActionUrl;
import com.bloatit.web.url.ModifyMemberActionUrl;
import com.bloatit.web.url.ModifyMemberPageUrl;
import com.bloatit.web.url.ModifyNewsletterActionUrl;
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
            final HtmlElveosForm nameForm = new HtmlElveosForm(targetUrl.urlString());
            nameForm.enableFileUpload();
            layout.addLeft(nameForm);
            FormBuilder nameFBuilder = new FormBuilder(ModifyMemberAction.class, targetUrl);

            // Full name
            HtmlTextField nameField = new HtmlTextField(targetUrl.getFullnameParameter().getName());
            nameFBuilder.add(nameForm, nameField);
            nameFBuilder.setDefaultValueIfNeeded(nameField, loggedUser.getFullname());

            // User description
            HtmlTextArea description = new HtmlTextArea(targetUrl.getDescriptionParameter().getName(), 4, 75);
            nameFBuilder.add(nameForm, description);
            nameFBuilder.setDefaultValueIfNeeded(description, loggedUser.getDescription());

            // Avatar
            nameFBuilder.add(nameForm, new HtmlFileInput(targetUrl.getAvatarParameter().getName()));

            // Delete avatar
            HtmlCheckbox deleteAvatar = new HtmlCheckbox(targetUrl.getDeleteAvatarParameter().getName(), LabelPosition.BEFORE);
            nameFBuilder.add(nameForm, deleteAvatar);
            if (loggedUser.getAvatar() == null && loggedUser.getAvatar().isNull()) {
                deleteAvatar.addAttribute("disabled", "disabled");
            }
            nameForm.addSubmit(new HtmlSubmit(Context.tr("Submit")));

            // ///////
            // password
            final ModifyPasswordActionUrl passwordUrl = new ModifyPasswordActionUrl(getSession().getShortKey());
            final HtmlElveosForm passwordForm = new HtmlElveosForm(passwordUrl.urlString());
            layout.addLeft(passwordForm);
            FormBuilder ftool = new FormBuilder(ModifyPasswordAction.class, passwordUrl);
            ftool.add(passwordForm, new HtmlPasswordField(passwordUrl.getCurrentPasswordParameter().getName()));
            ftool.add(passwordForm, new HtmlPasswordField(passwordUrl.getPasswordParameter().getName()));
            passwordForm.addSubmit(new HtmlSubmit(Context.tr("Submit")));

            // ///////
            // Details
            final ModifyDetailActionUrl detailUrl = new ModifyDetailActionUrl(getSession().getShortKey());
            final HtmlElveosForm detailForm = new HtmlElveosForm(detailUrl.urlString());
            layout.addLeft(detailForm);
            FormBuilder detailFBuilder = new FormBuilder(ModifyDetailAction.class, detailUrl);

            // Email
            HtmlTextField emailInput = new HtmlTextField(detailUrl.getEmailParameter().getName());
            detailFBuilder.add(detailForm, emailInput);
            if (loggedUser.hasEmailToActivate()) {
                emailInput.setComment(Context.tr("Waiting for activation: {0}", loggedUser.getEmailToActivate()));
            }
            detailFBuilder.setDefaultValueIfNeeded(emailInput, loggedUser.getEmail());

            // Country
            final HtmlDropDown countryInput = new HtmlDropDown(detailUrl.getCountryParameter().getName());
            detailFBuilder.add(detailForm, countryInput);
            for (final Country entry : Country.getAvailableCountries()) {
                countryInput.addDropDownElement(entry.getCode(), entry.getName());
            }
            detailFBuilder.setDefaultValueIfNeeded(countryInput, loggedUser.getLocale().getCountry());

            // Language
            final LanguageSelector langInput = new LanguageSelector(detailUrl.getLangParameter().getName());
            detailFBuilder.add(detailForm, langInput);
            langInput.setDefaultValue(detailUrl.getLangParameter().getStringValue(), loggedUser.getLocale().getLanguage());
            detailForm.addSubmit(new HtmlSubmit(Context.tr("Submit")));

            // Newsletter
            ModifyNewsletterActionUrl nlUrl = new ModifyNewsletterActionUrl(getSession().getShortKey());
            final HtmlElveosForm nlForm = new HtmlElveosForm(nlUrl.urlString());
            layout.addLeft(nlForm);
            FormBuilder nlFBuilder = new FormBuilder(ModifyNewsletterAction.class, nlUrl);

            HtmlCheckbox nlInput = new HtmlCheckbox(nlUrl.getNewsletterParameter().getName(), LabelPosition.BEFORE);
            nlFBuilder.add(nlForm, nlInput);
            nlFBuilder.setDefaultValueIfNeeded(nlInput, String.valueOf(loggedUser.getNewsletterAccept()));
            nlForm.addSubmit(new HtmlSubmit(Context.tr("Submit")));

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

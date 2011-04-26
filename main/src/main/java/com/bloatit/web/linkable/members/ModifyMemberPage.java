package com.bloatit.web.linkable.members;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import com.bloatit.framework.exceptions.highlevel.ShallNotPassException;
import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.exceptions.lowlevel.UnauthorizedOperationException;
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
import com.bloatit.framework.webprocessor.components.form.HtmlTextField;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.Member;
import com.bloatit.web.components.LanguageSelector;
import com.bloatit.web.linkable.documentation.SideBarDocumentationBlock;
import com.bloatit.web.pages.LoggedPage;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.pages.master.sidebar.TwoColumnLayout;
import com.bloatit.web.url.ModifyMemberActionUrl;
import com.bloatit.web.url.ModifyMemberPageUrl;

@ParamContainer(value="member/modify", protocol=Protocol.HTTPS)
public class ModifyMemberPage extends LoggedPage {
    ModifyMemberPageUrl url;

    public ModifyMemberPage(ModifyMemberPageUrl url) {
        super(url);
        this.url = url;
    }

    @Override
    public HtmlElement createRestrictedContent(Member loggedUser) throws RedirectException {
        TwoColumnLayout layout = new TwoColumnLayout(true, url);

        ModifyMemberActionUrl targetUrl = new ModifyMemberActionUrl();

        HtmlTitle title = new HtmlTitle(1);
        title.addText(Context.tr("Change account settings"));
        layout.addLeft(title);

        HtmlForm form = new HtmlForm(targetUrl.urlString());
        layout.addLeft(form);
        form.enableFileUpload();

        try {
            HtmlFormBlock nameBlock = new HtmlFormBlock(Context.tr("User name"));
            form.add(nameBlock);
            // ///////
            // Full name
            final FieldData fullnameFieldData = targetUrl.getFullnameParameter().pickFieldData();
            final HtmlTextField fullnameInput = new HtmlTextField(fullnameFieldData.getName(), tr("Full name"));
            fullnameInput.addErrorMessages(fullnameFieldData.getErrorMessages());
            if (fullnameFieldData.getSuggestedValue() != null && !fullnameFieldData.getSuggestedValue().isEmpty()) {
                fullnameInput.setDefaultValue(fullnameFieldData.getSuggestedValue());
            } else if (loggedUser.getFullname() != null) {
                fullnameInput.setDefaultValue(loggedUser.getFullname());
            }
            fullnameInput.setComment(Context.tr("If you set a value to your fullname, it will be used instead of your login to designate you."));
            nameBlock.add(fullnameInput);

            // ///////
            // Delete Full name
            final FieldData deleteFNFieldData = targetUrl.getDeleteFullNameParameter().pickFieldData();
            final HtmlCheckbox deleteFN = new HtmlCheckbox(deleteFNFieldData.getName(), Context.tr("Delete full name"), LabelPosition.BEFORE);
            if (loggedUser.getFullname() == null || loggedUser.getFullname().isEmpty()) {
                deleteFN.addAttribute("disabled", "disabled");
            }
            deleteFN.setComment(Context.tr("Checking this box will delete your full name, hence your login will be used again."));
            nameBlock.add(deleteFN);

            HtmlFormBlock passwordBlock = new HtmlFormBlock(Context.tr("User password"));
            form.add(passwordBlock);
            // ///////
            // Current password
            final FieldData currentPasswordFieldData = targetUrl.getCurrentPasswordParameter().pickFieldData();
            final HtmlPasswordField currentPasswordInput = new HtmlPasswordField(currentPasswordFieldData.getName(), tr("Current password"));
            currentPasswordInput.addErrorMessages(currentPasswordFieldData.getErrorMessages());
            currentPasswordInput.setComment(Context.tr("This is useful only if you intend to change your password."));
            passwordBlock.add(currentPasswordInput);

            // ///////
            // Password
            final FieldData passwordFieldData = targetUrl.getPasswordParameter().pickFieldData();
            final HtmlPasswordField passwordInput = new HtmlPasswordField(passwordFieldData.getName(), tr("New password"));
            passwordInput.addErrorMessages(passwordFieldData.getErrorMessages());
            passwordBlock.add(passwordInput);

            // ///////
            // Password check
            final FieldData passwordCheckFieldData = targetUrl.getPasswordCheckParameter().pickFieldData();
            final HtmlPasswordField passwordCheckInput = new HtmlPasswordField(passwordCheckFieldData.getName(), tr("Reenter new password"));
            passwordCheckInput.addErrorMessages(passwordCheckFieldData.getErrorMessages());
            passwordBlock.add(passwordCheckInput);

            // ///////
            // Email
            final FieldData emailFieldData = targetUrl.getEmailParameter().pickFieldData();
            final HtmlTextField emailInput = new HtmlTextField(emailFieldData.getName(), tr("Email"));
            if (emailFieldData.getSuggestedValue() != null && !emailFieldData.getSuggestedValue().isEmpty()) {
                emailInput.setDefaultValue(emailFieldData.getSuggestedValue());
            } else {
                emailInput.setDefaultValue(loggedUser.getEmail());
            }
            emailInput.addErrorMessages(emailFieldData.getErrorMessages());
            form.add(emailInput);

            // ///////
            // Country
            final HtmlDropDown countryInput = new HtmlDropDown(targetUrl.getCountryParameter().getName(), tr("Country"));
            for (final Country entry : Country.getAvailableCountries()) {
                countryInput.addDropDownElement(entry.getCode(), entry.getName());
            }
            if (targetUrl.getCountryParameter().getStringValue() != null && !targetUrl.getCountryParameter().getStringValue().isEmpty()) {
                countryInput.setDefaultValue(targetUrl.getCountryParameter().getStringValue());
            } else {
                countryInput.setDefaultValue(loggedUser.getLocale().getCountry());
            }
            form.add(countryInput);

            // ///////
            // Language
            final LanguageSelector langInput = new LanguageSelector(targetUrl.getLangParameter().getName(), tr("Language"));
            langInput.setDefaultValue(targetUrl.getLangParameter().getStringValue(), loggedUser.getLocale().getLanguage());
            form.add(langInput);

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
            deleteAvatar.setComment(Context.tr("Checking this box will delete your avatar. If you have a libravatar it will be used instead."));
            form.add(deleteAvatar);

            final HtmlSubmit submit = new HtmlSubmit(Context.tr("Submit"));
            form.add(submit);
        } catch (UnauthorizedOperationException e) {
            throw new ShallNotPassException("Couldn't access logged member information", e);
        }

        layout.addRight(new SideBarDocumentationBlock("modify_member"));

        return layout;
    }

    @Override
    public String getRefusalReason() {
        return Context.tr("You must be logged to modify your personnal information");
    }

    @Override
    protected String createPageTitle() {
        return Context.tr("Modify member information");
    }

    @Override
    public boolean isStable() {
        return false;
    }

    @Override
    protected Breadcrumb createBreadcrumb() {
        return generateBreadcrumb(Context.getSession().getAuthToken().getMember());
    }

    public static Breadcrumb generateBreadcrumb(final Member member) {
        final Breadcrumb breadcrumb = MemberPage.generateBreadcrumb(member);
        breadcrumb.pushLink(new ModifyMemberPageUrl().getHtmlLink(Context.tr("modify")));
        return breadcrumb;
    }
}

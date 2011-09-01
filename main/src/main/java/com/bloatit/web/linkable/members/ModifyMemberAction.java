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

import static com.bloatit.framework.utils.StringUtils.isEmpty;

import java.util.List;
import java.util.Locale;

import com.bloatit.common.Log;
import com.bloatit.framework.exceptions.highlevel.ShallNotPassException;
import com.bloatit.framework.mailsender.Mail;
import com.bloatit.framework.mailsender.MailServer;
import com.bloatit.framework.utils.FileConstraintChecker;
import com.bloatit.framework.webprocessor.annotations.MaxConstraint;
import com.bloatit.framework.webprocessor.annotations.MinConstraint;
import com.bloatit.framework.webprocessor.annotations.Optional;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.ParamContainer.Protocol;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.FileMetadata;
import com.bloatit.model.Member;
import com.bloatit.model.managers.FileMetadataManager;
import com.bloatit.model.managers.MemberManager;
import com.bloatit.model.right.UnauthorizedOperationException;
import com.bloatit.web.actions.LoggedElveosAction;
import com.bloatit.web.url.MemberActivationActionUrl;
import com.bloatit.web.url.MemberPageUrl;
import com.bloatit.web.url.ModifyMemberActionUrl;
import com.bloatit.web.url.ModifyMemberPageUrl;

@ParamContainer(value = "member/domodify", protocol = Protocol.HTTPS)
public class ModifyMemberAction extends LoggedElveosAction {
    @RequestParam(role = Role.POST)
    @Optional
    @MinConstraint(
        min = 7,
        message = @tr("Number of characters for password has to be superior to %constraint% but your text is %valueLength% characters long."))
    @MaxConstraint(
        max = 255,
        message = @tr("Number of characters for password has to be inferior to %constraint% but your text is %valueLength% characters long."))
    private final String password;

    @RequestParam(role = Role.POST)
    @Optional
    private final String currentPassword;

    @RequestParam(role = Role.POST)
    @Optional
    @MinConstraint(
        min = 7,
        message = @tr("Number of characters for password check has to be superior to %constraint% but your text is %valueLength% characters long."))
    @MaxConstraint(
        max = 255,
        message = @tr("Number of characters for password check has to be inferior to %constraint% but your text is %valueLength% characters long."))
    private final String passwordCheck;

    @RequestParam(role = Role.POST)
    @Optional
    @MinConstraint(
        min = 4,
        message = @tr("Number of characters for email has to be superior to %constraint% but your text is %valueLength% characters long."))
    @MaxConstraint(
        max = 255,
        message = @tr("Number of characters for email has to be inferior to %constraint% but your text is %valueLength% characters long."))
    private final String email;

    @RequestParam(role = Role.POST)
    @Optional
    @MinConstraint(
        min = 1,
        message = @tr("Number of characters for Fullname has to be superior to %constraint% but your text is %valueLength% characters long."))
    @MaxConstraint(
        max = 30,
        message = @tr("Number of characters for Fullname has to be inferior to %constraint% but your text is %valueLength% characters long."))
    private final String fullname;

    @RequestParam(role = Role.POST)
    @Optional
    private final Boolean deleteFullName;

    @RequestParam(role = Role.POST)
    @Optional
    private final Boolean deleteAvatar;

    @RequestParam(name = "avatar", role = Role.POST)
    @Optional
    private final String avatar;

    @RequestParam(name = "avatar/filename", role = Role.POST)
    @Optional
    private final String avatarFileName;

    @SuppressWarnings("unused")
    @RequestParam(name = "avatar/contenttype", role = Role.POST)
    @Optional
    private final String avatarContentType;

    @RequestParam(role = Role.POST)
    @Optional
    private final String country;

    @RequestParam(role = Role.POST)
    @Optional
    private final String lang;

    private final ModifyMemberActionUrl url;

    public ModifyMemberAction(final ModifyMemberActionUrl url) {
        super(url);
        this.password = url.getPassword();
        this.passwordCheck = url.getPasswordCheck();
        this.email = url.getEmail();
        this.fullname = url.getFullname();
        this.avatar = url.getAvatar();
        this.avatarFileName = url.getAvatarFileName();
        this.avatarContentType = url.getAvatarContentType();
        this.country = url.getCountry();
        this.lang = url.getLang();
        this.deleteFullName = url.getDeleteFullName();
        this.currentPassword = url.getCurrentPassword();
        this.deleteAvatar = url.getDeleteAvatar();
        this.url = url;
    }

    @Override
    protected Url doProcessRestricted(final Member me) {
        try {
            // PASSWORD
            if (password != null && !password.trim().isEmpty() && me.checkPassword(currentPassword.trim()) && !me.checkPassword(password.trim())) {
                session.notifyGood(Context.tr("Password updated."));
                me.setPassword(password.trim());
            }

            // EMAIL
            if (email != null && !email.trim().isEmpty() && !email.equals(me.getEmail())) {
                session.notifyGood(Context.tr("New email will replace the old one after you validate it with the link we send you."));
                me.setEmailToActivate(email.trim());

                final String activationKey = me.getEmailActivationKey();
                final MemberActivationActionUrl url = new MemberActivationActionUrl(me.getLogin(), activationKey);

                final String content = Context.tr("Hi {0},\n\nYou wanted to change the email for your Elveos.org account. Please click on the following link to activate your new email: \n\n {1}",
                                                  me.getLogin(),
                                                  url.externalUrlString());

                final Mail activationMail = new Mail(email, Context.tr("Elveos.org new email activation"), content, "member-domodify");

                MailServer.getInstance().send(activationMail);

            }

            // FULLNAME
            if (deleteFullName != null && deleteFullName.booleanValue()) {
                session.notifyGood(Context.tr("Deleted fullname."));
                me.setFullname(null);
            } else if (fullname != null && !fullname.trim().isEmpty() && !fullname.equals(me.getFullname())) {
                session.notifyGood(Context.tr("Fullname updated."));
                me.setFullname(fullname.trim());
            }

            // LANGUAGE
            String langString = null;
            if (lang != null && !lang.isEmpty() && !lang.equals(me.getLocale().getLanguage())) {
                session.notifyGood(Context.tr("Language updated."));
                langString = lang;
            }

            // COUNTRY
            String countryString = null;
            if (country != null && !country.isEmpty() && !country.equals(me.getLocale().getCountry())) {
                session.notifyGood(Context.tr("Country updated."));
                countryString = country;
            }

            // LOCALE
            if (langString != null || countryString != null) {
                final String locale = ((langString != null) ? langString : me.getLocale().getLanguage()) + "_"
                        + ((countryString != null) ? countryString : me.getLocale().getCountry());
                me.setLocal(new Locale(locale));
            }

            // AVATAR
            if (avatar != null) {
                final FileConstraintChecker fcc = new FileConstraintChecker(avatar);
                final List<String> imageErr = fcc.isImageAvatar();
                if (!isEmpty(avatarFileName) && imageErr == null) {
                    final FileMetadata file = FileMetadataManager.createFromTempFile(me, null, avatar, avatarFileName, "");
                    me.setAvatar(file);
                    session.notifyGood(Context.tr("Avatar updated."));
                } else {
                    if (imageErr != null) {
                        for (final String message : imageErr) {
                            session.notifyWarning(message);
                        }
                    }
                    if (isEmpty(avatarFileName)) {
                        session.notifyError(Context.tr("Filename is empty. Could you report that bug?"));
                    }
                    transmitParameters();
                    return doProcessErrors();
                }
            }

            // DELETE AVATAR
            if (deleteAvatar != null && deleteAvatar.booleanValue()) {
                session.notifyGood(Context.tr("Deleted avatar."));
                me.setAvatar(null);
            }

        } catch (final UnauthorizedOperationException e) {
            throw new ShallNotPassException(e);
        }

        return new MemberPageUrl(me);
    }

    @Override
    protected Url checkRightsAndEverything(final Member me) {
        boolean error = false;

        // Password
        if (!((isEmpty(password) && isEmpty(passwordCheck)) || (password != null && password.equals(passwordCheck)))) {
            session.notifyError(Context.tr("New password must be equal to password verification."));
            url.getPasswordCheckParameter().addErrorMessage(Context.tr("New password must be equal to password verification."));
            error = true;
        }
        if (!isEmpty(password)) {
            if (isEmpty(currentPassword)) {
                session.notifyError(Context.tr("You must input your current password to change password."));
                url.getCurrentPasswordParameter().addErrorMessage(Context.tr("You must input your current password to change password."));
                error = true;
            } else if (!me.checkPassword(currentPassword)) {
                session.notifyError(Context.tr("Your input for current password doesn't match your password."));
                url.getCurrentPasswordParameter().addErrorMessage(Context.tr("Your input for current password doesn't match your password."));
                error = true;
            }
        }

        // 2 if, because conditions are a bit rough to check in a single
        // statement ...
        if (!isEmpty(me.getFullname()) && !isEmpty(fullname) && !me.getFullname().equals(fullname)) {
            if (deleteFullName != null && deleteFullName.booleanValue()) {
                session.notifyError(Context.tr("You cannot delete your fullname, and indicate a new value at the same time."));
                url.getFullnameParameter().addErrorMessage(Context.tr("You cannot delete your fullname, and indicate a new value at the same time."));
                error = true;
            }
        }

        try {
            if (email != null && !email.trim().isEmpty() && !email.equals(me.getEmail()) && MemberManager.emailExists(email)) {
                session.notifyError(Context.tr("Email already used."));
                url.getEmailParameter().addErrorMessage(Context.tr("Email already used."));
                error = true;
            }
        } catch (final UnauthorizedOperationException e) {
            session.notifyWarning(Context.tr("Fail to read your email."));
            Log.web().error("Fail to read an email", e);
            error = true;
        }

        // Avatar and delete avatar
        if (deleteAvatar != null && deleteAvatar.booleanValue() && me.getAvatar() != null && !me.getAvatar().isNull()) {
            if (!isEmpty(avatar)) {
                url.getAvatarParameter().addErrorMessage(Context.tr("You cannot delete your avatar, and indicate a new one at the same time."));
                error = true;
            }
        }

        if (error) {
            return new ModifyMemberPageUrl();
        }
        return NO_ERROR;

    }

    @Override
    protected Url doProcessErrors() {
        return new ModifyMemberPageUrl();
    }

    @Override
    protected String getRefusalReason() {
        return Context.tr("You must be logged to modify your account settings");
    }

    @Override
    protected void transmitParameters() {

        if (url.getCurrentPasswordParameter().getValue() != null) {
            url.getCurrentPasswordParameter().setValue("xxxxxxxx");
        }
        session.addParameter(url.getCurrentPasswordParameter());

        session.addParameter(url.getPasswordParameter());
        session.addParameter(url.getPasswordCheckParameter());
        session.addParameter(url.getEmailParameter());
        session.addParameter(url.getFullnameParameter());
        session.addParameter(url.getAvatarParameter());
        session.addParameter(url.getAvatarFileNameParameter());
        session.addParameter(url.getAvatarContentTypeParameter());
        session.addParameter(url.getCountryParameter());
        session.addParameter(url.getLangParameter());
        session.addParameter(url.getDeleteFullNameParameter());
    }
}

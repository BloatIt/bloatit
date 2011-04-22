package com.bloatit.web.linkable.members;

import static com.bloatit.framework.utils.StringUtils.isEmpty;

import java.util.List;
import java.util.Locale;

import com.bloatit.framework.exceptions.highlevel.ShallNotPassException;
import com.bloatit.framework.exceptions.lowlevel.UnauthorizedOperationException;
import com.bloatit.framework.utils.FileConstraintChecker;
import com.bloatit.framework.webprocessor.annotations.Optional;
import com.bloatit.framework.webprocessor.annotations.ParamConstraint;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.FileMetadata;
import com.bloatit.model.Member;
import com.bloatit.model.managers.FileMetadataManager;
import com.bloatit.web.actions.LoggedAction;
import com.bloatit.web.url.MemberPageUrl;
import com.bloatit.web.url.ModifyMemberActionUrl;
import com.bloatit.web.url.ModifyMemberPageUrl;

@ParamContainer("member/domodify")
public class ModifyMemberAction extends LoggedAction {
    @RequestParam(role = Role.POST)
    @Optional
    @ParamConstraint(min = "4", minErrorMsg = @tr("Number of characters for password has to be superior to %constraint% but your text is %valueLength% characters long."),//
    max = "15", maxErrorMsg = @tr("Number of characters for password has to be inferior to %constraint% but your text is %valueLength% characters long."))
    private final String password;

    @RequestParam(role = Role.POST)
    @Optional
    private final String currentPassword;

    @RequestParam(role = Role.POST)
    @Optional
    @ParamConstraint(min = "4", minErrorMsg = @tr("Number of characters for password check has to be superior to %constraint% but your text is %valueLength% characters long."),//
    max = "15", maxErrorMsg = @tr("Number of characters for password check has to be inferior to %constraint% but your text is %valueLength% characters long."))
    private final String passwordCheck;

    @RequestParam(role = Role.POST)
    @Optional
    @ParamConstraint(min = "4", minErrorMsg = @tr("Number of characters for email has to be superior to %constraint% but your text is %valueLength% characters long."),//
    max = "30", maxErrorMsg = @tr("Number of characters for email has to be inferior to %constraint% but your text is %valueLength% characters long."))
    private final String email;

    @RequestParam(role = Role.POST)
    @Optional
    @ParamConstraint(min = "6", minErrorMsg = @tr("Number of characters for Fullname has to be superior to %constraint% but your text is %valueLength% characters long."),//
    max = "30", maxErrorMsg = @tr("Number of characters for Fullname has to be inferior to %constraint% but your text is %valueLength% characters long."))
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

    private ModifyMemberActionUrl url;

    public ModifyMemberAction(ModifyMemberActionUrl url) {
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
    protected Url doProcessRestricted(Member me) {
        try {
            // PASSWORD
            if (password != null && !password.trim().isEmpty() && me.checkPassword(currentPassword.trim()) && !me.checkPassword(password.trim())) {
                session.notifyGood(Context.tr("Password updated."));
                me.setPassword(password.trim());
            }

            // EMAIL
            if (email != null && !email.trim().isEmpty() && !email.equals(me.getEmail())) {
                session.notifyGood(Context.tr("Email updated."));
                me.setEmail(email.trim());
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
                String locale = (langString != null) ? langString : me.getLocale().getLanguage() + "_"
                        + ((countryString != null) ? countryString : me.getLocale().getCountry());
                me.setLocal(new Locale(locale));
            }

            // AVATAR
            if (avatar != null) {
                FileConstraintChecker fcc = new FileConstraintChecker(avatar);
                List<String> imageErr = fcc.isImageAvatar();
                if (!isEmpty(avatarFileName) && imageErr == null) {
                    FileMetadata file = FileMetadataManager.createFromTempFile(me, null, avatar, avatarFileName, "");
                    me.setAvatar(file);
                    session.notifyGood(Context.tr("Avatar updated."));
                } else {
                    if (imageErr != null) {
                        for (final String message : imageErr) {
                            session.notifyBad(message);
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

        } catch (UnauthorizedOperationException e) {
            e.printStackTrace();
        }

        return new MemberPageUrl(me);
    }

    @Override
    protected Url doCheckRightsAndEverything(Member me) {
        // TODO: link error messages to form field, so they an be red
        boolean error = false;

        // Password
        if (!((isEmpty(password) && isEmpty(passwordCheck)) || (password != null && password.equals(passwordCheck)))) {
            session.notifyBad(Context.tr("New password must be equal to password verification."));
            error = true;
        }
        if (!isEmpty(password)) {
            if (isEmpty(currentPassword)) {
                session.notifyBad(Context.tr("You must input your current password to change password."));
                error = true;
            } else if (!me.checkPassword(currentPassword)) {
                session.notifyBad(Context.tr("Your input for current password doesn't match your password."));
                error = true;
            }
        }

        // 2 if, because conditions are a bit rough to check in a single
        // statement ...
        if (!isEmpty(me.getFullname()) && !isEmpty(fullname) && !me.getFullname().equals(fullname)) {
            if (deleteFullName != null && deleteFullName.booleanValue()) {
                session.notifyBad(Context.tr("You cannot delete your fullname, and indicate a new value at the same time."));
                error = true;
            }
        }

        // Avatar and delete avatar
        if (deleteAvatar != null && deleteAvatar.booleanValue() && me.getAvatar() != null && !me.getAvatar().isNull()) {
            if (!isEmpty(avatar)) {
                session.notifyBad(Context.tr("You cannot delete your avatar, and indicate a new one at the same time."));
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

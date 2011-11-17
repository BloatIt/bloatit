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
package com.bloatit.web.linkable.login;

import com.bloatit.framework.exceptions.highlevel.ShallNotPassException;
import com.bloatit.framework.utils.StringUtils;
import com.bloatit.framework.webprocessor.annotations.MaxConstraint;
import com.bloatit.framework.webprocessor.annotations.MinConstraint;
import com.bloatit.framework.webprocessor.annotations.NonOptional;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.ParamContainer.Protocol;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.context.User.ActivationState;
import com.bloatit.framework.webprocessor.url.PageNotFoundUrl;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.Member;
import com.bloatit.model.managers.MemberManager;
import com.bloatit.model.right.AuthToken;
import com.bloatit.model.right.UnauthorizedOperationException;
import com.bloatit.web.linkable.master.ElveosAction;
import com.bloatit.web.url.RecoverPasswordActionUrl;
import com.bloatit.web.url.RecoverPasswordPageUrl;

/**
 * Action part of the password recovery process.
 * <p>
 * This action is used after {@link RecoverPasswordPage}
 * </p>
 */
@ParamContainer(value = "member/password/dorecover", protocol = Protocol.HTTPS)
public class RecoverPasswordAction extends ElveosAction {
    private final RecoverPasswordActionUrl url;

    private Member member;

    @RequestParam(role = Role.GET)
    @NonOptional(@tr("Key cannot be blank. Please make sure you didn't make a mistake while copying and pasting."))
    private final String resetKey;

    @RequestParam(role = Role.GET)
    @NonOptional(@tr("Login cannot be blank. Please make sure you didn't make a mistake while copying and pasting."))
    private final String login;

    @RequestParam(role = Role.POST)
    @NonOptional(@tr("Password cannot be blank."))
    @MinConstraint(min = 7, message = @tr("Minimal length for new password is %constraint%."))
    @MaxConstraint(max = 255, message = @tr("Number of characters for password has to be inferior to %constraint%."))
    private final String newPassword;

    @RequestParam(role = Role.POST)
    @NonOptional(@tr("Password check cannot be blank."))
    private final String checkNewPassword;

    public RecoverPasswordAction(final RecoverPasswordActionUrl url) {
        super(url);
        this.url = url;
        this.newPassword = url.getNewPassword();
        this.checkNewPassword = url.getCheckNewPassword();
        this.login = url.getLogin();
        this.resetKey = url.getResetKey();
    }

    @Override
    protected Url doProcess() {

        if (member.getActivationState() == ActivationState.VALIDATING) {
            member.activate(resetKey);
        }

        AuthToken.authenticate(member);
        try {
            member.setPassword(newPassword);
        } catch (final UnauthorizedOperationException e) {
            throw new ShallNotPassException("Error setting user password.", e);
        }
        session.notifyGood(Context.tr("Password change successful, you are now logged."));
        return session.pickPreferredPage();
    }

    @Override
    protected Url doProcessErrors() {
        if (StringUtils.isEmpty(login) || StringUtils.isEmpty(resetKey)) {
            session.notifyWarning(Context.tr("The URL you inputed is incorrect, please verify you didn't do a mistake while cutting and pasting."));
            return new PageNotFoundUrl();
        }
        return new RecoverPasswordPageUrl(login, resetKey);
    }

    @Override
    protected Url checkRightsAndEverything() {
        if (!newPassword.equals(checkNewPassword)) {
            session.notifyWarning(Context.tr("Password doesn't match confirmation."));
            url.getNewPasswordParameter().addErrorMessage(Context.tr("New password doesn't match with confirmation."));
            url.getCheckNewPasswordParameter().addErrorMessage(Context.tr("Confirmation doesn't match with new password."));
            return new RecoverPasswordPageUrl(resetKey, login);
        }
        member = MemberManager.getMemberByLogin(login);
        if (member == null || !member.getResetKey().equals(resetKey)) {
            session.notifyWarning(Context.tr("The login and/or key are invalid, please verify you didn't do a mistake while cutting and pasting."));
            return new PageNotFoundUrl();
        }
        return NO_ERROR;
    }

    @Override
    protected void transmitParameters() {
        session.addParameter(url.getNewPasswordParameter());
        session.addParameter(url.getCheckNewPasswordParameter());
    }
}

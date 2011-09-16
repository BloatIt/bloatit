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

import com.bloatit.framework.exceptions.highlevel.ShallNotPassException;
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
import com.bloatit.model.Member;
import com.bloatit.model.right.UnauthorizedOperationException;
import com.bloatit.web.linkable.master.LoggedElveosAction;
import com.bloatit.web.url.MemberPageUrl;
import com.bloatit.web.url.ModifyMemberPageUrl;
import com.bloatit.web.url.ModifyPasswordActionUrl;

@ParamContainer(value = "member/domodifypassword", protocol = Protocol.HTTPS)
public class ModifyPasswordAction extends LoggedElveosAction {
    @RequestParam(role = Role.POST)
    @Optional
    @MinConstraint(min = 7, message = @tr("Number of characters for password has to be superior to %constraint% but your text is %valueLength% characters long."))
    @MaxConstraint(max = 255, message = @tr("Number of characters for password has to be inferior to %constraint% but your text is %valueLength% characters long."))
    private final String password;

    @RequestParam(role = Role.POST)
    @Optional
    private final String currentPassword;

    @RequestParam(role = Role.POST)
    @Optional
    @MinConstraint(min = 7, message = @tr("Number of characters for password check has to be superior to %constraint% but your text is %valueLength% characters long."))
    @MaxConstraint(max = 255, message = @tr("Number of characters for password check has to be inferior to %constraint% but your text is %valueLength% characters long."))
    private final String passwordCheck;

    private final ModifyPasswordActionUrl url;

    public ModifyPasswordAction(final ModifyPasswordActionUrl url) {
        super(url);
        this.password = url.getPassword();
        this.passwordCheck = url.getPasswordCheck();
        this.currentPassword = url.getCurrentPassword();
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

        } catch (final UnauthorizedOperationException e) {
            throw new ShallNotPassException(e);
        }

        return new MemberPageUrl(me);
    }

    @Override
    protected Url checkRightsAndEverything(final Member me) {
        // Password
        if (!((isEmpty(password) && isEmpty(passwordCheck)) || (password != null && password.equals(passwordCheck)))) {
            session.notifyError(Context.tr("New password must be equal to password verification."));
            url.getPasswordCheckParameter().addErrorMessage(Context.tr("New password must be equal to password verification."));
            return doProcessErrors();
        }
        if (!isEmpty(password)) {
            if (isEmpty(currentPassword)) {
                session.notifyError(Context.tr("You must input your current password to change password."));
                url.getCurrentPasswordParameter().addErrorMessage(Context.tr("You must input your current password to change password."));
                return doProcessErrors();
            } else if (!me.checkPassword(currentPassword)) {
                session.notifyError(Context.tr("Your input for current password doesn't match your password."));
                url.getCurrentPasswordParameter().addErrorMessage(Context.tr("Your input for current password doesn't match your password."));
                return doProcessErrors();
            }
        }
        return NO_ERROR;
    }

    @Override
    protected Url doProcessErrors() {
        return new ModifyMemberPageUrl();
    }

    @Override
    protected String getRefusalReason() {
        return Context.tr("You must be logged to modify your account settings.");
    }

    @Override
    protected void transmitParameters() {
        if (url.getCurrentPasswordParameter().getValue() != null) {
            url.getCurrentPasswordParameter().setValue("xxxxxxxx");
        }
        session.addParameter(url.getCurrentPasswordParameter());

        session.addParameter(url.getPasswordParameter());
        session.addParameter(url.getPasswordCheckParameter());
    }
}

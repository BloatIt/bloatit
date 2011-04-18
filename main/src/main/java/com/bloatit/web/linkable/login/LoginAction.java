/*
 * Copyright (C) 2010 BloatIt. This file is part of BloatIt. BloatIt is free
 * software: you can redistribute it and/or modify it under the terms of the GNU
 * Affero General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 * BloatIt is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details. You should have received a copy of the GNU Affero General Public
 * License along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
package com.bloatit.web.linkable.login;

import com.bloatit.framework.webprocessor.annotations.ParamConstraint;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.masters.Action;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.framework.webprocessor.url.UrlParameter;
import com.bloatit.model.managers.LoginManager;
import com.bloatit.model.right.AuthToken;
import com.bloatit.web.url.LoginActionUrl;
import com.bloatit.web.url.LoginPageUrl;

/**
 * A response to a form used to log into the website
 */
@ParamContainer("action/login")
public final class LoginAction extends Action {

    public static final String LOGIN_CODE = "bloatit_login";
    public static final String PASSWORD_CODE = "bloatit_password";

    @ParamConstraint(optionalErrorMsg = @tr("You must enter a login."))
    @RequestParam(name = LOGIN_CODE, role = RequestParam.Role.POST)
    private final String login;

    @ParamConstraint(optionalErrorMsg = @tr("You must enter a password."))
    @RequestParam(name = PASSWORD_CODE, role = RequestParam.Role.POST)
    private final String password;
    private final LoginActionUrl url;

    public LoginAction(final LoginActionUrl url) {
        super(url);
        this.url = url;
        this.login = url.getLogin();
        this.password = url.getPassword();
    }

    @Override
    public Url doProcess() {
        AuthToken token = null;
        token = LoginManager.loginByPassword(login.trim(), password);

        if (token != null) {
            session.setAuthToken(token);
            session.notifyGood(Context.tr("Login success."));
            Context.getLocalizator().forceMemberChoice();
            return session.pickPreferredPage();
        }
        session.setAuthToken(null);
        session.addParameter(url.getLoginParameter());
        session.notifyBad(Context.tr("Login failed. Wrong login or password."));
        transmitParameters();
        return new LoginPageUrl();
    }

    @Override
    protected Url doProcessErrors() {
        return new LoginPageUrl();
    }

    @Override
    protected Url checkRightsAndEverything() {
        return NO_ERROR; // Nothing else to check
    }

    @Override
    protected void transmitParameters() {
        session.addParameter(url.getLoginParameter());
        final UrlParameter<String, String> passwordParameter = url.getPasswordParameter().clone();
        if(passwordParameter.getValue() != null) {
            passwordParameter.setValue("");
        }
        session.addParameter(passwordParameter);
    }
}

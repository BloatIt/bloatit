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
package com.bloatit.web.actions;

import com.bloatit.framework.exceptions.RedirectException;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.annotations.Message.Level;
import com.bloatit.framework.webserver.annotations.ParamConstraint;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.annotations.RequestParam;
import com.bloatit.framework.webserver.annotations.tr;
import com.bloatit.framework.webserver.masters.Action;
import com.bloatit.framework.webserver.url.Url;
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
    @RequestParam(level = Level.ERROR, name = LOGIN_CODE, role = RequestParam.Role.POST)
    private final String login;

    @ParamConstraint(optionalErrorMsg = @tr("You must enter a password."))
    @RequestParam(level = Level.ERROR, name = PASSWORD_CODE, role = RequestParam.Role.POST)
    private final String password;
    private final LoginActionUrl url;

    public LoginAction(final LoginActionUrl url) {
        super(url);
        this.url = url;
        this.login = url.getLogin();
        this.password = url.getPassword();

    }

    @Override
    public Url doProcess() throws RedirectException {
        AuthToken token = null;
        token = LoginManager.loginByPassword(login, password);

        if (token != null) {
            session.setAuthToken(token);
            session.notifyGood(Context.tr("Login success."));
            Context.getLocalizator().forceMemberChoice();
            return session.pickPreferredPage();
        }
        session.setAuthToken(null);
        session.addParameter(url.getLoginParameter());
        session.notifyBad(Context.tr("Login failed. Wrong login or password."));
        return new LoginPageUrl();
    }

    @Override
    protected Url doProcessErrors() throws RedirectException {
        session.notifyList(url.getMessages());

        session.addParameter(url.getLoginParameter());

        return new LoginPageUrl();
    }
}

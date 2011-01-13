/*
 * Copyright (C) 2010 BloatIt. This file is part of BloatIt. BloatIt is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version. BloatIt is distributed in the hope that it will
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public
 * License for more details. You should have received a copy of the GNU Affero General
 * Public License along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
package com.bloatit.web.actions;

import com.bloatit.framework.AuthToken;
import com.bloatit.framework.managers.LoginManager;
import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.ParamContainer;
import com.bloatit.web.annotations.RequestParam;
import com.bloatit.web.exceptions.RedirectException;
import com.bloatit.web.server.Context;
import com.bloatit.web.utils.url.LoginActionUrl;
import com.bloatit.web.utils.url.LoginPageUrl;
import com.bloatit.web.utils.url.Url;

/**
 * A response to a form used to log into the website
 */
@ParamContainer("action/login")
public final class LoginAction extends Action {

    public static final String LOGIN_CODE = "bloatit_login";
    public static final String PASSWORD_CODE = "bloatit_password";

    @RequestParam(level = Level.ERROR, name = LOGIN_CODE, role = RequestParam.Role.POST)
    private final String login;

    @RequestParam(level = Level.ERROR, name = PASSWORD_CODE, role = RequestParam.Role.POST)
    private final String password;

    public LoginAction(final LoginActionUrl url) {
        super(url);
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
        } else {
            session.setAuthToken(null);
            session.notifyBad(Context.tr("Login failed. Wrong login or password."));
            return new LoginPageUrl();
        }
    }

    @Override
	protected Url doProcessErrors() throws RedirectException {
    	//TODO
		return new LoginPageUrl();
	}

}

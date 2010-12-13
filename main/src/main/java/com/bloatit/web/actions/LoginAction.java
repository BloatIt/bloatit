/*
 * Copyright (C) 2010 BloatIt.
 * 
 * This file is part of BloatIt.
 * 
 * BloatIt is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * BloatIt is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
package com.bloatit.web.actions;


import com.bloatit.framework.AuthToken;
import com.bloatit.framework.managers.LoginManager;
import com.bloatit.web.exceptions.RedirectException;
import com.bloatit.web.html.pages.LoginPage;
import com.bloatit.web.utils.Message.Level;
import com.bloatit.web.utils.annotations.RequestParam;
import com.bloatit.web.utils.url.Request;
import com.bloatit.web.utils.url.UrlBuilder;

public class LoginAction extends Action {

    public final static String LOGIN_CODE = "bloatit_login";
    public final static String PASSWORD_CODE = "bloatit_password";
    @RequestParam(level = Level.ERROR, name = LOGIN_CODE, role=RequestParam.Role.POST)
    private String login;
    @RequestParam(level = Level.ERROR, name = PASSWORD_CODE, role=RequestParam.Role.POST)
    private String password;

    public LoginAction(final Request request){
        super(request);
        request.setValues(this);
        
    }

    @Override
    public String process() throws RedirectException {
        if (request.getMessages().hasMessage(Level.ERROR)) {
            // TODO get params.
            throw new RedirectException(new UrlBuilder(LoginPage.class).buildUrl());
        }

        AuthToken token = null;
        token = LoginManager.loginByPassword(login, password);

        if (token != null) {
            session.setLogged(true);
            session.setAuthToken(token);
            session.notifyGood(session.tr("Login success."));
            return session.getTargetPage();
        } else {
            session.setLogged(false);
            session.setAuthToken(null);
            session.notifyBad(session.tr("Login failed. Wrong login or password."));
            return new UrlBuilder(LoginPage.class).buildUrl();
        }
    }

}

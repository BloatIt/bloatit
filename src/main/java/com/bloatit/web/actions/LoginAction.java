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

import java.util.HashMap;
import java.util.Map;

import com.bloatit.framework.AuthToken;
import com.bloatit.framework.LoginManager;
import com.bloatit.web.htmlrenderer.HtmlTools;
import com.bloatit.web.pages.IndexPage;
import com.bloatit.web.pages.LoginPage;
import com.bloatit.web.server.Action;
import com.bloatit.web.server.Session;

public class LoginAction extends Action {

    public LoginAction(Session session, Map<String, String> parameters) {
        super(session, parameters);
    }

    public LoginAction(Session session) {
        this(session, new HashMap<String, String>());
    }

    @Override
    public String getCode() {
        return "login";
    }

    public String getLoginCode() {
        return "bloatit_login";
    }

    public String getPasswordCode() {
        return "bloatit_password";
    }

    @Override
    protected void process() {
        if (this.parameters.containsKey(this.getLoginCode()) && this.parameters.containsKey(this.getPasswordCode())) {
            
                String login = this.parameters.get(this.getLoginCode());
                String password = this.parameters.get(this.getPasswordCode());
                AuthToken token = null;
                token    = LoginManager.loginByPassword(login, password);
                
                if (token != null) {
                    loginSuccess(token);
                } else {
                    loginFailed();
                }
            
        }
    }

    private void loginSuccess(AuthToken token) {
        this.session.setLogged(true);
        this.session.setAuthToken(token);
        this.htmlResult.setRedirect(HtmlTools.generateUrl(this.session, new IndexPage(this.session)));
        this.session.notifyGood(this.session.tr("Login success."));
    }

    private void loginFailed() {
        this.session.setLogged(false);
        this.session.setAuthToken(null);
        this.htmlResult.setRedirect(HtmlTools.generateUrl(this.session, new LoginPage(this.session)));
        this.session.notifyBad(this.session.tr("Login failed. Wrong login or password."));
    }
}

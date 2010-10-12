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
import com.bloatit.framework.LoginManager;
import com.bloatit.model.exceptions.ElementNotFoundException;
import com.bloatit.web.htmlrenderer.HtmlTools;
import com.bloatit.web.pages.IndexPage;
import com.bloatit.web.pages.LoginPage;
import com.bloatit.web.server.Action;
import com.bloatit.web.server.RequestFactory;
import com.bloatit.web.server.Session;
import java.util.HashMap;
import java.util.Map;

public class LoginAction extends Action {

    public LoginAction() {
    }

    public LoginAction(Session session, Map<String, String> parameters) {
        super(session, parameters);
    }

    public  LoginAction(Session session){
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
        if (this.post.containsKey(this.getLoginCode()) && this.post.containsKey(this.getPasswordCode())) {
            try {
                String login = this.post.get(this.getLoginCode());
                String password = this.post.get(this.getPasswordCode());
                AuthToken token = LoginManager.loginByPassword(login, password);

                this.session.setLogged(true);
                this.session.setAuthToken(token);
                this.htmlResult.setRedirect(HtmlTools.generateUrl(this.session, new IndexPage(this.session)));
            } catch (ElementNotFoundException ex) {
                // TODO: Leave error treatment in there or put it in another class ?
                this.session.setLogged(false);
                this.session.setAuthToken(null);
                this.htmlResult.setRedirect(HtmlTools.generateUrl(this.session, new LoginPage(this.session)));
            }
        }
    }
}
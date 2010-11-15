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

import com.bloatit.web.server.Action;
import com.bloatit.web.server.Session;
import java.util.HashMap;
import java.util.Map;

public class ContributionAction extends Action {

    public ContributionAction(Session session, Map<String, String> parameters) {
        super(session, parameters);
    }

    public ContributionAction(Session session) {
        this(session, new HashMap<String, String>());
    }

    @Override
    public String getCode() {
        return "contribute";
    }

    public String getContributionCode(){
        return "bloatit_contribute";
    }

    @Override
    protected void process() {
        /*
        if (parameters.containsKey(getContributionCode()) && parameters.containsKey(getPasswordCode())) {

            final String login = parameters.get(getLoginCode());
            final String password = parameters.get(getPasswordCode());
            AuthToken token = null;
            token = LoginManager.loginByPassword(login, password);

            if (token != null) {
                loginSuccess(token);
            } else {
                loginFailed();
            }

        }
         * */
    }
}
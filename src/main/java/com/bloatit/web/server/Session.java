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

package com.bloatit.web.server;

import com.bloatit.framework.AuthToken;
import java.util.ArrayDeque;


public class Session {
    private Object auth_token;
    private Object key;
    private boolean logged;
    private ArrayDeque<Action> actionList;
    private Language language;
    private Page lastStablePage;
    private AuthToken authToken;

    public Session(){
        this.auth_token = null;
        this.key = null;
        this.logged = false;
        this.actionList = new ArrayDeque<Action>();
    }
    public String _(String s){
        return this.language.getText(s);
    }

    public Language getLanguage() {
        return this.language;
    }

    public void setLanguage(Language newLang){
        this.language = newLang;
    }

    public void setAuthToken(AuthToken token) {
        this.authToken = token;
    }

    public AuthToken getAuthToken() {
        return this.authToken;
    }

    public void setLogged(boolean logged) {
        this.logged = logged;
    }

    public boolean isLogged() {
        return this.logged;
    }

    public void setKey(String sb) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public Object getKey() {
        return key;
    }

    public void setKey(Object key) {
        this.key = key;
    }

    public ArrayDeque<Action> getActionList() {
        return actionList;
    }

    public void setLastStablePage(Page p){
        this.lastStablePage = p;
    }

    public Page getLastStablePage() {
        return lastStablePage;
    }
}
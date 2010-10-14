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
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;


public class Session {
    private final String key;
    private boolean logged;
    private Deque<Action> actionList;
    private Deque<Notification> notificationList;
    private Language language;
    private Page lastStablePage;
    private AuthToken authToken;

    private List<Language> preferredLanguages;
    

    Session(String key) {
        this.key = key;
        this.authToken = null;
        this.logged = false;
        this.actionList = new ArrayDeque<Action>();
        this.notificationList = new ArrayDeque<Notification>();

        // TODO : Following lines are for testing purposes only
        preferredLanguages = new ArrayList<Language>();
        preferredLanguages.add(new Language()); // TODO : ONLY FOR TEST
        preferredLanguages.add(new Language("fr")); // TODO : ONLY FOR TEST
    }
    public String tr(String s){
        return this.language.tr(s);
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

    public String getKey() {
        return key;
    }

    public Deque<Action> getActionList() {
        return actionList;
    }

    public void setLastStablePage(Page p){
        this.lastStablePage = p;
    }

    public Page getLastStablePage() {
        return lastStablePage;
    }

    public void notifyGood(String message) {
        this.notificationList.add(new Notification(message, Notification.Type.GOOD));
    }

    public void notifyBad(String message) {
        this.notificationList.add(new Notification(message, Notification.Type.BAD));
    }

    public void notifyError(String message) {
        this.notificationList.add(new Notification(message, Notification.Type.ERROR));
    }

    void flushNotifications() {
        this.notificationList.clear();
    }

    Deque<Notification> getNotifications() {
        return notificationList;
    }

    public List<Language> getPreferredLangs(){
        return this.preferredLanguages;
    }
}
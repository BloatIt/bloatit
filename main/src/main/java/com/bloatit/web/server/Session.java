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

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Locale;

import com.bloatit.framework.AuthToken;
import com.bloatit.web.actions.Action;
import com.bloatit.web.annotations.Message;
import com.bloatit.web.utils.url.IndexPageUrl;
import com.bloatit.web.utils.url.Parameters;

public class Session {
    private final String key;
    private boolean logged;
    private final Deque<Action> actionList;
    private final Deque<Notification> notificationList;
    private Language language;
    private String lastStablePage = null;
    private String targetPage = null;
    private AuthToken authToken;

    private Parameters sessionParams = new Parameters();

    private final List<Language> preferredLocales;

    Session(final String key) {
        this.key = key;
        authToken = null;
        logged = false;
        actionList = new ArrayDeque<Action>();
        notificationList = new ArrayDeque<Notification>();

        // TODO : Following lines are for testing purposes only
        preferredLocales = new ArrayList<Language>();
        preferredLocales.add(new Language(Locale.ENGLISH)); // TODO : ONLY FOR
                                                            // TEST
    }

    public String tr(final String s) {
        return language.tr(s);
    }

    public String tr(final String s, final Object[] objects) {
        return language.tr(s, objects);
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(final Language newLang) {
        language = newLang;
    }

    public void setAuthToken(final AuthToken token) {
        authToken = token;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public void setLogged(final boolean logged) {
        this.logged = logged;
    }

    public boolean isLogged() {
        return logged;
    }

    public String getKey() {
        return key;
    }

    public Deque<Action> getActionList() {
        return actionList;
    }

    public void setLastStablePage(final String p) {
        lastStablePage = p;
    }

    public String getLastStablePage() {
        return lastStablePage;
    }

    public String getTargetPage() {
        return targetPage;
    }

    public String getPreferredPage() {
        if (targetPage != null) {
            return targetPage;
        } else if (lastStablePage != null) {
            return lastStablePage;
        } else {
            return new IndexPageUrl().urlString();
        }
    }

    public void setTargetPage(final String targetPage) {
        this.targetPage = targetPage;
    }

    public void notifyGood(final String message) {
        notificationList.add(new Notification(message, Notification.Type.GOOD));
    }

    public void notifyBad(final String message) {
        notificationList.add(new Notification(message, Notification.Type.BAD));
    }

    public void notifyError(final String message) {
        notificationList.add(new Notification(message, Notification.Type.ERROR));
    }

    /**
     * Notifies all elements in a list as warnings
     * TODO : DELETE, for test purposes only
     */
    public void notifyList(final List<Message> errors) {
        for (final Message error : errors) {
            switch (error.getLevel()) {
            case ERROR:
                notifyError(error.getMessage());
                break;
            case WARNING:
                notifyBad(error.getMessage());
                break;
            case INFO:
                notifyGood(error.getMessage());
                break;
            default:
                break;
            }
        }
    }

    public void flushNotifications() {
        notificationList.clear();
    }

    public Deque<Notification> getNotifications() {
        return notificationList;
    }

    public List<Language> getPreferredLangs() {
        return preferredLocales;
    }

    public Parameters getParams(){
        return sessionParams;
    }

    public String getParam(String paramKey){
        return sessionParams.get(paramKey);
    }

    public void addParam(String paramKey, String paramValue){
        sessionParams.put(paramKey, paramValue);
    }
}

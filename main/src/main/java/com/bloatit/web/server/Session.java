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

package com.bloatit.web.server;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.UUID;

import com.bloatit.framework.AuthToken;
import com.bloatit.web.annotations.Message;
import com.bloatit.web.utils.url.IndexPageUrl;
import com.bloatit.web.utils.url.Parameters;
import com.bloatit.web.utils.url.Url;
import com.bloatit.web.utils.url.UrlParameter;

/**
 * <p>
 * A class to handle the user session on the web server
 * </p>
 * <p>
 * A session starts when the user arrives on the server (first GET request). When the user
 * login, his sessions continues (he'll therefore keep all his session informations), but
 * he simply gets a new authtoken that says he's logged
 * </p>
 * <p>
 * Session is used for various purposes :
 * <li>Store some parameters {@link Session#addParameter(String, String)}</li>
 * <li>Store pages that the user wishes to consult, but he couldn't because he didn't meet
 * the requirements</li>
 * </p>
 */
public final class Session {

    public static final long LOGGED_SESSION_DURATION = 1296000; // 15 days in seconds
    public static final long DEFAULT_SESSION_DURATION = 86400; // 1 days in seconds

    private final UUID key;
    private final Deque<Notification> notificationList;
    private AuthToken authToken;

    private Url lastStablePage = null;
    private Url targetPage = null;

    private long expirationTime;

    /**
     * The place to store session data
     */
    private final Parameters sessionParams = new Parameters();

    Session() {
        this(UUID.randomUUID());
    }

    Session(UUID id) {
        this.key = id;
        authToken = null;
        notificationList = new ArrayDeque<Notification>();
        resetExpirationTime();
    }

    UUID getKey() {
        return key;
    }

    public void resetExpirationTime() {
        if (isLogged()) {
            expirationTime = Context.getTime() + LOGGED_SESSION_DURATION;
        } else {
            expirationTime = Context.getTime() + DEFAULT_SESSION_DURATION;
        }
    }

    public void setAuthToken(final AuthToken token) {
        authToken = token;
        resetExpirationTime();
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public boolean isLogged() {
        return authToken != null;
    }

    public boolean isExpired() {
        return Context.getTime() > expirationTime;
    }

    public void setLastStablePage(final Url p) {
        lastStablePage = p;
    }

    public Url getLastStablePage() {
        return lastStablePage;
    }

    /**
     * You should use the pickPreferedPage instead.
     */
    @Deprecated
    public final Url getTargetPage() {
        return targetPage;
    }

    public Url pickPreferredPage() {
        if (targetPage != null) {
            final Url tempStr = targetPage;
            targetPage = null;
            return tempStr;
        } else if (lastStablePage != null) {
            return lastStablePage;
        } else {
            return new IndexPageUrl();
        }
    }

    public final void setTargetPage(final Url targetPage) {
        this.targetPage = targetPage;
    }

    public final void notifyGood(final String message) {
        notificationList.add(new Notification(message, Notification.Type.GOOD));
    }

    public final void notifyBad(final String message) {
        notificationList.add(new Notification(message, Notification.Type.BAD));
    }

    public final void notifyError(final String message) {
        notificationList.add(new Notification(message, Notification.Type.ERROR));
    }

    /**
     * Notifies all elements in a list as warnings
     */
    public final void notifyList(final List<Message> errors) {
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

    /**
     * Finds all the session parameters
     *
     * @return the parameter of the session
     * @deprecated use a RequestParam
     */
    @Deprecated
    public Parameters getParams() {
        return sessionParams;
    }

    /**
     * Finds a given parameter in the session
     *
     * @param paramKey the key of the parameter
     * @return the value of the parameter
     * @deprecated use a RequestParam
     */
    @Deprecated
    public String getParam(final String paramKey) {
        return sessionParams.get(paramKey);
    }

    /**
     * <p>
     * Saves a new parameter in the session. The parameter will be saved only if <code>
     * paramValue</code> is <i>not null</i>. If you want to save a <code>null</code>
     * value, use {@link #addParamForced(String, String)}.
     * </p>
     * <p>
     * Session parameters are available until they are checked, or session ends
     * </p>
     *
     * @param paramKey
     * @param paramValue
     */
    public void addParameter(final String paramKey, final String paramValue) {
        if (paramValue != null && paramKey != null) {
            sessionParams.put(paramKey, paramValue);
        }
    }

    public void addParameter(UrlParameter<?> param) {
        sessionParams.put(param.getName(), param.getStringValue());
    }

    /**
     * <p>
     * Saves a new parameter in the session. This method will save even <code>
     * null</code> parameters.
     * </p>
     * <p>
     * Session parameters are available until they are checked, or session ends
     * </p>
     *
     * @param paramKey
     * @param paramValue
     */
    public void addParamForced(final String paramKey, final String paramValue) {
        sessionParams.put(paramKey, paramValue);
    }

    /**
     * <p>
     * Saves a new <code>BigDecimal</code> in the session.
     * </p>
     * <p>
     * This method is null-safe : if <code>paramValue</code> is null, the method doesn't
     * fail but no parameter is added
     * </p>
     * <p>
     * Session parameters are available until they are checked, or session ends
     * </p>
     *
     * @param paramKey
     * @param paramValue
     */
    public void addParam(final String paramKey, final BigDecimal paramValue) {
        if (paramValue != null) {
            sessionParams.put(paramKey, paramValue.toPlainString());
        }
    }
}

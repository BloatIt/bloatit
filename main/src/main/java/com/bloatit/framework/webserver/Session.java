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

package com.bloatit.framework.webserver;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.bloatit.framework.exceptions.FatalErrorException;
import com.bloatit.framework.utils.SessionParameters;
import com.bloatit.framework.webserver.ErrorMessage.Level;
import com.bloatit.framework.webserver.annotations.Message;
import com.bloatit.framework.webserver.url.Url;
import com.bloatit.framework.webserver.url.UrlParameter;
import com.bloatit.model.right.AuthToken;
import com.bloatit.web.url.IndexPageUrl;

/**
 * <p>
 * A class to handle the user session on the web server
 * </p>
 * <p>
 * A session starts when the user arrives on the server (first GET request).
 * When the user login, his sessions continues (he'll therefore keep all his
 * session informations), but he simply gets a new authtoken that says he's
 * logged
 * </p>
 * <p>
 * Session is used for various purposes :
 * <li>Store some parameters {@link Session#addParameter(String, String)}</li>
 * <li>Store pages that the user wishes to consult, but he couldn't because he
 * didn't meet the requirements</li>
 * </p>
 */
public final class Session {

    public static final long LOGGED_SESSION_DURATION = 1296000; // 15 days in
                                                                // seconds
    public static final long DEFAULT_SESSION_DURATION = 86400; // 1 days in
                                                               // seconds

    private final UUID key;
    private final Deque<ErrorMessage> notificationList;

    // TODO: use string reference to avoid to keep reference on Member object
    private AuthToken authToken;

    private Url lastStablePage = null;
    private Url targetPage = null;

    private long expirationTime;

    /**
     * The place to store session data
     */
    private final SessionParameters parameters = new SessionParameters();
    private Url lastVisitedPage;
    private final Map<String, WebProcess> processes = new HashMap<String, WebProcess>();

    Session() {
        this(UUID.randomUUID());
    }

    Session(final UUID id) {
        this.key = id;
        authToken = null;
        notificationList = new ArrayDeque<ErrorMessage>();
        resetExpirationTime();
    }

    public UUID getKey() {
        return key;
    }

    public void resetExpirationTime() {
        if (isLogged()) {
            expirationTime = Context.getResquestTime() + LOGGED_SESSION_DURATION;
        } else {
            expirationTime = Context.getResquestTime() + DEFAULT_SESSION_DURATION;
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
        return Context.getResquestTime() > expirationTime;
    }

    public void setLastStablePage(final Url p) {
        lastStablePage = p;
    }

    public Url getLastStablePage() {
        if (lastStablePage == null) {
            return new IndexPageUrl();
        }
        return lastStablePage;
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

    public Url getLastVisitedPage() {
        // TODO
        return lastVisitedPage;
    }

    public void setLastVisitedPage(final Url lastVisitedPage) {
        this.lastVisitedPage = lastVisitedPage;
    }

    public final void setTargetPage(final Url targetPage) {
        this.targetPage = targetPage;
    }

    public final void notifyGood(final String message) {
        notificationList.add(new ErrorMessage(Level.INFO, message));
    }

    public final void notifyBad(final String message) {
        notificationList.add(new ErrorMessage(Level.WARNING, message));
    }

    public final void notifyError(final String message) {
        notificationList.add(new ErrorMessage(Level.FATAL, message));
    }

    /**
     * Notifies all elements in a list as warnings
     */
    public final void notifyList(final List<Message> errors) {
        for (final Message error : errors) {
            notifyError(error.getMessage());
        }
    }

    public void flushNotifications() {
        notificationList.clear();
    }

    public Deque<ErrorMessage> getNotifications() {
        return notificationList;
    }

    /**
     * Finds all the session parameters
     *
     * @return the parameter of the session
     * @deprecated use a RequestParam
     */
    @Deprecated
    public SessionParameters getParameters() {
        return parameters;
    }

    @SuppressWarnings("unchecked")
    public <T, U> UrlParameter<T, U> pickParameter(final UrlParameter<T, U> param) {
        return (UrlParameter<T, U>) parameters.pick(param.getName());
    }

    public void addParameter(final UrlParameter<?, ?> param) {
        if (!(param.getValue() == null && param.getStringValue() == null)) {
            parameters.add(param.getName(), param);
        }
        // Maybe auto notify here ?
    }

    public final String createWebProcess(WebProcess process) {

        int length = 5;
        String key = null;
        while(key == null) {
            String tempKey = sha1(UUID.randomUUID().toString()).substring(0,length);

            if(processes.containsKey(tempKey)) {
                length++;
            } else {
                key = tempKey;
            }

        }
        processes.put(key, process);
        return key;
    }

    public final WebProcess getWebProcess(String processId) {
        return processes.get(processId);
    }


    public static String sha1(final String digest) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-1");
        } catch (final NoSuchAlgorithmException ex) {
            throw new FatalErrorException("Algorithm Sha1 not available", ex);
        }
        md.update(digest.getBytes());
        final byte byteData[] = md.digest();

        final StringBuilder sb = new StringBuilder();
        for (final byte element : byteData) {
            sb.append(Integer.toString((element & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    public void destroyWebProcess(WebProcess webProcess) {
        processes.remove(webProcess.getId());
    }

}

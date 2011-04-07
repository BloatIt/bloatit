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

import com.bloatit.framework.FrameworkConfiguration;
import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;
import com.bloatit.framework.utils.DateUtils;
import com.bloatit.framework.utils.SessionParameters;
import com.bloatit.framework.webserver.ErrorMessage.Level;
import com.bloatit.framework.webserver.annotations.Message;
import com.bloatit.framework.webserver.url.Url;
import com.bloatit.framework.webserver.url.UrlParameter;
import com.bloatit.model.right.AuthToken;
import com.bloatit.web.url.IndexPageUrl;

import edu.emory.mathcs.backport.java.util.Collections;

/**
 * A class to handle the user session on the web server
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
    private final UUID key;
    private long expirationTime;

    private final Deque<ErrorMessage> notificationList;

    private AuthToken authToken;
    private Url lastStablePage = null;
    private Url targetPage = null;
    private Url lastVisitedPage;

    private final SessionParameters parameters = new SessionParameters();

    @SuppressWarnings("unchecked")
    private final Map<String, WebProcess> processes = Collections.synchronizedMap(new HashMap<String, WebProcess>());

    /**
     * Construct a new session
     */
    protected Session() {
        this(UUID.randomUUID());
    }

    /**
     * Construct a session based on the information from <code>id</code>
     * 
     * @param id the id of the session
     */
    protected Session(final UUID id) {
        this.key = id;
        authToken = null;
        notificationList = new ArrayDeque<ErrorMessage>();
        resetExpirationTime();
    }

    public synchronized UUID getKey() {
        return key;
    }

    public synchronized void resetExpirationTime() {
        if (isLogged()) {
            expirationTime = Context.getResquestTime() + FrameworkConfiguration.getSessionLoggedDuration() * DateUtils.SECOND_PER_DAY;
        } else {
            expirationTime = Context.getResquestTime() + FrameworkConfiguration.getSessionDefaultDuration() * DateUtils.SECOND_PER_DAY;
        }
    }

    public synchronized void setAuthToken(final AuthToken token) {
        authToken = token;
        resetExpirationTime();
    }

    public synchronized AuthToken getAuthToken() {
        return authToken;
    }

    public synchronized boolean isLogged() {
        return authToken != null;
    }

    public synchronized boolean isExpired() {
        return Context.getResquestTime() > expirationTime;
    }

    public synchronized void setLastStablePage(final Url p) {
        lastStablePage = p;
    }

    /**
     * Finds the <i>supposedly</i> best page to redirect the user to
     * <p>
     * Actions are ignored. <br />
     * <b>NOTE</b>: If the user has two active tabs, it will return the last
     * page visited in any tab, and can lead to <i>very</i> confusing result.
     * Avoid relying on this.
     * </p>
     * 
     * @return the best page to redirect the user to
     */
    public synchronized Url pickPreferredPage() {
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

    /**
     * Finds the last page the user visited on the website.
     * <p>
     * Actions are ignored. <br />
     * <b>NOTE</b>: If the user has two active tabs, it will return the last
     * page visited in any tab, and can lead to <i>very</i> confusing result.
     * Avoid relying on this.
     * </p>
     * 
     * @return the last page the user visited
     */
    public synchronized Url getLastVisitedPage() {
        return lastVisitedPage;
    }

    public synchronized void setLastVisitedPage(final Url lastVisitedPage) {
        this.lastVisitedPage = lastVisitedPage;
    }

    public final synchronized void setTargetPage(final Url targetPage) {
        this.targetPage = targetPage;
    }

    public final synchronized void notifyGood(final String message) {
        notificationList.add(new ErrorMessage(Level.INFO, message));
    }

    public final synchronized void notifyBad(final String message) {
        notificationList.add(new ErrorMessage(Level.WARNING, message));
    }

    public final synchronized void notifyError(final String message) {
        notificationList.add(new ErrorMessage(Level.FATAL, message));
    }

    /**
     * Notifies all elements in a list as warnings
     */
    public final synchronized void notifyList(final List<Message> errors) {
        for (final Message error : errors) {
            notifyError(error.getMessage());
        }
    }

    public final synchronized void flushNotifications() {
        notificationList.clear();
    }

    public final synchronized Deque<ErrorMessage> getNotifications() {
        return notificationList;
    }

    /**
     * Finds all the session parameters
     * 
     * @return the parameter of the session
     * @deprecated use a RequestParam
     */
    @Deprecated
    public final synchronized SessionParameters getParameters() {
        return parameters;
    }

    @SuppressWarnings("unchecked")
    public final synchronized <T, U> UrlParameter<T, U> pickParameter(final UrlParameter<T, U> param) {
        return (UrlParameter<T, U>) parameters.pick(param.getName());
    }

    public final synchronized void addParameter(final UrlParameter<?, ?> param) {
        if (!(param.getValue() == null && param.getStringValue() == null)) {
            parameters.add(param.getName(), param);
        }
        // Maybe auto notify here ?
    }

    public final synchronized String createWebProcess(WebProcess process) {
        int length = 5;
        String key = null;
        while (key == null) {
            String tempKey = sha1(UUID.randomUUID().toString()).substring(0, length);

            if (processes.containsKey(tempKey)) {
                length++;
            } else {
                key = tempKey;
            }

        }
        processes.put(key, process);
        return key;
    }

    public final synchronized WebProcess getWebProcess(String processId) {
        return processes.get(processId);
    }

    public final synchronized void destroyWebProcess(WebProcess webProcess) {
        processes.remove(webProcess.getId());
    }

    /**
     * Finds the sha1 hash of an <code>input</code> string
     * 
     * @param input the string to hash
     * @return the hashed string
     */
    private static String sha1(final String input) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-1");
        } catch (final NoSuchAlgorithmException ex) {
            throw new BadProgrammerException("Algorithm Sha1 not available", ex);
        }
        md.update(input.getBytes());
        final byte byteData[] = md.digest();

        final StringBuilder sb = new StringBuilder();
        for (final byte element : byteData) {
            sb.append(Integer.toString((element & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }
}

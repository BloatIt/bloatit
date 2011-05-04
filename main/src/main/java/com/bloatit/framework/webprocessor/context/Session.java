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

package com.bloatit.framework.webprocessor.context;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;

import com.bloatit.framework.FrameworkConfiguration;
import com.bloatit.framework.utils.datetime.DateUtils;
import com.bloatit.framework.utils.parameters.SessionParameters;
import com.bloatit.framework.webprocessor.ErrorMessage;
import com.bloatit.framework.webprocessor.ErrorMessage.Level;
import com.bloatit.framework.webprocessor.WebProcess;
import com.bloatit.framework.webprocessor.annotations.Message;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.framework.webprocessor.url.UrlDump;
import com.bloatit.framework.webprocessor.url.UrlParameter;
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
 * <li>Store some parameters {@link Session#addParameter(UrlParameter)}</li>
 * <li>Store pages that the user wishes to consult, but he couldn't because he
 * didn't meet the requirements</li>
 * </p>
 */
public final class Session {
    private static final int SHA1_SIZE = 20;
    
    private long expirationTime;
    private final UUID key;

    private final Deque<ErrorMessage> notificationList;
    private final SessionParameters parameters = new SessionParameters();
    private UserToken userToken;

    private UrlDump lastStablePage = null;
    private UrlDump targetPage = null;
    private UrlDump lastVisitedPage;


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
        userToken = null;
        notificationList = new ArrayDeque<ErrorMessage>();
        resetExpirationTime();
    }

    public synchronized UUID getKey() {
        return key;
    }

    public synchronized void resetExpirationTime() {
        if (userToken.isAuthenticated()) {
            expirationTime = Context.getResquestTime() + FrameworkConfiguration.getSessionLoggedDuration() * DateUtils.SECOND_PER_DAY;
        } else {
            expirationTime = Context.getResquestTime() + FrameworkConfiguration.getSessionDefaultDuration() * DateUtils.SECOND_PER_DAY;
        }
    }

    public synchronized void setAuthToken(final UserToken token) {
        userToken = token;
        resetExpirationTime();
    }

    public synchronized UserToken getUserToken() {
        return userToken;
    }

    public synchronized boolean isExpired() {
        return Context.getResquestTime() > expirationTime;
    }

    public synchronized void setLastStablePage(final Url p) {
        if (p == null) {
            this.lastStablePage = null;
        } else {
            this.lastStablePage = new UrlDump(p);
        }
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
        if (lastVisitedPage == null) {
            this.lastVisitedPage = null;
        } else {
            this.lastVisitedPage = new UrlDump(lastVisitedPage);
        }
    }

    public final synchronized void setTargetPage(final Url targetPage) {
        if (targetPage == null) {
            this.targetPage = null;
        } else {
            this.targetPage = new UrlDump(targetPage);
        }
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
    }

    public final synchronized String createWebProcess(final WebProcess process) {
        int length = 5;
        String key = null;
        while (key == null) {
            final String tempKey = DigestUtils.sha256Hex(UUID.randomUUID().toString()).substring(0, length);

            if (processes.containsKey(tempKey)) {
                if (length < SHA1_SIZE) {
                    length++;
                }
            } else {
                key = tempKey;
            }

        }
        processes.put(key, process);
        return key;
    }

    public final synchronized WebProcess getWebProcess(final String processId) {
        return processes.get(processId);
    }

    public final synchronized void destroyWebProcess(final WebProcess webProcess) {
        processes.remove(webProcess.getId());
    }

}

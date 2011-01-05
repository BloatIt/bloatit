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

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Locale;

import com.bloatit.framework.AuthToken;
import com.bloatit.web.actions.Action;
import com.bloatit.web.annotations.Message;
import com.bloatit.web.utils.i18n.DateLocale;
import com.bloatit.web.utils.url.IndexPageUrl;
import com.bloatit.web.utils.url.Parameters;
import com.bloatit.web.utils.url.Url;

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
 * <li>Store some parameters {@link Session#addParam(String, String)}</li>
 * <li>Perform localization</li>
 * <li>Store pages that the user wishes to consult, be he couldn't because he didn't meet
 * the requirements</li>
 * </p>
 */
public class Session {
	private final String key;
	private boolean logged;
	private final Deque<Action> actionList;
	private final Deque<Notification> notificationList;
	private AuthToken authToken;
	
	private Url lastStablePage = null;
	private Url targetPage = null;
	

	/**
	 * The place to store session data
	 */
	private final Parameters sessionParams = new Parameters();

	Session(final String key) {
		this.key = key;

		authToken = null;
		logged = false;
		actionList = new ArrayDeque<Action>();
		notificationList = new ArrayDeque<Notification>();
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
	public Url getTargetPage() {
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

	public void setTargetPage(final Url targetPage) {
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
	 * Notifies all elements in a list as warnings TODO : DELETE, for test purposes only
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

	/**
	 * Finds all the session parameters
	 * @return the parameter of the session
	 * @deprecated use a RequestParam
	 */
	@Deprecated
	public Parameters getParams() {
		return sessionParams;
	}

	/**
	 * Finds a given parameter in the session
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
	 * Saves a new parameter in the session
	 * </p>
	 * <p>
	 * Session parameters are available until they are checked, or session ends
	 * </p>
	 * @param paramKey
	 * @param paramValue
	 */
	public void addParam(final String paramKey, final String paramValue) {
		sessionParams.put(paramKey, paramValue);
	}


}

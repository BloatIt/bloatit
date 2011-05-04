//
// Copyright (c) 2011 Linkeos.
//
// This file is part of Elveos.org.
// Elveos.org is free software: you can redistribute it and/or modify it
// under the terms of the GNU General Public License as published by the
// Free Software Foundation, either version 3 of the License, or (at your
// option) any later version.
//
// Elveos.org is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
// more details.
// You should have received a copy of the GNU General Public License along
// with Elveos.org. If not, see http://www.gnu.org/licenses/.
//
package com.bloatit.web.actions;

import com.bloatit.framework.webprocessor.masters.Action;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.ElveosUserToken;
import com.bloatit.model.Member;
import com.bloatit.model.right.AuthenticatedUserToken;
import com.bloatit.web.url.LoginPageUrl;

/**
 * <p>
 * The mother of all actions that require the user to be logged
 * </p>
 * <p>
 * All implementing classes need to implement
 * <li>doProcessRestricted: The standard (expected) behavior of the action :
 * user is logged and no error have been detected in the
 * <code>RequestParam</code></li>
 * <li>doProcessErrors: called whenever one of the <code>mandatory</code>
 * request param contains a null value, or whenever a request param (either
 * optional or mandatory) contains a non valid value</li>
 * <li>getRefusalReason: Used to inform the user while he has to be logged to do
 * the action</li>
 * <li>transmitParameters: called when user is not logged. In this method,
 * children classes should most likely save all parameters into the session</li>
 * </p>
 */
public abstract class LoggedAction extends ElveosAction {
    private final Url meUrl;
    private final Member member;
    private final AuthenticatedUserToken userToken;

    public LoggedAction(final Url url) {
        super(url);
        this.meUrl = url;
        this.userToken = (AuthenticatedUserToken) session.getUserToken();
        this.member = userToken.getMember();
    }

    @Override
    protected final Url doProcess(ElveosUserToken userToken) {
        if (userToken.isAuthenticated()) {
            return doProcessRestricted(member);
        }
        session.notifyBad(getRefusalReason());
        session.setTargetPage(meUrl);
        transmitParameters();
        return new LoginPageUrl();
    }

    @Override
    protected final Url checkRightsAndEverything(ElveosUserToken userToken) {
        if (userToken.isAuthenticated()) {
            return checkRightsAndEverything(member);
        }

        // If member is null, let the Logged action do its work (return to the
        // logged page...)
        return NO_ERROR;
    }

    /**
     * Called before creating the page, used to check if there are additional
     * errors that can't be spotted by Url.
     * 
     * @param me the logged member
     * @return {@value Action#NO_ERROR} if there is no error, an Url to the page
     *         to handle errors otherwise
     */
    protected abstract Url checkRightsAndEverything(Member me);

    /**
     * Called when user is correctly authentified
     * 
     * @param me the currently logged user
     */
    protected abstract Url doProcessRestricted(Member me);

    /**
     * Called when some RequestParams contain erroneous parameters.
     */
    @Override
    protected abstract Url doProcessErrors(ElveosUserToken userToken);

    /**
     * <b>Do not forget to localize</p>
     * 
     * @return the error message to dislay to the user, informing him while he
     *         couldn't access the page
     */
    protected abstract String getRefusalReason();
}

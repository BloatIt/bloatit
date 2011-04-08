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
package com.bloatit.web.linkable.admin;

import com.bloatit.data.DaoMember;
import com.bloatit.data.DaoMember.Role;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.Member;
import com.bloatit.web.actions.LoggedAction;
import com.bloatit.web.url.LoginPageUrl;

/**
 * Mother class for all admin actions
 * <p>
 * Admin actions require the user to be logged and to have user privileges of
 * {@link Role#ADMIN}. <br />
 * Contrarily to common logged action, error message is <b>not</b> customizable.
 * Mainly because admins do not need beautiful messages.
 * </p>
 */
public abstract class AdminAction extends LoggedAction {

    /**
     * @param url
     */
    public AdminAction(final Url url) {
        super(url);
    }

    @Override
    public final Url doProcessRestricted(final Member authenticatedMember) {
        if (!authenticatedMember.hasUserPrivilege(DaoMember.Role.ADMIN)) {
            session.notifyError(getRefusalReason());
            return new LoginPageUrl();
        }
        return doProcessAdmin();
    }

    @Override
    protected final String getRefusalReason() {
        if (session.isLogged()) {
            return Context.tr("You must be logged as an admin to access this page");
        }
        return Context.tr("You must be logged to access an admin page");
    }

    /**
     * Called when the user is correctly logged as an admin
     * <p>
     * This is the place where extending classes should implement their standard
     * behavior
     * </p>
     * 
     * @return the destination Url
     */
    protected abstract Url doProcessAdmin();
}

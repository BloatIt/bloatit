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
package com.bloatit.web.linkable.admin.master;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import com.bloatit.data.DaoMember.Role;
import com.bloatit.data.SessionManager;
import com.bloatit.framework.exceptions.highlevel.ShallNotPassException;
import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.Member;
import com.bloatit.model.right.UnauthorizedOperationException;
import com.bloatit.web.linkable.master.LoggedElveosPage;
import com.bloatit.web.url.LoginPageUrl;

public abstract class AdminPage extends LoggedElveosPage {

    private final Url url2;

    public AdminPage(final Url url) {
        super(url);
        url2 = url;
        SessionManager.getSessionFactory().getCurrentSession().disableFilter("usercontent.nonDeleted");
    }

    @Override
    public final HtmlElement createRestrictedContent(final Member loggedUser) throws RedirectException {
        if (loggedUser.getRole() == Role.ADMIN) {
            try {
                return createAdminContent();
            } catch (final UnauthorizedOperationException e) {
                getSession().notifyError(tr("Are you sure you are admin?"));
                throw new ShallNotPassException("Admin content got a UnauthorizedOperationException", e);
            }
        }
        throw new RedirectException(new LoginPageUrl(url2.urlString()));
    }

    @Override
    public final String getRefusalReason() {
        return tr("You have to be Admin to use this page.");
    }

    protected abstract HtmlElement createAdminContent() throws UnauthorizedOperationException;
}

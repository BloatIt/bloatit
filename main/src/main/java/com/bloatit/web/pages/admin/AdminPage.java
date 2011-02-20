package com.bloatit.web.pages.admin;

import static com.bloatit.framework.webserver.Context.tr;

import com.bloatit.common.Log;
import com.bloatit.data.DaoMember.Role;
import com.bloatit.framework.exceptions.RedirectException;
import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.framework.webserver.components.meta.HtmlElement;
import com.bloatit.framework.webserver.url.Url;
import com.bloatit.web.pages.LoggedPage;
import com.bloatit.web.url.LoginPageUrl;

public abstract class AdminPage extends LoggedPage {

    protected AdminPage(final Url url) {
        super(url);
    }

    @Override
    public final HtmlElement createRestrictedContent() throws RedirectException {
        if (session.getAuthToken().getMember().getRole() == Role.ADMIN) {
            try {
                return createAdminContent();
            } catch (final UnauthorizedOperationException e) {
                session.notifyError(tr("Are you sure you are admin ? "));
                Log.web().fatal("Admin content got a UnauthorizedOperationException", e);
            }
        }
        throw new RedirectException(new LoginPageUrl());
    }

    @Override
    public final String getRefusalReason() {
        return tr("You have to be Admin to use this page.");
    }

    protected abstract HtmlElement createAdminContent() throws UnauthorizedOperationException;
}

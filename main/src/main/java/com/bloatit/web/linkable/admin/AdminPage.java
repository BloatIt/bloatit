package com.bloatit.web.linkable.admin;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import com.bloatit.data.DaoMember.Role;
import com.bloatit.framework.exceptions.highlevel.ShallNotPassException;
import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.exceptions.lowlevel.UnauthorizedOperationException;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.Member;
import com.bloatit.web.pages.LoggedPage;
import com.bloatit.web.url.LoginPageUrl;

public abstract class AdminPage extends LoggedPage {

    protected AdminPage(final Url url) {
        super(url);
    }

    @Override
    public final HtmlElement createRestrictedContent(final Member loggedUser) throws RedirectException {
        if (loggedUser.getRole() == Role.ADMIN) {
            try {
                return createAdminContent();
            } catch (final UnauthorizedOperationException e) {
                session.notifyError(tr("Are you sure you are admin?"));
                throw new ShallNotPassException("Admin content got a UnauthorizedOperationException", e);
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

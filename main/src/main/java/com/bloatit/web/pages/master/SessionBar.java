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
package com.bloatit.web.pages.master;

import com.bloatit.framework.exceptions.lowlevel.UnauthorizedOperationException;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlLink;
import com.bloatit.framework.webprocessor.components.HtmlSpan;
import com.bloatit.framework.webprocessor.components.meta.HtmlBranch;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.context.Session;
import com.bloatit.model.Member;
import com.bloatit.web.HtmlTools;
import com.bloatit.web.components.MoneyDisplayComponent;
import com.bloatit.web.url.LoginPageUrl;
import com.bloatit.web.url.LogoutActionUrl;
import com.bloatit.web.url.MemberPageUrl;
import com.bloatit.web.url.MessageListPageUrl;
import com.bloatit.web.url.SignUpPageUrl;

public class SessionBar extends HtmlDiv {

    private static final String SESSION_BAR_COMPONENT_CSS_CLASS = "session_bar_component";

    protected SessionBar() {
        super();

        setId("session_bar");

        final Session session = Context.getSession();
        if (session.isLogged()) {
            // Display user name
            String displayName = "John Doe";
            displayName = session.getAuthToken().getMember().getDisplayName();
            final HtmlLink memberLink = new MemberPageUrl(Context.getSession().getAuthToken().getMember()).getHtmlLink(displayName);

            // Display user karma
            final HtmlBranch karma = new HtmlSpan();
            karma.setCssClass("karma");
            try {
                karma.addText(HtmlTools.compressKarma(session.getAuthToken().getMember().getKarma()));
            } catch (final UnauthorizedOperationException e) {
                // No right, no display the karma
            }
            add(new HtmlSpan().setCssClass(SESSION_BAR_COMPONENT_CSS_CLASS).add(memberLink).add(karma));

            try {
                add(new HtmlSpan().setCssClass(SESSION_BAR_COMPONENT_CSS_CLASS).add(new MoneyDisplayComponent(session.getAuthToken()
                                                                                                                     .getMember()
                                                                                                                     .getInternalAccount()
                                                                                                                     .getAmount())));
            } catch (final UnauthorizedOperationException e) {
                session.notifyBad(Context.tr("An unexpected error prevent us from displaying your internal account amount. Please notify us."));
            }

            // Display link to private messages
            Member me = session.getAuthToken().getMember();
            long nb;
            if ((nb = me.getInvitationCount()) > 0) {
                final HtmlLink messagesLink = new MessageListPageUrl().getHtmlLink(Context.tr("Invitations ({0})", nb));
                messagesLink.setCssClass("bold");
                HtmlBranch componentSpan = new HtmlSpan().setCssClass(SESSION_BAR_COMPONENT_CSS_CLASS).add(messagesLink);
                add(componentSpan);
            }

            // Display logout link
            final HtmlLink logoutLink = new LogoutActionUrl().getHtmlLink(Context.tr("Logout"));
            add(new HtmlSpan().setCssClass(SESSION_BAR_COMPONENT_CSS_CLASS).add(logoutLink));

        } else {
            final HtmlLink loginLink = new LoginPageUrl().getHtmlLink(Context.trc("Login (verb)", "Login"));
            final HtmlLink signupLink = new SignUpPageUrl().getHtmlLink(Context.trc("Sign up (verb)", "Sign up"));
            add(new HtmlSpan().setCssClass(SESSION_BAR_COMPONENT_CSS_CLASS).add(loginLink));
            add(new HtmlSpan().setCssClass(SESSION_BAR_COMPONENT_CSS_CLASS).add(signupLink));
        }
    }
}

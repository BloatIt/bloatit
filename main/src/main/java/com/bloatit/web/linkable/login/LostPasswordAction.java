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
package com.bloatit.web.linkable.login;

import java.io.IOException;

import com.bloatit.common.TemplateFile;
import com.bloatit.framework.exceptions.highlevel.ExternalErrorException;
import com.bloatit.framework.mailsender.Mail;
import com.bloatit.framework.mailsender.MailServer;
import com.bloatit.framework.utils.i18n.Localizator;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.ParamContainer.Protocol;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.Member;
import com.bloatit.model.managers.MemberManager;
import com.bloatit.model.right.AuthToken;
import com.bloatit.web.actions.ElveosAction;
import com.bloatit.web.url.IndexPageUrl;
import com.bloatit.web.url.LostPasswordActionUrl;
import com.bloatit.web.url.LostPasswordPageUrl;
import com.bloatit.web.url.RecoverPasswordPageUrl;

/**
 * Action part of the password recovery process.
 * <p>
 * This action is used after {@link LostPasswordPage}
 * </p>
 */
@ParamContainer(value="password/dolost", protocol=Protocol.HTTPS)
public class LostPasswordAction extends ElveosAction {
    private final LostPasswordActionUrl url;

    @RequestParam(role = Role.POST)
    private final String email;

    private Member m;

    public LostPasswordAction(final LostPasswordActionUrl url) {
        super(url);
        this.email = url.getEmail();
        this.url = url;
    }

    @Override
    protected Url doProcess(AuthToken authToken) {
        //TODO check all template at startup
        final TemplateFile templateFile = new TemplateFile("recover-password.mail");

        final String resetUrl = new RecoverPasswordPageUrl(m.getResetKey(), m.getLogin()).externalUrlString();
        templateFile.addNamedParameter("recovery_url", resetUrl);
        templateFile.addNamedParameter("member", m.getDisplayName());

        final String title = new Localizator(m.getLocale()).tr("Elveos password recovery");
        String content;
        try {
            content = templateFile.getContent(m.getLocaleUnprotected());
        } catch (final IOException e) {
            throw new ExternalErrorException("Error when loading mail template file.", e);
        }

        final Mail mail = new Mail(m.getEmailUnprotected(), title, content, "password-recovery");
        MailServer.getInstance().send(mail);

        session.notifyGood(Context.tr("A mail with the process to reset your password has been sent to {0}. Please check your mailbox.", email));
        return new IndexPageUrl();
    }

    @Override
    protected Url checkRightsAndEverything(AuthToken authToken) {
        m = MemberManager.getMemberByEmail(email);
        if (m == null) {
            session.notifyBad(Context.tr("No account match this email address. Please input another one."));
            return new LostPasswordPageUrl();
        }
        return NO_ERROR;
    }

    @Override
    protected Url doProcessErrors(AuthToken authToken) {
        return new LostPasswordPageUrl();
    }

    @Override
    protected void transmitParameters() {
        session.addParameter(url.getEmailParameter());
    }
}

package com.bloatit.web.linkable.login;

import java.io.IOException;

import com.bloatit.common.TemplateFile;
import com.bloatit.framework.exceptions.highlevel.ExternalErrorException;
import com.bloatit.framework.exceptions.highlevel.ShallNotPassException;
import com.bloatit.framework.exceptions.lowlevel.UnauthorizedOperationException;
import com.bloatit.framework.mailsender.Mail;
import com.bloatit.framework.mailsender.MailServer;
import com.bloatit.framework.utils.i18n.Localizator;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.masters.Action;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.Member;
import com.bloatit.model.managers.MemberManager;
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
@ParamContainer("password/dolost")
public class LostPasswordAction extends Action {
    private final LostPasswordActionUrl url;

    @RequestParam(role = Role.POST)
    private final String email;

    private Member m;

    public LostPasswordAction(LostPasswordActionUrl url) {
        super(url);
        this.email = url.getEmail();
        this.url = url;
    }

    @Override
    protected Url doProcess() {
        TemplateFile templateFile = new TemplateFile("recover-password.mail");

        try {
            String resetUrl = new RecoverPasswordPageUrl(m.getResetKey(), m.getLogin()).externalUrlString(Context.getHeader().getHttpHeader());
            templateFile.addNamedParameter("recovery_url", resetUrl);
            templateFile.addNamedParameter("member", m.getDisplayName());
        } catch (UnauthorizedOperationException e) {
            throw new ShallNotPassException("An error prevented us to proceed with password reset. Please notify us.", e);
        }

        String title = new Localizator(m.getUserLocale()).tr("Elveos password recovery");
        String content;
        try {
            content = templateFile.getContent(m.getLocaleUnprotected());
        } catch (IOException e) {
            throw new ExternalErrorException("Error when loading mail template file.", e);
        }

        Mail mail = new Mail(m.getEmailUnprotected(), title, content, "password-recovery");
        MailServer.getInstance().send(mail);

        session.notifyGood(Context.tr("A mail with the process to reset your password has been sent to {0}. Please check your mailbox.", email));
        return new IndexPageUrl();
    }

    @Override
    protected Url checkRightsAndEverything() {
        m = MemberManager.getMemberByEmail(email);
        if (m == null) {
            session.notifyBad(Context.tr("No account match this email address. Please input another one."));
            return new LostPasswordPageUrl();
        }
        return NO_ERROR;
    }

    @Override
    protected Url doProcessErrors() {
        return new LostPasswordPageUrl();
    }

    @Override
    protected void transmitParameters() {
        session.addParameter(url.getEmailParameter());
    }
}

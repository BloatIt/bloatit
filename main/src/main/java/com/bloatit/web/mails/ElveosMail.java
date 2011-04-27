package com.bloatit.web.mails;

import java.io.IOException;

import com.bloatit.common.TemplateFile;
import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;
import com.bloatit.framework.exceptions.highlevel.ShallNotPassException;
import com.bloatit.framework.exceptions.lowlevel.UnauthorizedOperationException;
import com.bloatit.framework.mailsender.Mail;
import com.bloatit.framework.mailsender.MailServer;
import com.bloatit.framework.utils.i18n.Localizator;
import com.bloatit.model.Member;

public abstract class ElveosMail {

    private final TemplateFile content;
    private final String title;

    private ElveosMail(final TemplateFile content, final String title) {
        super();
        this.content = content;
        this.title = title;
    }

    public final void addNamedParameter(final String name, final String value) {
        content.addNamedParameter(name, value);
    }

    public final void sendMail(final Member to, final String mailSenderID) {
        try {
            content.addNamedParameter("member", to.getDisplayName());
            MailServer.getInstance().send(new Mail(to.getEmail(),
                                                   new Localizator(to.getUserLocale()).tr(title),
                                                   content.getContent(to.getUserLocale()),
                                                   mailSenderID));
        } catch (final IOException e) {
            throw new BadProgrammerException(e);
        } catch (final UnauthorizedOperationException e) {
            throw new ShallNotPassException(e);
        }
    }

    /**
     * Fake tr to have gettext parse this string.
     * 
     * @param str the string to return
     * @return <code>str</code>
     */
    private static String tr(final String str) {
        return str;
    }

    public static class ChargingAccountSuccess extends ElveosMail {
        public ChargingAccountSuccess(final String reference, final String totalAmount, final String credited) {
            super(new TemplateFile("charging-success.mail"), tr("elveos.org: Payment accepted"));
            addNamedParameter("reference", reference);
            addNamedParameter("total_amount", totalAmount);
            addNamedParameter("credited", credited);
        }
    }

    public static class ContributionSuccess extends ElveosMail {
        public ContributionSuccess(final String featureName, final String amount) {
            super(new TemplateFile("contribution-success.mail"), tr("elveos.org: Contribution validated"));
            addNamedParameter("feature_name", featureName);
            addNamedParameter("amount", amount);
        }
    }
}

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
package com.bloatit.framework.mails;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.bloatit.common.TemplateFile;
import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;
import com.bloatit.framework.exceptions.highlevel.ShallNotPassException;
import com.bloatit.framework.mailsender.Mail;
import com.bloatit.framework.mailsender.MailServer;
import com.bloatit.framework.utils.i18n.Localizator;
import com.bloatit.model.Member;
import com.bloatit.model.right.UnauthorizedOperationException;
import com.bloatit.web.url.MoneyWithdrawalAdminPageUrl;

public abstract class ElveosMail {

    private final TemplateFile content;
    private final String title;

    private ElveosMail(final TemplateFile content, final String title) {
        super();
        this.content = content;
        this.title = title;
    }

    protected final void addNamedParameter(final String name, final String value) {
        content.addNamedParameter(name, value);
    }

    public final void sendMail(final Member to, final String mailSenderID) {
        try {
            content.addNamedParameter("member", to.getDisplayName());
            MailServer.getInstance().send(new Mail(to.getEmail(),
                                                   new Localizator(to.getLocale()).tr(title),
                                                   content.getContent(to.getLocale()),
                                                   mailSenderID));
        } catch (final IOException e) {
            throw new BadProgrammerException(e);
        } catch (final UnauthorizedOperationException e) {
            throw new ShallNotPassException(e);
        }
    }

    /**
     * Sends a mail to a given mail address
     * 
     * @param to the mail addresse of the receiver
     * @param mailSenderID an ID that will be put at the end of each sent mail
     */
    public final void sendMail(final String to, final String mailSenderID) {
        try {
            MailServer.getInstance().send(new Mail(to, title, content.getContent(null), mailSenderID));
        } catch (final IOException e) {
            throw new BadProgrammerException(e);
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

    public static class WithdrawalRequestedMail extends ElveosMail {
        public WithdrawalRequestedMail(final String reference, final String amount, final String iban) {
            super(new TemplateFile("withdrawal-requested.mail"), tr("elveos.org: Money withdrawal request"));
            addNamedParameter("amount", amount);
            addNamedParameter("iban", iban);
            addNamedParameter("reference", reference);
        }
    }

    public static class WithdrawalCompleteMail extends ElveosMail {
        public WithdrawalCompleteMail(final String reference, final String amount, final String iban) {
            super(new TemplateFile("withdrawal-complete.mail"), tr("elveos.org: Money withdrawal complete"));
            addNamedParameter("amount", amount);
            addNamedParameter("iban", iban);
            addNamedParameter("reference", reference);
        }
    }

    public static class WithdrawalAdminMail extends ElveosMail {
        private static DateFormat ISO8601Local = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        public WithdrawalAdminMail(final String reference, final String amount, final String iban, final String memberName) {
            super(new TemplateFile("withdrawal-admin.mail"), "[ELVEOS ADMINISTRATION] New money withdrawal request");
            addNamedParameter("amount", amount);
            addNamedParameter("iban", iban);
            addNamedParameter("reference", reference);
            addNamedParameter("member", memberName);
            addNamedParameter("date", ISO8601Local.format(new Date()));
            addNamedParameter("url", new MoneyWithdrawalAdminPageUrl().externalUrlString());
        }
    }
}

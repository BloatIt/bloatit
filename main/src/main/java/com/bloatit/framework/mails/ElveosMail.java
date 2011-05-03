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

    public static class WithdrawalComplete extends ElveosMail {
        public WithdrawalComplete(final String reference, final String amount, final String iban) {
            super(new TemplateFile("withdrawal-complete.mail"), tr("elveos.org: Money withdrawal complete"));
            addNamedParameter("amount", amount);
            addNamedParameter("iban", iban);
            addNamedParameter("reference", reference);
        }
    }
}

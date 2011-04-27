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
package com.bloatit.framework.mailsender;

/**
 * A simple class that describes a mail
 */
public class Mail {

    private final String content;
    private final String to;
    private final String subject;
    private final String mailSenderId;

    /**
     * <p>
     * Creates a new mail
     * </p>
     * <p>
     * Example : <br />
     * <code>
     * String title = "This is a fantastic email"; <br />
     * String content = "Salut les loulous ...\n"; <br />
     * Mail mail = new Mail("mail@example.come", title, content, session.getAuthToken().getMember().getId()); <br />
     * </code>
     * </p>
     * 
     * @param to the email address of the receiver of the mail
     * @param title the title of the email
     * @param content the text content of the email
     * @param mailSenderID a unique identifier to the mail (recommended : the
     *            permanent id of the sender)
     */
    public Mail(final String to, final String title, final String content, final String mailSenderID) {
        this.to = to;
        this.content = content;
        this.subject = title;
        this.mailSenderId = mailSenderID;
    }

    /**
     * @return the text content of the mail
     */
    public String getContent() {
        return content;
    }

    /**
     * @return the email addresse of the receiver
     */
    public String getTo() {
        return to;
    }

    /**
     * @return the text title of the mail
     */
    public String getSubject() {
        return subject;
    }

    /**
     * @return an identifier to the mail sender
     */
    public String getMailSenderID() {
        return mailSenderId;
    }
}

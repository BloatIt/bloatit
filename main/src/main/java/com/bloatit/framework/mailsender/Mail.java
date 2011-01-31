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
     * @param to the email addresse of the receiver of the mail
     * @param title the title of the email
     * @param content the text content of the email
     * @param mailSenderID a unique identifier to the mail (recommended : the permanent id
     *        of the sender)
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

package com.bloatit.web.utils.url;


@SuppressWarnings("unused")
public class LoggedPageUrl extends UrlComponent {
    public LoggedPageUrl(final Parameters params, final Parameters session) {
        this();
        parseParameters(params, false);
        parseParameters(session, true);
    }

    public LoggedPageUrl() {
        super();
    }

    @Override
    protected void doRegister() {
    }

    @Override
    public LoggedPageUrl clone() {
        final LoggedPageUrl other = new LoggedPageUrl();
        return other;
    }
}

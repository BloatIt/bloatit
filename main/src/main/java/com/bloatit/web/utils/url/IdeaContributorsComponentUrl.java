package com.bloatit.web.utils.url;


@SuppressWarnings("unused")
public class IdeaContributorsComponentUrl extends UrlComponent {
    public IdeaContributorsComponentUrl(final Parameters params, final Parameters session) {
        this();
        parseParameters(params, false);
        parseParameters(session, true);
    }

    public IdeaContributorsComponentUrl() {
        super();
    }

    private HtmlPagedListUrl participationsListUrl = new HtmlPagedListUrl();

    public HtmlPagedListUrl getParticipationsListUrl() {
        return this.participationsListUrl;
    }

    public void setParticipationsListUrl(final HtmlPagedListUrl arg) {
        this.participationsListUrl = arg;
    }

    @Override
    protected void doRegister() {
        register(participationsListUrl);
    }

    @Override
    public IdeaContributorsComponentUrl clone() {
        final IdeaContributorsComponentUrl other = new IdeaContributorsComponentUrl();
        other.participationsListUrl = this.participationsListUrl.clone();
        return other;
    }
}

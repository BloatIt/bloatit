package com.bloatit.web.utils.url;


public class IdeaContributorsComponentUrl extends UrlComponent {
    public IdeaContributorsComponentUrl() {
        super();
    }

    public IdeaContributorsComponentUrl(final Parameters params) {
        this();
        parseParameters(params);
    }

    private HtmlPagedListUrl participationsListUrl = new HtmlPagedListUrl();

    public HtmlPagedListUrl getParticipationsListUrl() {
        return this.participationsListUrl;
    }

    public void setParticipationsListUrl(final HtmlPagedListUrl arg0) {
        this.participationsListUrl = arg0;
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

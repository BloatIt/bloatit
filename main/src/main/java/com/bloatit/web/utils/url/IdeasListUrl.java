package com.bloatit.web.utils.url;


public class IdeasListUrl extends Url {
    public IdeasListUrl() {
        super("IdeasList");
    }

    public IdeasListUrl(final Parameters params) {
        this();
        parseParameters(params);
    }

    private HtmlPagedListUrl pagedIdeaListUrl = new HtmlPagedListUrl();

    public HtmlPagedListUrl getPagedIdeaListUrl() {
        return this.pagedIdeaListUrl;
    }

    public void setPagedIdeaListUrl(final HtmlPagedListUrl arg0) {
        this.pagedIdeaListUrl = arg0;
    }

    @Override
    protected void doRegister() {
        register(pagedIdeaListUrl);
    }

    @Override
    public IdeasListUrl clone() {
        final IdeasListUrl other = new IdeasListUrl();
        other.pagedIdeaListUrl = this.pagedIdeaListUrl.clone();
        return other;
    }
}

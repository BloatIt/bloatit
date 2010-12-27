package com.bloatit.web.utils.url;

import com.bloatit.web.exceptions.RedirectException;

@SuppressWarnings("unused")
public class IdeasListUrl extends Url {
    public static String getName() {
        return "ideas/list";
    }

    @Override
    public com.bloatit.web.html.pages.IdeasList createPage() throws RedirectException {
        return new com.bloatit.web.html.pages.IdeasList(this);
    }

    public IdeasListUrl(final Parameters params, final Parameters session) {
        this();
        parseParameters(params, false);
        parseParameters(session, true);
    }

    public IdeasListUrl() {
        super(getName());
    }

    private HtmlPagedListUrl pagedIdeaListUrl = new HtmlPagedListUrl();

    public HtmlPagedListUrl getPagedIdeaListUrl() {
        return this.pagedIdeaListUrl;
    }

    public void setPagedIdeaListUrl(final HtmlPagedListUrl arg) {
        this.pagedIdeaListUrl = arg;
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

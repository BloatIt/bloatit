package com.bloatit.web.utils.url;

import com.bloatit.web.exceptions.RedirectException;

@SuppressWarnings("unused")
public class MembersListPageUrl extends Url {
    public static String getName() {
        return "memberList";
    }

    @Override
    public com.bloatit.web.html.pages.MembersListPage createPage() throws RedirectException {
        return new com.bloatit.web.html.pages.MembersListPage(this);
    }

    public MembersListPageUrl(final Parameters params, final Parameters session) {
        this();
        parseParameters(params, false);
        parseParameters(session, true);
    }

    public MembersListPageUrl() {
        super(getName());
    }

    private HtmlPagedListUrl pagedMemberListUrl = new HtmlPagedListUrl();

    public HtmlPagedListUrl getPagedMemberListUrl() {
        return this.pagedMemberListUrl;
    }

    public void setPagedMemberListUrl(final HtmlPagedListUrl arg) {
        this.pagedMemberListUrl = arg;
    }

    @Override
    protected void doRegister() {
        register(pagedMemberListUrl);
    }

    @Override
    public MembersListPageUrl clone() {
        final MembersListPageUrl other = new MembersListPageUrl();
        other.pagedMemberListUrl = this.pagedMemberListUrl.clone();
        return other;
    }
}

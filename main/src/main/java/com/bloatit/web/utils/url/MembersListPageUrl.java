package com.bloatit.web.utils.url;


public class MembersListPageUrl extends Url {
    public MembersListPageUrl() {
        super("MembersListPage");
    }

    public MembersListPageUrl(final Parameters params) {
        this();
        parseParameters(params);
    }

    private HtmlPagedListUrl pagedMemberListUrl = new HtmlPagedListUrl();

    public HtmlPagedListUrl getPagedMemberListUrl() {
        return this.pagedMemberListUrl;
    }

    public void setPagedMemberListUrl(final HtmlPagedListUrl arg0) {
        this.pagedMemberListUrl = arg0;
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

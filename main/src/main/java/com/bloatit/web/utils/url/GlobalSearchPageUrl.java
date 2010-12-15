package com.bloatit.web.utils.url;

import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.RequestParam.Role;

public class GlobalSearchPageUrl extends Url {
    public GlobalSearchPageUrl() {
        super("GlobalSearchPage");
    }

    public GlobalSearchPageUrl(final Parameters params) {
        this();
        parseParameters(params);
    }

    private java.lang.String searchString;
    private HtmlPagedListUrl pagedMemberListUrl = new HtmlPagedListUrl();

    public java.lang.String getSearchString() {
        return this.searchString;
    }

    public void setSearchString(final java.lang.String arg0) {
        this.searchString = arg0;
    }

    public HtmlPagedListUrl getPagedMemberListUrl() {
        return this.pagedMemberListUrl;
    }

    public void setPagedMemberListUrl(final HtmlPagedListUrl arg0) {
        this.pagedMemberListUrl = arg0;
    }

    @Override
    protected void doRegister() {
        register(new Parameter("global_search", getSearchString(), java.lang.String.class, Role.GET, Level.ERROR,
                "Error: invalid value (%value) for parameter \"%param\""));
        register(pagedMemberListUrl);
    }

    @Override
    public GlobalSearchPageUrl clone() {
        final GlobalSearchPageUrl other = new GlobalSearchPageUrl();
        other.searchString = this.searchString;
        other.pagedMemberListUrl = this.pagedMemberListUrl.clone();
        return other;
    }
}

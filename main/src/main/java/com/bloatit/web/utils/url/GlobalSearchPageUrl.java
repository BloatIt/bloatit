package com.bloatit.web.utils.url;

import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.RequestParam.Role;
import com.bloatit.web.exceptions.RedirectException;
import com.bloatit.web.utils.annotations.Loaders;
import com.bloatit.web.utils.annotations.Loaders.ConversionErrorException;

@SuppressWarnings("unused")
public class GlobalSearchPageUrl extends Url {
    public static String getName() {
        return "search";
    }

    @Override
    public com.bloatit.web.html.pages.GlobalSearchPage createPage() throws RedirectException {
        return new com.bloatit.web.html.pages.GlobalSearchPage(this);
    }

    public GlobalSearchPageUrl(final Parameters params, final Parameters session) {
        this();
        parseParameters(params, false);
        parseParameters(session, true);
    }

    public GlobalSearchPageUrl() {
        super(getName());
        try {
            this.searchString.setValue(Loaders.fromStr(java.lang.String.class, ""));
        } catch (final ConversionErrorException e) {
            e.printStackTrace();
            assert false;
        }
    }

    private UrlParameter<java.lang.String> searchString = new UrlParameter<java.lang.String>("global_search", null, java.lang.String.class, Role.GET,
            Level.ERROR, "Error: invalid value (%value) for parameter \"%param\"");
    private HtmlPagedListUrl pagedMemberListUrl = new HtmlPagedListUrl();

    public java.lang.String getSearchString() {
        return this.searchString.getValue();
    }

    public void setSearchString(final java.lang.String arg) {
        this.searchString.setValue(arg);
    }

    public HtmlPagedListUrl getPagedMemberListUrl() {
        return this.pagedMemberListUrl;
    }

    public void setPagedMemberListUrl(final HtmlPagedListUrl arg) {
        this.pagedMemberListUrl = arg;
    }

    @Override
    protected void doRegister() {
        register(searchString);
        register(pagedMemberListUrl);
    }

    @Override
    public GlobalSearchPageUrl clone() {
        final GlobalSearchPageUrl other = new GlobalSearchPageUrl();
        other.searchString = this.searchString.clone();
        other.pagedMemberListUrl = this.pagedMemberListUrl.clone();
        return other;
    }
}

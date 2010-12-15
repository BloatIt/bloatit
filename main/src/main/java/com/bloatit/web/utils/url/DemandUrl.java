package com.bloatit.web.utils.url;

import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.RequestParam.Role;

public class DemandUrl extends Url {
    public DemandUrl() {
        super("demand");
    }

    public DemandUrl(Parameters params) {
        this();
        parseParameters(params);
    }

    private PagedListUrl pagedMemberListUrl = new PagedListUrl();

    public PagedListUrl getPagedMemberListUrl() {
        return this.pagedMemberListUrl;
    }

    public void setPagedMemberListUrl(PagedListUrl arg0) {
        this.pagedMemberListUrl = arg0;
    }

    @Override
    protected void doRegister() {
        register(pagedMemberListUrl);
    }

    public DemandUrl clone() {
        DemandUrl other = new DemandUrl();
        other.pagedMemberListUrl = this.pagedMemberListUrl;
        return other;
    }
}

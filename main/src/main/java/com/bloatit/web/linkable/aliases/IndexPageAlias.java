package com.bloatit.web.linkable.aliases;

import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.web.linkable.master.AliasAction;
import com.bloatit.web.url.IndexPageAliasUrl;
import com.bloatit.web.url.IndexPageUrl;

@ParamContainer("index")
public class IndexPageAlias extends AliasAction {
    public IndexPageAlias(IndexPageAliasUrl url) {
        super(url, new IndexPageUrl());
    }
}

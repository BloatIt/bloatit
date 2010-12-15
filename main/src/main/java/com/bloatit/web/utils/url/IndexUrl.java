package com.bloatit.web.utils.url;

import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.RequestParam.Role;

public class IndexUrl extends Url {
    public IndexUrl() {
        super("index");
    }

    public IndexUrl(Parameters params) {
        this();
        parseParameters(params);
    }

    @Override
    protected void doRegister() {
    }

    public IndexUrl clone() {
        IndexUrl other = new IndexUrl();
        return other;
    }
}

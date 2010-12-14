package com.bloatit.web.utils.url;

import java.util.Map;

import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.RequestParam.Role;
import com.bloatit.web.utils.annotations.RequestParamSetter.Messages;

public class SubUrl extends Url {
    Double plop;
    Integer plip;
    UrlComponent cmp;

    public SubUrl() {
        super("Hello");
    }

    public SubUrl(Map<String, String> params) {
        super("Hello");
        parseParameterMap(params);
    }

    public Double getPlop() {
        return plop;
    }

    public void setPlop(Double plop) {
        this.plop = plop;
    }

    public Integer getPlip() {
        return plip;
    }

    public void setPlip(Integer plip) {
        this.plip = plip;
    }

    @Override
    protected void doRegister(Messages messages) {
        register(new Parameter(messages, "plop", plop, Double.class, Role.GET, Level.ERROR, "plop"));
        register(new Parameter(messages, "plip", plip, Integer.class, Role.GET, Level.ERROR, "plop"));
        register(cmp);
    }
}

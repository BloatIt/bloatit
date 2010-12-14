package com.bloatit.web.utils.url;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.bloatit.web.server.Context;
import com.bloatit.web.utils.annotations.RequestParamSetter.Messages;

public abstract class Url extends UrlComponent {

    private final String name;
    private List<UrlComponent> components = new ArrayList<UrlComponent>();

    protected Url(String name) {
        super();
        this.name = name;
    }

    protected abstract void doRegister(Messages messages);
    
    protected void register(UrlComponent component){
        components.add(component);
    }

    protected final Messages parseParameterMap(Map<String, String> params) {
        super.parseParameterMap(params); // doRegister done in super class.
        for (UrlComponent comp : components) {
            comp.parseParameterMap(params);
        }
        return messages;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("/").append(Context.getSession().getLanguage().getCode());
        sb.append("/").append(name);
        sb.append(super.toString());
        for (UrlComponent comp : components) {
            sb.append(comp.toString());
        }
        return sb.toString();
    }

}

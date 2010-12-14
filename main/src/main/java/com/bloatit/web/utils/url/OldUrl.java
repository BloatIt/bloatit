package com.bloatit.web.utils.url;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;


import com.bloatit.web.annotations.ParamContainer;
import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.server.Context;
import com.bloatit.web.server.Linkable;
import com.bloatit.web.server.Session;

public class OldUrl {
    private final Parameters parameters;
    private final Class<? extends Linkable> linkable;
    private final Session session;

    public static String getPageName(final Class<? extends Linkable> linkable) {
        if (linkable.getAnnotation(ParamContainer.class) != null) {
            return linkable.getAnnotation(ParamContainer.class).value();
        } else {
            return linkable.getSimpleName().toLowerCase();
        }
    }

    public OldUrl(final Class<? extends Linkable> linkable, final Parameters parameters) {
        super();
        this.parameters = parameters;
        this.linkable = linkable;
        this.session = Context.getSession();
    }

    protected OldUrl() {
        super();
        this.parameters = new Parameters();
        this.linkable = null;
        this.session = null;
    }

    public boolean isValid() {
        return session != null && linkable != null;
    }

    public String getPageName() {
        // find name
        if (linkable.getAnnotation(ParamContainer.class) != null) {
            return linkable.getAnnotation(ParamContainer.class).value();
        } else {
            return linkable.getName().toLowerCase();
        }

    }

    public OldUrl addParameter(final String name, final String value) {
        parameters.put(name, value);
        return this;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(session.getLanguage().getCode()).append("/").append(getPageName()).append("/");
        for (final Entry<String, String> param : parameters.entrySet()) {
            sb.append(param.getKey());
            sb.append("-");
            sb.append(param.getValue());
            sb.append("/");
        }
        // TODO testme
        return sb.toString();
    }

    public Parameters getParameters() {
        return parameters;
    }
    
    public static void main(String[] args) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", "12");
        params.put("title", "Hello ceci est---un ^¨ \n --& titre.               ---  ");
        params.put("demand_tab_key", "key");

        DemandUrl demandUrl = new DemandUrl(params);

        System.out.println(demandUrl);

        System.out.println(demandUrl.getDemand());

        System.out.println(demandUrl.getMessages().hasMessage(Level.ERROR));
    }

}

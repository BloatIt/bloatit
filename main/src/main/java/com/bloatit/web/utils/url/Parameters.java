package com.bloatit.web.utils.url;

import java.util.HashMap;
import java.util.Map;

public class Parameters {
    Map<String, String> param = new HashMap<String, String>();

    public Parameters() {
        super();
    }

    public Parameters(final String name, final String value) {
        super();
        param.put(value, name);
    }

    public final Parameters add(final String name, final String value) {
        param.put(value, name);
        return this;
    }

    public final String pick(final String name) {
        final String value = param.get(name);
        if (value != null) {
            param.remove(name);
        }
        return value;
    }

    public final String look(final String name) {
        return param.get(name);
    }
}

package com.bloatit.web.utils.url;

import java.util.HashMap;

public class Parameters extends HashMap<String, String> {
    private static final long serialVersionUID = 2992292463988772864L;

    public Parameters() {
        super();
    }

    public Parameters(final String name, final String value) {
        super();
        put(name, value);
    }

    public final Parameters add(final String name, final String value) {
        put(name, value );
        return this;
    }

    public final String pick(final String name) {
        final String value = get(name);
        if (value != null) {
            remove(name);
        }
        return value;
    }

    public final String look(final String name) {
        return get(name);
    }
}

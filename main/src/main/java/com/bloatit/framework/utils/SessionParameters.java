package com.bloatit.framework.utils;

import com.bloatit.framework.webserver.url.UrlParameter;

public class SessionParameters extends GenericParameters<UrlParameter<?, ?>, UrlParameter<?, ?>> {

     /**
     * Create an empty list of parameter
     */
    public SessionParameters() {
        super();
    }

    /**
     * Add a parameter to the list
     *
     * @param name the name of the parameter to add
     * @param value the value of the paramter to add
     */
    @Override
    public final void add(final String name, final UrlParameter<?, ?> value) {
        getElements().put(name, value);
    }




}

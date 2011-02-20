package com.bloatit.framework.webserver.components.form;

/**
 * A hidden field
 */
public class HtmlHidden extends HtmlSimpleInput {

    public HtmlHidden(final String name, final String value) {
        super("hidden");
        setName(name);
        addAttribute("value", value);
    }
}

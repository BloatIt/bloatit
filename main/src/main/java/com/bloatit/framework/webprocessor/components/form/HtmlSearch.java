package com.bloatit.framework.webprocessor.components.form;


public class HtmlSearch extends HtmlStringFormField {

    public HtmlSearch(final String name) {
        super(InputBlock.create(new HtmlSimpleInput("search")), name);
    }

    public HtmlSearch(final String name, final String label) {
        super(InputBlock.create(new HtmlSimpleInput("search")), name, label);
    }

    @Override
    protected void doSetDefaultStringValue(final String value) {
        addAttribute("value", value);
    }
}

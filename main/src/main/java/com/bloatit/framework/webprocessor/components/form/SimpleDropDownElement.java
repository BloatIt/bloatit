package com.bloatit.framework.webprocessor.components.form;

public class SimpleDropDownElement implements DropDownElement {

    private final String name;
    private final String code;

    public SimpleDropDownElement(final String name, final String code) {
        super();
        this.name = name;
        this.code = code;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getCode() {
        return code;
    }

}

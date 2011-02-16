package com.bloatit.framework.webserver.components.form;

public class SimpleDropDownElement implements DropDownElement {

    private String name;
    private String code;

    public SimpleDropDownElement(String name, String code) {
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

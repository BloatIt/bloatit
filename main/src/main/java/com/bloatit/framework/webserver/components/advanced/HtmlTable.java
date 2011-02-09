package com.bloatit.framework.webserver.components.advanced;

import com.bloatit.framework.webserver.components.HtmlGenericElement;

public class HtmlTable extends HtmlGenericElement {

    private final String key;

    public HtmlTable(String key) {
        super("table");
        this.key = key;

    }


}

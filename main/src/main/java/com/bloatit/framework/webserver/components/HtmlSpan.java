package com.bloatit.framework.webserver.components;

import com.bloatit.framework.webserver.components.meta.HtmlBranch;

public class HtmlSpan extends HtmlBranch {

    public HtmlSpan() {
        super("span");
    }

    public HtmlSpan(HtmlSpan span) {
        super(span);
    }

    public HtmlSpan(final String cssClass) {
        super("span");
        addAttribute("class", cssClass);
    }

    public HtmlSpan(final String cssClass, final String id) {
        super("span");
        addAttribute("class", cssClass);
        addAttribute("id", id);
    }

    @Override
    public HtmlSpan clone() {
        return new HtmlSpan(this);
    }
}

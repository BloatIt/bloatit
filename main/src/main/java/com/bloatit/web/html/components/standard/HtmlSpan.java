package com.bloatit.web.html.components.standard;

import com.bloatit.web.html.HtmlBranch;

public class HtmlSpan extends HtmlBranch {

    public HtmlSpan() {
        super("span");
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
}

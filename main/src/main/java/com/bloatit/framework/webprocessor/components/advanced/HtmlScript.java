package com.bloatit.framework.webprocessor.components.advanced;

import com.bloatit.framework.webprocessor.components.meta.HtmlLeaf;
import com.bloatit.framework.webprocessor.components.meta.XmlText;

public class HtmlScript extends HtmlLeaf {
    public HtmlScript() {
        super("script");
        addAttribute("type", "text/javascript");
    }

    public HtmlScript append(String script) {
        add(new XmlText(script + "\n"));
        return this;
    }

    @Override
    public boolean selfClosable() {
        return false;
    }
}

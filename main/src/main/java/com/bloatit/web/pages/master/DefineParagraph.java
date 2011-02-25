package com.bloatit.web.pages.master;

import com.bloatit.framework.webserver.components.HtmlParagraph;
import com.bloatit.framework.webserver.components.HtmlSpan;
import com.bloatit.framework.webserver.components.meta.HtmlElement;

public class DefineParagraph extends HtmlParagraph {

    public DefineParagraph(String key, String text) {
        add(new HtmlSpan("define_key").addText(key));
        addText(text);
    }

    public DefineParagraph(String key, HtmlElement body) {
        add(new HtmlSpan("define_key").addText(key));
        add(body);
    }
}

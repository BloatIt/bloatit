package com.bloatit.web.pages.master;

import com.bloatit.framework.webserver.components.HtmlParagraph;
import com.bloatit.framework.webserver.components.HtmlSpan;
import com.bloatit.framework.webserver.components.meta.HtmlElement;
import com.bloatit.framework.webserver.components.meta.XmlNode;

public class DefineParagraph extends HtmlParagraph {

    public DefineParagraph(String key, String text) {
        setCssClass("define_p");
        add(new HtmlSpan("define_key").addText(key));
        addText(text);
    }

    public DefineParagraph(String key, HtmlElement body) {
        setCssClass("define_p");
        add(new HtmlSpan("define_key").addText(key));
        add(body);
    }

    public XmlNode addCssClass(String css) {
        setCssClass("define_p " + css);
        return this;
    }
}

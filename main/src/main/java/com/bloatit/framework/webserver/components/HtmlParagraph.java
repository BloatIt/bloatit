package com.bloatit.framework.webserver.components;

import com.bloatit.framework.webserver.components.meta.HtmlBranch;
import com.bloatit.framework.webserver.components.meta.HtmlTagText;
import com.bloatit.framework.webserver.components.meta.HtmlText;

public class HtmlParagraph extends HtmlBranch {

    public HtmlParagraph() {
        super("p");
    }

    /**
     * Creates an HtmlParagraph with a preset text
     *
     * @param text the text that will be displayed
     */
    public HtmlParagraph(final String text) {
        this(new HtmlText(text));
    }

    /**
     * Creates an HtmlParagraph with a preset text and a given css style
     *
     * @param cssClass the name of the css class applied
     * @param text the text that will be displayed
     */
    public HtmlParagraph(final String text, final String cssClass) {
        super("p");
        addAttribute("class", cssClass);
        add(new com.bloatit.framework.webserver.components.meta.HtmlText(text));
    }

    public HtmlParagraph(final HtmlTagText htmlText) {
        super("p");
        add(htmlText);
    }
}

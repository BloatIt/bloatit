package com.bloatit.web.html.components.standard;

import com.bloatit.web.html.pages.HtmlContainerElement;

public class HtmlParagraph extends HtmlContainerElement {

    public HtmlParagraph() {
        super("p");
    }
    
    /**
     * Creates an HtmlParagraph with a preset text
     * 
     * @param text the text that will be displayed
     */
    public HtmlParagraph(final String text) {
        super("p");
        add(new com.bloatit.web.html.HtmlText(text));
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
        add(new com.bloatit.web.html.HtmlText(text));
    }

}

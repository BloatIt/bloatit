package com.bloatit.framework.webserver.components.renderer;

import com.bloatit.framework.exceptions.FatalErrorException;
import com.bloatit.framework.utils.parsers.MarkdownParser;
import com.bloatit.framework.utils.parsers.ParsingException;
import com.bloatit.framework.webserver.components.PlaceHolderElement;
import com.bloatit.framework.webserver.components.meta.HtmlTagText;

/**
 * A class used to create renderers 
 */
public class HtmlMarkdownRenderer extends PlaceHolderElement {
    private final MarkdownParser parser;
    
    public HtmlMarkdownRenderer(final String text) {
        super();
        parser = new MarkdownParser();
        try {
            String parse = parser.parse(text);
            add(new HtmlTagText(parse));
        } catch (ParsingException e) {
            throw new FatalErrorException("An error occured during markdown parsing", e);
        }
    }
}

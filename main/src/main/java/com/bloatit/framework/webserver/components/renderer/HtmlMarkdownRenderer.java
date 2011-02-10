package com.bloatit.framework.webserver.components.renderer;

import com.bloatit.framework.exceptions.FatalErrorException;
import com.bloatit.framework.utils.parsers.MarkdownParser;
import com.bloatit.framework.utils.parsers.ParsingException;
import com.bloatit.framework.webserver.components.PlaceHolderElement;
import com.bloatit.framework.webserver.components.meta.HtmlTagText;

/**
 * <p>
 * A class used to represent Markdown text in html format
 * </p>
 */
public class HtmlMarkdownRenderer extends PlaceHolderElement {
    private MarkdownParser parser;
    private String renderered;

    /**
     * Creates a new MarkdownRenderer based on some markdown text
     * 
     * @param text
     *            the markdown text
     */
    public HtmlMarkdownRenderer(final String text) {
        this(text, false);
    }

    /**
     * Creates a new MarkdownRenderer based on markdown or html text
     * 
     * @param text
     *            the content to display, must be markdown text if
     *            <code>alreadyRenderer</code> is <code>true</code> or html text
     *            if <code>alreadyRenderer</code> is <code>false</code>
     */
    public HtmlMarkdownRenderer(final String text, boolean alreadyRendered) {
        super();
        if (alreadyRendered) {
            this.renderered = text;
            add(new HtmlTagText(text));
        } else {
            parser = new MarkdownParser();
            try {
                renderered = parser.parse(text);
                add(new HtmlTagText(renderered));
            } catch (ParsingException e) {
                throw new FatalErrorException("An error occured during markdown parsing", e);
            }
        }
    }

    /**
     * @return the html content
     */
    public String getRendereredContent() {
        return renderered;
    }
}

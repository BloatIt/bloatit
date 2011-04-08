package com.bloatit.framework.webprocessor.components.renderer;

import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;
import com.bloatit.framework.utils.parsers.MarkdownParser;
import com.bloatit.framework.utils.parsers.ParsingException;
import com.bloatit.framework.webprocessor.components.PlaceHolderElement;
import com.bloatit.framework.webprocessor.components.meta.XmlText;

/**
 * <p>
 * A class used to represent Markdown text in html format
 * </p>
 */
public class HtmlMarkdownRenderer extends PlaceHolderElement {
    private final MarkdownParser parser;
    private String renderered;

    /**
     * Creates a new MarkdownRenderer based on markdown or html text
     * 
     * @param text the content to display, must be markdown text if
     *            <code>alreadyRenderer</code> is <code>true</code> or html text
     *            if <code>alreadyRenderer</code> is <code>false</code>
     */
    public HtmlMarkdownRenderer(final String text) {
        super();
        parser = new MarkdownParser();
        try {
            renderered = parser.parse(text);
            add(new XmlText(renderered));
        } catch (final ParsingException e) {
            throw new BadProgrammerException("An error occured during markdown parsing", e);
        }
    }

    /**
     * @return the html content
     */
    public String getRendereredContent() {
        return renderered;
    }

}

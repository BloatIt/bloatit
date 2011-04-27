package com.bloatit.framework.webprocessor.components.renderer;

import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;
import com.bloatit.framework.utils.cache.MemoryCache;
import com.bloatit.framework.utils.parsers.MarkdownParser;
import com.bloatit.framework.utils.parsers.ParsingException;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.meta.XmlText;

public class HtmlCachedMarkdownRenderer extends HtmlDiv {
    /**
     * Creates a new MarkdownRenderer based on markdown or html text
     *
     * @param text the content to display, must be markdown text if
     *            <code>alreadyRenderer</code> is <code>true</code> or html text
     *            if <code>alreadyRenderer</code> is <code>false</code>
     */
    public HtmlCachedMarkdownRenderer(final String text) {
        super("markdown_block");

        final String cached = MemoryCache.getInstance().get(text);
        if (cached == null) {
            final MarkdownParser parser = new MarkdownParser();
            try {
                final String renderered = parser.parse(text);
                MemoryCache.getInstance().cache(text, renderered);
                add(new XmlText(renderered));
            } catch (final ParsingException e) {
                throw new BadProgrammerException("An error occured during markdown parsing", e);
            }
        } else {
            add(new XmlText(cached));
        }
    }
}

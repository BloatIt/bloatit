package com.bloatit.web.html;

import java.util.Collections;
import java.util.Iterator;

import com.bloatit.model.data.util.NonOptionalParameterException;
import com.bloatit.web.server.Text;

/**
 * <p>
 * HtmlTagText are used to input <b>un</b>escaped text
 * </p>
 * <p>
 * Should be used in the weird situations where other standard tags are not flexible
 * enough
 * </p>
 */
public class HtmlTagText extends HtmlNode {

    protected String content;

    protected HtmlTagText() {
        super();
    }

    /**
     * Creates a component to add raw Html to a page
     *
     * @param content the Html string to add
     */
    public HtmlTagText(final String content) {
        super();
        if(content == null) {
            throw new NonOptionalParameterException();
        }
        this.content = content;
    }

    @Override
    @SuppressWarnings("unchecked")
    public final Iterator<HtmlNode> iterator() {
        return Collections.EMPTY_LIST.iterator();
    }

    /**
     * Do not use Only present as a quick hack to write a tad cleaner html content
     */
    public String _getContent() {
        return content;
    }

    @Override
    public final void write(final Text txt) {
        txt.writeRawText(content);
    }
}
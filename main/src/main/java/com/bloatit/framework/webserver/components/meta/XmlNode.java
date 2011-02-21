package com.bloatit.framework.webserver.components.meta;

import com.bloatit.framework.webserver.components.writers.QueryResponseStream;

/**
 * <p>
 * An HtmlNode represents <b>any</b> item in a DOM
 * </p>
 */
public abstract class XmlNode implements Iterable<XmlNode> {

    /**
     * A method that has to be implemented by all children, and that describes
     * the way it will be represented as an HtmlTag
     * 
     * @param txt the <code>Text</code> that will be used to display the Html
     *            tags
     */
    public abstract void write(QueryResponseStream txt);
}

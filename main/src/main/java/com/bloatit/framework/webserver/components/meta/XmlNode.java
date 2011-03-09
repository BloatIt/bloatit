package com.bloatit.framework.webserver.components.meta;

import java.util.List;

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

    /**
     * This method should be overriden by any components needing some special
     * css files.
     * 
     * @return the list of custom Css files needed by this component or null if
     *         no special js is needed
     */
    protected abstract List<String> getCustomCss();

    /**
     * This method should be overriden by any components needing some special
     * javascript files.
     * 
     * @return the list of custom js file needed by this component or null if no
     *         special js is needed
     */
    protected abstract List<String> getCustomJs();

    public final void getAllCustomCss(List<String> css) {
        for (XmlNode node : this) {
            node.getAllCustomCss(css);
        }
        if (getCustomCss() != null) {
            css.addAll(getCustomCss());
        }
    }

    public final void getAllCustomJs(List<String> js) {
        for (XmlNode node : this) {
            node.getAllCustomJs(js);
        }
        if (getCustomJs() != null) {
            js.addAll(getCustomJs());
        }
    }
}

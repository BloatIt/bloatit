package com.bloatit.framework.webserver.components.meta;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.NotImplementedException;

import com.bloatit.framework.webserver.components.writers.QueryResponseStream;

/**
 * <p>
 * An HtmlNode represents <b>any</b> item in a DOM
 * </p>
 */
public abstract class XmlNode implements Iterable<XmlNode>, Cloneable {

    @Override
    public XmlNode clone() {
        throw new NotImplementedException();
    }

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

    public final List<String> getAllCss() {
        ArrayList<String> css = new ArrayList<String>();
        doGetAllCustomCss(css);
        return css;
    }

    public final Set<String> getAllJs() {
        Set<String> js = new LinkedHashSet<String>();
        doGetAllCustomJs(js);
        return js;
    }

    private final void doGetAllCustomCss(List<String> css) {
        for (XmlNode node : this) {
            node.doGetAllCustomCss(css);
        }
        if (getCustomCss() != null) {
            css.addAll(getCustomCss());
        }
    }

    private final void doGetAllCustomJs(Set<String> js) {
        for (XmlNode node : this) {
            node.doGetAllCustomJs(js);
        }
        if (getCustomJs() != null) {
            js.addAll(getCustomJs());
        }
    }
}

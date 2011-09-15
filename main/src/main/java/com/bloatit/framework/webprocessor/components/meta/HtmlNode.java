/*
 * 
 */
package com.bloatit.framework.webprocessor.components.meta;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.NotImplementedException;

/**
 * <p>
 * An HtmlNode represents <b>any</b> item in a DOM
 * </p>
 */
public abstract class HtmlNode extends Node<HtmlNode> {

    public HtmlNode() {
        super((String) null);
    }

    public HtmlNode(String tag) {
        super(tag);
    }

    protected HtmlNode(HtmlTag tag) {
        super(tag);
    }

    protected final boolean autoCloseTag() {
        return selfClosable() && !hasChild();
    }

    protected abstract boolean selfClosable();

    @Override
    public HtmlNode clone() {
        throw new NotImplementedException();
    }
    
    /**
     * This method should be overridden by any components needing some special
     * css files.
     * 
     * @return the list of custom css files needed by this component or null if
     *         no special css is needed
     */
    protected List<String> getCustomCss() {
        return null;
    }

    /**
     * This method should be overridden by any components needing some special
     * javascript files.
     * 
     * @return the list of custom js file needed by this component or null if no
     *         special js is needed
     */
    protected List<String> getCustomJs() {
        return null;
    }

    /**
     * This method should be overridden by any components needing some special
     * javascript files.
     * 
     * @return the list of custom element needed by this component or null if no
     *         special element is needed
     */
    protected List<HtmlNode> getPostNodes() {
        return null;
    }

    public final List<String> getAllCss() {
        final ArrayList<String> css = new ArrayList<String>();
        doGetAllCustomCss(css);
        return css;
    }

    public final Set<String> getAllJs() {
        final Set<String> js = new LinkedHashSet<String>();
        doGetAllCustomJs(js);
        return js;
    }

    public final Set<HtmlNode> getAllPostNode() {
        final Set<HtmlNode> js = new LinkedHashSet<HtmlNode>();
        doGetAllPostNode(js);
        return js;
    }

    private final void doGetAllCustomCss(final List<String> css) {
        for (final HtmlNode node : this) {
            node.doGetAllCustomCss(css);
        }
        if (getCustomCss() != null) {
            css.addAll(getCustomCss());
        }
    }

    private final void doGetAllCustomJs(final Set<String> js) {
        for (final HtmlNode node : this) {
            node.doGetAllCustomJs(js);
        }
        if (getCustomJs() != null) {
            js.addAll(getCustomJs());
        }
    }

    private final void doGetAllPostNode(final Set<HtmlNode> nodes) {
        for (final HtmlNode node : this) {
            node.doGetAllPostNode(nodes);
        }
        if (getPostNodes() != null) {
            nodes.addAll(getPostNodes());
        }
    }

}

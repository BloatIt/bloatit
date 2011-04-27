package com.bloatit.web.pages.master;

import java.util.ArrayList;
import java.util.List;

import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlLink;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;

/**
 * Breadcrumbs are a "guide" displayed at the top of each page indicating where
 * a page hierarchy to the current page.
 * <p>
 * A breadcrumb is a simple array of links to previous pages. When generated,
 * they are separated by a separator character.
 * </p>
 * <p>
 * Simple example of construction : <code><pre>
 * Breadcrumb breadcrumb = new Breadcrumb();
 * breadcrumb.pushLink(new AdminPageUrl().getHtmlLink("Admin"));
 * breadcrumb.pushLink(url.getHtmlLink("Configuration")); 
 * return breadcrumb;</pre></code>
 * </p>
 */
public class Breadcrumb {
    private static final String SEPARATOR = " > ";

    private final List<HtmlLink> linkList = new ArrayList<HtmlLink>();

    /**
     * Adds a <code>link</code> at the end of the breadcrumb
     * 
     * @param link the link to add
     */
    public void pushLink(final HtmlLink link) {
        linkList.add(link);
    }

    /**
     * Generates the breadcrumb html text
     * 
     * @return a Node representing the breadcrumb
     */
    protected HtmlElement toHtmlElement() {
        final HtmlDiv breadcrumb = new HtmlDiv("breadcrumb");

        boolean first = true;
        for (final HtmlLink link : linkList) {
            if (!first) {
                breadcrumb.addText(SEPARATOR);
            }
            breadcrumb.add(link);

            first = false;
        }
        return breadcrumb;
    }
}

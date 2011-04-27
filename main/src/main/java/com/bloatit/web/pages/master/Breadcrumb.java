//
// Copyright (c) 2011 Linkeos.
//
// This file is part of Elveos.org.
// Elveos.org is free software: you can redistribute it and/or modify it
// under the terms of the GNU General Public License as published by the
// Free Software Foundation, either version 3 of the License, or (at your
// option) any later version.
//
// Elveos.org is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
// more details.
// You should have received a copy of the GNU General Public License along
// with Elveos.org. If not, see http://www.gnu.org/licenses/.
//
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

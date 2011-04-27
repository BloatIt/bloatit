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
package com.bloatit.web.pages.master.sidebar;

import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.advanced.HtmlClearer;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.components.meta.HtmlLeaf;
import com.bloatit.framework.webprocessor.url.Url;

public class TwoColumnLayout extends HtmlLeaf {

    private final HtmlDiv leftColumn;
    private final HtmlDiv rightColumn;
    private final PluginBox plugins;

    public TwoColumnLayout(final Url url) {
        this(false, url);
    }

    /**
     * Creates a two column layout with, or without a box around it
     * 
     * @param box <i>true</i> if a box should surround the whole layout,
     *            <i>false</i> otherwise
     */
    public TwoColumnLayout(final boolean box, final Url url) {
        super();

        HtmlDiv master = new HtmlDiv("two_column");
        add(master);

        final HtmlDiv leftColumnBlock = new HtmlDiv((box ? "left_column_block" : "left_column_block_without_box"));
        final HtmlDiv rightColumnBlock = new HtmlDiv("right_column_block");
        leftColumn = new HtmlDiv((box ? "left_column" : "left_column_without_box"));
        rightColumn = new HtmlDiv("right_column");

        plugins = new PluginBox(url);
        rightColumn.add(plugins);

        leftColumnBlock.add(leftColumn);
        rightColumnBlock.add(rightColumn);
        master.add(leftColumnBlock);
        master.add(rightColumnBlock);
        master.add(new HtmlClearer());
    }

    /**
     * Adds an element to the core of the page
     * 
     * @param element the element to add
     */
    public void addLeft(final HtmlElement element) {
        leftColumn.add(element);
    }

    /**
     * Adds an element to the right bar of the page
     * 
     * @param element
     */
    public void addRight(final SideBarElementLayout element) {
        // rightColumn.add(element);
        plugins.addPageContent(element);
    }
}

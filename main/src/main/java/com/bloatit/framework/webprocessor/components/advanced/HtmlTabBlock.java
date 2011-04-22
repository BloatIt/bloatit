/*
 * Copyright (C) 2010 BloatIt. This file is part of BloatIt. BloatIt is free
 * software: you can redistribute it and/or modify it under the terms of the GNU
 * Affero General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 * BloatIt is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details. You should have received a copy of the GNU Affero General Public
 * License along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
package com.bloatit.framework.webprocessor.components.advanced;

import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.meta.XmlNode;
import com.bloatit.framework.webprocessor.url.Url;

public final class HtmlTabBlock extends HtmlDiv {

    private final String tabBlockKey;
    private final String activeTabKey;
    private final Url tablinks;
    private final HtmlDiv tabBody;
    private final HtmlDiv tabHeader;

    /**
     * Creates a new tab block
     * 
     * @param tabBlockKey an identifier of the tab block (used to generate an
     *            anchor). Just use a unique string
     * @param activeTabKey the current active tab
     * @param tablinks the url of the page containing the tab pane
     */
    public HtmlTabBlock(final String tabBlockKey, final String activeTabKey, final Url tablinks) {
        super("tab_panel");

        this.tabBlockKey = tabBlockKey;
        this.activeTabKey = activeTabKey;
        this.tablinks = tablinks.clone();

        tabHeader = new HtmlDiv("tab_header");
        tabBody = new HtmlDiv("tab_body");

        setId(tabBlockKey);
        add(tabHeader);
        add(tabBody);
    }

    public void addTab(final HtmlTab tab) {
        if (tab.getTabKey().equals(activeTabKey)) {
            addTabHeader(tab, true);
            tabBody.add(tab.generateBody());
        } else {
            addTabHeader(tab, false);
        }
    }

    private void addTabHeader(final HtmlTab tab, final boolean active) {

        String tabTitleStyle;
        String tabTitleContentStyle;

        if (active) {
            tabTitleStyle = "active_tab_title";
            tabTitleContentStyle = "active_tab_title_content";
        } else {
            tabTitleStyle = "inactive_tab_title";
            tabTitleContentStyle = "inactive_tab_title_content";
        }

        final HtmlDiv tabTitle = new HtmlDiv(tabTitleStyle);
        tabHeader.add(tabTitle);

        final HtmlDiv div = new HtmlDiv(tabTitleContentStyle);
        div.addText(tab.getTitle());

        tablinks.addParameter(tabBlockKey, tab.getTabKey());
        tablinks.setAnchor(tabBlockKey);

        if (active) {
            tabTitle.add(div);
        } else {
            tabTitle.add(tablinks.getHtmlLink(div));
        }

    }

    public abstract static class HtmlTab {
        private final String title;
        private final String tabKey;

        public HtmlTab(final String title, final String tabKey) {
            this.title = title;
            this.tabKey = tabKey;
        }

        public String getTabKey() {
            return tabKey;
        }

        public String getTitle() {
            return title;
        }

        abstract public XmlNode generateBody();
    }
}

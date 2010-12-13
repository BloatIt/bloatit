/*
 * Copyright (C) 2010 BloatIt.
 * 
 * This file is part of BloatIt.
 * 
 * BloatIt is free software: you can redistribute it and/or modify it under the terms of
 * the GNU Affero General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 * 
 * BloatIt is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. See the GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License along with
 * BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */

package com.bloatit.web.html.components.custom;

import com.bloatit.web.html.components.standard.HtmlDiv;
import com.bloatit.web.html.components.standard.HtmlLink;

public class HtmlTabBlock extends HtmlDiv {

    private final HtmlDiv tabBody;
    private final HtmlDiv tabHeader;

    public HtmlTabBlock() {
        super("tab_panel");

        tabHeader = new HtmlDiv("tab_header");
        tabBody = new HtmlDiv("tab_body");

        add(tabHeader);
        add(tabBody);
    }

    public void addTabHeader(final HtmlTabHeader tab) {

        final HtmlDiv tabTile = new HtmlDiv("inactive_tab_title");
        tabHeader.add(tabTile);

        final HtmlLink link = new HtmlLink(tab.getLink(), new HtmlDiv("inactive_tab_title_content"));
        link.addText(tab.getTitle());

        tabTile.add(link);
    }

    public void addActiveTab(final HtmlTab tab) {
        addTabHeader(tab);
        tabBody.add(tab.getBody());
    }

    public static class HtmlTabHeader {
        private final String title;
        private final String link;

        public HtmlTabHeader(final String title, final String link) {
            this.title = title;
            this.link = link;
        }

        public String getLink() {
            return link;
        }

        public String getTitle() {
            return title;
        }
    }

    public static class HtmlTab extends HtmlTabHeader {
        private final HtmlDiv body;

        public HtmlTab(final HtmlTabHeader header, final HtmlDiv body) {
            super(header.title, header.link);
            this.body = body;
            body.addAttribute("class", "tab_body");
        }

        public HtmlDiv getBody() {
            return body;
        }

    }

}

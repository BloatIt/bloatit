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

package test.html.components.advanced;

import test.html.components.standard.HtmlLink;
import test.html.components.standard.HtmlBlock;


public class HtmlTabBlock extends HtmlBlock {

    private HtmlBlock tabBody;
    private HtmlBlock tabHeader;

    public HtmlTabBlock() {
        super("tab_panel");

        tabHeader = new HtmlBlock("tab_header");
        tabBody = new HtmlBlock("tab_body");

        add(tabHeader);
        add(tabBody);
    }

    public void addTabHeader(HtmlTabHeader tab) {

        HtmlBlock tabTile = new HtmlBlock("inactive_tab_title");
        tabHeader.add(tabTile);

        HtmlLink link = new HtmlLink(tab.getLink(), new HtmlBlock("inactive_tab_title_content"));
        link.addText(tab.getTitle());

        tabTile.add(link);
    }

    public void addActiveTab(HtmlTab tab) {
        addTabHeader(tab);
        tabBody.add(tab.getBody());
    }

    public static class HtmlTabHeader {
        private String title;
        private String link;

        public HtmlTabHeader(String title, String link) {
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
        private HtmlBlock body;

        public HtmlTab(HtmlTabHeader header, HtmlBlock body) {
            super(header.title, header.link);
            this.body = body;
            body.addAttribute("class", "tab_body");
        }

        public HtmlBlock getBody() {
            return body;
        }

    }

}

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

package test.htmlComponents;

import java.util.HashMap;
import java.util.Map;

import com.bloatit.web.server.Request;

public class HtmlTabBlock extends HtmlBlock {

    private final String id;
    private HtmlBlock tabBody;
    private HtmlBlock tabHeader;

    public HtmlTabBlock(String id) {
        super("tab_panel");

        tabHeader = new HtmlBlock("tab_header");
        tabBody = new HtmlBlock("tab_body");

        add(tabHeader);
        add(tabBody);

        this.id = id;
    }

    public void addTabHeader(HtmlTabHeader tab) {

        HtmlBlock tabTile = new HtmlBlock("inactive_tab_title");
        tabHeader.add(tabTile);

        Request currentLink = tab.getLink();
        Map<String, String> outputParams = new HashMap<String, String>();
        outputParams.put(id + "_key", tab.getId());

        HtmlLinkComponent link = new HtmlLinkComponent(currentLink.getUrl(outputParams), new HtmlBlock("inactive_tab_title_content"));
        link.addText(tab.getTitle());

        tabTile.add(link);
    }

    public void addActiveTab(HtmlTab tab) {
        addTabHeader(tab);
        tabBody.add(tab.getBody());
    }

    public static class HtmlTabHeader {
        private String id;
        private String title;
        private Request link;

        public HtmlTabHeader(String id, String title, Request link) {
            this.id = id;
            this.title = title;
            this.link = link;
        }

        public String getId() {
            return id;
        }

        public Request getLink() {
            return link;
        }

        public String getTitle() {
            return title;
        }
    }

    public static class HtmlTab extends HtmlTabHeader {
        private HtmlBlock body;

        public HtmlTab(String id, String title, Request link, HtmlBlock body) {
            super(id, title, link);
            this.body = body;
            body.addAttribute("class", "tab_body");
        }

        public HtmlBlock getBody() {
            return body;
        }

    }

}

/*
 * Copyright (C) 2010 BloatIt.
 *
 * This file is part of BloatIt.
 *
 * BloatIt is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BloatIt is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */

package com.bloatit.web.htmlrenderer.htmlcomponent;

import com.bloatit.web.htmlrenderer.HtmlResult;
import com.bloatit.web.server.Request;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


public class HtmlTabBlock extends HtmlComponent {

    private final Map<String, HtmlTab> tabList = new LinkedHashMap<String, HtmlTab>();
    private HtmlTab activeTab;
    private final String id;

    public HtmlTabBlock(String id) {
        this.id = id;
    }
    
    public void addTab(HtmlTab tab) {
        tabList.put(tab.getId(),tab);
    }

    public void selectTab(String tabKey) {
        activeTab = tabList.get(tabKey);
    }

    @Override
    public void generate(HtmlResult htmlResult) {
        htmlResult.write("<div class=\"tab_pane\">");
        htmlResult.indent();

        //Tab header
        htmlResult.write("<div class=\"tab_header\">");
        htmlResult.indent();

        for(HtmlTab tab : tabList.values()) {
            if(tab != activeTab) {
                htmlResult.write("<div class=\"inactive_tab_title\">");
                htmlResult.indent();

                Request currentLink = tab.getLink();
                currentLink.setOutputParam(id+"_key", tab.getId());

                htmlResult.write("<a href=\""+currentLink.getUrl()+"\" >");
                htmlResult.indent();

                htmlResult.write("<div class=\"inactive_tab_title_content\">");
                htmlResult.indent();

                htmlResult.write(tab.getTitle());

                htmlResult.unindent();
                htmlResult.write("</div>");

                htmlResult.unindent();
                htmlResult.write("</a>");

                htmlResult.unindent();
                htmlResult.write("</div>");
            } else {
                htmlResult.write("<div class=\"active_tab_title\">");
                htmlResult.indent();

                htmlResult.write("<div class=\"active_tab_title_content\">");
                htmlResult.indent();

                htmlResult.write(tab.getTitle());
                
                htmlResult.unindent();
                htmlResult.write("</div>");

                htmlResult.unindent();
                htmlResult.write("</div>");
            }
        }

        htmlResult.unindent();
        htmlResult.write("</div>");

        //Tab body
        htmlResult.write("<div class=\"tab_body\">");
        htmlResult.indent();
        if(activeTab != null) {
            activeTab.getContent().generate(htmlResult);
        }
        
        htmlResult.unindent();
        htmlResult.write("</div>");

        

        htmlResult.unindent();
        htmlResult.write("</div>");

    }

    public static class HtmlTab {
        private String id;
        private String title;
        private Request link;
        private HtmlComponent content;

        public HtmlTab(String id, String title, Request link, HtmlComponent content) {
            this.id = id;
            this.title = title;
            this.link = link;
            this.content = content;
        }

        public HtmlComponent getContent() {
            return content;
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

}

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

import java.util.ArrayList;
import java.util.List;

import com.bloatit.web.htmlrenderer.HtmlResult;

public class HtmlList extends HtmlComponent {

    final private String text;
    final private String cssClass;

    final private List<HtmlListItem> itemList = new ArrayList<HtmlListItem>();

    public HtmlList(String text) {
        this.text = text;
        this.cssClass = null;
    }

    public HtmlList(String cssClass, String text) {
        this.text = text;
        this.cssClass = cssClass;
    }

    public HtmlList() {
        this.text = "";
        this.cssClass = null;
    }

    public void addItem(HtmlListItem item) {
        itemList.add(item);
    }

    @Override
    public void generate(HtmlResult htmlResult) {
        if (cssClass == null) {
            htmlResult.write("<ul>" + text);
        } else {
            htmlResult.write("<ul class=\"" + cssClass + "\">" + text);
        }

        htmlResult.indent();

        for (final HtmlListItem item : itemList) {
            item.generate(htmlResult);
        }

        htmlResult.unindent();
        htmlResult.write("</ul>");

    }
}

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

import test.HtmlElement;

import com.bloatit.common.PageIterable;
import com.bloatit.web.htmlrenderer.HtmlTools;
import com.bloatit.web.server.Request;
import com.bloatit.web.server.Session;

public class HtmlPagedList<T> extends HtmlList {

    private Session session;
    private Request currentRequest;
    private int currentPage;
    private int pageCount;
    private int pageSize;

    public HtmlPagedList(HtmlRenderer<T> itemRenderer, PageIterable<T> itemList, Request currentRequest, Session session) {
        super();
        this.session = session;
        this.currentRequest = currentRequest;
        this.currentPage = itemList.getCurrentPage() + 1;
        this.pageCount = itemList.pageNumber();
        this.pageSize = itemList.getPageSize();

        if (pageCount > 1) {
            add(generateLinksBar());
        }

        for (T item : itemList) {
            addItem(new HtmlListItem(itemRenderer.generate(item)));
        }

        if (pageCount > 1) {
            add(generateLinksBar());
        }
    }

    public HtmlPagedList(String cssClass, HtmlRenderer<T> itemRenderer, PageIterable<T> itemList, Request currentRequest, Session session) {
        this(itemRenderer, itemList, currentRequest, session);
        addAttribute("class", cssClass);
    }

    private HtmlElement generateLinksBar() {

        HtmlElement span = new HtmlElement("span");

        if (currentPage > 1) {
            span.add(generateLink(currentPage - 1, session.tr("Previous")));
        }

        // first page
        span.add(generateLink(1));

        if (currentPage - 4 > 1) {
            span.addText("...");
        }

        // center pages
        for (int i = currentPage - 3; i < currentPage + 3; i++) {
            if (i > 1 && i < pageCount) {
                span.add(generateLink(i));
            }
        }

        if (currentPage + 4 < pageCount) {
            span.addText("...");
        }

        // Last page
        span.add(generateLink(pageCount));

        if (currentPage < pageCount) {
            span.add(generateLink(currentPage + 1, session.tr("Next")));
        }
        return span;
    }

    private HtmlText generateLink(int i) {
        String iString = new Integer(i).toString();
        return generateLink(i, iString);
    }

    private HtmlText generateLink(int i, String text) {
        String iString = new Integer(i).toString();
        if (i != currentPage) {
            Map<String, String> outputParams = new HashMap<String, String>();
            outputParams.put("list_page", iString);
            outputParams.put("list_page_size", new Long(pageSize).toString());

            return new HtmlText(HtmlTools.generateLink(session, text, currentRequest, outputParams));
        }
        return new HtmlText(iString);
    }
}

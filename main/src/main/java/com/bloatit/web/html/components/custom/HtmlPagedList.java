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


import com.bloatit.common.PageIterable;
import com.bloatit.web.html.HtmlElement;
import com.bloatit.web.html.HtmlNode;
import com.bloatit.web.html.HtmlText;
import com.bloatit.web.html.components.standard.HtmlGenericElement;
import com.bloatit.web.html.components.standard.HtmlList;
import com.bloatit.web.html.components.standard.HtmlListItem;
import com.bloatit.web.html.components.standard.HtmlRenderer;
import com.bloatit.web.server.Session;
import com.bloatit.web.utils.annotations.RequestParam;
import com.bloatit.web.utils.url.UrlBuilder;

public class HtmlPagedList<T> extends HtmlList {

    private Session session;
    private UrlBuilder urlBuilder;

    @RequestParam(defaultValue = "1", name = "current_page")
    private Integer currentPage;
    @RequestParam(defaultValue = "1", name = "page_count")
    private Integer pageCount;
    @RequestParam(defaultValue = "42", name = "page_count")
    private Integer pageSize;

    public HtmlPagedList(final HtmlRenderer<T> itemRenderer, final PageIterable<T> itemList, final UrlBuilder urlBuilder, final Session session) {
        super();
        this.session = session;
        this.urlBuilder = urlBuilder;
        this.currentPage = itemList.getCurrentPage() + 1;
        this.pageCount = itemList.pageNumber();
        this.pageSize = itemList.getPageSize();

        if (pageCount > 1) {
            add(generateLinksBar());
        }

        for (final T item : itemList) {
            add(itemRenderer.generate(item));
        }

        if (pageCount > 1) {
            add(generateLinksBar());
        }
    }

    public HtmlPagedList(final String cssClass,
                         final HtmlRenderer<T> itemRenderer,
                         final PageIterable<T> itemList,
                         final UrlBuilder urlBuilder,
                         final Session session) {
        this(itemRenderer, itemList, urlBuilder, session);
        addAttribute("class", cssClass);
    }

    private HtmlElement generateLinksBar() {

        final HtmlGenericElement span = new HtmlGenericElement("span");

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

    private HtmlNode generateLink(final int i) {
        final String iString = new Integer(i).toString();
        return generateLink(i, iString);
    }

    private HtmlNode generateLink(final int i, final String text) {
        final String iString = new Integer(i).toString();
        if (i != currentPage) {

            urlBuilder.addParameter("list_page", iString);
            urlBuilder.addParameter("list_page_size", new Long(pageSize).toString());

            return urlBuilder.getHtmlLink(text);
        }
        return new HtmlText(iString);
    }
}

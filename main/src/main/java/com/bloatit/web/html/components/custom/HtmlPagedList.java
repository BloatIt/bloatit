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
import com.bloatit.web.server.Context;
import com.bloatit.web.server.Session;
import com.bloatit.web.utils.annotations.RequestParam;
import com.bloatit.web.utils.url.Request;
import com.bloatit.web.utils.url.UrlBuilder;

public class HtmlPagedList<T> extends HtmlList {

    private Session session;
    private UrlBuilder urlBuilder;
    private Integer pageCount;
    private final static String CURRENT_PAGE_FIELD_NAME = "current_page";
    private final static String PAGE_SIZE_FIELD_NAME = "page_size";
    @RequestParam(defaultValue = "1", name = CURRENT_PAGE_FIELD_NAME)
    private Integer currentPage;
    @RequestParam(defaultValue = "10", name = PAGE_SIZE_FIELD_NAME)
    private Integer pageSize;

    public HtmlPagedList(final HtmlRenderer<T> itemRenderer, final PageIterable<T> itemList, final UrlBuilder urlBuilder, final Request request) {
        super();
        this.session = Context.getSession();
        this.urlBuilder = urlBuilder;
        request.setValues(this);

        itemList.setPageSize(pageSize);
        this.pageCount = itemList.pageNumber();


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
            final Request request) {
        this(itemRenderer, itemList, urlBuilder, request);
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

            urlBuilder.addParameter(CURRENT_PAGE_FIELD_NAME, iString);
            urlBuilder.addParameter(PAGE_SIZE_FIELD_NAME, new Long(pageSize).toString());

            return urlBuilder.getHtmlLink(text);
        }
        return new HtmlText(iString);
    }
}

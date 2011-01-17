/*
 * Copyright (C) 2010 BloatIt. This file is part of BloatIt. BloatIt is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version. BloatIt is distributed in the hope that it will
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public
 * License for more details. You should have received a copy of the GNU Affero General
 * Public License along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
package com.bloatit.web.html.components.custom;

import com.bloatit.common.PageIterable;
import com.bloatit.web.annotations.ParamContainer;
import com.bloatit.web.annotations.RequestParam;
import com.bloatit.web.html.HtmlElement;
import com.bloatit.web.html.HtmlNode;
import com.bloatit.web.html.HtmlTagText;
import com.bloatit.web.html.components.standard.HtmlList;
import com.bloatit.web.html.components.standard.HtmlRenderer;
import com.bloatit.web.html.components.standard.HtmlSpan;
import com.bloatit.web.server.Context;
import com.bloatit.web.utils.url.HtmlPagedListUrl;
import com.bloatit.web.utils.url.UrlComponent;

@ParamContainer(value = "pagedList", isComponent = true)
public class HtmlPagedList<T> extends HtmlList {

    private static final int NB_PAGES_RIGHT = 4;
    private static final int NB_PAGES_CENTER = 3;
    private static final int NB_PAGES_LEFT = 4;
    private static final String CURRENT_PAGE_FIELD_NAME = "current_page";
    private static final String PAGE_SIZE_FIELD_NAME = "page_size";

    private final Integer pageCount;
    private final UrlComponent currentUrl;
    private final HtmlPagedListUrl url;

    @RequestParam(defaultValue = "1", name = CURRENT_PAGE_FIELD_NAME)
    private final Integer currentPage;
    @RequestParam(defaultValue = "10", name = PAGE_SIZE_FIELD_NAME)
    private final Integer pageSize;

    // Explain contract for URL and PageListUrl
    /**
     * Do not forget to clone the Url !!!
     */
    public HtmlPagedList(final HtmlRenderer<T> itemRenderer, final PageIterable<T> itemList, final UrlComponent url2, final HtmlPagedListUrl url) {
        super();
        this.currentPage = url.getCurrentPage();
        this.pageSize = url.getPageSize();
        this.currentUrl = url2;
        this.url = url;

        itemList.setPageSize(pageSize);
        itemList.setPage(currentPage - 1);
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

    private HtmlElement generateLinksBar() {

        final HtmlSpan span = new HtmlSpan();

        if (currentPage > 1) {
            span.add(generateLink(currentPage - 1, Context.tr("Previous")));
        }

        // first page
        span.add(generateLink(1));

        if (currentPage - NB_PAGES_RIGHT > 1) {
            span.addText("...");
        }

        // center pages
        for (int i = currentPage - NB_PAGES_CENTER; i < currentPage + NB_PAGES_CENTER; i++) {
            if (i > 1 && i < pageCount) {
                span.add(generateLink(i));
            }
        }

        if (currentPage + NB_PAGES_LEFT < pageCount) {
            span.addText("...");
        }

        // Last page
        span.add(generateLink(pageCount));

        if (currentPage < pageCount) {
            span.add(generateLink(currentPage + 1, Context.tr("Next")));
        }
        return span;
    }

    private HtmlNode generateLink(final int i) {
        final String iString = Integer.valueOf(i).toString();
        return generateLink(i, iString);
    }

    private HtmlNode generateLink(final int i, final String text) {
        final String iString = Integer.valueOf(i).toString();
        if (i != currentPage) {

            url.setCurrentPage(i);
            url.setPageSize(pageSize);

            return currentUrl.getHtmlLink(text);
        }
        return new HtmlTagText(iString);
    }
}

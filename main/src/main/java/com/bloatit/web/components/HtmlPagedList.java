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
package com.bloatit.web.components;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import com.bloatit.framework.utils.PageIterable;
import com.bloatit.framework.webprocessor.annotations.MaxConstraint;
import com.bloatit.framework.webprocessor.annotations.MinConstraint;
import com.bloatit.framework.webprocessor.annotations.Optional;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlLink;
import com.bloatit.framework.webprocessor.components.HtmlList;
import com.bloatit.framework.webprocessor.components.HtmlRenderer;
import com.bloatit.framework.webprocessor.components.HtmlSpan;
import com.bloatit.framework.webprocessor.components.PlaceHolderElement;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.components.meta.HtmlNode;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.web.url.HtmlPagedListUrlComponent;

@ParamContainer(value = "pagedList", isComponent = true)
public class HtmlPagedList<T> extends HtmlDiv {
    private static final int NB_PAGES_RIGHT = 2;
    private static final int NB_PAGES_TOTAL = 12;
    private static final int NB_PAGES_LEFT = 2;
    private static final String CURRENT_PAGE_FIELD_NAME = "current_page";
    private static final String PAGE_SIZE_FIELD_NAME = "page_size";

    private final Integer pageCount;
    private final Url currentUrl;
    private final HtmlPagedListUrlComponent url;

    @RequestParam(name = CURRENT_PAGE_FIELD_NAME)
    @Optional("1")
    private final Integer currentPage;
    @RequestParam(name = PAGE_SIZE_FIELD_NAME)
    @Optional("36")
    @MaxConstraint(max = 100, message = @tr("Page size must be inferior to %constraint%."))
    @MinConstraint(min = 1, message = @tr("Page size must be superior or equal to %constraint%."))
    private final Integer pageSize;

    // Explain contract for URL and PageListUrl
    /**
     * Do not forget to clone the Url !!!
     */
    public HtmlPagedList(final HtmlRenderer<T> itemRenderer, final PageIterable<T> itemList, final Url url2, final HtmlPagedListUrlComponent url) {
        this(itemRenderer, itemList, url2, url, new PlaceHolderElement(), new PlaceHolderElement());
    }

    public HtmlPagedList(final HtmlRenderer<T> itemRenderer,
                         final PageIterable<T> itemList,
                         final Url url2,
                         final HtmlPagedListUrlComponent url,
                         final HtmlNode preListElement,
                         final HtmlNode postListElement) {

        super("paged_list");
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
        add(preListElement);

        final HtmlList items = new HtmlList();
        items.setCssClass("items_list");
        for (final T item : itemList) {
            items.add(itemRenderer.generate(item));
        }
        add(items);
        add(postListElement);

        if (pageCount > 1) {
            add(generateLinksBar());
        }
    }

    private HtmlElement generateLinksBar() {
        final HtmlSpan span = new HtmlSpan("pages_list");
        if (currentPage > 1) {
            span.add(generateLink(currentPage - 1, tr("Previous"), true, "prev"));
        } else {
            span.add(generateEmptyLongBlock());
        }

        if (pageCount <= NB_PAGES_TOTAL) {
            // Display all pages
            for (int i = 1; i < pageCount + 1; i++) {
                span.add(generateLink(i, false));
            }
        } else {
            // 3 cases:
            // - the current page is near to the first 1 2 3 4 5 -6- 7 8 ... 32
            // 33 34
            // - the current page is near to the last 1 2 3 ... 27 28 29 -30- 31
            // 32 33 34
            // - the current page is isolated 1 2 3 ... 9 10 -11- 12 ... 32 33
            // 34

            final int slotForCenter = NB_PAGES_TOTAL - NB_PAGES_RIGHT - NB_PAGES_LEFT;

            final int maxSlotForLeft = currentPage - 1 - NB_PAGES_LEFT;
            final int maxSlotForRight = pageCount - currentPage - NB_PAGES_RIGHT;

            int slotForLeft = maxSlotForLeft;
            int slotForRight = maxSlotForRight;

            if (slotForLeft <= (slotForCenter - 1) / 2) {
                slotForRight = slotForCenter - 1 - slotForLeft;
            } else if (slotForRight <= (slotForCenter - 1) / 2) {
                slotForLeft = slotForCenter - 1 - slotForRight;
            } else {
                if (slotForLeft > (slotForCenter - 1) / 2) {
                    slotForLeft = (slotForCenter - 1) / 2;
                }
                slotForRight = slotForCenter - 1 - slotForLeft;
            }

            boolean needLeftSpacer = true;
            boolean needRightSpacer = true;
            if (currentPage - slotForLeft - 1 <= NB_PAGES_LEFT) {
                needLeftSpacer = false;
            }

            if (currentPage + slotForRight >= pageCount - NB_PAGES_RIGHT) {
                needRightSpacer = false;
            }

            if (needLeftSpacer) {
                slotForLeft--;
            }

            if (needRightSpacer) {
                slotForRight--;
            }

            // Begin
            for (int i = 1; i < NB_PAGES_LEFT + 1; i++) {
                span.add(generateLink(i, false));
            }

            // Begin spacer
            if (needLeftSpacer) {
                span.add(generateShortBlock("…"));
            }

            for (int i = currentPage - slotForLeft; i < currentPage + slotForRight + 1; i++) {
                span.add(generateLink(i, false));
            }

            // End spacer
            if (needRightSpacer) {
                span.add(generateShortBlock("…"));
            }

            // End
            for (int i = pageCount - NB_PAGES_RIGHT + 1; i < pageCount + 1; i++) {
                span.add(generateLink(i, false));
            }
        }

        if (currentPage < pageCount) {
            span.add(generateLink(currentPage + 1, tr("Next"), true, "next"));
        } else {
            span.add(generateEmptyLongBlock());
        }

        return span;
    }

    private HtmlNode generateLink(final int i, final boolean longBlock) {
        final String iString = Integer.valueOf(i).toString();
        return generateLink(i, iString, longBlock, null);
    }

    private HtmlNode generateLink(final int page, final String text, final boolean longBlock, final String rel) {
        final String iString = Integer.valueOf(page).toString();
        final String css = (longBlock ? "long_block" : "short_block");
        if (page != currentPage) {
            url.setCurrentPage(page);
            url.setPageSize(pageSize);

            final HtmlLink htmlLink = currentUrl.getHtmlLink(text);
            htmlLink.setCssClass(css);

            if (rel != null) {
                htmlLink.addAttribute("rel", rel);
            }

            return htmlLink;
        }
        return new HtmlSpan(css + "_active").addText(iString);
    }

    private HtmlNode generateEmptyLongBlock() {
        return new HtmlSpan("long_block");
    }

    private HtmlNode generateShortBlock(final String text) {
        return new HtmlSpan("short_block").addText(text);
    }
}

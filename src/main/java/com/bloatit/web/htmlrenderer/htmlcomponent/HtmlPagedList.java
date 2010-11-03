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

import com.bloatit.common.PageIterable;

import com.bloatit.web.htmlrenderer.HtmlResult;
import com.bloatit.web.htmlrenderer.HtmlTools;
import com.bloatit.web.server.Page;
import com.bloatit.web.server.Request;
import com.bloatit.web.server.Session;
import java.util.ArrayList;
import java.util.List;

public class HtmlPagedList<T> extends HtmlComponent {

    final private String cssClass;
    final private HtmlRenderer<T> itemRenderer;
    final private PageIterable<T> itemList;
    final private Request currentRequest;
    final private Session session;
    private int currentPage;
    private int pageCount;
    private int pageSize;
    private HtmlResult htmlResult;

    public HtmlPagedList(HtmlRenderer<T> itemRenderer, PageIterable<T> itemList, Request currentRequest, Session session) {
        this.itemRenderer = itemRenderer;
        this.itemList = itemList;
        this.cssClass = null;
        this.currentRequest = currentRequest;
        this.session = session;
    }

    public HtmlPagedList(String cssClass, HtmlRenderer<T> itemRenderer, PageIterable<T> itemList, Request currentRequest, Session session) {
        this.itemRenderer = itemRenderer;
        this.itemList = itemList;
        this.cssClass = cssClass;
        this.currentRequest = currentRequest;
        this.session = session;
    }

    @Override
    public void generate(HtmlResult htmlResult) {

        this.currentPage = itemList.getCurrentPage() + 1;
        this.pageCount = itemList.pageNumber();
        this.pageSize = itemList.getPageSize();
        this.htmlResult = htmlResult;

        if (cssClass == null) {
            htmlResult.write("<ul>");
        } else {
            htmlResult.write("<ul class=\"" + cssClass + "\">");
        }

        if(currentPage > 1) {
            generateLinksBar();
        }

        htmlResult.indent();



        for (T item : itemList) {
            itemRenderer.generate(htmlResult, item);
        }


        htmlResult.unindent();
        if(pageCount > 1) {
            generateLinksBar();
        }

        htmlResult.write("</ul>");

    }

    public void setCurrentPage(int currentPage) {
        itemList.setPage(currentPage);
    }

    public void setPageSize(int pageSize) {
        itemList.setPageSize(pageSize);
    }

    private void generateLinksBar() {
        htmlResult.write("<span>");
        htmlResult.indent();

        currentRequest.setOutputParam("page_size", new Long(pageSize).toString());

        if (currentPage > 1) {
            generateLink(currentPage - 1, session.tr("Previous"));
        }

        // first page
        generateLink(1);

        if(currentPage - 4 > 1) {
            htmlResult.write("...");
        }

        // center pages
        for (int i = currentPage - 3; i < currentPage + 3; i++) {
            if (i > 1 && i < pageCount) {
                generateLink(i);
            }
        }

        if(currentPage + 4 < pageCount) {
            htmlResult.write("...");
        }

        //Last page
        generateLink(pageCount);



        if (currentPage < pageCount ) {
            generateLink(currentPage + 1, session.tr("Next"));
        }


        htmlResult.unindent();
        htmlResult.write("</span>");
    }

    private void generateLink(int i) {
        String iString = new Integer(i).toString();
        if (i != currentPage) {
            currentRequest.setOutputParam("page", iString);
            htmlResult.write(HtmlTools.generateLink(session, iString, currentRequest));
        } else {
            htmlResult.write(iString);

        }

    }

    private void generateLink(int i, String text) {
        String iString = new Integer(i).toString();
        if (i != currentPage) {
            currentRequest.setOutputParam("page", iString);
            htmlResult.write(HtmlTools.generateLink(session, text, currentRequest));
        } else {
            htmlResult.write(iString);

        }

    }
}

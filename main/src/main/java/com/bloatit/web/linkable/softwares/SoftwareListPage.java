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
package com.bloatit.web.linkable.softwares;

import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.SubParamContainer;
import com.bloatit.framework.webprocessor.components.HtmlRenderer;
import com.bloatit.framework.webprocessor.components.HtmlTitleBlock;
import com.bloatit.framework.webprocessor.components.advanced.HtmlClearer;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.Software;
import com.bloatit.model.managers.SoftwareManager;
import com.bloatit.web.WebConfiguration;
import com.bloatit.web.components.HtmlPagedList;
import com.bloatit.web.components.SideBarButton;
import com.bloatit.web.components.SoftwareListRenderer;
import com.bloatit.web.linkable.IndexPage;
import com.bloatit.web.linkable.documentation.SideBarDocumentationBlock;
import com.bloatit.web.linkable.master.Breadcrumb;
import com.bloatit.web.linkable.master.ElveosPage;
import com.bloatit.web.linkable.master.sidebar.TwoColumnLayout;
import com.bloatit.web.url.CreateSoftwarePageUrl;
import com.bloatit.web.url.SoftwareListPageUrl;

@ParamContainer("softwares")
public final class SoftwareListPage extends ElveosPage {

    // Keep me here ! I am needed for the Url generation !
    @SubParamContainer
    private HtmlPagedList<Software> pagedSoftwareList;

    private final SoftwareListPageUrl url;

    public SoftwareListPage(final SoftwareListPageUrl url) {
        super(url);
        this.url = url;
    }

    @Override
    protected HtmlElement createBodyContent() throws RedirectException {
        final TwoColumnLayout layout = new TwoColumnLayout(true, url);

        final HtmlTitleBlock pageTitle = new HtmlTitleBlock(Context.tr("Software list"), 1);

        final PageIterable<Software> softwareList = SoftwareManager.getAll();
        final HtmlRenderer<Software> softwareItemRenderer = new SoftwareListRenderer();

        final SoftwareListPageUrl clonedUrl = url.clone();
        pagedSoftwareList = new HtmlPagedList<Software>(softwareItemRenderer, softwareList, clonedUrl, clonedUrl.getPagedSoftwareListUrl());

        pageTitle.add(pagedSoftwareList);
        pageTitle.add(new HtmlClearer());

        layout.addLeft(pageTitle);

        layout.addRight(new SideBarButton(Context.tr("Add a software"), new CreateSoftwarePageUrl(), WebConfiguration.getImgSoftware()));
        layout.addRight(new SideBarDocumentationBlock("software"));

        return layout;
    }

    @Override
    protected String createPageTitle() {
        return Context.tr("Software list");
    }

    @Override
    public boolean isStable() {
        return true;
    }

    @Override
    protected Breadcrumb createBreadcrumb() {
        return SoftwareListPage.generateBreadcrumb();
    }

    protected static Breadcrumb generateBreadcrumb() {
        final Breadcrumb breadcrumb = IndexPage.generateBreadcrumb();

        breadcrumb.pushLink(new SoftwareListPageUrl().getHtmlLink(Context.trc("Software (list)","Software")));

        return breadcrumb;
    }

}

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
package com.bloatit.web.linkable.meta.bugreport;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.meta.MetaBug;
import com.bloatit.framework.meta.MetaBugManager;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlTitleBlock;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.components.renderer.HtmlCachedMarkdownRenderer;
import com.bloatit.framework.webprocessor.masters.Header;
import com.bloatit.framework.webprocessor.masters.Header.Robot;
import com.bloatit.web.pages.IndexPage;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.pages.master.MasterPage;
import com.bloatit.web.pages.master.sidebar.TwoColumnLayout;
import com.bloatit.web.url.MetaBugDeleteActionUrl;
import com.bloatit.web.url.MetaBugEditPageUrl;
import com.bloatit.web.url.MetaBugsListPageUrl;

@ParamContainer("meta/bug/list")
public final class MetaBugsListPage extends MasterPage {
    private final MetaBugsListPageUrl url;

    public MetaBugsListPage(final MetaBugsListPageUrl url) {
        super(url);
        this.url = url;
    }

    @Override
    protected HtmlElement createBodyContent() throws RedirectException {
        final TwoColumnLayout layout = new TwoColumnLayout(true, url);
        final List<MetaBug> bugList = MetaBugManager.getOpenBugs();

        final HtmlTitleBlock pageTitle = new HtmlTitleBlock(tr("Bugs list ({0})", bugList.size()), 1);

        for (final MetaBug bug : bugList) {
            final HtmlDiv bugBox = new HtmlDiv("meta_bug_box");
            if (session.isLogged()) {
                final HtmlDiv editBox = new HtmlDiv("float_right");
                bugBox.add(editBox);
                editBox.add(new MetaBugEditPageUrl(bug.getId()).getHtmlLink(tr("edit")));
                editBox.addText(" - ");
                editBox.add(new MetaBugDeleteActionUrl(bug.getId()).getHtmlLink(tr("delete")));
            }
            bugBox.add(new HtmlCachedMarkdownRenderer(bug.getDescription()));
            pageTitle.add(bugBox);

        }

        layout.addLeft(pageTitle);

        return layout;
    }

    @Override
    protected String createPageTitle() {
        return "Bugs list";
    }

    @Override
    public boolean isStable() {
        return true;
    }

    @Override
    protected Set<Robot> getRobots() {
        Set<Robot> robots = new HashSet<Header.Robot>();
        robots.add(Robot.NO_INDEX);
        return robots;
    }

    @Override
    protected Breadcrumb createBreadcrumb() {
        return MetaBugsListPage.generateBreadcrumb();
    }

    public static Breadcrumb generateBreadcrumb() {
        final Breadcrumb breadcrumb = IndexPage.generateBreadcrumb();
        breadcrumb.pushLink(new MetaBugsListPageUrl().getHtmlLink(tr("Bugs")));
        return breadcrumb;
    }
}

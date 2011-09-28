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
package com.bloatit.web.linkable.meta.feedback;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.meta.MetaFeedback;
import com.bloatit.framework.meta.MetaFeedbackManager;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlTitleBlock;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.components.renderer.HtmlCachedMarkdownRenderer;
import com.bloatit.framework.webprocessor.masters.Header;
import com.bloatit.framework.webprocessor.masters.Header.Robot;
import com.bloatit.model.right.AuthToken;
import com.bloatit.web.linkable.IndexPage;
import com.bloatit.web.linkable.master.Breadcrumb;
import com.bloatit.web.linkable.master.ElveosPage;
import com.bloatit.web.linkable.master.sidebar.TwoColumnLayout;
import com.bloatit.web.linkable.meta.bugreport.MetaBugsListPage;
import com.bloatit.web.url.MetaBugDeleteActionUrl;
import com.bloatit.web.url.MetaBugEditPageUrl;
import com.bloatit.web.url.MetaFeedbackListPageUrl;

@ParamContainer("meta/feedback/list")
public final class MetaFeedbackListPage extends ElveosPage {
    private final MetaFeedbackListPageUrl url;

    public MetaFeedbackListPage(final MetaFeedbackListPageUrl url) {
        super(url);
        this.url = url;
    }

    @Override
    protected HtmlElement createBodyContent() throws RedirectException {
        final TwoColumnLayout layout = new TwoColumnLayout(true, url);
        final List<MetaFeedback> feedbackList = MetaFeedbackManager.getOpenFeedbacks();

        final HtmlTitleBlock pageTitle = new HtmlTitleBlock(tr("Feedback list ({0})", feedbackList.size()), 1);

        for (final MetaFeedback feedback : feedbackList) {
            final HtmlDiv feedbackBox = new HtmlDiv("meta_bug_box");
            if (AuthToken.isAuthenticated()) {
                final HtmlDiv editBox = new HtmlDiv("float_right");
                feedbackBox.add(editBox);
                editBox.add(new MetaBugEditPageUrl(feedback.getId()).getHtmlLink(tr("edit")));
                editBox.addText(" - ");
                editBox.add(new MetaBugDeleteActionUrl(getSession().getShortKey(), feedback.getId()).getHtmlLink(tr("delete")));
            }
            feedbackBox.add(new HtmlCachedMarkdownRenderer(feedback.getDescription()));
            pageTitle.add(feedbackBox);

        }

        layout.addLeft(pageTitle);

        return layout;
    }

    @Override
    protected String createPageTitle() {
        return "Feedback list";
    }

    @Override
    public boolean isStable() {
        return true;
    }

    @Override
    protected Set<Robot> getRobots() {
        final Set<Robot> robots = new HashSet<Header.Robot>();
        robots.add(Robot.NO_INDEX);
        return robots;
    }

    @Override
    protected Breadcrumb createBreadcrumb() {
        return MetaFeedbackListPage.generateBreadcrumb();
    }

    private static Breadcrumb generateBreadcrumb() {
        final Breadcrumb breadcrumb = IndexPage.generateBreadcrumb();
        breadcrumb.pushLink(new MetaFeedbackListPageUrl().getHtmlLink(tr("Feedback")));
        return breadcrumb;
    }
}

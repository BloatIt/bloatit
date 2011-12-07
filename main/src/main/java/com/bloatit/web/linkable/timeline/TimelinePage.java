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
package com.bloatit.web.linkable.timeline;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.webprocessor.annotations.Optional;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.model.Member;
import com.bloatit.web.linkable.IndexPage;
import com.bloatit.web.linkable.master.Breadcrumb;
import com.bloatit.web.linkable.master.ElveosPage;
import com.bloatit.web.linkable.members.MemberPage;
import com.bloatit.web.url.TimelinePageUrl;

/**
 * Page that hosts the form to create a new feature
 */
@ParamContainer("timeline")
public final class TimelinePage extends ElveosPage {

    @RequestParam(role = Role.GET, message = @tr("I cannot find the member number: ''%value%''."))
    @Optional()
    private final Member member;

    public TimelinePage(final TimelinePageUrl url) {
        super(url);
        this.member = url.getMember();
    }

    @Override
    protected String createPageTitle() {
        return tr("Timeline");
    }

    @Override
    public boolean isStable() {
        return true;
    }

    @Override
    protected HtmlElement createBodyContent() throws RedirectException {

        final HtmlDiv layout = new HtmlDiv("timeline-page");

        layout.add(new HtmlTimelineBlock(member));

        return layout;
    }

    @Override
    protected Breadcrumb createBreadcrumb() {
        return TimelinePage.generateBreadcrumb(member);
    }

    static Breadcrumb generateBreadcrumb(final Member member) {
        final Breadcrumb breadcrumb;
        
        if(member != null) {
            breadcrumb = MemberPage.generateBreadcrumb(member);
        } else {
            breadcrumb = IndexPage.generateBreadcrumb();
        }

        TimelinePageUrl timelinePageUrl = new TimelinePageUrl();
        if(member != null) {
            timelinePageUrl.setMember(member);
        }
        breadcrumb.pushLink(timelinePageUrl.getHtmlLink(tr("Timeline")));

        return breadcrumb;
    }
}

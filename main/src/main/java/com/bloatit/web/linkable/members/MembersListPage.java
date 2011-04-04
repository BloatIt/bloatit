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
package com.bloatit.web.linkable.members;

import static com.bloatit.framework.webserver.Context.tr;

import java.util.ArrayList;
import java.util.List;

import com.bloatit.common.Log;
import com.bloatit.framework.exceptions.highlevel.ShallNotPassException;
import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.exceptions.lowlevel.UnauthorizedOperationException;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.components.HtmlDiv;
import com.bloatit.framework.webserver.components.HtmlLink;
import com.bloatit.framework.webserver.components.HtmlRenderer;
import com.bloatit.framework.webserver.components.HtmlSpan;
import com.bloatit.framework.webserver.components.HtmlTitleBlock;
import com.bloatit.framework.webserver.components.PlaceHolderElement;
import com.bloatit.framework.webserver.components.advanced.HtmlClearer;
import com.bloatit.framework.webserver.components.meta.XmlNode;
import com.bloatit.model.Member;
import com.bloatit.model.managers.MemberManager;
import com.bloatit.web.HtmlTools;
import com.bloatit.web.components.HtmlPagedList;
import com.bloatit.web.pages.IndexPage;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.pages.master.MasterPage;
import com.bloatit.web.pages.master.TwoColumnLayout;
import com.bloatit.web.url.MemberPageUrl;
import com.bloatit.web.url.MembersListPageUrl;

@ParamContainer("member/list")
public final class MembersListPage extends MasterPage {
    // Keep me here ! I am needed for the Url generation !
    private HtmlPagedList<Member> pagedMemberList;
    private final MembersListPageUrl url;

    public MembersListPage(final MembersListPageUrl url) {
        super(url);
        this.url = url;
    }

    @Override
    protected void doCreate() throws RedirectException {
        TwoColumnLayout layout = new TwoColumnLayout(true);

        final HtmlTitleBlock pageTitle = new HtmlTitleBlock("Members list", 1);
        final PageIterable<Member> memberList = MemberManager.getAll();
        final HtmlRenderer<Member> memberItemRenderer = new MemberRenderer();

        // TODO: avoid conflict
        final MembersListPageUrl clonedUrl = url.clone();
        pagedMemberList = new HtmlPagedList<Member>(memberItemRenderer, memberList, clonedUrl, clonedUrl.getPagedMemberListUrl());

        pageTitle.add(pagedMemberList);
        pageTitle.add(new HtmlClearer());

        layout.addLeft(pageTitle);

        add(layout);
    }

    @Override
    protected String getPageTitle() {
        return "Members list";
    }

    @Override
    public boolean isStable() {
        return true;
    }

    private final class MemberRenderer implements HtmlRenderer<Member> {
        public MemberRenderer() {
        }

        @Override
        public XmlNode generate(final Member member) {
            final MemberPageUrl memberUrl = new MemberPageUrl(member);
            try {
                HtmlDiv box = new HtmlDiv("member_box");

                box.add(new HtmlDiv("float_right").add(MembersTools.getMemberAvatar(member)));

                HtmlDiv textBox = new HtmlDiv("member_text");
                HtmlLink htmlLink;
                htmlLink = memberUrl.getHtmlLink(member.getDisplayName());
                final HtmlSpan karma = new HtmlSpan("karma");
                karma.addText(HtmlTools.compressKarma(member.getKarma()));

                textBox.add(htmlLink);
                textBox.add(karma);
                box.add(textBox);
                box.add(new HtmlClearer());

                return box;
            } catch (final UnauthorizedOperationException e) {
                session.notifyError(Context.tr("An error prevented us from displaying user information. Please notify us."));
                throw new ShallNotPassException("User cannot access user information", e); 
            }
        }
    }

    @Override
    protected List<String> getCustomCss() {
        ArrayList<String> custom = new ArrayList<String>();
        custom.add("members-list.css");
        return custom;
    }


    @Override
    protected Breadcrumb getBreadcrumb() {
        return MembersListPage.generateBreadcrumb();
    }

    public static Breadcrumb generateBreadcrumb() {
        Breadcrumb breadcrumb = IndexPage.generateBreadcrumb();

        breadcrumb.pushLink(new MembersListPageUrl().getHtmlLink(tr("Members")));

        return breadcrumb;
    }
}

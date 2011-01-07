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
package com.bloatit.web.html.pages;

import com.bloatit.common.PageIterable;
import com.bloatit.framework.Member;
import com.bloatit.framework.managers.MemberManager;
import com.bloatit.web.annotations.PageComponent;
import com.bloatit.web.annotations.ParamContainer;
import com.bloatit.web.exceptions.RedirectException;
import com.bloatit.web.html.HtmlNode;
import com.bloatit.web.html.HtmlTagText;
import com.bloatit.web.html.HtmlText;
import com.bloatit.web.html.HtmlTools;
import com.bloatit.web.html.components.custom.HtmlPagedList;
import com.bloatit.web.html.components.standard.HtmlLink;
import com.bloatit.web.html.components.standard.HtmlListItem;
import com.bloatit.web.html.components.standard.HtmlRenderer;
import com.bloatit.web.html.components.standard.HtmlTitleBlock;
import com.bloatit.web.html.pages.master.Page;
import com.bloatit.web.utils.url.MemberPageUrl;
import com.bloatit.web.utils.url.MembersListPageUrl;

@ParamContainer("memberList")
public class MembersListPage extends Page {

    @PageComponent
    private HtmlPagedList<Member> pagedMemberList;
    private final MembersListPageUrl url;

    public MembersListPage(final MembersListPageUrl url) throws RedirectException {
        super(url);
        this.url = url;
        generateContent();
    }

    private void generateContent() {

        final HtmlTitleBlock pageTitle = new HtmlTitleBlock("Members list", 2);

        final PageIterable<Member> memberList = MemberManager.getMembers();

        final HtmlRenderer<Member> memberItemRenderer = new HtmlRenderer<Member>() {

            @Override
            public HtmlNode generate(final Member member) {
                final MemberPageUrl memberUrl = new MemberPageUrl(member);
                final HtmlLink htmlLink = memberUrl.getHtmlLink(member.getFullname());
                final HtmlTagText htmlKarma = new HtmlTagText("<span class=\"karma\">" + HtmlTools.compressKarma(member.getKarma()) + "</span>");
                return new HtmlListItem(htmlLink).add(htmlKarma);
            }
        };

        // TODO: avoid conflict
        final MembersListPageUrl clonedUrl = url.clone();
        pagedMemberList = new HtmlPagedList<Member>(memberItemRenderer, memberList, clonedUrl, clonedUrl.getPagedMemberListUrl());

        pageTitle.add(pagedMemberList);

        add(pageTitle);

    }

    @Override
    protected String getTitle() {
        return "Members list";
    }

    @Override
    public boolean isStable() {
        return true;
    }
}

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
package com.bloatit.web.pages;

import com.bloatit.common.Log;
import com.bloatit.framework.exceptions.RedirectException;
import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.components.HtmlLink;
import com.bloatit.framework.webserver.components.HtmlListItem;
import com.bloatit.framework.webserver.components.HtmlRenderer;
import com.bloatit.framework.webserver.components.HtmlTitleBlock;
import com.bloatit.framework.webserver.components.PlaceHolderElement;
import com.bloatit.framework.webserver.components.meta.HtmlNode;
import com.bloatit.framework.webserver.components.meta.HtmlTagText;
import com.bloatit.model.Member;
import com.bloatit.model.managers.MemberManager;
import com.bloatit.web.HtmlTools;
import com.bloatit.web.components.HtmlPagedList;
import com.bloatit.web.pages.master.MasterPage;
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
        final HtmlTitleBlock pageTitle = new HtmlTitleBlock("Members list", 2);
        final PageIterable<Member> memberList = MemberManager.getMembers();
        final HtmlRenderer<Member> memberItemRenderer = new MemberRenderer();

        // TODO: avoid conflict
        final MembersListPageUrl clonedUrl = url.clone();
        pagedMemberList = new HtmlPagedList<Member>(memberItemRenderer, memberList, clonedUrl, clonedUrl.getPagedMemberListUrl());

        pageTitle.add(pagedMemberList);
        add(pageTitle);
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
        @Override
        public HtmlNode generate(final Member member) {
            final MemberPageUrl memberUrl = new MemberPageUrl(member);
            try {
                HtmlLink htmlLink;
                htmlLink = memberUrl.getHtmlLink(member.getDisplayName());
                final HtmlTagText htmlKarma = new HtmlTagText("<span class=\"karma\">" + HtmlTools.compressKarma(member.getKarma()) + "</span>");
                return new HtmlListItem(htmlLink).add(htmlKarma);
            } catch (final UnauthorizedOperationException e) {
                Log.web().warn(e);
            }
            return new PlaceHolderElement();
        }
    }
}

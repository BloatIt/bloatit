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
package com.bloatit.web.pages;

import java.util.HashMap;
import java.util.Map;

import com.bloatit.common.PageIterable;
import com.bloatit.framework.Member;
import com.bloatit.framework.managers.MemberManager;
import com.bloatit.web.htmlrenderer.HtmlResult;
import com.bloatit.web.htmlrenderer.HtmlTools;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlComponent;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlListItem;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlPagedList;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlRenderer;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlTitle;
import com.bloatit.web.server.Page;
import com.bloatit.web.server.Session;

public class MembersListPage extends Page {

    public MembersListPage(Session session, Map<String, String> parameters) {
        super(session, parameters);
    }

    public MembersListPage(Session session) {
        this(session, new HashMap<String, String>());
    }

    @Override
    protected HtmlComponent generateContent() {

        final HtmlTitle pageTitle = new HtmlTitle("Members list", "");

        final PageIterable<Member> memberList = MemberManager.getMembers();

        HtmlRenderer<Member> memberItemRenderer = new HtmlRenderer<Member>() {

            @Override
            public void generate(HtmlResult htmlResult, Member member) {
                final MemberPage memberPage = new MemberPage(session, member);
                final HtmlListItem item = new HtmlListItem(HtmlTools.generateLink(session, member.getFullname(), memberPage)
                        + "<span class=\"karma\">" + HtmlTools.compressKarma(member.getKarma()) + "</span>");
                item.generate(htmlResult);
            }
        };

        HtmlPagedList<Member> pagedMemberList = new HtmlPagedList<Member>(memberItemRenderer, memberList, this, session);

        int pageSize = 42;
        int currentPage = 0;

        if (parameters.containsKey("page_size")) {
            try {
                pageSize = Integer.parseInt(parameters.get("page_size"));
            } catch (NumberFormatException e) {
            }
        }

        if (parameters.containsKey("page")) {
            try {
                currentPage = Integer.parseInt(parameters.get("page")) - 1;
            } catch (NumberFormatException e) {
            }
        }

        pagedMemberList.setPageSize(pageSize);
        pagedMemberList.setCurrentPage(currentPage);

        pageTitle.add(pagedMemberList);

        return pageTitle;

    }

    @Override
    public String getCode() {
        return "members_list";
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

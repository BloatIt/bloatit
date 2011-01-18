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

import static com.bloatit.web.server.Context.tr;

import com.bloatit.common.UnauthorizedOperationException;
import com.bloatit.framework.Member;
import com.bloatit.framework.right.RightManager.Action;
import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.ParamContainer;
import com.bloatit.web.annotations.RequestParam;
import com.bloatit.web.exceptions.PageNotFoundException;
import com.bloatit.web.exceptions.RedirectException;
import com.bloatit.web.html.HtmlText;
import com.bloatit.web.html.components.standard.HtmlParagraph;
import com.bloatit.web.html.components.standard.HtmlTitleBlock;
import com.bloatit.web.html.pages.master.Page;
import com.bloatit.web.server.Context;
import com.bloatit.web.utils.url.MemberPageUrl;

@ParamContainer("member")
public final class MemberPage extends Page {

    public static final String MEMBER_FIELD_NAME = "id";

    @RequestParam(name = MEMBER_FIELD_NAME, level = Level.ERROR)
    private final Member member;

    private final MemberPageUrl url;

    public MemberPage(final MemberPageUrl url) {
        super(url);
        this.url = url;
        this.member = url.getMember();
    }

    @Override
    protected void doCreate() throws RedirectException {
        if (url.getMessages().hasMessage(Level.ERROR)) {
            throw new PageNotFoundException();
        }

        member.authenticate(session.getAuthToken());

        try {
            HtmlTitleBlock memberTitle;
            memberTitle = new HtmlTitleBlock(member.getFullname(), 1);

            memberTitle.add(new HtmlText("Full name: " + member.getFullname()));

            memberTitle.add(new HtmlText("Login: " + member.getLogin()));
            if (member.canAccessEmail(Action.READ)) {
                memberTitle.add(new HtmlText("Email: " + member.getEmail()));
            }
            memberTitle.add(new HtmlText("Karma: " + member.getKarma()));
            add(memberTitle);
        } catch (final UnauthorizedOperationException e) {
            add(new HtmlParagraph(Context.tr("For obscure reasons, you are not allowed to see the details of this member.")));
        }
    }

    @Override
    protected String getTitle() {
        if (member != null) {
            try {
                return tr("Member - ") + member.getLogin();
            } catch (final UnauthorizedOperationException e) {
                return tr("Member - Jhon Do");
            }
        }
        return tr("Member - No member");
    }

    @Override
    public boolean isStable() {
        return true;
    }
}

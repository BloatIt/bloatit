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

import static com.bloatit.framework.webserver.Context.tr;

import com.bloatit.framework.exceptions.RedirectException;
import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.framework.webserver.PageNotFoundException;
import com.bloatit.framework.webserver.annotations.Message.Level;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.annotations.RequestParam;
import com.bloatit.framework.webserver.components.HtmlParagraph;
import com.bloatit.framework.webserver.components.HtmlTitleBlock;
import com.bloatit.framework.webserver.components.meta.HtmlText;
import com.bloatit.model.Member;
import com.bloatit.model.right.RightManager.Action;
import com.bloatit.web.pages.master.MasterPage;
import com.bloatit.web.url.MemberPageUrl;

@ParamContainer("member")
public final class MemberPage extends MasterPage {

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

            memberTitle.add(new HtmlText(tr("Full name: ") + member.getFullname()));

            memberTitle.add(new HtmlText(tr("Login: ") + member.getLogin()));
            if (member.canAccessEmail(Action.READ)) {
                memberTitle.add(new HtmlText(tr("Email: ") + member.getEmail()));
            }
            memberTitle.add(new HtmlText(tr("Karma: ") + member.getKarma()));
            add(memberTitle);
        } catch (final UnauthorizedOperationException e) {
            add(new HtmlParagraph(tr("For obscure reasons, you are not allowed to see the details of this member.")));
        }
    }

    @Override
    protected String getPageTitle() {
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

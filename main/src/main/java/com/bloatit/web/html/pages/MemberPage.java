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
package com.bloatit.web.html.pages;


import com.bloatit.framework.Member;
import com.bloatit.web.annotations.RequestParam;
import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.exceptions.PageNotFoundException;
import com.bloatit.web.exceptions.RedirectException;
import com.bloatit.web.html.HtmlText;
import com.bloatit.web.html.components.standard.HtmlTitleBlock;
import com.bloatit.web.html.pages.master.Page;
import com.bloatit.web.utils.url.Request;

public class MemberPage extends Page {

    @RequestParam(level = Level.ERROR)
    private Member member;

    public MemberPage(final Request request) throws RedirectException {
        super(request);
        generateContent();
        this.request.setValues(this);
        addNotifications(request.getMessages());

        if (request.getMessages().hasMessage(Level.ERROR)) {
            throw new PageNotFoundException();
        }
    }

    private void generateContent() {
        member.authenticate(session.getAuthToken());

        final HtmlTitleBlock memberTitle = new HtmlTitleBlock(member.getFullname(),2);

        memberTitle.add(new HtmlText("Full name: " + member.getFullname()));
        memberTitle.add(new HtmlText("Login: " + member.getLogin()));
        if (member.canGetEmail()) {
            memberTitle.add(new HtmlText("Email: " + member.getEmail()));
        }
        memberTitle.add(new HtmlText("Karma: " + member.getKarma()));
        add(memberTitle);
    }

    @Override
    protected String getTitle() {
        if (member != null) {
            return "Member - " + member.getLogin();
        } else {
            return "Member - No member";
        }
    }

    @Override
    public boolean isStable() {
        return true;
    }
}

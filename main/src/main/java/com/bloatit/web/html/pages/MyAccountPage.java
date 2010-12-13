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
import com.bloatit.web.exceptions.RedirectException;
import com.bloatit.web.html.HtmlText;
import com.bloatit.web.html.components.standard.HtmlTitle;
import com.bloatit.web.html.components.standard.HtmlTitleBlock;
import com.bloatit.web.html.pages.master.Page;
import com.bloatit.web.utils.url.Request;

public class MyAccountPage extends Page {

    public MyAccountPage(final Request request) throws RedirectException {
        super(request);
        generateContent();
    }

    private void generateContent() {
        if (session.getAuthToken() != null) {
            final Member member = session.getAuthToken().getMember();
            member.authenticate(session.getAuthToken());
            final HtmlTitleBlock memberTitle = new HtmlTitleBlock(member.getFullname(),2);

            memberTitle.add(new HtmlText("Full name: " + member.getFullname()));
            memberTitle.add(new HtmlText("Login: " + member.getLogin()));
            memberTitle.add(new HtmlText("Email: " + member.getEmail()));
            memberTitle.add(new HtmlText("Karma: " + member.getKarma()));

            add(memberTitle);
        } else {
            add(new HtmlTitle("No account",2));
        }
    }

    @Override
    protected String getTitle() {
        if (session.getAuthToken() != null) {
            return "My account - " + session.getAuthToken().getMember().getLogin();
        } else {
            return "My account - No account";
        }
    }

    @Override
    public boolean isStable() {
        return true;
    }
}

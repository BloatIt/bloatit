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

import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.components.HtmlList;
import com.bloatit.framework.webserver.components.HtmlParagraph;
import com.bloatit.framework.webserver.components.HtmlTitle;
import com.bloatit.framework.webserver.components.HtmlTitleBlock;
import com.bloatit.model.Member;
import com.bloatit.web.pages.master.Page;
import com.bloatit.web.url.MyAccountPageUrl;

@ParamContainer("myaccount")
public final class MyAccountPage extends Page {

    public MyAccountPage(final MyAccountPageUrl myAccountPageUrl) {
        super(myAccountPageUrl);
        generateContent();
    }

    private void generateContent() {
        if (session.getAuthToken() != null) {
            final Member member = session.getAuthToken().getMember();
            member.authenticate(session.getAuthToken());
            HtmlTitleBlock memberTitle;
            try {
                memberTitle = new HtmlTitleBlock(member.getFullname(), 2);

                final HtmlList userInfo = new HtmlList();
                memberTitle.add(userInfo);

                userInfo.add(tr("Full name: ") + member.getFullname());
                userInfo.add(tr("Login: ") + member.getLogin());
                userInfo.add(tr("Email: ") + member.getEmail());
                userInfo.add(tr("Karma: ") + member.getKarma());

                add(memberTitle);
            } catch (final UnauthorizedOperationException e) {
                add(new HtmlParagraph(tr("For obscure reasons, you are not allowed to see your own details.")));
            }
        } else {
            add(new HtmlTitle(tr("No account"), 2));
        }
    }

    @Override
    protected String getTitle() {
        if (session.getAuthToken() != null) {
            try {
                return tr("My account - ") + session.getAuthToken().getMember().getLogin();
            } catch (final UnauthorizedOperationException e) {
                tr("My account - John Doe");
            }
        }
        return tr("My account - No account");
    }

    @Override
    public boolean isStable() {
        return true;
    }
}

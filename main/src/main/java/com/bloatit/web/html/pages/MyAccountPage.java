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

import com.bloatit.common.UnauthorizedOperationException;
import com.bloatit.framework.Member;
import com.bloatit.web.annotations.ParamContainer;
import com.bloatit.web.html.components.standard.HtmlList;
import com.bloatit.web.html.components.standard.HtmlTitle;
import com.bloatit.web.html.components.standard.HtmlTitleBlock;
import com.bloatit.web.html.pages.master.Page;
import com.bloatit.web.server.Context;
import com.bloatit.web.utils.url.MyAccountPageUrl;

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

                HtmlList userInfo = new HtmlList();
                memberTitle.add(userInfo);

                userInfo.add(Context.tr("Full name: ") + member.getFullname());
                userInfo.add(Context.tr("Login: ") + member.getLogin());
                userInfo.add(Context.tr("Email: ") + member.getEmail());
                userInfo.add(Context.tr("Karma: ") + member.getKarma());

                add(memberTitle);
            } catch (UnauthorizedOperationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            add(new HtmlTitle("No account", 2));
        }
    }

    @Override
    protected String getTitle() {
        if (session.getAuthToken() != null) {
            return "My account - " + session.getAuthToken().getMember().getLogin();
        }
        return "My account - No account";
    }

    @Override
    public boolean isStable() {
        return true;
    }
}

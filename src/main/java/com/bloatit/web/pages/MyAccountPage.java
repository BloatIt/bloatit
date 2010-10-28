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

import com.bloatit.framework.Member;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlComponent;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlText;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlTitle;
import com.bloatit.web.server.Page;
import com.bloatit.web.server.Session;
import java.util.Map;


public class MyAccountPage extends Page {

    public MyAccountPage(Session session, Map<String, String> parameters) {
        super(session, parameters);
    }

    public MyAccountPage(Session session) {
        super(session);
    }

    @Override
    protected HtmlComponent generateContent() {
        if (this.session.getAuthToken() != null) {
            Member member = this.session.getAuthToken().getMember();
            member.unLock(this.session.getAuthToken());
            HtmlTitle memberTitle = new HtmlTitle(member.getFullName(), "");
            
            memberTitle.add(new HtmlText("Full name: " + member.getFullName()));
            memberTitle.add(new HtmlText("Login: " + member.getLogin()));
            memberTitle.add(new HtmlText("Email: " + member.getEmail()));
            memberTitle.add(new HtmlText("Karma: " + member.getKarma()));

            return memberTitle;
        } else {
            return new HtmlTitle("No account", "");
        }
    }

    @Override
    public String getCode() {
        return "my_account";
    }

    @Override
    protected String getTitle() {
        if (this.session.getAuthToken() != null) {
            return "My account - " + this.session.getAuthToken().getMember().getLogin();
        } else {
            return "My account - No account";
        }
    }

}

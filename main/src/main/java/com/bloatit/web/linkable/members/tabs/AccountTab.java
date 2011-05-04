//
// Copyright (c) 2011 Linkeos.
//
// This file is part of Elveos.org.
// Elveos.org is free software: you can redistribute it and/or modify it
// under the terms of the GNU General Public License as published by the
// Free Software Foundation, either version 3 of the License, or (at your
// option) any later version.
//
// Elveos.org is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
// more details.
// You should have received a copy of the GNU General Public License along
// with Elveos.org. If not, see http://www.gnu.org/licenses/.
//
package com.bloatit.web.linkable.members.tabs;

import com.bloatit.framework.exceptions.lowlevel.UnauthorizedOperationException;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.advanced.HtmlTabBlock.HtmlTab;
import com.bloatit.framework.webprocessor.components.meta.XmlNode;
import com.bloatit.model.Member;
import com.bloatit.web.components.AccountComponent;

public class AccountTab extends HtmlTab {
    private final Member member;

    public AccountTab(Member member, String title, String tabKey) {
        super(title, tabKey);
        this.member = member;
    }

    @Override
    public XmlNode generateBody() {
        // TODO rights.
        // if (!team.hasTeamPrivilege(UserTeamRight.BANK)) {
        // throw new
        // ShallNotPassException("You cannot access team bank information.");
        // }

        HtmlDiv master = new HtmlDiv("tab_pane");

        try {
            master.add(new AccountComponent(member));
        } catch (UnauthorizedOperationException e) {
            e.printStackTrace();
        }

        return master;
    }
}

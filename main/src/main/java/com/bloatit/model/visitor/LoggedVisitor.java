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
package com.bloatit.model.visitor;

import com.bloatit.framework.webprocessor.context.Visitor;
import com.bloatit.model.Member;
import com.bloatit.model.Team;
import com.bloatit.model.managers.MemberManager;

public class LoggedVisitor implements Visitor {

    private final int memberId;

    public LoggedVisitor(int memberId) {
        this.memberId = memberId;
    }

    @Override
    public Member getMember() {
        return MemberManager.getById(memberId);
    }

    @Override
    public boolean isAnonymous() {
        return false;
    }

    @Override
    public boolean hasModifyTeamRight(Team targetTeam) {
        return getMember().hasModifyTeamRight(targetTeam);
    }

    @Override
    public boolean isInTeam(Team targetTeam) {
        return getMember().isInTeam(targetTeam);
    }

    @Override
    public boolean hasInviteTeamRight(Team targetTeam) {
        return getMember().hasInviteTeamRight(targetTeam);
    }

}

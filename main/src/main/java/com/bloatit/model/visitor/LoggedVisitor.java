package com.bloatit.model.visitor;

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

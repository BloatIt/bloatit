package com.bloatit.model.visitor;

import com.bloatit.model.Member;
import com.bloatit.model.Team;

public class AnonymousVisitor implements Visitor {

    @Override
    public Member getMember() {
        return null;
    }

    @Override
    public boolean isAnonymous() {
        return true;
    }

    @Override
    public boolean hasModifyTeamRight(Team targetTeam) {
        return false;
    }

    @Override
    public boolean isInTeam(Team targetTeam) {
        return false;
    }

    @Override
    public boolean hasInviteTeamRight(Team targetTeam) {
        return false;
    }

}

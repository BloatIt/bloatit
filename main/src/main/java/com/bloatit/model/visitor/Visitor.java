package com.bloatit.model.visitor;

import com.bloatit.model.Member;
import com.bloatit.model.Team;

public interface Visitor {

    Member getMember();

    boolean isAnonymous();

    boolean hasModifyTeamRight(Team targetTeam);

    boolean isInTeam(Team targetTeam);

    boolean hasInviteTeamRight(Team targetTeam);


}

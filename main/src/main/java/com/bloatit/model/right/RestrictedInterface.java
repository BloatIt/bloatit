package com.bloatit.model.right;

import com.bloatit.model.Member;
import com.bloatit.model.Rights;
import com.bloatit.model.Team;

public interface RestrictedInterface {

    void authenticate(final AuthToken token);

    Team getAsTeam();

    Member getAuthenticatedMember();
    
    Rights getRights();

}

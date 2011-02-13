package com.bloatit.model;

import java.util.EnumSet;

import com.bloatit.data.DaoKudosable;
import com.bloatit.data.DaoKudosable.PopularityState;
import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.framework.exceptions.UnauthorizedOperationException.SpecialCode;

public interface KudosableInterface<T extends DaoKudosable> extends UserContentInterface<T> {

    EnumSet<SpecialCode> canVoteUp();

    EnumSet<SpecialCode> canVoteDown();

    /**
     * Gets the influence value the authenticated user has added to this kudosable. If the
     * user has not voted yet then the returned value is 0. It is a positive value for a
     * vote up and a negative vote for a kudos down.
     *
     * @return the influence value that the authenticated user has added to this
     *         kudosable.
     */
    int getUserVoteValue();

    int voteDown() throws UnauthorizedOperationException;

    int voteUp() throws UnauthorizedOperationException;

    PopularityState getState();

    int getPopularity();

    public abstract boolean isOwnedByMe();

}
package com.bloatit.model;

import java.util.EnumSet;

import com.bloatit.data.DaoKudosable;
import com.bloatit.data.DaoKudosable.State;
import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.framework.exceptions.UnauthorizedOperationException.SpecialCode;

public interface KudosableInterface<T extends DaoKudosable> extends UserContentInterface<T> {

    EnumSet<SpecialCode> canVoteUp();

    EnumSet<SpecialCode> canVoteDown();

    int getVote();

    int voteDown() throws UnauthorizedOperationException;

    int voteUp() throws UnauthorizedOperationException;

    State getState();

    int getPopularity();

    public abstract boolean isOwnedByMe();

}
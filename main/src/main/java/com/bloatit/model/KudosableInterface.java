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
     * Gets the influence value the authenticated user has added to this
     * kudosable. If the user has not voted yet then the returned value is 0. It
     * is a positive value for a vote up and a negative vote for a kudos down.
     *
     * @return the influence value that the authenticated user has added to this
     *         kudosable.
     */
    int getUserVoteValue();

    int voteDown() throws UnauthorizedOperationException;

    int voteUp() throws UnauthorizedOperationException;

    PopularityState getState();

    int getPopularity();

}

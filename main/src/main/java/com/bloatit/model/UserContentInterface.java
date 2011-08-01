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

import java.util.Date;

import com.bloatit.model.right.RestrictedInterface;
import com.bloatit.model.right.UnauthorizedOperationException;

/**
 * The Interface UserContentInterface. A User content is a content created by a
 * user. The {@link Member} that has created the content can say he has done so
 * in the name of a team.
 */
public interface UserContentInterface extends IdentifiableInterface, RestrictedInterface, Attachmentable {

    /**
     * Gets the member author.
     * 
     * @return the author
     */
    Member getMember();

    /**
     * Get the author. It can be the member or the team if this
     * {@link UserContent} has been created as a team.
     * 
     * @return the author (member or team).
     */
    Actor<?> getAuthor();

    /**
     * Gets the creation date.
     * 
     * @return the creation date
     */
    Date getCreationDate();

    /**
     * Gets the as team. Can (may) be null.
     * 
     * @return the as team
     */
    Team getAsTeam();

    boolean isDeleted();

    void delete() throws UnauthorizedOperationException;

    void restore() throws UnauthorizedOperationException;
}

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

import com.bloatit.data.DaoUserContent;
import com.bloatit.data.IdentifiableInterface;
import com.bloatit.framework.exceptions.lowlevel.UnauthorizedOperationException;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.right.RestrictedInterface;

/**
 * The Interface UserContentInterface. A User content is a content created by a
 * user. The {@link Member} that has created the content can say he has done so
 * in the name of a team.
 * 
 * @param <T> the Dao class corresponding to this UserContent.
 */
public interface UserContentInterface<T extends DaoUserContent> extends IdentifiableInterface, RestrictedInterface {

    /**
     * Gets the author.
     * 
     * @return the author
     */
    Member getAuthor();

    /**
     * Gets the creation date.
     * 
     * @return the creation date
     */
    Date getCreationDate();

    public abstract boolean canAccessAsTeam(final Team asTeam);

    /**
     * Sets the as team. The author is saying that he is creating this content
     * in the name of the team <code>asTeam</code>.
     * 
     * @param asTeam the new as team
     * @throws UnauthorizedOperationException
     */
    void setAsTeam(final Team asTeam) throws UnauthorizedOperationException;

    /**
     * Gets the as team. Can (may) be null. See {@link #setAsTeam(Team)} for
     * more information on this property.
     * 
     * @return the as team
     */
    Team getAsTeam();

    /**
     * Gets the files associated with this user content.
     * 
     * @return the files
     */
    PageIterable<FileMetadata> getFiles();

    /**
     * Associate a file with this user content.
     * 
     * @throws UnauthorizedOperationException
     */
    void addFile(FileMetadata file) throws UnauthorizedOperationException;

    boolean canAddFile();

    boolean isDeleted() throws UnauthorizedOperationException;

}

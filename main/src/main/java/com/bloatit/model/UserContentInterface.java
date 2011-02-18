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
import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.framework.utils.PageIterable;

/**
 * The Interface UserContentInterface. A User content is a content created by a
 * user. The {@link Member} that has created the content can say he has done so
 * in the name of a group.
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

    public abstract boolean canAccessAsGroup();

    /**
     * Sets the as group. The author is saying that he is creating this content
     * in the name of the group <code>asGroup</code>.
     * 
     * @param asGroup the new as group
     * @throws UnauthorizedOperationException
     */
    void setAsGroup(final Group asGroup) throws UnauthorizedOperationException;

    /**
     * Gets the as group. Can (may) be null. See {@link #setAsGroup(Group)} for
     * more information on this property.
     * 
     * @return the as group
     */
    Group getAsGroup();

    /**
     * Gets the files associated with this user content.
     * 
     * @return the files
     */
    PageIterable<FileMetadata> getFiles();

}

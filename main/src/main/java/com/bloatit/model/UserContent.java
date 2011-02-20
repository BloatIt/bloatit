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
import java.util.EnumSet;

import com.bloatit.data.DaoGroupRight.UserGroupRight;
import com.bloatit.data.DaoUserContent;
import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.lists.FileMetadataList;
import com.bloatit.model.right.Action;
import com.bloatit.model.right.UserContentRight;

/**
 * The Class UserContent. Model vision of the {@link DaoUserContent} class.
 * 
 * @param <T> the generic type. Must be the concrete Dao version of the concrete subClass.
 * For example: Demand extends UserContent<DaoDemand>
 */
public abstract class UserContent<T extends DaoUserContent> extends Identifiable<T> implements UserContentInterface<T> {

    /**
     * Instantiates a new user content.
     * 
     * @param dao the dao
     */
    protected UserContent(final T dao) {
        super(dao);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.bloatit.model.UserContentInterface#getAuthor()
     */
    @Override
    public final Member getAuthor() {
        return Member.create(getDao().getAuthor());
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.bloatit.model.UserContentInterface#getCreationDate()
     */
    @Override
    public final Date getCreationDate() {
        return getDao().getCreationDate();
    }

    @Override
    public final boolean canAccessAsGroup() {
        return canAccess(new UserContentRight.AsGroup(), Action.WRITE);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.bloatit.model.UserContentInterface#setAsGroup(com.bloatit.model.Group )
     */
    @Override
    public final void setAsGroup(final Group asGroup) throws UnauthorizedOperationException {
        tryAccess(new UserContentRight.AsGroup(), Action.WRITE);
        getDao().setAsGroup(asGroup.getDao());
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.bloatit.model.UserContentInterface#getAsGroup()
     */
    @Override
    public final Group getAsGroup() {
        return Group.create(getDao().getAsGroup());
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.bloatit.model.UserContentInterface#getFiles()
     */
    @Override
    public PageIterable<FileMetadata> getFiles() {
        return new FileMetadataList(getDao().getFiles());
    }

    // TODO right management
    public void delete() {
        getDao().setIsDeleted(true);
    }

    // TODO right management
    public void restore() {
        getDao().setIsDeleted(false);
    }

    @Override
    protected boolean isMine(Member member) {
        return member.isMine(member);
    }

    @Override
    protected EnumSet<UserGroupRight> calculateMyGroupRights(Member member) {

        if (getAsGroup() != null && member.isInGroup(getAsGroup())) {
            return getAsGroup().getUserGroupRight(member);
        }
        return EnumSet.noneOf(UserGroupRight.class);
    }

}

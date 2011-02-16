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
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.lists.FileMetadataList;

/**
 * The Class UserContent. Model vision of the {@link DaoUserContent} class.
 * 
 * @param <T> the generic type. Must be the concrete Dao version of the concrete
 *            subClass. For example: Demand extends UserContent<DaoDemand>
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

    /**
     * Gets the dao user content. This should only be used by subClasses.
     * 
     * @return the dao user content
     */
    protected abstract DaoUserContent getDaoUserContent();

    /*
     * (non-Javadoc)
     * 
     * @see com.bloatit.model.UserContentInterface#getAuthor()
     */
    @Override
    public final Member getAuthor() {
        return Member.create(getDaoUserContent().getAuthor());
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.bloatit.model.UserContentInterface#getCreationDate()
     */
    @Override
    public final Date getCreationDate() {
        return getDaoUserContent().getCreationDate();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.bloatit.model.UserContentInterface#setAsGroup(com.bloatit.model.Group
     * )
     */
    @Override
    public final void setAsGroup(final Group asGroup) {
        getDaoUserContent().setAsGroup(asGroup.getDao());
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.bloatit.model.UserContentInterface#getAsGroup()
     */
    @Override
    public final Group getAsGroup() {
        return Group.create(getDaoUserContent().getAsGroup());
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.bloatit.model.UserContentInterface#getFiles()
     */
    @Override
    public PageIterable<FileMetadata> getFiles() {
        return new FileMetadataList(getDaoUserContent().getFiles());
    }

    // TODO right management
    public void delete() {
        this.getDao().setIsDeleted(true);
    }

    // TODO right management
    public void restore() {
        this.getDao().setIsDeleted(false);
    }

}

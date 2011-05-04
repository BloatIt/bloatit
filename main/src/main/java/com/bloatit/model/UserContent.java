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
import com.bloatit.model.right.Action;
import com.bloatit.model.right.RgtUserContent;
import com.bloatit.model.right.UnauthorizedOperationException;
import com.bloatit.model.right.UnauthorizedOperationException.SpecialCode;

/**
 * The Class UserContent. Model vision of the {@link DaoUserContent} class.
 * 
 * @param <T> the generic type. Must be the concrete Dao version of the concrete
 *            subClass. For example: Feature extends UserContent<DaoFeature>
 */
public abstract class UserContent<T extends DaoUserContent> extends Identifiable<T> implements UserContentInterface {

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
     * @see com.bloatit.model.UserContentInterface#getAuthor()
     */
    @Override
    public final Member getMember() {
        return Member.create(getDao().getMember());
    }

    @Override
    public final Actor<?> getAuthor() {
        return getAsTeam() == null ? getMember() : getAsTeam();
    }

    /*
     * (non-Javadoc)
     * @see com.bloatit.model.UserContentInterface#getCreationDate()
     */
    @Override
    public final Date getCreationDate() {
        return getDao().getCreationDate();
    }

    /*
     * (non-Javadoc)
     * @see com.bloatit.model.UserContentInterface#getAsTeam()
     */
    @Override
    public final Team getAsTeam() {
        return Team.create(getDao().getAsTeam());
    }

    /*
     * (non-Javadoc)
     * @see com.bloatit.model.UserContentInterface#getFiles()
     */
    @Override
    public PageIterable<FileMetadata> getFiles() {
        return new FileMetadataList(getDao().getFiles());
    }

    public void delete() throws UnauthorizedOperationException {
        if (!getRights().hasAdminUserPrivilege()) {
            throw new UnauthorizedOperationException(SpecialCode.ADMIN_ONLY);
        }
        getDao().setIsDeleted(true);
    }

    public void restore() throws UnauthorizedOperationException {
        if (!getRights().hasAdminUserPrivilege()) {
            throw new UnauthorizedOperationException(SpecialCode.ADMIN_ONLY);
        }
        getDao().setIsDeleted(false);
    }

    @Override
    public boolean isDeleted() throws UnauthorizedOperationException {
        if (!getRights().hasAdminUserPrivilege()) {
            throw new UnauthorizedOperationException(SpecialCode.ADMIN_ONLY);
        }
        return getDao().isDeleted();
    }

    @Override
    public final boolean canAddFile() {
        return canAccess(new RgtUserContent.File(), Action.WRITE);
    }

    @Override
    public void addFile(final FileMetadata file) throws UnauthorizedOperationException {
        tryAccess(new RgtUserContent.File(), Action.WRITE);
        getDao().addFile(file.getDao());
    }

}

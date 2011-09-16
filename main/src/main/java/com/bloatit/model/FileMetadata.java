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

import java.io.File;

import com.bloatit.data.DaoFileMetadata;
import com.bloatit.data.DaoFileMetadata.FileType;
import com.bloatit.data.DaoUserContent;
import com.bloatit.model.right.Action;
import com.bloatit.model.right.RgtFileMetadata;
import com.bloatit.model.right.UnauthorizedPublicAccessException;
import com.bloatit.model.visitor.ModelClassVisitor;

public final class FileMetadata extends UserContent<DaoFileMetadata> {

    // /////////////////////////////////////////////////////////////////////////////////////////
    // CONSTRUCTION
    // /////////////////////////////////////////////////////////////////////////////////////////

    private static final class MyCreator extends Creator<DaoFileMetadata, FileMetadata> {
        @SuppressWarnings("synthetic-access")
        @Override
        public FileMetadata doCreate(final DaoFileMetadata dao) {
            return new FileMetadata(dao);
        }
    }

    @SuppressWarnings("synthetic-access")
    public static FileMetadata create(final DaoFileMetadata dao) {
        return new MyCreator().create(dao);
    }

    private FileMetadata(final DaoFileMetadata dao) {
        super(dao);
    }

    /**
     * Create a new FileMetadata.
     * 
     * @see DaoFileMetadata#createAndPersist(com.bloatit.data.DaoMember,
     *      com.bloatit.data.DaoTeam, DaoUserContent, String, String, FileType,
     *      int)
     */
    public FileMetadata(final Member author, final Team team, final String filename, final String url, final FileType type, final int size) {
        this(DaoFileMetadata.createAndPersist(author.getDao(), DaoGetter.get(team), null, filename, url, type, size));
    }

    /**
     * @param shortDescription
     * @throws UnauthorizedPublicAccessException
     * @see com.bloatit.data.DaoFileMetadata#setShortDescription(java.lang.String)
     */
    public void setDescription(final String shortDescription) throws UnauthorizedPublicAccessException {
        tryAccess(new RgtFileMetadata.Description(), Action.WRITE);
        setDescriptionUnprotected(shortDescription);
    }

    public void setDescriptionUnprotected(final String shortDescription) {
        getDao().setShortDescription(shortDescription);
    }

    /**
     * Tells if the authenticated user can access the <i>Description</i>
     * property.
     * 
     * @param action the type of access you want to do on the <i>Description</i>
     *            property.
     * @return true if you can access the <i>Description</i> property.
     */
    public final boolean canAccessDescription(final Action action) {
        return canAccess(new RgtFileMetadata.Description(), action);
    }

    /**
     * @see com.bloatit.data.DaoFileMetadata#getShortDescription()
     */
    // no right management: this is public data
    public String getShortDescription() {
        return getDao().getShortDescription();
    }

    /**
     * @see com.bloatit.data.DaoFileMetadata#getFilename()
     */
    // no right management: this is public data
    public File getFile() {
        return new File(getDao().getFilename());
    }

    /**
     * @see com.bloatit.data.DaoFileMetadata#getUrl()
     */
    // no right management: this is public data
    public String getUrl() {
        return getDao().getUrl();
    }

    /**
     * @see com.bloatit.data.DaoFileMetadata#getSize()
     */
    // no right management: this is public data
    public int getSize() {
        return getDao().getSize();
    }

    /**
     * @see com.bloatit.data.DaoFileMetadata#getType()
     */
    // no right management: this is public data
    public FileType getType() {
        return getDao().getType();
    }

    /**
     * @see com.bloatit.data.DaoFileMetadata#getFilename()
     */
    // no right management: this is public data
    public String getFileName() {
        return getDao().getFilename();
    }

    // /////////////////////////////////////////////////////////////////////////////////////////
    // Visitor
    // /////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public <ReturnType> ReturnType accept(final ModelClassVisitor<ReturnType> visitor) {
        return visitor.visit(this);
    }

}

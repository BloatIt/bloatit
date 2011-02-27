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

public class FileMetadata extends UserContent<DaoFileMetadata> {

    // /////////////////////////////////////////////////////////////////////////////////////////
    // CONSTRUCTION
    // /////////////////////////////////////////////////////////////////////////////////////////

    private static final class MyCreator extends Creator<DaoFileMetadata, FileMetadata> {
        @Override
        public FileMetadata doCreate(final DaoFileMetadata dao) {
            return new FileMetadata(dao);
        }
    }

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
     *      DaoUserContent, String, String, FileType, int)
     */
    public FileMetadata(final Member author, final String filename, final String url, final FileType type, final int size) {
        this(DaoFileMetadata.createAndPersist(author.getDao(), null, filename, url, type, size));
    }

    /**
     * @param shortDescription
     * @see com.bloatit.data.DaoFileMetadata#setShortDescription(java.lang.String)
     */
    public final void setShortDescription(final String shortDescription) {
        getDao().setShortDescription(shortDescription);
    }

    /**
     * @return
     * @see com.bloatit.data.DaoFileMetadata#getShortDescription()
     */
    public final String getShortDescription() {
        return getDao().getShortDescription();
    }

    /**
     * @return
     * @see com.bloatit.data.DaoFileMetadata#getFilename()
     */
    public final File getFile() {
        return new File(getDao().getFilename());
    }

    /**
     * @return
     * @see com.bloatit.data.DaoFileMetadata#getUrl()
     */
    public final String getUrl() {
        return getDao().getUrl();
    }

    /**
     * @return
     * @see com.bloatit.data.DaoFileMetadata#getSize()
     */
    public final int getSize() {
        return getDao().getSize();
    }

    /**
     * @return
     * @see com.bloatit.data.DaoFileMetadata#getType()
     */
    public FileType getType() {
        return getDao().getType();
    }

    /**
     * @return
     * @see com.bloatit.data.DaoFileMetadata#getFileName()
     */
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

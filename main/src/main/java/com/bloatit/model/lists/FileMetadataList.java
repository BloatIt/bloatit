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
package com.bloatit.model.lists;

import java.util.Iterator;

import com.bloatit.data.DaoFileMetadata;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.FileMetadata;

public final class FileMetadataList extends ListBinder<FileMetadata, DaoFileMetadata> {

    public FileMetadataList(final PageIterable<DaoFileMetadata> daoCollection) {
        super(daoCollection);
    }

    @Override
    protected Iterator<FileMetadata> createFromDaoIterator(final Iterator<DaoFileMetadata> dao) {
        return new FileMetadataIterator(dao);
    }

    static final class FileMetadataIterator extends com.bloatit.model.lists.IteratorBinder<FileMetadata, DaoFileMetadata> {

        public FileMetadataIterator(final Iterable<DaoFileMetadata> daoIterator) {
            super(daoIterator);
        }

        public FileMetadataIterator(final Iterator<DaoFileMetadata> daoIterator) {
            super(daoIterator);
        }

        @Override
        protected FileMetadata createFromDao(final DaoFileMetadata dao) {
            return FileMetadata.create(dao);
        }

    }

}

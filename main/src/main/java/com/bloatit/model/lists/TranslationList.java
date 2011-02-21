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

import com.bloatit.data.DaoTranslation;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.Translation;

/**
 * The Class TranslationList transforms PageIterable<Translation> to
 * PageIterable<Translation>.
 */
public final class TranslationList extends ListBinder<Translation, DaoTranslation> {

    /**
     * Instantiates a new translation list.
     * 
     * @param daoCollection the dao collection
     */
    public TranslationList(final PageIterable<DaoTranslation> daoCollection) {
        super(daoCollection);
    }

    /*
     * (non-Javadoc)
     * @see
     * com.bloatit.model.lists.ListBinder#createFromDaoIterator(java.util.Iterator
     * )
     */
    @Override
    protected Iterator<Translation> createFromDaoIterator(final Iterator<DaoTranslation> dao) {
        return new TranslationIterator(dao);
    }

    /**
     * The Class TranslationIterator.
     */
    static final class TranslationIterator extends com.bloatit.model.lists.IteratorBinder<Translation, DaoTranslation> {

        /**
         * Instantiates a new translation iterator.
         * 
         * @param daoIterator the dao iterator
         */
        public TranslationIterator(final Iterable<DaoTranslation> daoIterator) {
            super(daoIterator);
        }

        /**
         * Instantiates a new translation iterator.
         * 
         * @param daoIterator the dao iterator
         */
        public TranslationIterator(final Iterator<DaoTranslation> daoIterator) {
            super(daoIterator);
        }

        /*
         * (non-Javadoc)
         * @see
         * com.bloatit.model.lists.IteratorBinder#createFromDao(java.lang.Object
         * )
         */
        @Override
        protected Translation createFromDao(final DaoTranslation dao) {
            return Translation.create(dao);
        }

    }

}

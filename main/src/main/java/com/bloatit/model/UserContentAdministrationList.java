package com.bloatit.model;

import java.util.Iterator;

import com.bloatit.data.DaoUserContent;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.lists.ListBinder;

/**
 * The Class TransactionList transforms PageIterable<DaoUserContent> to
 * PageIterable<Transaction>.
 */
public final class UserContentAdministrationList<T extends DaoUserContent> extends ListBinder<UserContentAdministration<T>, T> {

    /**
     * Instantiates a new UserContentAdministration list.
     * 
     * @param daoCollection the dao collection
     */
    public UserContentAdministrationList(final PageIterable<T> daoCollection) {
        super(daoCollection);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.bloatit.model.lists.ListBinder#createFromDaoIterator(java.util
     * .Iterator )
     */
    @Override
    protected Iterator<UserContentAdministration<T>> createFromDaoIterator(final Iterator<T> dao) {
        return new UserContentAdministrationList.UserContentAdministrationIterator<T>(dao);
    }

    /**
     * The Class UserContentAdministrationIterator.
     */
    static final class UserContentAdministrationIterator<T extends DaoUserContent> extends
            com.bloatit.model.lists.IteratorBinder<UserContentAdministration<T>, T> {

        /**
         * Instantiates a new UserContentAdministration iterator.
         * 
         * @param daoIterator the dao iterator
         */
        public UserContentAdministrationIterator(final Iterable<T> daoIterator) {
            super(daoIterator);
        }

        /**
         * Instantiates a new UserContentAdministration iterator.
         * 
         * @param daoIterator the dao iterator
         */
        public UserContentAdministrationIterator(final Iterator<T> daoIterator) {
            super(daoIterator);
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * com.bloatit.model.lists.IteratorBinder#createFromDao(java.lang
         * .Object )
         */
        @Override
        protected UserContentAdministration<T> createFromDao(final T dao) {
            return UserContentAdministration.create(dao);
        }
    }
}
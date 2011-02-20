package com.bloatit.model.admin;

import java.util.Iterator;

import com.bloatit.data.DaoDemand;
import com.bloatit.data.DaoKudosable;
import com.bloatit.data.DaoUserContent;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.lists.ListBinder;

/**
 * The Class TransactionList transforms PageIterable<DaoUserContent> to
 * PageIterable<Transaction>.
 */
public abstract class AdminList<T extends DaoUserContent, U extends UserContentAdmin<T>> extends ListBinder<U, T> {

    /**
     * Instantiates a new UserContentAdministration list.
     * 
     * @param daoCollection the dao collection
     */
    public AdminList(final PageIterable<T> daoCollection) {
        super(daoCollection);
    }

    /**
     * The Class UserContentAdministrationIterator.
     */
    static abstract class AdminIterator<T extends DaoUserContent, U extends UserContentAdmin<T>> extends com.bloatit.model.lists.IteratorBinder<U, T> {

        /**
         * Instantiates a new UserContentAdministration iterator.
         * 
         * @param daoIterator the dao iterator
         */
        public AdminIterator(final Iterator<T> daoIterator) {
            super(daoIterator);
        }
    }

    public static class UserContentAdminList extends AdminList<DaoUserContent, UserContentAdmin<DaoUserContent>> {
        public UserContentAdminList(final PageIterable<DaoUserContent> daoCollection) {
            super(daoCollection);
        }

        @Override
        protected Iterator<UserContentAdmin<DaoUserContent>> createFromDaoIterator(final Iterator<DaoUserContent> dao) {
            return new AdminIterator<DaoUserContent, UserContentAdmin<DaoUserContent>>(dao) {
                @SuppressWarnings("unchecked")
                @Override
                protected UserContentAdmin<DaoUserContent> createFromDao(final DaoUserContent dao) {
                    return UserContentAdmin.create(dao);
                }
            };
        }
    }

    public static class KudosableAdminList extends AdminList<DaoKudosable, KudosableAdmin<DaoKudosable>> {
        public KudosableAdminList(final PageIterable<DaoKudosable> daoCollection) {
            super(daoCollection);
        }

        @Override
        protected Iterator<KudosableAdmin<DaoKudosable>> createFromDaoIterator(final Iterator<DaoKudosable> dao) {
            return new AdminIterator<DaoKudosable, KudosableAdmin<DaoKudosable>>(dao) {
                @SuppressWarnings("unchecked")
                @Override
                protected KudosableAdmin<DaoKudosable> createFromDao(final DaoKudosable dao) {
                    return KudosableAdmin.create(dao);
                }
            };
        }
    }

    public static class DemandAdminList extends AdminList<DaoDemand, DemandAdmin> {
        public DemandAdminList(final PageIterable<DaoDemand> daoCollection) {
            super(daoCollection);
        }

        @Override
        protected Iterator<DemandAdmin> createFromDaoIterator(final Iterator<DaoDemand> dao) {
            return new AdminIterator<DaoDemand, DemandAdmin>(dao) {
                @SuppressWarnings("unchecked")
                @Override
                protected DemandAdmin createFromDao(final DaoDemand dao) {
                    return DemandAdmin.create(dao);
                }
            };
        }
    }
}

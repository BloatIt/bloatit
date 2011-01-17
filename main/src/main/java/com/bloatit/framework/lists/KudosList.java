package com.bloatit.framework.lists;

import java.util.Iterator;

import com.bloatit.common.PageIterable;
import com.bloatit.framework.Kudos;
import com.bloatit.model.data.DaoKudos;

public final class KudosList extends ListBinder<Kudos, DaoKudos> {

    public KudosList(final PageIterable<DaoKudos> daoCollection) {
        super(daoCollection);
    }

    @Override
    protected Iterator<Kudos> createFromDaoIterator(final Iterator<DaoKudos> dao) {
        return new KudosIterator(dao);
    }

    static final class KudosIterator extends com.bloatit.framework.lists.IteratorBinder<Kudos, DaoKudos> {

        public KudosIterator(final Iterable<DaoKudos> daoIterator) {
            super(daoIterator);
        }

        public KudosIterator(final Iterator<DaoKudos> daoIterator) {
            super(daoIterator);
        }

        @Override
        protected Kudos createFromDao(final DaoKudos dao) {
            return new Kudos(dao);
        }

    }

}

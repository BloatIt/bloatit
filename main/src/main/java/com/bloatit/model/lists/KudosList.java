package com.bloatit.model.lists;

import java.util.Iterator;

import com.bloatit.data.DaoKudos;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.Kudos;

public final class KudosList extends ListBinder<Kudos, DaoKudos> {

    public KudosList(final PageIterable<DaoKudos> daoCollection) {
        super(daoCollection);
    }

    @Override
    protected Iterator<Kudos> createFromDaoIterator(final Iterator<DaoKudos> dao) {
        return new KudosIterator(dao);
    }

    static final class KudosIterator extends com.bloatit.model.lists.IteratorBinder<Kudos, DaoKudos> {

        public KudosIterator(final Iterable<DaoKudos> daoIterator) {
            super(daoIterator);
        }

        public KudosIterator(final Iterator<DaoKudos> daoIterator) {
            super(daoIterator);
        }

        @Override
        protected Kudos createFromDao(final DaoKudos dao) {
            return Kudos.create(dao);
        }

    }

}

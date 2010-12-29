package com.bloatit.framework.lists;

import java.util.Iterator;

import com.bloatit.framework.Kudos;
import com.bloatit.model.data.DaoKudos;

public final class KudosIterator extends com.bloatit.framework.lists.IteratorBinder<Kudos, DaoKudos> {

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

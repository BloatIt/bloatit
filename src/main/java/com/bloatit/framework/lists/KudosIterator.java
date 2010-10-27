package com.bloatit.framework.lists;

import java.util.Iterator;

import com.bloatit.model.Kudos;
import com.bloatit.model.data.DaoKudos;

public class KudosIterator extends com.bloatit.framework.lists.IteratorBinder<Kudos, DaoKudos> {

    public KudosIterator(Iterable<DaoKudos> daoIterator) {
        super(daoIterator);
    }

    public KudosIterator(Iterator<DaoKudos> daoIterator) {
        super(daoIterator);
    }

    @Override
    protected Kudos createFromDao(DaoKudos dao) {
        return new Kudos(dao);
    }

}

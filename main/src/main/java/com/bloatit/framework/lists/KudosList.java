package com.bloatit.framework.lists;

import java.util.Iterator;

import com.bloatit.common.PageIterable;
import com.bloatit.framework.Kudos;
import com.bloatit.model.data.DaoKudos;

public class KudosList extends ListBinder<Kudos, DaoKudos> {

    public KudosList(final PageIterable<DaoKudos> daoCollection) {
        super(daoCollection);
    }

    @Override
    protected Iterator<Kudos> createFromDaoIterator(final Iterator<DaoKudos> dao) {
        return new KudosIterator(dao);
    }

}

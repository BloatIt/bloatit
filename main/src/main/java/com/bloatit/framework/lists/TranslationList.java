package com.bloatit.framework.lists;

import java.util.Iterator;

import com.bloatit.common.PageIterable;
import com.bloatit.framework.Translation;
import com.bloatit.model.data.DaoTranslation;

public final class TranslationList extends ListBinder<Translation, DaoTranslation> {

    public TranslationList(final PageIterable<DaoTranslation> daoCollection) {
        super(daoCollection);
    }

    @Override
    protected Iterator<Translation> createFromDaoIterator(final Iterator<DaoTranslation> dao) {
        return new TranslationIterator(dao);
    }

}

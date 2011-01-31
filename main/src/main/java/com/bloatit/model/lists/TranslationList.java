package com.bloatit.model.lists;

import java.util.Iterator;

import com.bloatit.data.DaoTranslation;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.Translation;

public final class TranslationList extends ListBinder<Translation, DaoTranslation> {

    public TranslationList(final PageIterable<DaoTranslation> daoCollection) {
        super(daoCollection);
    }

    @Override
    protected Iterator<Translation> createFromDaoIterator(final Iterator<DaoTranslation> dao) {
        return new TranslationIterator(dao);
    }

    static final class TranslationIterator extends com.bloatit.model.lists.IteratorBinder<Translation, DaoTranslation> {

        public TranslationIterator(final Iterable<DaoTranslation> daoIterator) {
            super(daoIterator);
        }

        public TranslationIterator(final Iterator<DaoTranslation> daoIterator) {
            super(daoIterator);
        }

        @Override
        protected Translation createFromDao(final DaoTranslation dao) {
            return Translation.create(dao);
        }

    }

}

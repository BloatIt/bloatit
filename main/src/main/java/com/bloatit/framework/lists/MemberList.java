package com.bloatit.framework.lists;

import java.util.Iterator;

import com.bloatit.common.PageIterable;
import com.bloatit.framework.Member;
import com.bloatit.model.data.DaoMember;

public final class MemberList extends ListBinder<Member, DaoMember> {

    public MemberList(final PageIterable<DaoMember> daoCollection) {
        super(daoCollection);
    }

    @Override
    protected Iterator<Member> createFromDaoIterator(final Iterator<DaoMember> dao) {
        return new MemberIterator(dao);
    }

    static final class MemberIterator extends IteratorBinder<Member, DaoMember> {

        public MemberIterator(final Iterable<DaoMember> daoIterator) {
            super(daoIterator);
        }

        public MemberIterator(final Iterator<DaoMember> daoIterator) {
            super(daoIterator);
        }

        @Override
        protected Member createFromDao(final DaoMember dao) {
            return Member.create(dao);
        }

    }

}

package com.bloatit.framework.lists;

import java.util.Iterator;

import com.bloatit.framework.Member;
import com.bloatit.model.data.DaoMember;

public class MemberIterator extends IteratorBinder<Member, DaoMember> {

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

package com.bloatit.framework.lists;

import java.util.Iterator;

import com.bloatit.common.PageIterable;
import com.bloatit.framework.Member;
import com.bloatit.model.data.DaoMember;

public class MemberList extends ListBinder<Member, DaoMember> {

    public MemberList(final PageIterable<DaoMember> daoCollection) {
        super(daoCollection);
    }

    @Override
    protected Iterator<Member> createFromDaoIterator(final Iterator<DaoMember> dao) {
        return new MemberIterator(dao);
    }

}

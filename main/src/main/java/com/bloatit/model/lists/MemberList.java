//
// Copyright (c) 2011 Linkeos.
//
// This file is part of Elveos.org.
// Elveos.org is free software: you can redistribute it and/or modify it
// under the terms of the GNU General Public License as published by the
// Free Software Foundation, either version 3 of the License, or (at your
// option) any later version.
//
// Elveos.org is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
// more details.
// You should have received a copy of the GNU General Public License along
// with Elveos.org. If not, see http://www.gnu.org/licenses/.
//
package com.bloatit.model.lists;

import java.util.Iterator;

import com.bloatit.data.DaoMember;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.Member;

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

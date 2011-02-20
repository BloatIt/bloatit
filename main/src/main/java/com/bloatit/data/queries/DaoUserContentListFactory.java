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
package com.bloatit.data.queries;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.bloatit.data.DaoGroup;
import com.bloatit.data.DaoMember;
import com.bloatit.data.DaoUserContent;
import com.bloatit.data.SessionManager;

public class DaoUserContentListFactory<T extends DaoUserContent> extends DaoIdentifiableListFactory<T> {

    private static final String CREATION_DATE = "creationDate";
    private static final String MEMBER = "member";
    private static final String MEMBER_LOGIN = "m.member";
    private static final String FILES = "files";
    private static final String IS_DELETED = "isDeleted";
    private static final String AS_GROUP = "asGroup";

    protected DaoUserContentListFactory(final Criteria criteria) {
        super(criteria);
        criteria.createAlias("member", "m");
        criteria.setReadOnly(true);
    }

    public DaoUserContentListFactory() {
        this(SessionManager.getSessionFactory().getCurrentSession().createCriteria(DaoUserContent.class));
    }

    public void groupByMember() {
        add(Projections.groupProperty(MEMBER));
    }

    public void groupByAsGroup() {
        add(Projections.groupProperty(AS_GROUP));
    }

    public void orderByMember(final DaoAbstractListFactory.OrderType order) {
        if (order == OrderType.ASC) {
            addOrder(Order.asc(MEMBER_LOGIN));
        } else {
            addOrder(Order.desc(MEMBER_LOGIN));
        }
    }

    public void orderByAsGroup(final DaoAbstractListFactory.OrderType order) {
        if (order == OrderType.ASC) {
            addOrder(Order.asc(AS_GROUP));
        } else {
            addOrder(Order.desc(AS_GROUP));
        }
    }

    public void orderBy(final String column, final OrderType order) {
        if (order == OrderType.ASC) {
            addOrder(Order.asc(column));
        } else {
            addOrder(Order.desc(column));
        }
    }

    public void orderByCreationDate(final OrderType order) {
        if (order == OrderType.ASC) {
            addOrder(Order.asc(CREATION_DATE));
        } else {
            addOrder(Order.desc(CREATION_DATE));
        }
    }

    public void deletedOnly() {
        add(Restrictions.eq(IS_DELETED, true));
    }

    public void nonDeletedOnly() {
        add(Restrictions.eq(IS_DELETED, false));
    }

    public void withoutFile() {
        add(Restrictions.isEmpty(FILES));
    }

    public void withFile() {
        add(Restrictions.isNotEmpty(FILES));
    }

    public void withAnyGroup() {
        add(Restrictions.isNotNull(AS_GROUP));
    }

    public void withNoGroup() {
        add(Restrictions.isNull(AS_GROUP));
    }

    public void fromMember(final DaoMember member) {
        add(Restrictions.eq(MEMBER, member));
    }

    public void fromGroup(final DaoGroup group) {
        add(Restrictions.eq(AS_GROUP, group));
    }

}

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

import com.bloatit.data.DaoMember;
import com.bloatit.data.DaoTeam;
import com.bloatit.data.DaoUserContent;
import com.bloatit.data.SessionManager;

/**
 * The Class DaoUserContentQuery is a generic way of querying for DaoUserContent
 * using criterias.
 * 
 * @param <T> the generic type
 */
public class DaoUserContentQuery<T extends DaoUserContent> extends DaoIdentifiableQuery<T> {

    private static String CREATION_DATE = "creationDate";
    private static String MEMBER = "member";
    private static String MEMBER_LOGIN = "m.member";
    private static String FILES = "files";
    private static String IS_DELETED = "isDeleted";
    private static String AS_TEAM = "asTeam";

    /**
     * Instantiates a new dao user content query.
     * 
     * @param criteria the criteria to use for this query
     */
    protected DaoUserContentQuery(final Criteria criteria) {
        super(criteria);
        criteria.createAlias("member", "m");
        criteria.setReadOnly(true);
    }

    /**
     * Instantiates a new dao user content query.
     */
    public DaoUserContentQuery() {
        this(SessionManager.getSessionFactory().getCurrentSession().createCriteria(DaoUserContent.class));
    }

    /**
     * Group by member.
     */
    public void groupByMember() {
        add(Projections.groupProperty(MEMBER));
    }

    /**
     * Group by as team.
     */
    public void groupByAsTeam() {
        add(Projections.groupProperty(AS_TEAM));
    }

    /**
     * Order by member.
     * 
     * @param order the order (ASC/DESC)
     */
    public void orderByMember(final DaoAbstractQuery.OrderType order) {
        if (order == OrderType.ASC) {
            addOrder(Order.asc(MEMBER_LOGIN));
        } else {
            addOrder(Order.desc(MEMBER_LOGIN));
        }
    }

    /**
     * Order by as team.
     * 
     * @param order the order (ASC/DESC)
     */
    public void orderByAsTeam(final DaoAbstractQuery.OrderType order) {
        if (order == OrderType.ASC) {
            addOrder(Order.asc(AS_TEAM));
        } else {
            addOrder(Order.desc(AS_TEAM));
        }
    }

    /**
     * Order by creation date.
     * 
     * @param order the order (ASC/DESC)
     */
    public void orderByCreationDate(final OrderType order) {
        if (order == OrderType.ASC) {
            addOrder(Order.asc(CREATION_DATE));
        } else {
            addOrder(Order.desc(CREATION_DATE));
        }
    }

    /**
     * show Deleted content only.
     */
    public void deletedOnly() {
        add(Restrictions.eq(IS_DELETED, true));
        SessionManager.getSessionFactory().getCurrentSession().disableFilter("usercontent.nonDeleted");
    }

    /**
     * show Non deleted content only.
     */
    public final void nonDeletedOnly() {
        add(Restrictions.eq(IS_DELETED, false));
    }

    /**
     * Show content without file
     */
    public void withoutFile() {
        add(Restrictions.isEmpty(FILES));
    }

    /**
     * Show content with file.
     */
    public void withFile() {
        add(Restrictions.isNotEmpty(FILES));
    }

    /**
     * Show content created as team.
     */
    public void withAnyTeam() {
        add(Restrictions.isNotNull(AS_TEAM));
    }

    /**
     * Show content with no team
     */
    public void withNoTeam() {
        add(Restrictions.isNull(AS_TEAM));
    }

    /**
     * Show content created from member.
     * 
     * @param member the member
     */
    public void fromMember(final DaoMember member) {
        add(Restrictions.eq(MEMBER, member));
    }

    /**
     * Show content created from team.
     * 
     * @param team the team
     */
    public void fromTeam(final DaoTeam team) {
        add(Restrictions.eq(AS_TEAM, team));
    }

}

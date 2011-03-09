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
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.metadata.ClassMetadata;

import com.bloatit.data.DaoDemand;
import com.bloatit.data.DaoIdentifiable;
import com.bloatit.data.DaoUserContent;
import com.bloatit.data.SessionManager;
import com.bloatit.framework.utils.PageIterable;

/**
 * These are some static DB requests on generic DAO type. Most of the time, the
 * names are quite easy to understand. There are some common rules: <li>If a
 * method return a simple object then the return value can be null (If the
 * object is not found.)</li> <li>If a method return a collection then the
 * collection is always != null (but can be empty)</li>
 */
public class DBRequests {

    public enum Comparator {
        EQUAL, LESS, GREATER, LESS_EQUAL, GREATER_EQUAL
    }

    /**
     * Disactivating the default ctor.
     */
    private DBRequests() {
        // disactivated
    }

    public static PageIterable<DaoUserContent> getUserContents() {
        final Session session = SessionManager.getSessionFactory().getCurrentSession();
        final Criteria criteria = session.createCriteria(DaoUserContent.class);
        return new CriteriaCollection<DaoUserContent>(criteria);
    }

    /**
     * Make sure you test if the return is != null:
     *
     * <pre>
     * public static Group create() {
     *     DaoGroup dao = DBRequests.getById(DaoGroup.class, 12);
     *     if (dao == null) {
     *         return null;
     *     }
     *     return new Group(dao);
     * }
     * </pre>
     *
     * @param <T>
     * @param persistant
     * @param id
     * @return the persistent object that has a id equals to <code>id</code>, or
     *         null if non existing.
     */
    @SuppressWarnings("unchecked")
    public static <T> T getById(final Class<T> persistant, final Integer id) {
        return (T) SessionManager.getSessionFactory().getCurrentSession().get(persistant, id);
    }

    public static <T extends DaoIdentifiable> PageIterable<T> getAll(final Class<T> persistent) {
        final ClassMetadata meta = SessionManager.getSessionFactory().getClassMetadata(persistent);
        final Query query = SessionManager.createQuery("FROM " + meta.getEntityName());
        final Query size = SessionManager.createQuery("SELECT count(*) FROM " + meta.getEntityName());
        return new QueryCollection<T>(query, size);
    }

    public static <T extends DaoUserContent> PageIterable<T> getAllUserContentOrderByDate(final Class<T> persistent) {
        final ClassMetadata meta = SessionManager.getSessionFactory().getClassMetadata(persistent);
        return new QueryCollection<T>(SessionManager.createQuery("from " + meta.getEntityName() + " order by creationDate DESC"),
                                      SessionManager.createQuery("select count(*) from " + meta.getEntityName()));
    }

    public static <T> int count(final Class<T> persistent) {
        final ClassMetadata meta = SessionManager.getSessionFactory().getClassMetadata(persistent);
        return ((Long) SessionManager.getSessionFactory()
                                     .getCurrentSession()
                                     .createQuery("select count(*) from " + meta.getEntityName())
                                     .uniqueResult()).intValue();
    }

    public static PageIterable<DaoDemand> demandsOrderByPopularity() {
        return demandsOrderBy("popularity");
    }

    public static PageIterable<DaoDemand> demandsOrderByContribution() {
        return demandsOrderBy("contribution");
    }

    public static PageIterable<DaoDemand> demandsOrderByDate() {
        return demandsOrderBy("creationDate");
    }

    private static PageIterable<DaoDemand> demandsOrderBy(final String field) {
        final Query query = SessionManager.createQuery("from DaoDemand where state == PENDING order by " + field);
        final Query size = SessionManager.createQuery("SELECT count(*) from DaoDemand where state == PENDING order by " + field);
        return new QueryCollection<DaoDemand>(query, size);
    }

    public static PageIterable<DaoDemand> demandsThatShouldBeValidated() {
        final Query query = SessionManager.createQuery("FROM DaoDemand " + //
                "WHERE selectedOffer is not null " + //
                "AND validationDate is not null " + //
                "AND validationDate < now() " + //
                "AND demandState = :state");
        final Query size = SessionManager.createQuery("SELECT count(*) " + //
                "FROM DaoDemand " + //
                "WHERE selectedOffer is not null " + //
                "AND validationDate is not null " + //
                "AND validationDate < now() " + //
                "AND demandState = :state");
        return new QueryCollection<DaoDemand>(query, size).setParameter("state", DaoDemand.DemandState.PREPARING);
    }

    public static PageIterable<DaoDemand> demandsThatShouldBeValidatedInTheFuture() {
        final Query query = SessionManager.createQuery("FROM DaoDemand " + //
                "WHERE selectedOffer is not null " + //
                "AND validationDate is not null " + //
                "AND validationDate > now() " + //
                "AND demandState = :state");
        final Query size = SessionManager.createQuery("SELECT count(*) " + //
                "FROM DaoDemand " + //
                "WHERE selectedOffer is not null " + //
                "AND validationDate is not null " + //
                "AND validationDate > now() " + //
                "AND demandState = :state");
        return new QueryCollection<DaoDemand>(query, size).setParameter("state", DaoDemand.DemandState.PREPARING);
    }

}

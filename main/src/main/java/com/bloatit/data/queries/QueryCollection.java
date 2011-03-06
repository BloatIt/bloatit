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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import org.hibernate.HibernateException;
import org.hibernate.Query;

import com.bloatit.data.SessionManager;
import com.bloatit.framework.utils.PageIterable;

// TODO: Auto-generated Javadoc
/**
 * This is the implementation of the {@link PageIterable} interface using a
 * Hibernate HQL query.
 * 
 * @param <T> The type stored in this collection. T must implements a Dao object.
 */
public class QueryCollection<T> implements PageIterable<T> {

    /** The query. */
    private final Query query;

    /** The size query. */
    private final Query sizeQuery;

    /** The page size. */
    private int pageSize;

    /** The size. */
    private int size;

    /** The current page. */
    private int currentPage;

    /**
     * Create a {@link QueryCollection} using a named query. This constructor
     * will look for <b>two</b> named queries:
     * <ul>
     * <li>The query named: <code>nameQuery</code></li>
     * <li>The query named: <code>nameQuery + ".size"</code>, for the size of
     * the collection.</li>
     * </ul>
     * This should not be a problem if you follow the naming convention for
     * queries.
     * <p>
     * If One of the two queries is missing this will throws an
     * 
     * @param nameQuery the name of the query to find the collection. The query
     *            named <code>nameQuery + ".size"</code> <b>must</b> exists.
     *            {@link HibernateException}.
     *            </p>
     */
    public QueryCollection(String nameQuery) {
        this(SessionManager.getNamedQuery(nameQuery), SessionManager.getNamedQuery(nameQuery + ".size"));
    }

    /**
     * Instantiates a new query collection.
     * 
     * @param query the query
     * @param sizeQuery the size query
     */
    public QueryCollection(Query query, Query sizeQuery) {
        this.pageSize = 0;
        this.size = -1;
        this.query = query;
        this.sizeQuery = sizeQuery;
    }

    /**
     * Sets an entity.
     * 
     * @param name the parameter name
     * @param entity the entity
     * @return this query collection
     */
    public QueryCollection<T> setEntity(String name, Object entity) {
        query.setEntity(name, entity);
        sizeQuery.setEntity(name, entity);
        return this;
    }

    /**
     * Sets a named parameter.
     * 
     * @param name the parameter name
     * @param entity the entity
     * @return this query collection
     */
    public QueryCollection<T> setParameter(String name, Object entity) {
        query.setParameter(name, entity);
        sizeQuery.setParameter(name, entity);
        return this;
    }

    /**
     * Sets a <code>string</code> named parameter.
     * 
     * @param name the parameter name
     * @param val the val
     * @return this query collection
     */
    public QueryCollection<T> setString(String name, String val) {
        sizeQuery.setString(name, val);
        query.setString(name, val);
        return this;
    }

    /**
     * Sets a <code>character</code> named parameter.
     * 
     * @param name the parameter name
     * @param val the val
     * @return this query collection
     */
    public QueryCollection<T> setCharacter(String name, char val) {
        sizeQuery.setCharacter(name, val);
        query.setCharacter(name, val);
        return this;
    }

    /**
     * Sets a <code>boolean</code> named parameter.
     * 
     * @param name the parameter name
     * @param val the val
     * @return this query collection
     */
    public QueryCollection<T> setBoolean(String name, boolean val) {
        sizeQuery.setBoolean(name, val);
        query.setBoolean(name, val);
        return this;
    }

    /**
     * Sets a <code>byte</code> named parameter.
     * 
     * @param name the parameter name
     * @param val the val
     * @return this query collection
     */
    public QueryCollection<T> setByte(String name, byte val) {
        sizeQuery.setByte(name, val);
        query.setByte(name, val);
        return this;
    }

    /**
     * Sets a <code>short</code> named parameter.
     * 
     * @param name the parameter name
     * @param val the val
     * @return this query collection
     */
    public QueryCollection<T> setShort(String name, short val) {
        sizeQuery.setShort(name, val);
        query.setShort(name, val);
        return this;
    }

    /**
     * Sets a <code>integer</code> named parameter.
     * 
     * @param name the parameter name
     * @param val the val
     * @return this query collection
     */
    public QueryCollection<T> setInteger(String name, int val) {
        sizeQuery.setInteger(name, val);
        query.setInteger(name, val);
        return this;
    }

    /**
     * Sets a <code>long</code> named parameter.
     * 
     * @param name the parameter name
     * @param val the val
     * @return this query collection
     */
    public QueryCollection<T> setLong(String name, long val) {
        sizeQuery.setLong(name, val);
        query.setLong(name, val);
        return this;
    }

    /**
     * Sets a <code>float</code> named parameter.
     * 
     * @param name the parameter name
     * @param val the val
     * @return this query collection
     */
    public QueryCollection<T> setFloat(String name, float val) {
        sizeQuery.setFloat(name, val);
        query.setFloat(name, val);
        return this;
    }

    /**
     * Sets a <code>double</code> named parameter.
     * 
     * @param name the parameter name
     * @param val the val
     * @return this query collection
     */
    public QueryCollection<T> setDouble(String name, double val) {
        sizeQuery.setDouble(name, val);
        query.setDouble(name, val);
        return this;
    }

    /**
     * Sets a binary named parameter.
     * 
     * @param name the parameter name
     * @param val the val
     * @return this query collection
     */
    public QueryCollection<T> setBinary(String name, byte[] val) {
        sizeQuery.setBinary(name, val);
        query.setBinary(name, val);
        return this;
    }

    /**
     * Sets a text named parameter.
     * 
     * @param name the parameter name
     * @param val the val
     * @return this query collection
     */
    public QueryCollection<T> setText(String name, String val) {
        sizeQuery.setText(name, val);
        query.setText(name, val);
        return this;
    }

    /**
     * Sets a <code>locale</code> named parameter.
     * 
     * @param name the parameter name
     * @param locale the locale
     * @return this query collection
     */
    public QueryCollection<T> setLocale(String name, Locale locale) {
        sizeQuery.setLocale(name, locale);
        query.setLocale(name, locale);
        return this;
    }

    /**
     * Sets a <code>BigDecimal</code> named parameter.
     * 
     * @param name the parameter name
     * @param number the number
     * @return this query collection
     */
    public QueryCollection<T> setBigDecimal(String name, BigDecimal number) {
        sizeQuery.setBigDecimal(name, number);
        query.setBigDecimal(name, number);
        return this;
    }

    /**
     * Sets a <code>BigInteger</code> named parameter.
     * 
     * @param name the parameter name
     * @param number the number
     * @return this query collection
     */
    public QueryCollection<T> setBigInteger(String name, BigInteger number) {
        sizeQuery.setBigInteger(name, number);
        query.setBigInteger(name, number);
        return this;
    }

    /**
     * Sets a <code>date</code> named parameter.
     * 
     * @param name the parameter name
     * @param date the date
     * @return this query collection
     */
    public QueryCollection<T> setDate(String name, Date date) {
        sizeQuery.setDate(name, date);
        query.setDate(name, date);
        return this;
    }

    /**
     * Sets a time named parameter.
     * 
     * @param name the parameter name
     * @param date the date
     * @return this query collection
     */
    public QueryCollection<T> setTime(String name, Date date) {
        sizeQuery.setTime(name, date);
        query.setTime(name, date);
        return this;
    }

    /**
     * Sets a <code>timestamp</code> named parameter.
     * 
     * @param name the parameter name
     * @param date the date
     * @return this query collection
     */
    public QueryCollection<T> setTimestamp(String name, Date date) {
        sizeQuery.setTimestamp(name, date);
        query.setTimestamp(name, date);
        return this;
    }

    /**
     * Sets a calendar named parameter.
     * 
     * @param name the parameter name
     * @param calendar the calendar
     * @return this query collection
     */
    public QueryCollection<T> setCalendar(String name, Calendar calendar) {
        sizeQuery.setCalendar(name, calendar);
        query.setCalendar(name, calendar);
        return this;
    }

    /**
     * Sets a calendar date named parameter.
     * 
     * @param name the parameter name
     * @param calendar the calendar
     * @return this query collection
     */
    public QueryCollection<T> setCalendarDate(String name, Calendar calendar) {
        sizeQuery.setCalendarDate(name, calendar);
        query.setCalendarDate(name, calendar);
        return this;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Iterable#iterator()
     */
    @Override
    @SuppressWarnings("unchecked")
    public Iterator<T> iterator() {
        return query.list().iterator();
    }

    /*
     * (non-Javadoc)
     * @see com.bloatit.framework.utils.PageIterable#setPage(int)
     */
    @Override
    public void setPage(int page) {
        currentPage = page;
        query.setFirstResult(page * pageSize);
    }

    /*
     * (non-Javadoc)
     * @see com.bloatit.framework.utils.PageIterable#setPageSize(int)
     */
    @Override
    public void setPageSize(int pageSize) {
        query.setMaxResults(pageSize);
        query.setFetchSize(pageSize);
        this.pageSize = pageSize;
    }

    /*
     * (non-Javadoc)
     * @see com.bloatit.framework.utils.PageIterable#getPageSize()
     */
    @Override
    public int getPageSize() {
        return pageSize;
    }

    /*
     * (non-Javadoc)
     * @see com.bloatit.framework.utils.PageIterable#size()
     */
    @Override
    public int size() {
        if (size == -1) {
            size = ((Long) sizeQuery.uniqueResult()).intValue();
            return size;
        }
        return size;
    }

    /*
     * (non-Javadoc)
     * @see com.bloatit.framework.utils.PageIterable#pageNumber()
     */
    @Override
    public int pageNumber() {
        if (pageSize != 0) {
            return (int) Math.ceil((double) size() / (double) pageSize);
        }
        return 1;
    }

    /*
     * (non-Javadoc)
     * @see com.bloatit.framework.utils.PageIterable#getCurrentPage()
     */
    @Override
    public int getCurrentPage() {
        return currentPage;
    }
}

package com.bloatit.model.data;

import java.util.Iterator;

import org.hibernate.Query;

import com.bloatit.common.PageIterable;
import com.bloatit.model.data.util.SessionManager;

public class QueryCollection<T> implements PageIterable<T> {

	private final Query query;
	private final Query sizeQuery;
	private int pageSize;
	private long size;

	/**
	 * Use this constructor with query that start with "from ..."
	 * 
	 * @param query
	 */
	protected QueryCollection(Query query) {
		this(query, SessionManager.getSessionFactory().getCurrentSession().createQuery("select count (*) " + query.getQueryString()));
	}

	protected QueryCollection(Query query, Query sizeQuery) {
		pageSize = 0;
		size = -1;
		this.query = query;
		this.sizeQuery = sizeQuery;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Iterator<T> iterator() {
		return query.list().iterator();
	}

	@Override
	public void setPage(int page) {
		query.setFirstResult(page * pageSize);
	}

	@Override
	public void setPageSize(int pageSize) {
		query.setFetchSize(pageSize);
		this.pageSize = pageSize;
	}

	@Override
	public int getPageSize() {
		return pageSize;
	}

	@Override
	public long size() {
		if (size == -1) {
			return size = (Long) sizeQuery.uniqueResult();
		}
		return size;
	}

	@Override
	public long pageNumber() {
		if (pageSize != 0) {
			return size() / pageSize;
		} else {
			return 1;
		}
	}

}

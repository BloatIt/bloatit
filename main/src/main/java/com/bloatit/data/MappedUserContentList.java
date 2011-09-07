package com.bloatit.data;

import java.util.List;

import org.hibernate.Query;

import com.bloatit.data.queries.QueryCollection;

public class MappedUserContentList<T extends DaoUserContent> extends QueryCollection<T> {

    public MappedUserContentList(final List<T> content) {
        super(createFilter(content), createFilterSize(content));
    }

    private static Query createFilterSize(final List<?> content) {
        if (SessionManager.getSessionFactory().getCurrentSession().getEnabledFilter("usercontent.nonDeleted") != null) {
            return SessionManager.createFilter(content, "select count(*) where isDeleted = 'false'");
        }
        return SessionManager.createFilter(content, "select count(*)");
    }

    private static Query createFilter(final List<?> content) {
        if (SessionManager.getSessionFactory().getCurrentSession().getEnabledFilter("usercontent.nonDeleted") != null) {
            return SessionManager.createFilter(content, "where isDeleted = 'false'");
        }
        return SessionManager.createFilter(content, "");
    }

}

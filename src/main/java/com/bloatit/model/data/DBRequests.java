package com.bloatit.model.data;

import org.hibernate.metadata.ClassMetadata;

import com.bloatit.common.PageIterable;
import com.bloatit.model.data.util.SessionManager;

public class DBRequests {

    @SuppressWarnings("unchecked")
    public static <T> T getById(Class<T> persistant, Integer id) {
        return (T) SessionManager.getSessionFactory().getCurrentSession().get(persistant, id);
    }

    public static <T> PageIterable<T> getAll(Class<T> persistent) {
        final ClassMetadata meta = SessionManager.getSessionFactory().getClassMetadata(persistent);
        return new QueryCollection<T>("from " + meta.getEntityName());
    }

    public static <T> int count(Class<T> persistent) {
        final ClassMetadata meta = SessionManager.getSessionFactory().getClassMetadata(persistent);
        return ((Long) SessionManager.getSessionFactory().getCurrentSession().createQuery("select count(*) from " + meta.getEntityName()).uniqueResult()).intValue();
    }



}

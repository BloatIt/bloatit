package com.bloatit.model.data;

import org.hibernate.metadata.ClassMetadata;

import com.bloatit.model.data.util.SessionManger;

public class DBRequests {

    @SuppressWarnings("unchecked")
    public static <T> T getById(Class<T> persistant, Integer id) {
        return (T) SessionManger.getSessionFactory().getCurrentSession().get(persistant, id);
    }

    public static <T> QueryCollection<T> getAll(Class<T> persistent) {
        ClassMetadata meta = SessionManger.getSessionFactory().getClassMetadata(persistent);
        System.out.println("from " + meta.getEntityName());
        return new QueryCollection<T>(SessionManger.getSessionFactory().getCurrentSession().createQuery("from " + meta.getEntityName()));
    }

}

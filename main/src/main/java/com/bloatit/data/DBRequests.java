package com.bloatit.data;

import org.hibernate.metadata.ClassMetadata;

import com.bloatit.framework.utils.PageIterable;

/**
 * These are some static DB requests on generic DAO type. Most of the time, the names are
 * quite easy to understand. There are some common rules: <li>If a method return a simple
 * object then the return value can be null (If the object is not found.)</li> <li>If a
 * method return a collection then the collection is always != null (but can be empty)</li>
 */
public final class DBRequests {

    /**
     * Disactivating the default ctor.
     */
    private DBRequests() {
        // disactivated
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
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T getById(final Class<T> persistant, final Integer id) {
        return (T) SessionManager.getSessionFactory().getCurrentSession().get(persistant, id);
    }

    public static <T> PageIterable<T> getAll(final Class<T> persistent) {
        final ClassMetadata meta = SessionManager.getSessionFactory().getClassMetadata(persistent);
        return new QueryCollection<T>("from " + meta.getEntityName());
    }

    public static <T extends DaoUserContent> PageIterable<T> getAllUserContentOrderByDate(final Class<T> persistent) {
        final ClassMetadata meta = SessionManager.getSessionFactory().getClassMetadata(persistent);
        return new QueryCollection<T>(SessionManager.createQuery("from " + meta.getEntityName() + " order by creationDate DESC"),
                SessionManager.createQuery("select count(*) from " + meta.getEntityName()));
    }

    public static <T> int count(final Class<T> persistent) {
        final ClassMetadata meta = SessionManager.getSessionFactory().getClassMetadata(persistent);
        return ((Long) SessionManager.getSessionFactory().getCurrentSession().createQuery("select count(*) from " + meta.getEntityName())
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
        return new QueryCollection<DaoDemand>("from DaoDemand where state == PENDING order by " + field);
    }

    // By kudosable
    // by near end
    // by date
    // by contrib
}

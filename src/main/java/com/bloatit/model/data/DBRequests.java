package com.bloatit.model.data;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.util.Version;
import org.hibernate.metadata.ClassMetadata;

import com.bloatit.common.PageIterable;
import com.bloatit.model.data.util.SessionManager;

/**
 * These are some static DB requests on generic DAO type. Most of the time, the
 * names are
 * quite easy to understand.
 * 
 * There are some common rules: <li>If a method return a simple object then the
 * return value can be null (If the object is not found.)</li> <li>If a method
 * return a collection then the collection is always != null (but can be empty)</li>
 */
public class DBRequests {

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

    public static <T> int count(final Class<T> persistent) {
        final ClassMetadata meta = SessionManager.getSessionFactory().getClassMetadata(persistent);
        return ((Long) SessionManager.getSessionFactory().getCurrentSession().createQuery("select count(*) from " + meta.getEntityName())
                .uniqueResult()).intValue();
    }

    public static PageIterable<DaoDemand> searchDemands(final String searchStr) {
        return search(DaoDemand.class, new String[] { "description.translations.title", "description.translations.text",
                "offers.description.translations.title" }, searchStr);
    }

    /**
     * Create a search on the db using Hibernate Search and Lucene
     * 
     * <pre>
     * DBRequests.search(DaoDemand.class, new String[] { &quot;description.translations.title&quot;, &quot;description.translations.text&quot;,
     *         &quot;offers.description.translations.title&quot; }, &quot;Search string&quot;);
     * </pre>
     * 
     * @param <T> is a persistent class (something like Dao...)
     * @param persistent is the class object associated with T.
     * @param fields is a list of field on which we are doing the search. These
     *        field are
     *        relative to the persistent class.
     * @param searchStr is the string we are looking for.
     * @return a PageIterable with the search results.
     */
    private static <T> PageIterable<T> search(final Class<T> persistent, final String[] fields, final String searchStr) {
        final MultiFieldQueryParser parser = new MultiFieldQueryParser(Version.LUCENE_29, fields, new StandardAnalyzer(Version.LUCENE_29));
        try {
            final org.apache.lucene.search.Query query = parser.parse(searchStr);
            return new SearchCollection<T>(SessionManager.getCurrentFullTextSession().createFullTextQuery(query, persistent));
        } catch (final ParseException e) {
            return new NullCollection<T>();
        }
    }

    public static PageIterable<DaoDemand> DemandsOrderByPopularity() {
        return demandsOrderBy("popularity");
    }

    public static PageIterable<DaoDemand> DemandsOrderByContribution() {
        return demandsOrderBy("contribution");
    }

    public static PageIterable<DaoDemand> DemandsOrderByDate() {
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

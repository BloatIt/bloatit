package com.bloatit.model.data;

import java.util.NoSuchElementException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.util.Version;
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
        return ((Long) SessionManager.getSessionFactory().getCurrentSession()
                .createQuery("select count(*) from " + meta.getEntityName()).uniqueResult()).intValue();
    }

    public static PageIterable<DaoDemand> searchDemands(String searchStr) {
        return search(DaoDemand.class, new String[] { "description.translations.title", "description.translations.text",
                "offers.description.translations.title" }, searchStr);
    }

    /**
     * Create a search on the db using Hibernate Search and Lucene
     * 
     * @param <T> is a persistent class (something like Dao...)
     * @param persistent is the class object associated with T.
     * @param fields is a list of field on which we are doing the search. These
     *        field are relative to the persistent class.
     * @param searchStr is the string we are looking for.
     * @return a PageIterable with the search results.
     */
    private static <T> PageIterable<T> search(Class<T> persistent, String[] fields, String searchStr) {
        MultiFieldQueryParser parser = new MultiFieldQueryParser(Version.LUCENE_29, fields, new StandardAnalyzer(
                Version.LUCENE_29));
        try {
            org.apache.lucene.search.Query query = parser.parse(searchStr);
            return new SearchCollection<T>(SessionManager.getCurrentFullTextSession().createFullTextQuery(query, persistent));

        } catch (ParseException e) {
            e.printStackTrace();
            return null;
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

    private static PageIterable<DaoDemand> demandsOrderBy(String field) {
        return new QueryCollection<DaoDemand>("from DaoDemand where state == PENDING order by " + field);
    }

    // By kudosable
    // by near end
    // by date
    // by contrib
}

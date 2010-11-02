package com.bloatit.model.data;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
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

    public static PageIterable<DaoDemand> search(String searchStr) {
        String[] fields = new String[] { "description.translation.title", "offers.description.translation.title" };
        MultiFieldQueryParser parser = new MultiFieldQueryParser(fields, new StandardAnalyzer());
        try {
            org.apache.lucene.search.Query query = parser.parse(searchStr);
            // wrap Lucene query in a org.hibernate.Query
            return new SearchCollection<DaoDemand>(SessionManager.getCurrentFullTextSession().createFullTextQuery(query,
                    DaoDemand.class));

        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> int count(Class<T> persistent) {
        final ClassMetadata meta = SessionManager.getSessionFactory().getClassMetadata(persistent);
        return ((Long) SessionManager.getSessionFactory().getCurrentSession().createQuery("select count(*) from " + meta.getEntityName()).uniqueResult()).intValue();
    }


}

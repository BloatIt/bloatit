package com.bloatit.data.search;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.util.Version;
import org.hibernate.search.FullTextQuery;

import com.bloatit.data.NullCollection;
import com.bloatit.data.SessionManager;
import com.bloatit.framework.utils.PageIterable;

public abstract class Search<T> {


    private String searchStr;
    private Class<T> persistent;
    private String[] fields;


    protected void configure(final Class<T> persistent, final String[] fields, final String searchStr) {
        this.persistent = persistent;
        this.fields = fields;
        this.searchStr = searchStr;
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
     * @param fields is a list of field on which we are doing the search. These field are
     *        relative to the persistent class.
     * @param searchStr is the string we are looking for.
     * @return a PageIterable with the search results.
     */
    protected final PageIterable<T> doSearch() {
        prepareSearch();

        final MultiFieldQueryParser parser = new MultiFieldQueryParser(Version.LUCENE_29, fields, new StandardAnalyzer(Version.LUCENE_29));

        try {

            final org.apache.lucene.search.Query query = parser.parse(searchStr);


            FullTextQuery luceneQuery = SessionManager.getCurrentFullTextSession().createFullTextQuery(query, persistent);

            luceneQuery.enableFullTextFilter("searchFilter");

            return new SearchCollection<T>(luceneQuery);
        } catch (final ParseException e) {
            return new NullCollection<T>();
        }

    }

    protected abstract void prepareSearch();


}

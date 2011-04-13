package com.bloatit.data.search;

import java.util.List;

import org.apache.lucene.search.Filter;
import org.hibernate.search.annotations.Factory;
import org.hibernate.search.annotations.Key;
import org.hibernate.search.filter.FilterKey;
import org.hibernate.search.filter.StandardFilterKey;

import com.bloatit.data.search.Search.Pair;

public class DaoFeatureSearchFilterFactory {

    private List<Pair<String, String>> filteredTerms = null;

    public void setFilteredTerms(final List<Pair<String, String>> filteredTerms) {
        this.filteredTerms = filteredTerms;
    }

    @Key
    public FilterKey getKey() {
        final StandardFilterKey key = new StandardFilterKey();
        key.addParameter(filteredTerms);
        return key;
    }

    @Factory
    public Filter getFilter() {
        final DaoFeatureSearchFilter searchFilter = new DaoFeatureSearchFilter();
        searchFilter.setFilteredTerms(filteredTerms);
        filteredTerms = null;
        return searchFilter;
    }

}

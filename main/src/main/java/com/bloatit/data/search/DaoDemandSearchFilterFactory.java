package com.bloatit.data.search;

import java.util.List;

import org.apache.lucene.search.Filter;
import org.hibernate.search.annotations.Factory;
import org.hibernate.search.annotations.Key;
import org.hibernate.search.filter.FilterKey;
import org.hibernate.search.filter.StandardFilterKey;

import com.bloatit.data.search.Search.Pair;

public class DaoDemandSearchFilterFactory {

    private List<Pair<String, String>> filteredTerms = null;

    public void setFilteredTerms( List<Pair<String, String>> filteredTerms) {
        this.filteredTerms = filteredTerms;
    }

    @Key
    public FilterKey getKey() {
         StandardFilterKey key = new StandardFilterKey();
        key.addParameter(filteredTerms);
        return key;
    }

    @Factory
    public Filter getFilter() {
         DaoDemandSearchFilter searchFilter = new DaoDemandSearchFilter();
        searchFilter.setFilteredTerms(filteredTerms);
        filteredTerms = null;
        return searchFilter;
    }

}

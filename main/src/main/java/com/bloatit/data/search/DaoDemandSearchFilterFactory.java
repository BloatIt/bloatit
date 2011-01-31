package com.bloatit.data.search;

import org.apache.lucene.search.CachingWrapperFilter;
import org.apache.lucene.search.Filter;
import org.hibernate.search.annotations.Factory;

public class DaoDemandSearchFilterFactory {

    @Factory
    public Filter getFilter() {
        Filter searchFilter = new DaoDemandSearchFilter();
        return new CachingWrapperFilter(searchFilter);
    }


}

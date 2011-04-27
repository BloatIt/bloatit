//
// Copyright (c) 2011 Linkeos.
//
// This file is part of Elveos.org.
// Elveos.org is free software: you can redistribute it and/or modify it
// under the terms of the GNU General Public License as published by the
// Free Software Foundation, either version 3 of the License, or (at your
// option) any later version.
//
// Elveos.org is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
// more details.
// You should have received a copy of the GNU General Public License along
// with Elveos.org. If not, see http://www.gnu.org/licenses/.
//
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

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

import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;

import com.bloatit.data.DaoFeature;
import com.bloatit.data.DaoFeature.FeatureState;

public class FeatureSearch extends Search<DaoFeature> {

    private SortMethod sortMethod;

    public enum SortMethod {
        SORT_BY_RELEVANCE, SORT_BY_CONTRIBUTION, SORT_BY_PROGRESS, SORT_BY_POPULARITY, SORT_BY_CREATION_DATE, SORT_BY_EXPIRATION_DATE
    }

    public FeatureSearch(final String searchText) {
        super();
        sortMethod = SortMethod.SORT_BY_RELEVANCE;
        configure(DaoFeature.class, new String[] { //
                                                   // feature description
                          "description.translations.text.content",
                          "description.translations.title",

                          // Feature state
                          "featureState",
                          "progress",
                          "popularity",

                          // Comment
                          "comments.text.content",
                          "comments.children.text.content",

                          // offers descriptions
                          "offers.milestones.description.translations.title",
                          "offers.milestones.description.translations.text.content", },
                  searchText);
        // Remove deleted content from search.
        addFilterTerm("isDeleted", "TRUE");
    }

    /**
     * The features with state as FeatureState will not be in the search results
     * 
     * @param state
     */
    public void addFeatureStateFilter(final FeatureState state) {
        addFilterTerm("featureState", state.toString());
    }

    @Override
    protected void prepareSearch() {
        enableFilter("searchFilter");

        final Sort sort = new Sort();

        switch (sortMethod) {
            case SORT_BY_CONTRIBUTION:
                sort.setSort(new SortField("contribution", SortField.FLOAT, true), new SortField("progress", SortField.FLOAT, true));
                break;
            case SORT_BY_CREATION_DATE:
                sort.setSort(new SortField("creationDate", SortField.STRING, true), new SortField("popularity", SortField.INT, true));
                break;
            case SORT_BY_EXPIRATION_DATE:
                sort.setSort(new SortField("selectedOffer.expirationDate", SortField.LONG, true), new SortField("popularity", SortField.INT, true));
                break;
            case SORT_BY_POPULARITY:
                sort.setSort(new SortField("popularity", SortField.INT, true));
                break;
            case SORT_BY_PROGRESS:
                sort.setSort(new SortField("featureState", SortField.STRING), new SortField("progress", SortField.FLOAT, true));
                break;
            case SORT_BY_RELEVANCE:
                sort.setSort(SortField.FIELD_SCORE, new SortField("popularity", SortField.INT, true));
                break;
        }

        setSort(sort);
    }

    public void setSortMethod(final SortMethod sortMethod) {
        this.sortMethod = sortMethod;
    }

}

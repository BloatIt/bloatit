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
        configure(DaoFeature.class, new String[] { "description.translations.title",
                                                  "description.translations.text",
                                                  "offers.description.translations.title" }, searchText);
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
                sort.setSort(new SortField("contribution", SortField.FLOAT, true));
                break;
            case SORT_BY_CREATION_DATE:
                sort.setSort(new SortField("creationDate", SortField.STRING, false));
                break;
            case SORT_BY_EXPIRATION_DATE:
                // TODO implement
                break;
            case SORT_BY_POPULARITY:
                sort.setSort(new SortField("popularity", SortField.INT, true));
                break;
            case SORT_BY_PROGRESS:
                // TODO index progress
                break;
            case SORT_BY_RELEVANCE:
                // TODO relevance score based on multiple variable
                break;
        }

        setSort(sort);
    }

    public void setSortMethod(final SortMethod sortMethod) {
        this.sortMethod = sortMethod;
    }

}

package com.bloatit.data.search;

import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;

import com.bloatit.data.DaoDemand;
import com.bloatit.data.DaoDemand.DemandState;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.demand.DemandInterface;
import com.bloatit.model.demand.DemandList;

public class DemandSearch extends Search<DaoDemand> {

    private SortMethod sortMethod;

    public enum SortMethod {
        SORT_BY_RELEVANCE, SORT_BY_CONTRIBUTION, SORT_BY_PROGRESS, SORT_BY_POPULARITY, SORT_BY_CREATION_DATE, SORT_BY_EXPIRATION_DATE
    }

    public DemandSearch(String searchText) {
        super();
        sortMethod = SortMethod.SORT_BY_RELEVANCE;
        configure(DaoDemand.class, new String[] { "description.translations.title", "description.translations.text",
                "offers.description.translations.title" }, searchText);
    }

    /**
     * The demands with state as DemandState will not be in the search results
     *
     * @param state
     */
    public void addDemandStateFilter(DemandState state) {
        addFilterTerm("demandState", state.toString());
    }

    @Override
    protected void prepareSearch() {
        enableFilter("searchFilter");

        Sort sort = new Sort();

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

    public PageIterable<DemandInterface> search() {
        return new DemandList(doSearch());
    }

    public void setSortMethod(SortMethod sortMethod) {
        this.sortMethod = sortMethod;
    }

}

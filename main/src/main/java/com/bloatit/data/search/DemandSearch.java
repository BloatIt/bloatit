package com.bloatit.data.search;

import com.bloatit.data.DaoDemand;
import com.bloatit.data.DaoDemand.DemandState;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.demand.Demand;
import com.bloatit.model.demand.DemandList;

public class DemandSearch  extends Search<DaoDemand>{

    public DemandSearch(String searchText) {
        super();

        configure(DaoDemand.class, new String[] { "description.translations.title", "description.translations.text",
        "offers.description.translations.title" }, searchText);
    }


    /**
     * The demands with state as DemandState will not be in the search results
     * @param state
     */
    public void addDemandStateFilter(DemandState state) {
        addFilterTerm("demandState", state.toString());
    }


    @Override
    protected void prepareSearch() {
        enableFilter("searchFilter");
    }




    public PageIterable<Demand>  search() {
        return new DemandList(doSearch());
    }




}

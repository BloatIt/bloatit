package com.bloatit.data.search;

import java.util.ArrayList;
import java.util.List;

import com.bloatit.data.DaoDemand;
import com.bloatit.data.DaoDemand.DemandState;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.demand.Demand;
import com.bloatit.model.demand.DemandList;

public class DemandSearch  extends Search<DaoDemand>{


    private final List<DemandState> filteredStates = new ArrayList<DemandState>();

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
        filteredStates.add(state);
    }


    @Override
    protected void prepareSearch() {
        // TODO Auto-generated method stub

    }

    public PageIterable<Demand>  search() {
        return new DemandList(doSearch());
    }




}

package com.bloatit.model.admin;

import com.bloatit.data.DaoDemand;
import com.bloatit.data.DaoDemand.DemandState;
import com.bloatit.data.queries.DaoDemandListFactory;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.Demand;
import com.bloatit.model.demand.DemandList;

public class DemandAdminListFactory extends KudosableAdminListFactory<DaoDemand, Demand> {

    public DemandAdminListFactory() {
        super(new DaoDemandListFactory());
    }

    @Override
    protected DaoDemandListFactory getfactory() {
        return (DaoDemandListFactory) super.getfactory();
    }

    @Override
    public PageIterable<Demand> list() {
        return new DemandList(getfactory().createCollection());
    }

    public void stateEquals(final DemandState state) {
        getfactory().stateEquals(state);
    }

    public void withSelectedOffer() {
        getfactory().withSelectedOffer();
    }

    public void withoutSelectedOffer() {
        getfactory().withoutSelectedOffer();
    }

    public void withOffer() {
        getfactory().withOffer();
    }

    public void withoutOffer() {
        getfactory().withoutOffer();
    }

    public void withContribution() {
        getfactory().hasContributions();
    }

    public void withoutContribution() {
        getfactory().hasNoContribution();
    }

}
